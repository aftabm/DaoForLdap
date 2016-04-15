/**
 * 
 */
package org.easy.ldap.importer;

import static org.junit.Assert.*;

import org.apache.directory.server.core.integ.AbstractLdapTestUnit;
import org.apache.directory.server.core.integ.FrameworkRunner;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.easy.ldap.LdapEnvironment;
import org.easy.ldap.LdapEnvironment.FileType;
import org.easy.ldap.LdapEnvironment.PropertyNames;
import org.easy.ldap.importer.CvsImporter;
import org.easy.ldap.importer.CvsImporter.Location;


/**
 * @author amahmood
 * 
 */

@RunWith(FrameworkRunner.class)
public class CvsImporterTest extends AbstractLdapTestUnit
{
    private static CvsImporter importer = null;

    /**
     * @throws java.lang.Exception
     */
    
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		if (isRunInSuite)
    	{
    		LdapEnvironment environment = new LdapEnvironment("ldap-context-test.properties", FileType.PROPERTIES);
    		environment.setProperty(PropertyNames.SERVER_PORT, ldapServer.getPort());
    		importer = new CvsImporter(environment);
    	}
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{
		importer = null;
	}    

	
	@Test
	public final void testImport()
	{
		assertNotNull(importer);
		
		try
		{
			importer.cvsImport("import_test_data.cvs", "ASCII", Location.CLASSPATH);
		}
		catch(Throwable t)
		{
			t.printStackTrace();
		}
	}
}
