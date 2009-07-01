package com.jolira.enzian.hibernate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public interface EntityManagerProxy {
	
	EntityManagerFactory getEntityManagerFactory();
	
	EntityManager getEntityManager();

}
