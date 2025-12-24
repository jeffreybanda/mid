package com.mid.app.common.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public abstract class GenericRepository<T> {

	protected abstract Class<T> getPersistentClass();

	protected abstract EntityManager getEntityManager();

	public T add(final T entity) {
		getEntityManager().persist(entity);
		return entity;
	}

	public T findById(final Long id) {
		if (id == null) {
			return null;
		}
		return getEntityManager().find(getPersistentClass(), id);
	}

	public T findByKey(final String polNumber, final Integer rencnt, final Integer endtcnt) {
		if (polNumber == null) {
			return null;
		}
		return null;
	}

	public void update(final T entity) {
		getEntityManager().merge(entity);
	}

	@SuppressWarnings("unchecked")
	public List<T> findAll(final String orderField) {
		return getEntityManager().createQuery(
				"Select e From " + getPersistentClass().getSimpleName() + " e Order by e." + orderField)
				.setMaxResults(30)
				.getResultList();
	}

	public boolean alreadyExists(final String propertyName, final String propertyValue) {
		final StringBuilder jpql = new StringBuilder();
		jpql.append("Select 1 From " + getPersistentClass().getSimpleName() + " e where e." + propertyName
				+ " = :propertyValue");

		final Query query = getEntityManager().createQuery(jpql.toString());
		query.setParameter("propertyValue", propertyValue);

		return query.setMaxResults(1).getResultList().size() > 0;
	}

	public boolean existsById(final Long id) {
		return getEntityManager()
				.createQuery("Select 1 From " + getPersistentClass().getSimpleName() + " e where e.id = :id")
				.setParameter("id", id)
				.setMaxResults(1)
				.getResultList().size() > 0;
	}

}