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
package org.easy.ldap;


import static org.junit.Assert.*;

import java.util.List;

import org.apache.directory.server.core.annotations.ApplyLdifs;
import org.apache.directory.server.core.integ.AbstractLdapTestUnit;
import org.apache.directory.server.core.integ.FrameworkRunner;
import org.easy.ldap.LdapAdminService;
import org.easy.ldap.LdapEnvironment;
import org.easy.ldap.ServiceFactory;
import org.easy.ldap.LdapEnvironment.FileType;
import org.easy.ldap.LdapEnvironment.PropertyNames;
import org.easy.ldap.model.LdapUser;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;



@RunWith(FrameworkRunner.class)
public class RolesTest extends AbstractLdapTestUnit
{

    private static LdapAdminService adminService = null;

    // TODO add negative tests.
    /**
     * @throws java.lang.Exception
     */
    
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		if (isRunInSuite)
    	{
    		LdapEnvironment envirment = new LdapEnvironment("ldap-context-test.properties", FileType.PROPERTIES);
    		envirment.setProperty(PropertyNames.SERVER_PORT, ldapServer.getPort());
    		ServiceFactory serviceFactory = new ServiceFactory(envirment);
    		adminService = serviceFactory.createAdminService();
    	}
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{
		adminService = null;
	}    

    /**
     * Test method for {@link
     * org.easy.ldap.AdminServiceImpl#addUser(LdapUser} .
     */
    @Test
    public final void testGrantRole()
    {
        // TODO user service factory
    	if (isRunInSuite)
    	{
    		try
    		{
    			LdapUser user = new LdapUser("tnt1","testuser2");
    			user.setCommonName("testuser2");
    			user.setFirstName("Test");
    			user.setLastName("User2");
    			user.setEmail("testuser2@tnt1.easy.org");
    			user.setPassword(user.getUserId());
    			adminService.addUser(user);    			
    			adminService.grantRole(user, "SUPER_USERS");    			
    			//adminService.getUserRoles(user);
    			
    		}
    		catch(RuntimeException e)
    		{
    			fail(e.getMessage());
    		}
        	
    	}
        // TODO check results;
    }

    
    @Test
    public final void testRevokeRole()
    {
        // TODO user service factory
    	if (isRunInSuite)
    	{
    		try
    		{
    			LdapUser user = new LdapUser("tnt1","testuser");
    			user.setCommonName("testuser");
    			user.setFirstName("Test");
    			user.setLastName("User");
    			user.setEmail("testuser@tnt1.easy.org");
    			user.setPassword(user.getUserId());
    			
    			adminService.revokeRole(user, "USERS");    			
    			//adminService.getUserRoles(user);//tbd
    			
    		}
    		catch(RuntimeException e)
    		{
    			fail(e.getMessage());
    		}
        	
    	}
    }
    
    
    @Test
    public final void testAddRole()
    {
    	if (isRunInSuite)
    	{
    		try
    		{
    			adminService.addRole("tnt1", "ADMIN2");
    		}
    		catch(RuntimeException e)
    		{
    			fail(e.getMessage());
    		}        	
    	}    			
    }
    
    
    @Test
    @ApplyLdifs(
    	    {
    	    	"dn: cn=ADMIN2,ou=Roles,o=tnt1,dc=easy,dc=org",
    	    	"objectClass: groupOfUniqueNames",
    	    	"objectClass: top",
    	    	"cn: ADMIN2",
    	    	"uniqueMember: uid=testadmin,ou=People,o=tnt1,dc=easy,dc=org",
    	    	"ou: Roles"
    	    })        
    public final void testDeleteRole()
    {
    	if (isRunInSuite)
    	{
    		try
    		{
    			adminService.deleteRole("tnt1", "ADMIN2");
    		}
    		catch(RuntimeException e)
    		{
    			fail(e.getMessage());
    		}        	
    	}    			
    }    
    
    
    @Test
    public final void testListGrantedRole()
    {
    	if (isRunInSuite)
    	{
    		try
    		{
    			LdapUser user = new LdapUser("tnt1","testuser");
    			user.setCommonName("testuser");
    			user.setFirstName("Test");
    			user.setLastName("User");
    			user.setEmail("testuser@tnt1.easy.org");
    			user.setPassword(user.getUserId());

    			List<String> result = adminService.findGrantedRoles(user);
    			assertNotNull(result);
    			assertTrue(result.size()>0);
    			System.out.println(result.toString());
    		}
    		catch(RuntimeException e)
    		{
    			fail(e.getMessage());
    		}        	
    	}    			
    }


    @Test
    public final void testIsRoleExists()
    {
    	if (isRunInSuite)
    	{
    		try
    		{
    			boolean result = adminService.isRoleExists("tnt1", "USERS");
    			assertTrue(result);
    		}
    		catch(RuntimeException e)
    		{
    			fail(e.getMessage());
    		}        	
    	}    			
    }
    

    @Test
    public final void testIsRoleExistsFalse()
    {
    	if (isRunInSuite)
    	{
    		try
    		{
    			boolean result = adminService.isRoleExists("tnt1","xyz");
    			assertFalse(result);
    		}
    		catch(RuntimeException e)
    		{
    			fail(e.getMessage());
    		}        	
    	}    			
    }    


}
