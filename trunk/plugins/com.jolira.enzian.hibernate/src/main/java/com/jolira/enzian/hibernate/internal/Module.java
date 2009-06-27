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

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;

import com.jolira.enzian.hibernate.JpaService;
import com.jolira.enzian.hibernate.ThreadLocalEntityManagerService;
import com.jolira.enzian.guice.BundleModule;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;

public class Module extends BundleModule {

	public Module(BundleContext context) {
		super(context);
	}

	protected void createModule() {
		bind(JpaService.class).to(JpaServiceHibernate.class).asEagerSingleton();
		Map<String,String> map = new HashMap<String, String>();
		map.put(Constants.SERVICE_PID, "org.modulefusion.hibernate.jpaservicehibernate");
		exportService(JpaService.class, map);

		bind(ThreadLocalEntityManagerService.class).to(ThreadLocalEntityManagerServiceImpl.class);
		exportService(ThreadLocalEntityManagerService.class);

		bind(EntityManager.class).toProvider(EntityManagerProvider.class);
		exportService(EntityManager.class);
	}

}
