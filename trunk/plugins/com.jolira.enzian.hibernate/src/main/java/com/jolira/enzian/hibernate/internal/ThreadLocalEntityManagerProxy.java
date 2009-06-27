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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import javax.persistence.EntityManager;

import com.jolira.enzian.hibernate.ThreadLocalEntityManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

public class ThreadLocalEntityManagerProxy implements InvocationHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(ThreadLocalEntityManagerProxy.class);
	
	@Inject
	private ThreadLocalEntityManagerService entityManagerService;

	public EntityManager createProxy() {
		Class<?>[] ifaces = { EntityManager.class };
		EntityManager em = (EntityManager) Proxy.newProxyInstance(this.getClass().getClassLoader(), ifaces, this);
		return em;
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		LOGGER.trace("Delegating method call '{}'", method.getName());
		return method.invoke(entityManagerService.getEntityManager(), args);
	}

}
