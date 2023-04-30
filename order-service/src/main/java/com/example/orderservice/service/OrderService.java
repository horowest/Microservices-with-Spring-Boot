package com.example.orderservice.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.orderservice.dto.OrderLineItemTO;
import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.model.Order;
import com.example.orderservice.model.OrderLineItem;
import com.example.orderservice.repo.OrderRepository;

@Service
@Transactional
public class OrderService {

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    private OrderRepository orderRepository;

    public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();

        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItem> orderLineItems = orderRequest.getOrderLineItems()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        // check if item is in stock
        for (OrderLineItem item : orderLineItems) {
            Boolean isInStock = webClientBuilder.build().get()
                    .uri("http://inventory-service/api/inventory", builder -> {
                        return builder.queryParam("skuCode", item.getSkuCode()).build();
                    })
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();

            if (isInStock) {
                order.addItemToOrderLine(item);
            } else {
                throw new IllegalArgumentException(item.getSkuCode() + " is not in stock.");
            }
        }

        orderRepository.save(order);
    }

    private OrderLineItem mapToDTO(OrderLineItemTO orderLineItemsDto) {
        OrderLineItem orderLineItems = new OrderLineItem();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }
}
