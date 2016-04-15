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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import javax.naming.Context;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

import org.apache.directory.server.core.integ.AbstractLdapTestUnit;
import org.apache.directory.server.core.integ.FrameworkRunner;
import org.easy.ldap.LdapContextFactory;
import org.easy.ldap.LdapEnvironment;
import org.easy.ldap.LdapEnvironment.FileType;
import org.easy.ldap.LdapEnvironment.PropertyNames;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * @author mahmood.aftab
 * 
 */
@RunWith(FrameworkRunner.class)
public class ContextFactoryTest extends AbstractLdapTestUnit
{

    /**
     * @throws java.lang.Exception
     */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{
	}	

    /**
     * Test method for
     * {@link org.easy.ldap.LdapDao#createContext(java.lang.String)}
     * .
     */
    @Test
    public final void testCreateContextString()
    {
        // fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for
     * {@link org.easy.ldap.LdapDao#createContext(java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public final void testCreateContextStringString()
    {
    	if (isRunInSuite)
    	{
	        try
	        {
	            LdapEnvironment envirment = new LdapEnvironment("ldap-context-test.properties", FileType.PROPERTIES);
	            envirment.setProperty(PropertyNames.SERVER_PORT, ldapServer.getPort());
	            
	            Context ctx = new LdapContextFactory(envirment).createDomainContext();
	            assertNotNull(ctx);
	            NamingEnumeration<NameClassPair> list = ctx.list("");
	            assertNotNull(list);
	
	            assertTrue(list.hasMoreElements());
	
	            NameClassPair resultElement = list.nextElement();
	            assertNotNull(resultElement);
	            assertEquals("uid=admin", resultElement.getName());
	
	            ctx.close();
	        }
	        catch (NamingException e)
	        {
	            fail(e.getMessage());
	        }
	        catch (IOException e)
	        {
	            fail(e.getMessage());
	        } catch (Exception e) 
	        {
	        	fail(e.getMessage());
			}
    	}
    }

}
