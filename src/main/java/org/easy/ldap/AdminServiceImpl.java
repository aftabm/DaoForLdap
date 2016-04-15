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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.naming.InvalidNameException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.easy.ldap.LdapEnvironment.PropertyNames;
import org.easy.ldap.model.LdapUser;

import com.google.common.base.Preconditions;

/**
 * Implementation class for LDAP CRUD operations
 *  tenant = org = division = department
 * @author mahmood.aftab
 * 
 */
public class AdminServiceImpl implements LdapAdminService
{
    private static Log log = LogFactory.getLog(AdminServiceImpl.class);
    private LdapDao ldapDao = null;    
    private LdapEnvironment environment=null;
    private NamingFactory namingFactory=null;

    /**
     * @return the ldapDao
     */
    public LdapDao getLdapDao()
    {
    	return ldapDao;
    }

	/**
     * @param ldapDao the ldapDao to set
     */
    public void setLdapDao(LdapDao ldapDao)
    {
    	this.ldapDao = ldapDao;
    }

	/**
     * @param properties - LDAP connection and structure properties.
     */
    public AdminServiceImpl(LdapEnvironment properties)
    {
    	Preconditions.checkNotNull(properties);
        this.ldapDao = new LdapDao(properties);
        this.environment =properties;
        this.namingFactory = ldapDao.getContextFactory().getNamingFactory();
    }
    
    public AdminServiceImpl()
    {
    }    

    /*
     * (non-Javadoc)
     * 
     * @see org.easy.ldap.internal.test#addUser(java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void addUser(LdapUser newUser)
    {
        Preconditions.checkNotNull(newUser);
        LoggingUtil.createDebugLog(log, "addUser", newUser);
        
        try
        {
            Attributes attributes = LdapDao.toAttributes(newUser);            
            attributes.put(NamingFactory.getUserObjectClasses());
            
            String userRdnName = environment.getProperty(PropertyNames.USERS_RDN);
            userRdnName = userRdnName.replace(RdnType.OU.toString()+"=", "");            
            Attribute ouAttr = new BasicAttribute(RdnType.OU.toString(), userRdnName);
            attributes.put(ouAttr);
                 
            LdapName rootDn = namingFactory.createUsersDn(newUser.getTenantId());
            Rdn userRdn = NamingFactory.createRdn(RdnType.UID, newUser.getUserId());
                 
            ldapDao.createSubContext(rootDn, userRdn, attributes);

        }
        catch (Exception e)
        {
            log.error(e);
            throw new java.lang.RuntimeException(e);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.easy.ldap.internal.test#deleteUser(java.lang.String)
     */
    @Override
    public void deleteUser(LdapUser user)
    {
        Preconditions.checkNotNull(user.getTenantId());
        Preconditions.checkNotNull(user.getUserId());
        LoggingUtil.createDebugLog(log, "deleteUser", user.toString());
        
        try
        {
        	//bug remove user from roles first.
            LdapName rootDn = namingFactory.createUsersDn(user.getTenantId());
            Rdn userRdn = NamingFactory.createRdn(RdnType.UID, user.getUserId());
            LdapName subContextName = NamingFactory.createName(userRdn);
            	
            ldapDao.deleteSubContext(rootDn, subContextName);

        }
        catch (Exception e)
        {
            log.error(e);
            throw new java.lang.RuntimeException(e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.easy.ldap.internal.test#updateUser(java.lang.String,
     * org.easy.ldap.internal.LdapAttributesEnum, java.lang.String)
     */
    @Override
    public void updateUser(LdapUser user, Map<RdnType, String> newData)
    {
        Preconditions.checkNotNull(newData);
        Preconditions.checkNotNull(user.getTenantId());
        Preconditions.checkNotNull(user.getUserId());
        Preconditions.checkArgument(user.getTenantId().trim().length() > 0);
        Preconditions.checkArgument(user.getUserId().trim().length() > 0);
        Preconditions.checkArgument(newData.size() > 0);
        
        try
        {
            ModificationItem[] modifications = new ModificationItem[newData.size()];

            int i=0;
            
            for (RdnType attributeName:newData.keySet())
            {
               /* if (attributeName.equals(RdnType.UID))
                    throw new RuntimeException("Cannot change uid.");*/
                
                modifications[i] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
                        new BasicAttribute(attributeName.toString(), newData.get(attributeName)));
                
                i++;
            }

        	LdapName rootDn = namingFactory.createUsersDn(user.getTenantId());
        	Rdn userRdn = NamingFactory.createRdn(RdnType.UID, user.getUserId());
        	LdapName subContextName = NamingFactory.createName(userRdn);
        	
        	ldapDao.updateSubContext(rootDn, subContextName, modifications);
        }
        catch (Exception e)
        {
            log.error(e);
            throw new java.lang.RuntimeException(e);
        }
    }
    
    
	/* (non-Javadoc)
	 * @see org.easy.ldap.LdapAdminService#findAllUsers(org.easy.ldap.model.LdapUser)
	 */
	@Override
    public List<LdapUser> findAllUsers(LdapUser example)
    {
		Preconditions.checkNotNull(example);
		Preconditions.checkNotNull(example.getTenantId());
		
		List<LdapUser> out  = new ArrayList<LdapUser>(0);
		
        try
        {
        	LdapName rootDn = namingFactory.createUsersDn(example.getTenantId());
        	NamingEnumeration<SearchResult> result = ldapDao.findAll(rootDn,LdapDao.toAttributes(example));
                
    	    while(result.hasMore())
    	    {
    	      	out.add(LdapDao.toModel(example.getTenantId(),result.next().getAttributes()));
    	    }            
        }
        catch (NamingException e)
        {
        	log.error(e);
        	 throw new java.lang.RuntimeException(e);
        }
		
	    return out;
    }
	
	
	/* (non-Javadoc)
	 * @see org.easy.ldap.LdapAdminService#findUniqueUser(org.easy.ldap.model.LdapUser)
	 */
	@Override
    public LdapUser findUniqueUser(LdapUser example)
    {
		Preconditions.checkNotNull(example);
		Preconditions.checkNotNull(example.getTenantId());
		LdapUser out = null;
		
		List<LdapUser>   result = findAllUsers(example);
			
		if (result.size()==0)
		{
			throw new RuntimeException("NoResultException");
		}
			
		if (result.size()>1)
		{
			throw new RuntimeException("NonUniqueResultException");
		}
			
		out = result.get(0);
		
	    return out;
    }
	
	

	/* (non-Javadoc)
	 * @see org.easy.ldap.LdapAdminService#grantRole(org.easy.ldap.model.LdapUser, java.lang.String)
	 */
	@Override
    public void grantRole(LdapUser user, String role)
    {
		try
        {
			
	    	LdapName rootDn = namingFactory.createUsersDn(user.getTenantId());
	    	Rdn userRdn = NamingFactory.createRdn(RdnType.UID, user.getUserId());
	    	LdapName userName = NamingFactory.createName(userRdn);
	    	
	    	boolean isUserExists= ldapDao.isRdnExists(rootDn, userName);
	    	
	        if(!isUserExists)
	        {
	        	throw new IllegalArgumentException(user+" does not exists");
	        }
	        
			LdapName userDn = namingFactory.createUserDn(user.getTenantId(), user.getUserId());
			LdapName roleDn = namingFactory.createRoleDn(user.getTenantId(), role);
	       	ldapDao.addRdn(roleDn, RdnType.UNIQUE_MEMBER, userDn.toString());	        
	        
        }
        catch (RuntimeException e)
        {
        	log.error(e);
	        throw new java.lang.RuntimeException(e);
        }
		
	    
    }

	/* (non-Javadoc)
	 * @see org.easy.ldap.LdapAdminService#revokeRole(org.easy.ldap.model.LdapUser, java.lang.String)
	 */
	@Override
    public void revokeRole(LdapUser user, String role)
    {
		try
        {
			LdapName userDn = namingFactory.createUserDn(user.getTenantId(), user.getUserId());
			LdapName roleDn = namingFactory.createRoleDn(user.getTenantId(), role);	
			ldapDao.removeRdn(roleDn, RdnType.UNIQUE_MEMBER, userDn.toString());
        }
        catch (RuntimeException e)
        {
        	log.error(e);
        	throw e;
        }
	    
    }

	/* (non-Javadoc)
	 * @see org.easy.ldap.LdapAdminService#isRoleExists(java.lang.String, java.lang.String)
	 */
	@Override
    public boolean isRoleExists(String tenantId, String role)
    {
		boolean result=false;
		try
        {
	       	LdapName rootDn = namingFactory.createRolesDn(tenantId);
	       	Rdn roleRdn = NamingFactory.createRdn(RdnType.CN, role);
	       	LdapName roleName = NamingFactory.createName(roleRdn);
	            
	       	result = ldapDao.isRdnExists(rootDn, roleName);
        }
        catch (RuntimeException e)
        {
        	log.error(e);
        }
        
        return result;
    }
	
	/* (non-Javadoc)
	 * @see org.easy.ldap.LdapAdminService#findGrantedRoles(org.easy.ldap.model.LdapUser)
	 */
	@Override
    public List<String> findGrantedRoles(LdapUser user)
    {
		List<String> out=null;

		String uniqueMemberDn=namingFactory.createUserDn(user.getTenantId(), user.getUserId()).toString();
		LdapName rolesDn = namingFactory.createRolesDn(user.getTenantId());
		Attributes attributes = new BasicAttributes();    		    		
		attributes.put(new BasicAttribute(RdnType.UNIQUE_MEMBER.toString(), uniqueMemberDn));
			
		out = ldapDao.findRdnValues(rolesDn, attributes, RdnType.CN);
        
        return out;
    }

	/* (non-Javadoc)
	 * @see org.easy.ldap.LdapAdminService#addRole(java.lang.String, java.lang.String)
	 */
	@Override
    public void addRole(String tenantId, String role)
    {
		
		try
        {   
            Attributes attributes = new BasicAttributes();
            
            //get OU name for roles default is "Roles"
            Attribute cnAttr = new BasicAttribute(RdnType.CN.toString(), role);                              
            String roleRdnName = environment.getProperty(PropertyNames.ROLES_RDN);
            roleRdnName = roleRdnName.replace(RdnType.OU.toString()+"=", "");
            
            Attribute ouAttr = new BasicAttribute(RdnType.OU.toString(), roleRdnName);
            
            //atleast one uniqueMember is required.
            String highestRole = environment.getProperty(PropertyNames.HIGHEST_ROLE);
            Attribute defaultMember = new BasicAttribute(RdnType.UNIQUE_MEMBER.toString(), 
            		ldapDao.getContextFactory().getNamingFactory().createRoleDn(tenantId,highestRole ).toString());
        
            // Add these to the container
            attributes.put(NamingFactory.getRoleObjectClasses());
            attributes.put(cnAttr);
            attributes.put(ouAttr);
            attributes.put(defaultMember);
                        
    		LdapName rolesDn = namingFactory.createRolesDn(tenantId);
    		Rdn roleRdn = namingFactory.createRoleRdn(role);
    		
    		ldapDao.createSubContext(rolesDn, roleRdn, attributes);
	        
        }
        catch (InvalidNameException e)
        {
        	log.error(e);
       	 	throw new java.lang.RuntimeException(e);
        }
		catch (RuntimeException e)
        {
        	log.error(e);
        	 throw new java.lang.RuntimeException(e);
        }

    }

	
	
	/* (non-Javadoc)
	 * @see org.easy.ldap.LdapAdminService#deleteRole(java.lang.String, java.lang.String)
	 */
	@Override
    public void deleteRole(String tenantId, String role)
    {
		try
        {
			LdapName roleDn = namingFactory.createRoleDn(tenantId, role);
			LdapName rolesDn = namingFactory.createRolesDn(tenantId);
			Attributes attributes = new BasicAttributes();    		    		
			attributes.put(new BasicAttribute(RdnType.UNIQUE_MEMBER.toString(), roleDn));
			
			List<String> grantedRoles = ldapDao.findRdnValues(rolesDn, attributes, RdnType.CN);
			
			for (String grantedRole:grantedRoles)
			{
				LdapName grantedRoleDn = namingFactory.createRoleDn(tenantId, grantedRole);
				ldapDao.removeRdn(roleDn, RdnType.UNIQUE_MEMBER, grantedRoleDn.toString());
			}
			
			Rdn roleRdn=namingFactory.createRoleRdn(role);
			ldapDao.deleteSubContext(rolesDn, roleRdn);
        }
        catch (NamingException e)
        {
        	log.error(e);
       	 	throw new java.lang.RuntimeException(e);
        }
	    
    }

	/* (non-Javadoc)
	 * @see org.easy.ldap.LdapAdminService#findAllRoles(java.lang.String)
	 */
	@Override
    public List<String> findAllRoles(String tenantId)
    {
		List<String> out=null;
	    try
        {
			LdapName rolesDn = namingFactory.createRolesDn(tenantId);
			out = ldapDao.findRdnValues(rolesDn, null, RdnType.CN);
        }
        catch (RuntimeException e)
        {
        	log.error(e);
        	throw (e);
        }
        
        return out;
    }
	
	/* (non-Javadoc)
	 * @see org.easy.ldap.LdapAdminService#revokeAllRoles(org.easy.ldap.model.LdapUser)
	 */
	@Override
	public void revokeAllRoles(LdapUser user) 
	{
			LdapName userDn = namingFactory.createUserDn(user.getTenantId(), user.getUserId());
			LdapName rolesDn = namingFactory.createRolesDn(user.getTenantId());
    		Attributes attributes = new BasicAttributes();    		    		
    		attributes.put(new BasicAttribute(RdnType.UNIQUE_MEMBER.toString(), userDn));
			
			List<String> grantedRoles = ldapDao.findRdnValues(rolesDn, attributes, RdnType.CN);
			
			for (String role:grantedRoles)
			{
				LdapName roleDn = namingFactory.createRoleDn(user.getTenantId(), role);
				ldapDao.removeRdn(roleDn, RdnType.UNIQUE_MEMBER, userDn.toString());
			}
	}	
	
	
	
	/* (non-Javadoc)
	 * @see org.easy.ldap.LdapAdminService#findAllUsers(java.lang.String, org.easy.ldap.model.LdapUser)
	 */
	@Override
    public List<LdapUser> findAllUsers(String tenantId, LdapUser example) 
    {
    	
    	NamingEnumeration<SearchResult> result = null;
    	List<LdapUser> out  = new ArrayList<LdapUser>();

    	LdapName rootDn = namingFactory.createUsersDn(tenantId);
        result = ldapDao.findAll(rootDn,LdapDao.toAttributes(example));
            
	    try
        {
	        while(result.hasMore())
	        {
	          	out.add(LdapDao.toModel(tenantId,result.next().getAttributes()));
	        }
        }
        catch (NamingException e)
        {
	        log.error(e.getMessage(),e);
	        throw new RuntimeException(e);
        }            
        
        return out;
    }

    
	/*
	dn: o=tnt1,dc=easy,dc=org
			objectClass: organization
			objectClass: top
			o: tnt1
			
			
	dn: ou=Roles,o=tnt1,dc=easy,dc=org
	objectClass: organizationalUnit
	objectClass: top
	ou: Roles			
*/
	@Override
    public void addTenant(String tenantId)
    {
		try
        {   
			//Add org node.
            Attributes attributes = new BasicAttributes();
            
            attributes.put(NamingFactory.getOrgObjectClasses());
            attributes.put(new BasicAttribute(RdnType.O.toString(), tenantId));
            
    		LdapName domainDn = namingFactory.getDomainDn();
    		Rdn tenantRdn = namingFactory.createTenantRdn(tenantId);
    		
    		ldapDao.createSubContext(domainDn, tenantRdn, attributes);
    		
    		//add roles node under org
    		attributes = new BasicAttributes();
    		attributes.put(NamingFactory.getRolesObjectClasses());
    		attributes.put(new BasicAttribute(RdnType.OU.toString(), environment.getProperty(PropertyNames.ROLES_RDN_VALUE)));

    		LdapName tenantDn = namingFactory.createTenantDn(tenantId);
    		Rdn rolesRdn = namingFactory.createRolesRdn();
    		
    		ldapDao.createSubContext(tenantDn, rolesRdn, attributes);
    		
    		//create users node
    		attributes = new BasicAttributes();
    		attributes.put(NamingFactory.getUsersObjectClasses());
    		attributes.put(new BasicAttribute(RdnType.OU.toString(), environment.getProperty(PropertyNames.USERS_RDN_VALUE)));

    		Rdn peopleRdn = namingFactory.createPeopleRdn();
    		
    		ldapDao.createSubContext(tenantDn, peopleRdn, attributes);    		
    		
        }
		catch (RuntimeException e)
        {
        	log.error(e);
        	 throw new java.lang.RuntimeException(e);
        }

	  
    }

    @Override
    public boolean isUserExists(String tenantId, String userId)
    {
    	boolean result =false;
        Preconditions.checkNotNull(tenantId);
        Preconditions.checkNotNull(userId);
        Preconditions.checkArgument(tenantId.trim().length() > 0);
        Preconditions.checkArgument(userId.trim().length() > 0);
        LoggingUtil.createDebugLog(log, "isUserExists", tenantId,userId); 
                
        try
        {
	    	LdapName rootDn = ldapDao.getContextFactory().getNamingFactory().createUsersDn(tenantId);
	    	Rdn userRdn = NamingFactory.createRdn(RdnType.UID, userId);
	    	LdapName userName = NamingFactory.createName(userRdn);
	    	
	    	result= ldapDao.isRdnExists(rootDn, userName);	        
        }
        catch (RuntimeException e)
        {
        	 log.error(e);
        }
        return result;
    }

	@Override
    public boolean isTenantExists(String tenantId)
    {
    	boolean result =false;
        Preconditions.checkNotNull(tenantId);
        Preconditions.checkArgument(tenantId.trim().length() > 0);
        LoggingUtil.createDebugLog(log, "isTenentExists", tenantId); 
                
        try
        {
	    	LdapName rootDn = ldapDao.getContextFactory().getNamingFactory().getDomainDn();
	    	Rdn userRdn = NamingFactory.createRdn(RdnType.O, tenantId);
	    	LdapName tntName = NamingFactory.createName(userRdn);
	    	
	    	result= ldapDao.isRdnExists(rootDn, tntName);	        
        }
        catch (RuntimeException e)
        {
        	 log.error(e);
        }
        return result;
    }

}
