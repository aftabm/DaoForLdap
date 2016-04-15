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
package org.easy.ldap.model;

import java.util.ArrayList;
import java.util.List;

/**
 * A representation of LDAP User object and its attributes
 * 
 * @author  mahmood.aftab
 * 
 */
public class LdapUser
{

    private String tenantId;
    private String firstName;
    private String lastName;
    private String userId;
    private String password;
    private String email;
    private String commonName;
    private List<String> roles = new ArrayList<String>(0);


/*    public LdapUser(String uid)
    {
        super();
        this.userId = uid;
    }*/
    

    /**
     * @param tenantId
     * @param userId
     */
    public LdapUser(String tenantId, String userId)
    {
        super();
        this.tenantId = tenantId;
        this.userId = userId;
    }
    
      

     /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        LdapUser other = (LdapUser) obj;

        //unique keys;
        if (tenantId == null)
        {
            if (other.tenantId != null)
                return false;
        }
        else if (!tenantId.equals(other.tenantId))
            return false;
        
        if (userId == null)
        {
            if (other.userId != null)
                return false;
        }
        else if (!userId.equals(other.userId))
            return false;
        
        return true;
    }

    public String getCommonName()
    {
        return this.commonName;
    }

    
    public void setCommonName(String commonName)
    {
        this.commonName=commonName;
    }

    public String getEmail()
    {
        return email;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public String getGivenName()
    {
        return firstName;
    }
    
    
    public void setGivenName(String givenName)
    {
        this.firstName=givenName;
    }    

    public String getLastName()
    {
        return lastName;
    }

    public String getTenantId()
    {
        return tenantId;
    }

    public String getPassword()
    {
        return password;
    }

    public String getSurname()
    {
        return lastName;
    }
    
    public void setSurname(String surName)
    {
        this.lastName=surName;
    }    

    public String getUserId()
    {
        return userId;
    }

       
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((tenantId == null) ? 0 : tenantId.hashCode());
        result = prime * result + ((userId == null) ? 0 : userId.hashCode());
        return result;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }


    public void setPassword(String password)
    {
        this.password = password;
    }

    public List<String> getRoles()
    {
    	return roles;
    }


    public void setRoleName(List<String> roles)
    {
    	this.roles = roles;
    }





	/* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
	    return "LdapUser [tenantId=" + tenantId + ", firstName=" + firstName + ", lastName=" + lastName + ", uid=" + userId
	            + ", password=" + password + ", email=" + email + ", commonName=" + commonName + "]";
    }

}
