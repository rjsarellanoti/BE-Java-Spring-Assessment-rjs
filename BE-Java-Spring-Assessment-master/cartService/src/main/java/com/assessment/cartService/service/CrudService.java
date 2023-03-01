package com.assessment.cartService.service;

import com.assessment.cartService.entity.EntityBase;

import java.util.Collection;

public interface CrudService<T extends EntityBase>
{
	T get(Long id);
	Collection<T> get();
	T save(T item);
	Collection<T> save(Collection<T> items);
	boolean exists(Long id);
}
