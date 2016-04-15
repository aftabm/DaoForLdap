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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.common.base.Preconditions;

/**
 * Facilitates creating service class instances
 * 
 * 
 * 
 * @author  mahmood.aftab
 */
public class ServiceFactory
{
    private static Log log = LogFactory.getLog(ServiceFactory.class);

    private  LdapEnvironment properties=null;
    
    public ServiceFactory()
    {
    }
    
    public ServiceFactory(LdapEnvironment properties)
    {
    	this.properties = properties;
    }
    /**
     * Creates service for LDAP CRUD operations
     * 
     * @param properties
     *            - LDAP connection and structure properties.
     * @return
     */
    public LdapAdminService createAdminService()
    {
    	Preconditions.checkState(properties!=null);
        return new AdminServiceImpl(properties);
    }

    /**
     * Creates service for LDAP authentication operations
     * 
     * @param properties
     *            - LDAP connection and structure properties.
     * @return
     */
    public LdapAuthService createAuthService()
    {
    	Preconditions.checkState(properties!=null);
    	return new AuthServiceImpl(properties);
    }
    
    
    public NamingFactory createNamingFactory()
    {
    	Preconditions.checkState(properties!=null);
    	return new NamingFactory(properties);
    }
    
    public LdapContextFactory createLdapContextFactory()
    {
    	Preconditions.checkState(properties!=null);
    	return new LdapContextFactory(properties);    	
    }
    
    public LdapDao createLdapDao()
    {
    	Preconditions.checkState(properties!=null);
    	return new LdapDao(properties);    	
    }
    
    
    
}
