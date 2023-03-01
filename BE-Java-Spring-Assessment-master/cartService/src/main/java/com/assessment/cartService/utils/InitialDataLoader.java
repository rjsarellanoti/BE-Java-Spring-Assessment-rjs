package com.assessment.cartService.utils;

import com.assessment.cartService.entity.Cart;
import com.assessment.cartService.entity.Product;
import com.assessment.cartService.service.impl.CartServiceImpl;
import com.assessment.cartService.service.impl.ProductServiceImpl;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.cj.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.File;


@Component
public class InitialDataLoader {

	private static final String JSON_DIR = "src/main/resources/json-examples";
	private static final String PRODUCT_JSON_FILE_1 = "product_1.json";
	private static final String PRODUCT_JSON_FILE_2 = "product_2.json";
	private static final String CART_JSON_FILE_1 = "cart_1.json";

	@Autowired
	private ProductServiceImpl productService;

	@Autowired
	private CartServiceImpl cartService;

	private static final Logger log = LoggerFactory.getLogger(InitialDataLoader.class);


	@EventListener
	public void onApplicationEvent(ContextRefreshedEvent event) {
		ObjectMapper mapper = new ObjectMapper();

		try
		{
			if (!productService.exists(1L))
			{
				Product product1 = mapper.treeToValue(getJsonFromFile(PRODUCT_JSON_FILE_1), Product.class);
				productService.save(product1);
			}
			if (!productService.exists(2L))
			{
				Product product2 = mapper.treeToValue(getJsonFromFile(PRODUCT_JSON_FILE_2), Product.class);
				productService.save(product2);
			}
			if (!cartService.exists(1L) && productService.exists(1L) && productService.exists(2L))
			{
				Cart cart1 = mapper.treeToValue(getJsonFromFile(CART_JSON_FILE_1), Cart.class);
				cartService.save(cart1);
			}
		}
		catch (Exception e) {
			log.info("ERROR: failed to load initial data", e);
		}
	}

	private JsonNode getJsonFromFile(String file) {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode result = null;
		String fileWithPath = String.format("%s/%s", JSON_DIR, file);

		try
		{
			if (!StringUtils.isNullOrEmpty(file))
			{
				result = mapper.readTree(new File(fileWithPath));
			}
		}
		catch  (Exception e) {
			log.info("Failed to load json from file: [{}]", fileWithPath);
		}

		return result;
	}
}
