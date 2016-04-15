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

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.directory.server.annotations.CreateLdapServer;
import org.apache.directory.server.annotations.CreateTransport;
import org.apache.directory.server.core.annotations.ApplyLdifFiles;
import org.apache.directory.server.core.annotations.CreateDS;
import org.apache.directory.server.core.annotations.CreateIndex;
import org.apache.directory.server.core.annotations.CreatePartition;
import org.apache.directory.server.core.integ.FrameworkSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import org.easy.ldap.importer.CvsImporterTest;

@RunWith( FrameworkSuite.class )
@Suite.SuiteClasses( { ContextFactoryTest.class , UsersTest.class, AuthServiceImplTest.class, RolesTest.class, TenantTest.class, CvsImporterTest.class} )
@CreateDS( 
    name = "SuiteDS",
    partitions =
    {
        @CreatePartition(
            name = "easy-ldap",
            suffix = "dc=easy,dc=org",
            
            indexes = 
            {
                @CreateIndex( attribute = "objectClass" ),
                @CreateIndex( attribute = "dc" ),
                @CreateIndex( attribute = "ou" )
            } )
    } )
@ApplyLdifFiles( "easy-ldap.ldif" )
@CreateLdapServer ( 
    transports = 
    {
        @CreateTransport( protocol = "LDAP" ) 
    })
    
public class AllTests
{
	public static Test suite()
	{
		TestSuite suite = new TestSuite(AllTests.class.getName());

		return suite;
	}
	
}	
