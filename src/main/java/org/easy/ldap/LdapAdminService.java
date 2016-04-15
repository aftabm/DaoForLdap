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
import java.util.Map;

import org.easy.ldap.model.LdapUser;


/**
 * Ldap Service for CRUD operations
 * 
 * @author mahmood.aftab
 * 
 */
public interface LdapAdminService
{
	
	
	/**
	 * @param tenentId
	 */
	public abstract void addTenant(String tenentId);
	
	//public abstract void deleteTenant(String tenentId);
	
	/**
	 * @return
	 */
	
	//public abstract List<String> findeAllTenants();
    
	/**
     * @param newUser
     * @return
     * @throws runtime exception on failure
     */
    public abstract void addUser(LdapUser newUser);

    /**
     * @param tenantId
     * @param userId
     * @return
     * @throws runtime exception on failure
     */
    public abstract void deleteUser(LdapUser user);

    /**
     * @param tenantId
     * @param uid
     * @param attribute
     *            - Name of the user attribute that need to be updated.
     * @param newValue
     * @return
     * @throws runtime exception on failure
     */
    public abstract void updateUser(LdapUser user, Map<RdnType, String> newData);
    
    /**
     * @param tenantId
     * @param example
     * @return
     */
    public List<LdapUser> findAllUsers(String tenantId, LdapUser example); 
    
    /**
     * @param user
     * @param role
     */
    public abstract void grantRole(LdapUser user, String role);
    
    /**
     * @param tenantId
     * @param userId
     * @return
     */
    public List<String> findGrantedRoles(LdapUser user);
    
    
    /**
     * @param user
     * @param role
     */
    public abstract void revokeRole(LdapUser user, String role);
    
    /**
     * @param user
     */
    public void revokeAllRoles(LdapUser user);
    
    
    /**
     * @param tenantId
     * @param role
     */
    public abstract void addRole(String tenantId, String role);
    
    /**
     * @param tenantId
     * @param role
     */
    public abstract void deleteRole(String tenantId, String role);
    
    
    /**
     * LdapUser example must not be null and "tenant Id" should be set.
     * 
     * @param orgName
     * @param userName
     * @return
     */
    public abstract LdapUser findUniqueUser(LdapUser example);
    
    
    /**
     * @param example
     * @return
     */
    public abstract List<LdapUser> findAllUsers(LdapUser example);    
    
    /**
     * @param tenantId
     * @param role
     * @return
     */
    public abstract boolean isRoleExists(String tenantId, String role);

	/**
	 * @param ordId
	 * @return
	 */
	public abstract List<String> findAllRoles(String ordId);
	
    /**
     * @param tenantId
     * @param userId
     * @return
     */
    public abstract boolean isUserExists(String tenantId, String userId);
    /**
     * @param tenantId
     * @return
     */
    public abstract boolean isTenantExists(String tenantId);
}
