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

/**
 * @author  mahmood.aftab
 * 
 */
public enum RdnType {
    DN("Distinguish Name", "dn"), UID("User Id", "uid"), CN("Common Name", "cn"), 
    SN("Surname", "sn"), O("Organization", "o"), OU("Organizational Unit", "ou"), 
    DC("Domain Component", "dc"), GIVEN_NAME("Given Name", "givenName"), 
    MAIL("Email", "mail"), PASSWORD("password", "userpassword"), ROLE("Roles", "Roles"), 
    PEOPLE("People", "People"), UNIQUE_MEMBER("uniquemember","uniquemember");

    public final String readableText;
    private final String ldapAttribName;

    private RdnType(String readableText, String ldapText)
    {
        this.readableText = readableText;
        this.ldapAttribName = ldapText;
    }

    @Override
    public String toString()
    {
        return ldapAttribName;
    }

}
