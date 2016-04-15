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

import java.util.List;

import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.easy.ldap.model.LdapUser;

import com.google.common.base.Preconditions;

/**
 * @author mahmood.aftab
 * 
 */
public class AuthServiceImpl implements LdapAuthService
{
    private static Log log = LogFactory.getLog(AuthServiceImpl.class);
    private LdapDao ldapDao = null;    
    //private LdapEnvironment environment=null;
    private NamingFactory namingFactory=null;
    
    /**
     * @param properties
     */
    public AuthServiceImpl(LdapEnvironment properties)
    {
        Preconditions.checkNotNull(properties);
        LoggingUtil.createDebugLog(log, "AuthServiceImpl", properties);
        this.ldapDao = new LdapDao(properties);
        //this.environment =properties;
        this.namingFactory = ldapDao.getContextFactory().getNamingFactory();
        
    }

    public AuthServiceImpl()
    {
        
    }    
    
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

	/*
     * (non-Javadoc)
     * 
     * @see
     * org.easy.ldap.AuthService#isValidCredential(java.lang.String
     * , java.lang.String)
     */
    @Override
    public boolean isValidUser(String tenantId, String userId, String password)
    {
    	boolean result=false;
        Preconditions.checkNotNull(tenantId);
        Preconditions.checkNotNull(userId);
        Preconditions.checkNotNull(password);
        Preconditions.checkArgument(tenantId.trim().length() > 0);
        Preconditions.checkArgument(userId.trim().length() > 0);
        Preconditions.checkArgument(password.trim().length() > 0);
        LoggingUtil.createDebugLog(log, "isValidUser", tenantId,userId);        
        
	    result = ldapDao.isValidUser(tenantId, userId, password);
        
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.easy.ldap.AuthService#isValidUser(java.lang.String)
     */
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
    public boolean isUserHasRole(String tenantId, String userId, String role)
    {
		boolean result=false;
        Preconditions.checkNotNull(tenantId);
        Preconditions.checkNotNull(userId);
        Preconditions.checkArgument(tenantId.trim().length() > 0);
        Preconditions.checkArgument(userId.trim().length() > 0);
        LoggingUtil.createDebugLog(log, "isUserRole", tenantId,userId,role); 
                
        try
        {
	       	LdapName rootDn = ldapDao.getContextFactory().getNamingFactory().createRoleDn(tenantId, role);
	       	Rdn uniqueMemberRdn = NamingFactory.createRdn(RdnType.UNIQUE_MEMBER, userId);
	       	LdapName uniqueMemberName = NamingFactory.createName(uniqueMemberRdn);
	        result= ldapDao.isRdnExists(rootDn, uniqueMemberName);	        
        }
        catch (RuntimeException e)
        {
        	 log.error(e);
        }
        
        return result;
    }

	@Override
    public List<String> getAssignedRoles(LdapUser user)
    {
		List<String> out=null;

		String uniqueMemberDn=namingFactory.createUserDn(user.getTenantId(), user.getUserId()).toString();
		LdapName rolesDn = namingFactory.createRolesDn(user.getTenantId());
		Attributes attributes = new BasicAttributes();    		    		
		attributes.put(new BasicAttribute(RdnType.UNIQUE_MEMBER.toString(), uniqueMemberDn));
				
		out = ldapDao.findRdnValues(rolesDn, attributes, RdnType.CN);
	        
	    return out;
    }
}
