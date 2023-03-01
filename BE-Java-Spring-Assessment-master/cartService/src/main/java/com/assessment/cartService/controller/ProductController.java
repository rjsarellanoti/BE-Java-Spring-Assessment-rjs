package com.assessment.cartService.controller;

import com.assessment.cartService.entity.Product;
import com.assessment.cartService.service.impl.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("cartService/product")
public class ProductController
{
	@Autowired
	private ProductServiceImpl productService;

	@GetMapping(produces = "application/json")
	public Collection<Product> getAll() {
		return productService.get();
	}

	@GetMapping(path = "/{id}", produces = "application/json")
	public Product getProduct(@PathVariable long id) {
		return productService.get(id);
	}

	@PostMapping(produces = "application/json")
	public ResponseEntity<Product> postProduct(@RequestBody Product input) {
		try
		{
			Product result = productService.save(input);
			return new ResponseEntity(result, HttpStatus.OK);
		}
		catch  (Exception e) {
			return new ResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
