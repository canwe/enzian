/**
 * (C) 2009 jolira (http://www.jolira.com). Licensed under the GNU General
 * Public License, Version 3.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * http://www.gnu.org/licenses/gpl-3.0-standalone.html Unless required by
 * applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
 * OF ANY KIND, either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */
package com.jolira.enzian.app.internal;

import org.apache.wicket.Request;
import org.apache.wicket.authentication.AuthenticatedWebApplication;
import org.apache.wicket.authentication.AuthenticatedWebSession;
import org.apache.wicket.authorization.strategies.role.Roles;

import com.jolira.enzian.app.EnzianSession;

/**
 * 
 * @author Gabriel Hopper
 *
 */
public class EnzianSessionImpl extends 
		EnzianSession {


	public EnzianSessionImpl(Request request) {
		super(request);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean authenticate(String username, String password) {
		// Check username and password
		//TODO: implement authentication
		return username.equals("enzian") && password.equals("enzian");
	}

	@Override
	public Roles getRoles() {
		if (isSignedIn())
		{
			// If the user is signed in, they have these roles
			return new Roles(Roles.ADMIN);
		}
		return null;
	}

}
