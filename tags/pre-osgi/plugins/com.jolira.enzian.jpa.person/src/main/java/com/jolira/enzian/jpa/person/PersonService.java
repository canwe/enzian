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
package com.jolira.enzian.jpa.person;

import java.util.List;

/**
 * The PersonService can be used to save/delete/etc instances
 * of {@link Person}.
 * 
 * @author r.roelofsen@prosyst.com (Roman Roelofsen)
 */
public interface PersonService {
	
	/**
	 * Persist a Person instance
	 * 
	 * @param person The instance that should be persisted 
	 * @return The persisted entity
	 */
	public Person savePerson(Person person);
	
	/**
	 * Delete a Person
	 * 
	 * @param id The ID of the person instance
	 */
	public void deletePerson(Long id);
	
	/**
	 * Load a Person
	 * 
	 * @param id The ID of the person
	 * @return
	 */
	public Person getPerson(Long id);
	
	/**
	 * Get a list of all persons
	 * 
	 * @return The list of persons
	 */
	public List<Person> getPersonList();

}
