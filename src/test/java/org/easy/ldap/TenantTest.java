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
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;



@RunWith(FrameworkRunner.class)
public class TenantTest extends AbstractLdapTestUnit
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


    
    @Test
    public final void testAddTenant()
    {
    	if (isRunInSuite)
    	{
    		try
    		{
    			adminService.addTenant("tnt2");
    		}
    		catch(RuntimeException e)
    		{
    			fail(e.getMessage());
    		}        	
    	}    			
    }
    
    
    @Test
    public final void testIsTenantExists()
    {
    	if (isRunInSuite)
    	{
    		try
    		{
    			boolean result = adminService.isTenantExists("tnt1");
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
    			boolean result = adminService.isTenantExists("tnt2");
    			assertFalse(result);
    		}
    		catch(RuntimeException e)
    		{
    			fail(e.getMessage());
    		}        	
    	}    			
    }    
    
    @Ignore
    @Test
    @ApplyLdifs(
    	    {
    	    	"dn: o=tnt2,dc=easy,dc=org",
    	    	"objectClass: organization",
    	    	"objectClass: top",
    	    	"o: tnt1",

    	    	"dn: ou=People,o=tnt2,dc=easy,dc=org",
    	    	"objectClass: organizationalUnit",
    	    	"objectClass: top",
    	    	"ou: People",    	  
    	    	
    	    	"dn: ou=Roles,o=tnt2,dc=easy,dc=org",
    	    	"objectClass: organizationalUnit",
    	    	"objectClass: top",
    	    	"ou: Roles"    	    
    	    })        
    
    public final void testDeleteTenant()
    {
    	if (isRunInSuite)
    	{
    		try
    		{
    		//	adminService.deleteTenant("tnt2");
    		}
    		catch(RuntimeException e)
    		{
    			fail(e.getCause().toString());
    		}        	
    	}    			
    }    
    
    @Ignore
    @Test
    public final void testListTenent()
    {
    	if (isRunInSuite)
    	{
    		try
    		{
    			List<String> result=null;// adminService.findeAllTenants();
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
}
