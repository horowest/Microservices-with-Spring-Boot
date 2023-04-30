package com.example.inventoryservice.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.inventoryservice.model.Inventory;
import com.example.inventoryservice.repo.InventoryRepository;

@Service
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public boolean isInStock(String skuCode) {
        Optional<Inventory> item = inventoryRepository.findInventoryBySkuCode(skuCode);

        if(item.isPresent()) {
            return item.get().getQuantity() > 0;
        }
        return false;
    }

}
