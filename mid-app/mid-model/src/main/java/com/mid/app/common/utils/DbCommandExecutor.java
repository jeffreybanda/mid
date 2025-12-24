package com.mid.app.common.utils;

import javax.persistence.EntityManager;

public class DbCommandExecutor {

	EntityManager em;

	public DbCommandExecutor(final EntityManager em) {

		this.em = em;
	}

	public <T> T executeCommand(final DBCommand<T> dbCommand) {
		try {
			em.getTransaction().begin();
			final T toReturn = dbCommand.execute();
			em.getTransaction().commit();

			em.clear();

			return toReturn;

		} catch (final Exception e) {
			em.getTransaction().rollback();
			throw new IllegalArgumentException(e);
		}

	}

}
