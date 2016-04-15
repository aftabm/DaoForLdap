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

import org.easy.ldap.model.LdapUser;

/**
 * @author mahmood.aftab
 *
 */
public interface LdapAuthService
{

    /**
     * @param tenantId
     * @param userId
     * @param password
     * @return
     */
    public abstract boolean isValidUser(String tenantId, String userId, String password);

    /**
     * @param tenantId
     * @param userId
     * @param role
     * @return
     */
    public abstract boolean isUserHasRole(String tenantId, String userId, String role);
    
    /**
     * @param tenantId
     * @param userId
     * @return
     */
    public List<String> getAssignedRoles(LdapUser user);    
    
    
    /**
     * @param tenantId
     * @param userId
     * @return
     */
    public abstract boolean isUserExists(String tenantId, String userId);

}