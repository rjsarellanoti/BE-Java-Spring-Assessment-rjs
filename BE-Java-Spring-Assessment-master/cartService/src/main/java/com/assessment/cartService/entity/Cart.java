package com.assessment.cartService.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class Cart extends EntityBase
{

	@ManyToMany
	@JoinTable(
			name = "cart_products",
			joinColumns = @JoinColumn(name = "cart_id"),
			inverseJoinColumns = @JoinColumn(name = "product_id")
	)
	private List<Product> products;

	public List<Product> getProducts()
	{
		return products;
	}

	public void setProducts(List<Product> products)
	{
		this.products = products;
	}

	// *** derived fields:

	// returns the total price of all products in the cart
	public BigDecimal getTotalPrice() {
		double priceSum = products.stream().map(Product::getPrice).collect(Collectors.summingDouble(i->i));
		return new BigDecimal(priceSum).setScale(2, RoundingMode.HALF_UP);
	}
}
