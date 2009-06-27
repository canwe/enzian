/*
 * Copyright (C) 2008 ProSyst Software GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jolira.enzian.hibernate.internal;

import javax.persistence.EntityManager;

import com.jolira.enzian.hibernate.JpaService;
import com.jolira.enzian.hibernate.ThreadLocalEntityManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

public class ThreadLocalEntityManagerServiceImpl implements ThreadLocalEntityManagerService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ThreadLocalEntityManagerServiceImpl.class);

	private JpaService jpaService;

	private ThreadLocal<EntityManager> entityManagers = new ThreadLocal<EntityManager>();

	@Inject
	public void setDependencies(JpaService jpaService) {
		this.jpaService = jpaService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.modulefusion.api.JpaService#getEntityManager()
	 */
	/* (non-Javadoc)
	 * @see org.modulefusion.hibernate.internal.ThreadLocalEntityManagerService#getEntityManager()
	 */
	public EntityManager getEntityManager() {
		LOGGER.trace("Getting EntityManager for thread '{}'", Thread.currentThread().hashCode());
		EntityManager em = entityManagers.get();
		// if (em == null || (!em.isOpen()) ||
		// (!em.getTransaction().isActive())) {
		if (em == null || (!em.isOpen())) {
			LOGGER.info("Creating new EntityManager");
			em = jpaService.getEntityManagerFactory().createEntityManager();
			entityManagers.set(em);
		}
		LOGGER.trace("EntityManager hashcode is '{}'", em.hashCode());
		return em;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.modulefusion.api.JpaService#beginTransaction()
	 */
	/* (non-Javadoc)
	 * @see org.modulefusion.hibernate.internal.ThreadLocalEntityManagerService#beginTransaction()
	 */
	public void beginTransaction() {
		beginTransaction(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.modulefusion.api.JpaService#beginTransaction(boolean)
	 */
	/* (non-Javadoc)
	 * @see org.modulefusion.hibernate.internal.ThreadLocalEntityManagerService#beginTransaction(boolean)
	 */
	public void beginTransaction(boolean failOnActiveTransaction) {
		LOGGER.info("Transaction start");
		EntityManager em = getEntityManager();
		if (!em.getTransaction().isActive()) {
			em.getTransaction().begin();
		}
		else if (failOnActiveTransaction) {
			IllegalStateException e = new IllegalStateException("Transaction already active");
			LOGGER.error("failOnActiveTransaction was set to true", e);
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.modulefusion.api.JpaService#commitTransaction()
	 */
	/* (non-Javadoc)
	 * @see org.modulefusion.hibernate.internal.ThreadLocalEntityManagerService#commitTransaction()
	 */
	public void commitTransaction() {
		LOGGER.info("Transaction commit");
		EntityManager em = getEntityManager();
		em.flush();
		em.getTransaction().commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.modulefusion.api.JpaService#setRollbackOnlyTransation()
	 */
	/* (non-Javadoc)
	 * @see org.modulefusion.hibernate.internal.ThreadLocalEntityManagerService#setRollbackOnlyTransaction()
	 */
	public void setRollbackOnlyTransaction() {
		LOGGER.info("Transaction rollback-only");
		getEntityManager().getTransaction().setRollbackOnly();
	}

}
