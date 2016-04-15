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

import java.util.Arrays;
import javax.naming.InvalidNameException;
import javax.naming.directory.Attribute;
import javax.naming.directory.BasicAttribute;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.easy.ldap.LdapEnvironment.PropertyNames;

/**
 * @author mahmood.aftab
 *
 * tenant = org = division = department
 */
public class NamingFactory
{
    private static Log log = LogFactory.getLog(NamingFactory.class);
    
    private LdapEnvironment environment = null;

    public NamingFactory()
    {
    }

    /**
     * @param properties - LDAP connection and structural properties
     */
    public NamingFactory(LdapEnvironment properties)
    {
        if (log.isDebugEnabled())
        {
            log.debug("Setting new ldap properties");
            log.debug(properties.toString());
        }

        this.environment = properties;
    }

    

    /**
     * @param tenantId
     * @return
     */
    public LdapName createTenantDn(String tenantId)
    {
        StringBuilder sb = new StringBuilder();

        sb.append(environment.getProperty(PropertyNames.TNT_RDN_TYPE)).append("=").append(tenantId).append(",")
                .append(environment.getProperty(PropertyNames.DOMAIN_DN));

        return createName(sb.toString());
    }


    /**
     * @param uid
     * @param tenantId
     * @return
     */
    public LdapName createUserDn(String tenantId, String uid)
    {
        StringBuilder sb = new StringBuilder();

        sb.append(environment.getProperty(PropertyNames.USER_RDN_TYPE)).append("=").append(uid).append(",")
                .append(environment.getProperty(PropertyNames.USERS_RDN)).append(",")
                .append(environment.getProperty(PropertyNames.TNT_RDN_TYPE)).append("=").append(tenantId).append(",")
                .append(environment.getProperty(PropertyNames.DOMAIN_DN));

        return createName(sb.toString());
    }


    /**
     * @param tenantId
     * @return
     */
    public LdapName createUsersDn(String tenantId)
    {
        StringBuilder sb = new StringBuilder();

        sb.append(environment.getProperty(PropertyNames.USERS_RDN)).append(",").
        append(environment.getProperty(PropertyNames.TNT_RDN_TYPE)).append("=").append(tenantId).append(",").
        append(environment.getProperty(PropertyNames.DOMAIN_DN));

        return createName(sb.toString());
    }



	
    /**
     * @param tenantId
     * @return
     */
	public LdapName createRolesDn(String tenantId)
    {
        StringBuilder sb = new StringBuilder();

        sb.append(environment.getProperty(PropertyNames.ROLES_RDN)).append(",").
        append(environment.getProperty(PropertyNames.TNT_RDN_TYPE)).append("=").append(tenantId).append(",").
        append(environment.getProperty(PropertyNames.DOMAIN_DN));

        return createName(sb.toString());
    }
    
    
    /**
     * @param roleName
     * @param tenantId
     * @return
     */
    public LdapName createRoleDn(String tenantId, String roleName)
    {
        StringBuilder sb = new StringBuilder();

        sb.append(environment.getProperty(PropertyNames.ROLE_RDN_TYPE)).append("=").append(roleName).append(",")
                .append(environment.getProperty(PropertyNames.ROLES_RDN)).append(",")
                .append(environment.getProperty(PropertyNames.TNT_RDN_TYPE)).append("=").append(tenantId).append(",")
                .append(environment.getProperty(PropertyNames.DOMAIN_DN));
        
        return createName(sb.toString());
    }
    
    
    public static LdapName createName(String dn)
    {
        LdapName out=null;
        try
        {
	        out = new LdapName(dn);
        }
        catch (InvalidNameException e)
        {
	        throw new RuntimeException(dn,e) ;
        }
        
    	return out;
    }
    
    public static Rdn createRdn(String rdn)
    {
    	try
        {
	        return new Rdn(rdn);
        }
        catch (InvalidNameException e)
        {
        	throw new RuntimeException(rdn,e) ;
        }
    }    
    
    public static Rdn createRdn(RdnType type, String rdnValue)
    {
    	try
        {
	        return new Rdn(type.toString(), rdnValue);
        }
        catch (InvalidNameException e)
        {
        	throw new RuntimeException("type "+type+" rdnValue "+rdnValue,e) ;
        }
    }
    
    
    public static Rdn createRdn(String rdnType, String rdnValue)
    {
    	try
        {
	        return new Rdn(rdnType, rdnValue);
        }
        catch (InvalidNameException e)
        {
        	throw new RuntimeException("Type "+rdnType+" rdnValue "+rdnValue,e) ;
        }
    }  
    

    public static Attribute getOrgObjectClasses()
    {
        Attribute objClasses = new BasicAttribute(LdapClasseNames.OBJECT_CLASS.toString());
        objClasses.add(LdapClasseNames.TOP.toString());
        objClasses.add(LdapClasseNames.ORGANIZATION.toString());
        
        return objClasses;
    }
    
    public static Attribute getUserObjectClasses()
    {
        Attribute objClasses = new BasicAttribute(LdapClasseNames.OBJECT_CLASS.toString());
        objClasses.add(LdapClasseNames.TOP.toString());
        objClasses.add(LdapClasseNames.PERSON.toString());
        objClasses.add(LdapClasseNames.ORG_PERSON.toString());
        objClasses.add(LdapClasseNames.INT_ORG_PERSON.toString());
        objClasses.add(LdapClasseNames.EXTENSIBLE_OBJECT.toString());
        
        return objClasses;
    }

    
    public static Attribute getRoleObjectClasses()
    {
        Attribute objClasses = new BasicAttribute(LdapClasseNames.OBJECT_CLASS.toString());
        objClasses.add(LdapClasseNames.TOP.toString());
        objClasses.add(LdapClasseNames.GROUP_OF_UNIQUE_NAMES.toString());
        
        return objClasses;
    }

	public static LdapName createName(Rdn...rdns)
    {
	    return new LdapName(Arrays.asList(rdns));
    }

	public Rdn createRoleRdn(String roleName) throws InvalidNameException
    {
		return new Rdn(environment.getProperty(PropertyNames.ROLE_RDN_TYPE),roleName);
    }

	public LdapName getDomainDn()
    {
	    return createName(environment.getProperty(PropertyNames.DOMAIN_DN));
    }

	public Rdn createTenantRdn(String tenantId)
    {
	    // TODO Auto-generated method stub
	    return createRdn(RdnType.O, tenantId);
    }

	/*dn: ou=Roles,o=tnt1,dc=easy,dc=org
	objectClass: organizationalUnit
	objectClass: top
	ou: Roles*/
	public static Attribute getRolesObjectClasses()
    {
	       Attribute objClasses = new BasicAttribute(LdapClasseNames.OBJECT_CLASS.toString());
	        objClasses.add(LdapClasseNames.TOP.toString());
	        objClasses.add(LdapClasseNames.ORG_UNIT.toString());
	        
	        return objClasses;
    }

	public Rdn createRolesRdn()
    {
	    return createRdn(RdnType.OU, environment.getProperty(PropertyNames.ROLES_RDN_VALUE));
    }

	public static Attribute getUsersObjectClasses()
    {
	       Attribute objClasses = new BasicAttribute(LdapClasseNames.OBJECT_CLASS.toString());
	        objClasses.add(LdapClasseNames.TOP.toString());
	        objClasses.add(LdapClasseNames.ORG_UNIT.toString());
	        
	        return objClasses;

    }

	public Rdn createPeopleRdn()
    {
	    return createRdn(RdnType.OU, environment.getProperty(PropertyNames.USERS_RDN_VALUE));
    }    
    
}
