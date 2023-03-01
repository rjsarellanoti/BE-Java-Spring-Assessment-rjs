package com.assessment.cartService.service.impl;

import com.assessment.cartService.entity.Product;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl extends CrudServiceImpl<Product>
{
	@Override
	protected Product update(Product input) {
		Product dbProduct = get(input.getId());
		if(dbProduct == null) {
			throw new IllegalArgumentException("Attempting to update entity that does not exist");
		}
		// only merge selected incoming data
		dbProduct.setName(input.getName());
		dbProduct.setDescription(input.getDescription());
		dbProduct.setPrice(input.getPrice());
		dbProduct.setQuantity(input.getQuantity());

		return dbProduct;
	}
}
