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

import javax.persistence.Entity;
import javax.persistence.EntityManagerFactory;

/**
 * This service creates JPA configurations based on the installed bundles.
 * <br>
 * A bundle that would like to contribute to the configuration has to provide 
 * the file "OSGI-INF/jpa/classes" and list all the entities in it. The entity classes must 
 * be annotated with {@link Entity}.
 * 
 * @author r.roelofsen@prosyst.com (Roman Roelofsen)
 */
public interface JpaService {
	
	/**
	 * Create a {@link EntityManagerFactory} based on the installed bundles.
	 * 
	 * @return The {@link EntityManagerFactory}
	 */
	public EntityManagerFactory getEntityManagerFactory();

}
