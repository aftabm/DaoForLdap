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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.directory.server.core.integ.AbstractLdapTestUnit;
import org.apache.directory.server.core.integ.FrameworkRunner;
import org.easy.ldap.LdapAuthService;
import org.easy.ldap.LdapEnvironment;
import org.easy.ldap.ServiceFactory;
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
public class AuthServiceImplTest extends AbstractLdapTestUnit
{
    private static LdapAuthService authService = null;

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
    		authService = serviceFactory.createAuthService();
    	}
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{
		authService = null;
	}

	@Test
    public final void testIsValidCredentialBadPassword()
    {
    	if (isRunInSuite)
    	{
    		boolean result = authService.isValidUser("tnt1", "testuser", "1243testuser123");
    		assertEquals(false, result);
    	}

    }

    /**
     * Test method for
     * {@link org.easy.ldap.AuthServiceImpl#isValidCredential(java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public final void testIsValidCredentialGoodPassword()
    {
    	if (isRunInSuite)
    	{
    		assertTrue(authService.isValidUser("tnt1", "testuser", "testuser"));
    	}
    }
    
    //@Test(expected=java.lang.IllegalArgumentException.class)
    @Test
    public final void testIsValidCredentialWrongOrg()
    {
    	if (isRunInSuite)
    	{
    		assertFalse(authService.isValidUser("foo", "testuser", "testuser"));
    	}
    }    

    @Test
    public final void testIsValidUserNegitive()
    {
    	if (isRunInSuite)
    	{
    		assertFalse(authService.isUserExists("tnt1", "123testuser123"));
    	}
    }

    /**
     * Test method for
     * {@link org.easy.ldap.AuthServiceImpl#isValidUser(java.lang.String)}
     * .
     */
    @Test
    public final void testIsValidUserPositive()
    {
    	if (isRunInSuite)
    	{
    		assertTrue(authService.isUserExists("tnt1", "testuser"));
    	}
    }

}
