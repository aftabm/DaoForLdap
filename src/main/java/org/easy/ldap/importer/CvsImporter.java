/**
 *   Copyright © 2011 Aftab Mahmood
 * 
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 **/
package org.easy.ldap.importer;

import org.easy.ldap.AdminServiceImpl;
import org.easy.ldap.LdapEnvironment;
import org.easy.ldap.importer.CvsRow.Column;
import org.easy.ldap.model.LdapUser;

public class CvsImporter 
{
	private LdapEnvironment environment=null;
	public enum Location{CLASSPATH, FOLDER};

	public CvsImporter(LdapEnvironment environment) 
	{
		this.environment = environment;
	}

	public void cvsImport(String filename, String charSet, Location location) 
	{
		CvsFile file = null;
		
		try 
		{
			file = new CvsFile(filename, charSet, location);
			file.open();
			
			while (file.hasNext())
			{
				CvsRow row = file.read();
				
				saveToLdap(row);
			}
		} 
		catch (Exception e) 
		{
			throw new RuntimeException(e);
		}
	}

	private  void saveToLdap(CvsRow row) 
	{
		LdapUser user = toLdapUser(row);
		AdminServiceImpl adminService = new AdminServiceImpl(environment);
		adminService.addUser(user);
		adminService.grantRole(user, user.getRoles().get(0));
	}

	private LdapUser toLdapUser(CvsRow row) 
	{
		LdapUser out = new LdapUser(row.getProperty(Column.Tenant_Name), row.getProperty(Column.User_Name));
		out.setFirstName(row.getProperty(Column.First_Name));
		out.setEmail(row.getProperty(Column.Email_Address));
		out.setLastName(row.getProperty(Column.Last_Name));
		out.setPassword(row.getProperty(Column.Password));
		out.setCommonName(out.getFirstName() + " " + out.getLastName());
		out.getRoles().add(row.getProperty(Column.Role));
		
		return out;
	}
}