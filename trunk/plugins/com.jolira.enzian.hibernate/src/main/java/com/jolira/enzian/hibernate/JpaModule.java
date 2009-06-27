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

import static org.ops4j.peaberry.Peaberry.service;

import javax.persistence.EntityManager;

import com.jolira.enzian.hibernate.internal.TransactionInterceptor;
import org.ops4j.peaberry.Peaberry;
import org.osgi.framework.BundleContext;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

/**
 * This module contains the following artifacts:
 * <ul>
 * <li>{@link ThreadLocalEntityManagerService} - a service useful for
 * interaction with {@link EntityManager}s and transactions</li>
 * 
 * <li>{@link EntityManager} - a thread local {@link EntityManager} instance</li>
 * 
 * <li>{@link Transactional} - a method annotation used to mark a transactional
 * method</li>
 * </ul>
 * Applications will normally either use the
 * {@link ThreadLocalEntityManagerService} or the thread local
 * {@link EntityManager} and {@link Transactional} annotation.
 * 
 * @author r.roelofsen@prosyst.com (Roman Roelofsen)
 */
public class JpaModule extends AbstractModule {

	private final BundleContext context;

	public JpaModule(BundleContext context) {
		this.context = context;
	}

	public void configure() {
		TransactionInterceptor i = new TransactionInterceptor(context);
		bindInterceptor(Matchers.any(), Matchers.annotatedWith(Transactional.class), i);

		bind(EntityManager.class).toProvider(service(EntityManager.class).single());

		bind(ThreadLocalEntityManagerService.class).toProvider(
				Peaberry.service(ThreadLocalEntityManagerService.class).single());
	}
}
