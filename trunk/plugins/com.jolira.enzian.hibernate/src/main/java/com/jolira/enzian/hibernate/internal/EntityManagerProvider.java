package com.jolira.enzian.hibernate.internal;

import javax.persistence.EntityManager;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class EntityManagerProvider implements Provider<EntityManager> {
	
	@Inject
	private ThreadLocalEntityManagerProxy proxy;
	
	public EntityManager get() {
		return proxy.createProxy();
	}

}
