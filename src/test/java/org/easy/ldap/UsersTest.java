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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.directory.server.core.annotations.ApplyLdifs;
import org.apache.directory.server.core.integ.AbstractLdapTestUnit;
import org.apache.directory.server.core.integ.FrameworkRunner;
import org.easy.ldap.LdapAdminService;
import org.easy.ldap.LdapEnvironment;
import org.easy.ldap.RdnType;
import org.easy.ldap.ServiceFactory;
import org.easy.ldap.LdapEnvironment.FileType;
import org.easy.ldap.LdapEnvironment.PropertyNames;
import org.easy.ldap.model.LdapUser;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;



/**
 * @author mahmood.aftab
 * 
 */

@RunWith(FrameworkRunner.class)
public class UsersTest extends AbstractLdapTestUnit
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
    public final void testAddUser()
    {
        // TODO user service factory
    	if (isRunInSuite)
    	{
    		try
    		{
    			LdapUser user = new LdapUser("tnt1", "testuser2");
    			user.setCommonName("testuser2");
    			user.setFirstName("Test");
    			user.setLastName("User2");
    			user.setEmail("testuser2@tnt1.easy.org");
    			user.setPassword(user.getUserId());
    			adminService.addUser(user);
    			assertTrue(true);
    		}
    		catch(RuntimeException e)
    		{
    			fail(e.getMessage());
    		}
        	
    	}
        // TODO check results;
    }

    /**
     * Test method for
     * {@link org.easy.ldap.AdminServiceImpl#deleteUser(java.lang.String)}
     * .
     */
    @Test
    @ApplyLdifs(
    {
    	"dn: uid=testuser2,ou=People,o=tnt1,dc=easy,dc=org",
    	"objectClass: organizationalPerson",
    	"objectClass: person",
    	"objectClass: extensibleObject",
    	"objectClass: inetOrgPerson",
    	"objectClass: top",
    	"cn: Test User2",
    	"givenname: Test",
    	"mail: testuser2@tnt1.easy.org",
    	"ou: People",
    	"sn: User",
    	"uid: testuser2",
    	"userPassword::"
    })  
    public final void testDeleteUser()
    {
    	if (isRunInSuite)
    	{
    		adminService.deleteUser(new LdapUser("tnt1", "testuser2"));    		
    		assertTrue(true);
    	}
    }

    /**
     * Test method for
     * {@link org.easy.ldap.AdminServiceImpl#deleteUser(java.lang.String)}
     * .
     */    
    @Test(expected=RuntimeException.class)
    public final void testDeleteUserNagitive()
    {
    	if (isRunInSuite)
    	{
  			adminService.deleteUser(new LdapUser("tnt1", "testuser33"));    			
    	}
    }    
    
    /**
     * Test method for
     * {@link org.easy.ldap.AdminServiceImpl#updateUser(java.lang.String, java.lang.String, java.lang.String)}
     * .
     */
    @Test
    @ApplyLdifs(
    	    {
    	    	"dn: uid=testuser2,ou=People,o=tnt1,dc=easy,dc=org",
    	    	"objectClass: organizationalPerson",
    	    	"objectClass: person",
    	    	"objectClass: extensibleObject",
    	    	"objectClass: inetOrgPerson",
    	    	"objectClass: top",
    	    	"cn: Test User2",
    	    	"givenname: Test",
    	    	"mail: testuser2@tnt1.easy.org",
    	    	"ou: People",
    	    	"sn: User",
    	    	"uid: testuser2",
    	    	"userPassword::"
    	    })    
    public final void testUpdateUser()
    {
    	if (isRunInSuite)
    	{
    		Map<RdnType, String> newData = new HashMap<RdnType, String>();
    		newData.put(RdnType.CN, "Test User2b");
    		
    		adminService.updateUser(new LdapUser("tnt1", "testuser2"),newData );
    		assertTrue(true);
    	}
    }

    
    
    /**
     * Test method for
     * {@link org.easy.ldap.AdminServiceImpl#updateUser(java.lang.String, java.lang.String, java.lang.String)}
     * .
     */
    @Test(expected=RuntimeException.class)
    public final void testUpdateUserNagitive()
    {
    	if (isRunInSuite)
    	{
    		Map<RdnType, String> newData = new HashMap<RdnType, String>();
    		newData.put(RdnType.CN, "Test User33");
    		adminService.updateUser(new LdapUser("tnt1", "testuser3"),newData );
    	}
    }
 

    /**
     * 
     */
    @Test
    @ApplyLdifs(
    	    {
    	    	"dn: uid=testuser2,ou=People,o=tnt1,dc=easy,dc=org",
    	    	"objectClass: organizationalPerson",
    	    	"objectClass: person",
    	    	"objectClass: extensibleObject",
    	    	"objectClass: inetOrgPerson",
    	    	"objectClass: top",
    	    	"cn: Test User2",
    	    	"givenname: Test",
    	    	"mail: testuser2@tnt1.easy.org",
    	    	"ou: People",
    	    	"sn: User",
    	    	"uid: testuser2",
    	    	"userPassword:: testuser2"
    	    })    
    public final void testFindUser()
    {
    	if (isRunInSuite)
    	{
    		LdapUser example = new LdapUser("tnt1", "testuser2");
    		
    		LdapUser result = adminService.findUniqueUser(example);
    		assertNotNull(result);
    		assertEquals("testuser2@tnt1.easy.org", result.getEmail());
    		
    	}
    }
    
    
    @Test
    @ApplyLdifs(
    	    {
    	    	"dn: uid=testuser1,ou=People,o=tnt1,dc=easy,dc=org",
    	    	"objectClass: organizationalPerson",
    	    	"objectClass: person",
    	    	"objectClass: extensibleObject",
    	    	"objectClass: inetOrgPerson",
    	    	"objectClass: top",
    	    	"cn: Test User1",
    	    	"givenname: Test",
    	    	"mail: testuser1@tnt1.easy.org",
    	    	"ou: Users",
    	    	"sn: User",
    	    	"uid: testuser1",
    	    	"userPassword:: testuser1",
    	    	"dn: uid=testuser2,ou=People,o=tnt1,dc=easy,dc=org",
    	    	"objectClass: organizationalPerson",
    	    	"objectClass: person",
    	    	"objectClass: extensibleObject",
    	    	"objectClass: inetOrgPerson",
    	    	"objectClass: top",
    	    	"cn: Test User2",
    	    	"givenname: Test",
    	    	"mail: testuser2@tnt1.easy.org",
    	    	"ou: Users",
    	    	"sn: User",
    	    	"uid: testuser2",
    	    	"userPassword:: testuser2"
    	    }
    	    
    	    )    
    public final void testFindUsers()
    {
    	if (isRunInSuite)
    	{
    		LdapUser example = new LdapUser("tnt1", null);
    		example.setGivenName("Test");
    		
    		List<LdapUser> result = adminService.findAllUsers(example);
    		
    		assertNotNull(result);
    		assertTrue(result.size()>0);
    	}
    }  
    
    
    
    @Test
    public final void testIsUserExists()
    {
    	if (isRunInSuite)
    	{
    		try
    		{
    			boolean result = adminService.isUserExists("tnt1", "testuser");
    			assertTrue(result);
    		}
    		catch(RuntimeException e)
    		{
    			fail(e.getMessage());
    		}        	
    	}    			
    }
    

    @Test
    public final void testIsTenantExistsFalse()
    {
    	if (isRunInSuite)
    	{
    		try
    		{
    			boolean result = adminService.isUserExists("tnt1", "foo");
    			assertFalse(result);
    		}
    		catch(RuntimeException e)
    		{
    			fail(e.getMessage());
    		}        	
    	}    			
    }        
}
