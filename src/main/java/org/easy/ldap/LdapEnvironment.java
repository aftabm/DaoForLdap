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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.common.base.Preconditions;

/**
 * @author  mahmood.aftab
 * 
 */
public class LdapEnvironment
{
	 private static Log log = LogFactory.getLog(LdapEnvironment.class);

	 public static String USER_AUTHENTICATION_METHOD="simple";
	 
    public enum FileType 
    {
        XML, PROPERTIES
    }
    
    public enum RdnType
    {
    	dc, o, ou, uid, cn;    	
    }

    public enum PropertyNames 
    {
        SERVER_NAME, SERVER_PORT, DOMAIN_DN, TNT_RDN_TYPE, USERS_RDN, USERS_RDN_TYPE,USERS_RDN_VALUE,
        USER_RDN_TYPE, INITIAL_CONTEXT_FACTORY_CLASS, USE_LDAP_CONNECT_POOL, 
        ADMIN_AUTHENTICATION_METHOD, ADMIN_PRINCIPAL, ADMIN_CREDENTIALS, ADMIN_PASSWORD_ENCRIPTION_METHOD, 
        DEFAULT_ROLE, ROLES_RDN,ROLES_RDN_TYPE,ROLES_RDN_VALUE,ROLE_RDN_TYPE, USERS_RDN_NAME, 
        ROLES_RDN_NAME, HIGHEST_ROLE, LOWEST_ROLE;
        
        Properties pop = new Properties();
    }

    public static LdapEnvironment getDefaults()
    {
        if (defaultInstance == null)
        {
            synchronized (LdapEnvironment.class)
            {
                if (defaultInstance == null)
                {
                    try
                    {
	                    defaultInstance = new LdapEnvironment(DEFAULT_PROP_FILENAME, FileType.PROPERTIES);
                    }
                    catch (IOException e)
                    {
	                    log.debug(e.getMessage(), e);
                    }
                }
            }
        }

        return defaultInstance;
    }

    private Properties properties = null;
    private static final long serialVersionUID = 9055719872250575657L;

    private static final String DEFAULT_PROP_FILENAME = "ldap-context-defaults.properties";

    private static LdapEnvironment defaultInstance = null;

    public LdapEnvironment()
    {
    }
    

    public LdapEnvironment(Properties newProperties)
    {
    	Preconditions.checkNotNull(newProperties);
    	
    	if (log.isDebugEnabled())
    	{
    		log.debug("LDAP Properties");
    		
    		for (Entry entry: newProperties.entrySet())
    		{
    			log.debug("key="+entry.getKey()+" value="+entry.getValue());
    		}
    	}

        setProperties(newProperties);
    }

    public LdapEnvironment(String filename, FileType fileType) throws IOException
    {
        this.setProperties(this.readFromFile(filename, fileType));
    }



    private Properties readFromFile(String filename, FileType fileType) throws IOException
    {
        InputStream file = null;
        Properties out = null;

        try
        {
            file = this.getClass().getClassLoader().getResourceAsStream(filename);
            
            if (file==null)
            	throw new FileNotFoundException("Unable to find "+filename+" on classpath");
            
            out = new Properties();

            switch (fileType)
            {
            case PROPERTIES:
                out.load(file);
                break;
            case XML:
                out.loadFromXML(file);
                break;
            }
        }
        finally
        {
            if (file != null)
                file.close();
        }

        return out;

    }

    
    public Properties getProperties()
    {
        // TODO return clone?
        return this.properties;
    }

    public String getProperty(PropertyNames key)
    {
        return properties.getProperty(key.toString());
    }    
    
     public void setProperty(PropertyNames key, Object value) 
     {
    	  properties.setProperty(key.toString(), value.toString()); 
     }
     

    public void setProperties(Properties newProperties)
    {
        this.properties = newProperties;
    }

    @Override
    public String toString()
    {

        return properties.toString();
    }

}
