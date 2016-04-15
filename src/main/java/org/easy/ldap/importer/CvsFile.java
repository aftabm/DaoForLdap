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
package org.easy.ldap.importer;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

import org.easy.ldap.importer.CvsImporter.Location;

import com.google.common.base.Preconditions;

public class CvsFile 
{
	private String filename=null;
	private String charSet="ASCII";
	private Location location=Location.CLASSPATH;
	
    private static final String NL = System.getProperty("line.separator");
    Scanner scanner =null;
	
	public CvsFile(String filename, String charSet, Location location)
	{
		Preconditions.checkNotNull(filename);
		this.filename = filename;
		
		if (charSet!=null && charSet.trim().length() >0)
			this.charSet = charSet;
	
		if (location!=null)
			this.location = location;
	}
	
	public boolean hasNext()
	{
		return scanner.hasNextLine();
	}
	
	
	public void open() throws Exception
	{
		boolean result=false;
		
		switch(this.location)
		{
			case CLASSPATH:
				scanner = new Scanner(openFromClasspath(),this.charSet);
				result = verifyHeader();
				break;
			case FOLDER:
				throw new UnsupportedOperationException("Not yet implemented");
				//scanner = new Scanner(openFromFolder(),this.charSet);
				//break;
		}

		
		if (result==false)
		{
			throw new Exception("Invalid header. Header of the data file does not match.");
		}
	}
	
	private boolean verifyHeader() 
	{
		String line=null;
			
		if (scanner.hasNextLine())
		{
			line =scanner.nextLine() + NL;
		}
		
		return CvsRow.validateHeader(line);
	}

	private InputStream openFromFolder() 
	{
		return null;
	}

	private InputStream openFromClasspath() throws FileNotFoundException 
	{
		InputStream out = this.getClass().getClassLoader().getResourceAsStream(filename);
		
		if (out==null)
			throw new FileNotFoundException(filename);
		
		return out; 
	}

	public CvsRow read()
	{
		String line;
		CvsRow out;
		
		if (scanner.hasNextLine())
		{
			line =scanner.nextLine() + NL;
			out = new CvsRow(line);
		}
		else	
			return null;
		
		return out;
	}
	
	public void close()
	{
		if (this.scanner!=null)
		{
			scanner.close();
			scanner = null;
		}
	}
	

	@Override
	protected void finalize() throws Throwable 
	{
		this.close();
		super.finalize();
		
	}
}
