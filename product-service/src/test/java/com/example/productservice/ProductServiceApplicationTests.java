package com.example.productservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.example.productservice.dto.ProductRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@Container
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");

	static void setProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
	}

	@Test
	void shouldCreateProduct() throws Exception {
		ProductRequest productRequest = ProductRequest.builder()
				.name("iphone 12")
				.description("apple iphone 12")
				.price(1200.00)
				.build();

		String ptoductRequestJsonString = objectMapper.writeValueAsString(productRequest);

		mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
				.contentType(MediaType.APPLICATION_JSON)
				.content(ptoductRequestJsonString))
				.andExpect(status().isCreated());
	}

}
