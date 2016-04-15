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

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.ldap.LdapName;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.easy.ldap.LdapEnvironment.PropertyNames;


/**
 * Helper class to create LDAP context for the given distinguish name.
 * 
 * @author  mahmood.aftab
 * 
 */
public class LdapContextFactory
{
    private static Log log = LogFactory.getLog(LdapContextFactory.class);
    public static String PROVIDER_URL_TEMPLATE = "ldap://SERVER_NAME:SERVER_PORT/ROOT_DN";
    private LdapEnvironment environment = null;
    private NamingFactory namingFactory=null;
    
    public NamingFactory getNamingFactory()
    {
    	return namingFactory;
    }

    public LdapContextFactory()
    {
    }

    /**
     * @param properties - LDAP connection and structural properties
     */
    public LdapContextFactory(LdapEnvironment properties)
    {
        if (log.isDebugEnabled())
        {
            log.debug("Setting new ldap properties");
            log.debug(properties.toString());
        }

        this.environment = properties;
        
        namingFactory = new NamingFactory(properties);
    }

    /**
     * @param ctx
     */
    public void closeContext(Context ctx)
    {
        if (ctx != null)
        {
            try
            {
                ctx.close();
            }
            catch (NamingException e)
            {
                log.warn(e);
            }
        }
    }

    /**
     * @param enviroment
     * @return
     * @throws NamingException
     */
    public DirContext createContext(Hashtable<String, String> enviroment) throws NamingException
    {
        if (log.isDebugEnabled())
        {
            log.debug("Creating context with enviroment settings of:");
            log.debug(enviroment.toString());
        }

        DirContext ctx = null;
        ctx = new InitialDirContext(enviroment);

        return ctx;
    }

    /**
     * @param rootDn
     * @return
     * @throws NamingException
     */
    public DirContext createContext(String rootDn) throws NamingException
    {
        Hashtable<String, String> properties = getEnviroment();
        properties.put(Context.PROVIDER_URL, createProviderUrl(rootDn));

        return createContext(properties);
    }

    /**
     * @return
     * @throws NamingException
     */
    public DirContext createDomainContext() throws NamingException
    {
        Hashtable<String, String> properties = getEnviroment();
        properties.put(Context.PROVIDER_URL, createProviderUrl(environment.getProperty(PropertyNames.DOMAIN_DN)));

        return createContext(properties);
    }

    /**
     * @param orgId
     * @return
     * @throws NamingException
     */
    public DirContext createOrgContext(String orgId) throws NamingException
    {
        Hashtable<String, String> environment = getEnviroment();
        environment.put(Context.PROVIDER_URL, createProviderUrl(namingFactory.createTenantDn(orgId).toString()));

        return createContext(environment);
    }


    /**
     * @param rootDn
     * @return
     */
    private String createProviderUrl(String rootDn)
    {
            return PROVIDER_URL_TEMPLATE.replace("SERVER_NAME", environment.getProperty(PropertyNames.SERVER_NAME))
                .replace("SERVER_PORT", environment.getProperty(PropertyNames.SERVER_PORT)).replace("ROOT_DN", rootDn);
    }
    
    
    
    public DirContext createSecureContext(LdapName rootDn, LdapName principal, String password, String securityMethod)
    throws NamingException
    {
    	Hashtable<String, String> environment = getEnviroment();
    	environment.put(Context.PROVIDER_URL, createProviderUrl(rootDn.toString()));

    	environment.put(Context.SECURITY_AUTHENTICATION, securityMethod);
    	environment.put(Context.SECURITY_PRINCIPAL, principal.toString());
    	environment.put(Context.SECURITY_CREDENTIALS, password);

    	return createContext(environment);
    }
    

    /**
     * @param orgId
     * @return
     * @throws NamingException
     */
    public DirContext createUsersContext(String orgId) throws NamingException
    {
        Hashtable<String, String> properties = getEnviroment();
        properties.put(Context.PROVIDER_URL, createProviderUrl(namingFactory.createTenantDn(orgId).toString()));

        return createContext(properties);
    }


    /**
     * @return
     */
    private Hashtable<String, String> getEnviroment()
    {
        Hashtable<String, String> properties = new Hashtable<String, String>();

        properties.put(Context.PROVIDER_URL, createProviderUrl(environment.getProperty(PropertyNames.DOMAIN_DN)));

        properties.put(Context.INITIAL_CONTEXT_FACTORY,
        		environment.getProperty(PropertyNames.INITIAL_CONTEXT_FACTORY_CLASS));
        properties.put("com.sun.jndi.ldap.connect.pool", environment.getProperty(PropertyNames.USE_LDAP_CONNECT_POOL));
        properties.put(Context.SECURITY_AUTHENTICATION,
        		environment.getProperty(PropertyNames.ADMIN_AUTHENTICATION_METHOD));
        properties.put(Context.SECURITY_PRINCIPAL, environment.getProperty(PropertyNames.ADMIN_PRINCIPAL));
        properties.put(Context.SECURITY_CREDENTIALS, environment.getProperty(PropertyNames.ADMIN_CREDENTIALS));

        return properties;
    }

    /**
     * @return
     */
    public LdapEnvironment getEnvironment()
    {
        return environment;
    }
    
    /**
     * @return
     */
    public void setEnvironment(LdapEnvironment environment)
    {
        this.environment = environment;
        this.namingFactory = new NamingFactory(environment);
    }    


	/**
	 * @param orgId
	 * @param role
	 * @return
	 * @throws NamingException
	 */
	public DirContext createRoleContext(String orgId, String role) throws NamingException
    {
        Hashtable<String, String> properties = getEnviroment();
        properties.put(Context.PROVIDER_URL, createProviderUrl(namingFactory.createRoleDn(orgId, role).toString()));

        return createContext(properties);
    }
	
    

	/**
	 * @param orgId
	 * @return
	 * @throws NamingException
	 */
	public DirContext createRolesContext(String orgId) throws NamingException
    {
        Hashtable<String, String> properties = getEnviroment();
        properties.put(Context.PROVIDER_URL, createProviderUrl(namingFactory.createRolesDn(orgId).toString()));

        return createContext(properties);
    }    
}
