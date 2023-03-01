package com.assessment.cartService.service.impl;

import com.assessment.cartService.entity.EntityBase;
import com.assessment.cartService.service.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Date;
import java.util.Optional;

public abstract class CrudServiceImpl<T extends EntityBase> implements CrudService<T>
{
	@Autowired
	private JpaRepository<T, Long> repoBase;

	public T get(Long id) {
		if(id == null) {
			throw new IllegalArgumentException();
		}

		Optional<T> entityOptional = repoBase.findById(id);
		return entityOptional.isPresent() ? entityOptional.get() : null;
	}

	public Collection<T> get() {
		return repoBase.findAll();
	}

	public T save(T item) {
		if(item == null) {
			throw new IllegalArgumentException();
		}
		if(item.getId() != null && repoBase.existsById(item.getId())) {
			item = update(item);
		}
		setTimeStamps(item);

		return repoBase.save(item);
	}

	// this should be implemented by any extending classes
	// this should merge the incoming data with the existing data saved in the db
	abstract protected T update(T item);

	public Collection<T> save(Collection<T> items) {
		if(CollectionUtils.isEmpty(items)) {
			throw new IllegalArgumentException();
		}
		// TODO: check if items need to be updated
		// TODO: set the timestamps of the items
		for (T item : items) {
			if (item.getId() != null && repoBase.existsById(item.getId())) {
				item = update(item);
			}
			setTimeStamps(item);
		}

		return repoBase.saveAll(items);
	}

	public boolean exists(Long id) {
		return repoBase.existsById(id);
	}

	private void setTimeStamps(T item) {
		if(item.getId() == null || !repoBase.existsById(item.getId())) {
			item.setCreatedOn(new Date());
		}
		item.setLastModified(new Date());
	}
}
