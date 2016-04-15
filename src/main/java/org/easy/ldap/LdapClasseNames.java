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
 * @author mahmood.aftab
 * 
 */
enum LdapClasseNames {
	OBJECT_CLASS("objectClass"), TOP("top"), PERSON("person"), ORG_PERSON("organizationalPerson"), INT_ORG_PERSON(
    "inetOrgPerson"), EXTENSIBLE_OBJECT("extensibleObject"), GROUP_OF_UNIQUE_NAMES("groupOfUniqueNames"), 
    ORGANIZATION("organization"), ORG_UNIT("organizationalUnit");


	private final String ldapText;

	private LdapClasseNames(String ldapText)
	{
		this.ldapText = ldapText;
	}

	@Override
	public String toString()
	{
		return ldapText;
	}

}
