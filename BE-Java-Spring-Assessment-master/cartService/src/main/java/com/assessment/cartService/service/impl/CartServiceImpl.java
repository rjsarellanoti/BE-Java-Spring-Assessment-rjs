package com.assessment.cartService.service.impl;

import com.assessment.cartService.entity.Cart;
import com.assessment.cartService.entity.Product;
import com.assessment.cartService.repository.CartRepository;
import com.assessment.cartService.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class CartServiceImpl extends CrudServiceImpl<Cart>
{
	// FIXME: add ProductServiceImpl dependency
	@Autowired
	ProductRepository productRepository;

	@Autowired
	CartRepository cartRepository;

	@Override
	protected Cart update(Cart input) {
		Cart dbCart = get(input.getId());
		if(dbCart == null) {
			throw new IllegalArgumentException("Attempting to update entity that does not exist");
		}
		// this update method will replace the stored products list with the list from the input
		// any logic to merge or remove items from the cart collection should happen in business logic prior to this
		dbCart.setProducts(input.getProducts());

		return dbCart;
	}

	// FIXME: applicant should finish the implementation of this method
	public Cart submitCart(Cart cart) throws Exception {


		//check product if it exists
		for (Product product : cart.getProducts()) {
			if (!productRepository.existsById(product.getId())) {
				throw new IllegalArgumentException("Product with ID " + product.getId() + " does not exist");
			}
		}

		if (cart.getId() != null && cartRepository.existsById(cart.getId())) {
			// Fetch the persisted cart from the database
			Cart persistedCart = get(cart.getId());

			// Merge the product lists and add their quantities
			Map<Long, Product> mergedProducts = new HashMap<>();

			for (Product product : persistedCart.getProducts()) {
				mergedProducts.put(product.getId(), product);
			}

			for (Product product : cart.getProducts()) {
				long productId = product.getId();
				int quantity = product.getQuantity();

				if (mergedProducts.containsKey(productId)) {
					Product mergedProduct = mergedProducts.get(productId);
					int totalQuantity = mergedProduct.getQuantity() + quantity;

					if (totalQuantity > productRepository.findById(productId).get().getQuantity()) {
						throw new IllegalArgumentException("Quantity of product " + productId + " in cart exceeds available quantity");
					}

					mergedProduct.setQuantity(totalQuantity);
				} else {
					if (quantity > productRepository.findById(productId).get().getQuantity()) {
						throw new IllegalArgumentException("Quantity of product " + productId + " in cart exceeds available quantity");
					}

					mergedProducts.put(productId, product);
				}
			}

			List<Product> mergedProductList = new ArrayList<>(mergedProducts.values());

			// Set the merged product list to the persisted cart
			persistedCart.setProducts(mergedProductList);

			// Save the merged cart
			persistedCart = save(persistedCart);

			return persistedCart;
		} else {
			return save(cart);
		}
	}

}

