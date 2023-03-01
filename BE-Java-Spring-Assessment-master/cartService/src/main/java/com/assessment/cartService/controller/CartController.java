package com.assessment.cartService.controller;

import com.assessment.cartService.entity.Cart;
import com.assessment.cartService.service.impl.CartServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("cartService/cart")
public class CartController
{
	@Autowired
	private CartServiceImpl cartService;

	@GetMapping(produces = "application/json")
	public Collection<Cart> getAll() {
		return cartService.get();
	}

	@GetMapping(path = "/{id}", produces = "application/json")
	public Cart getCart(@PathVariable long id) {
		return cartService.get(id);
	}

	@PostMapping(produces = "application/json")
	public ResponseEntity<Cart> postCart(@RequestBody Cart input) {
		try
		{
			Cart result = cartService.submitCart(input);
			return new ResponseEntity(result, HttpStatus.OK);
		}
		catch  (Exception e) {
			return new ResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
