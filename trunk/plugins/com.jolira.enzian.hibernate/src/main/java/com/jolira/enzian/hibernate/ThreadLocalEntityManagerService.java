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
package com.jolira.enzian.hibernate;

import javax.persistence.EntityManager;

/**
 * Service to ease the use of {@link EntityManager}s.<br>
 * <br>
 * This service allow access to a thread local {@link EntityManager}.
 * Applications that use one {@link EntityManager} per thread can use this
 * service for interaction with the {@link EntityManager} and transaction
 * control.<br>
 * <br>
 * Implementations of this interface need to manage a list of thread local
 * {@link EntityManager}s. All the methods in this interface must use the
 * {@link EntityManager} that belongs to the calling thread.
 * 
 * @author r.roelofsen@prosyst.com (Roman Roelofsen)
 */
public interface ThreadLocalEntityManagerService {

	/**
	 * Get the {@link EntityManager} that belongs to the current thread. If no
	 * such {@link EntityManager} exists yet it will be created.
	 * 
	 * @return The {@link EntityManager}
	 */
	public abstract EntityManager getEntityManager();

	/**
	 * Start a transaction. This method will do nothing if a transaction is
	 * already active. This is like calling {@code beginTransaction(false)}.
	 */
	public abstract void beginTransaction();

	/**
	 * Start a transaction.
	 * 
	 * @param failOnActiveTransaction If {@code true}, this method will throw an
	 *            {@link IllegalStateException} in the case of an already active
	 *            transaction associated with the {@link EntityManager} that
	 *            belongs to the current thread. If {@code false}, this method
	 *            will start a new transaction or do nothing if there is already
	 *            an active one.
	 */
	public abstract void beginTransaction(boolean failOnActiveTransaction);

	/**
	 * Commit the current transaction.
	 */
	public abstract void commitTransaction();

	/**
	 * Mark the current transaction as rollback only.
	 */
	public abstract void setRollbackOnlyTransaction();

}
