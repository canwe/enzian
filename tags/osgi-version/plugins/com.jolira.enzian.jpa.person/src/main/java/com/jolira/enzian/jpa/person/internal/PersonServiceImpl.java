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
package com.jolira.enzian.jpa.person.internal;

import java.util.List;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.jolira.enzian.jpa.person.Person;
import com.jolira.enzian.jpa.person.PersonService;

public class PersonServiceImpl implements PersonService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PersonServiceImpl.class);

	private EntityManager entityManager;
	
	@Inject
	public PersonServiceImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	public Person savePerson(Person person) {
		entityManager.getTransaction().begin();
		LOGGER.info("save Person");
		Person newP = entityManager.merge(person);
		entityManager.getTransaction().commit();
		return newP;
	}
	
	public void deletePerson(Long id) {
		entityManager.getTransaction().begin();
		LOGGER.info("delete Person");
		entityManager.remove(entityManager.find(Person.class, id));
		entityManager.getTransaction().commit();
	}

	public Person getPerson(Long id) {
		entityManager.getTransaction().begin();
		LOGGER.info("get Person");
		Person person = entityManager.find(Person.class, id);
		entityManager.getTransaction().commit();
		return person;
	}

	@SuppressWarnings("unchecked")
	public List<Person> getPersonList() {
		entityManager.getTransaction().begin();
		LOGGER.info("list all persons");
		List<Person> list = entityManager.createQuery("from Person").getResultList();
		entityManager.getTransaction().commit();
		return list;
	}
	
}
