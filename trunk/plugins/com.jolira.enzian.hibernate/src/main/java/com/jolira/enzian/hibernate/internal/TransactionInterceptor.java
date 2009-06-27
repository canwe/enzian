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

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import com.jolira.enzian.hibernate.ThreadLocalEntityManagerService;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransactionInterceptor implements MethodInterceptor {

	static final Logger LOGGER = LoggerFactory.getLogger(TransactionInterceptor.class);

	private ServiceTracker tracker;

	public TransactionInterceptor(BundleContext context) {
		tracker = new ServiceTracker(context, ThreadLocalEntityManagerService.class.getName(), null);
		tracker.open();
	}

	public Object invoke(MethodInvocation mi) throws Throwable {
		ThreadLocalEntityManagerService service = (ThreadLocalEntityManagerService) tracker.getService();
		if (service == null) {
			final RuntimeException serviceException = new NullPointerException("ThreadLocalEntityManagerService must not be null!");
			LOGGER.error("ThreadLocalEntityManagerService not available!", serviceException);
			throw serviceException;
		}
		try {
			LOGGER.trace("Executing transactional method '{}'", mi.getMethod().getName());

			boolean txOpenedByThisMethod = false;
			boolean activeTx = service.getEntityManager().getTransaction().isActive();
			if (!activeTx) {
				LOGGER.trace("Starting transaction for method call");
				service.beginTransaction();
				txOpenedByThisMethod = true;
			}
			else {
				LOGGER.trace("Found Active transaction");
			}
			Object result = mi.proceed();
			if (txOpenedByThisMethod) {
				service.commitTransaction();
			}
			return result;
		}
		catch (Throwable e) {
			LOGGER.error("Error while executing transactional method. Setting rollback-only", e);
			service.setRollbackOnlyTransaction();
			throw new RuntimeException(e);
		}
	}

}
