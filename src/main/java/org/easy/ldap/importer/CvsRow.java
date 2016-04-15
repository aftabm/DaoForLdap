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

import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Preconditions;

public class CvsRow 
{
	
	private Map<Column, String> data = new HashMap<Column, String>(0);
	
	public enum Column 
	{
		Tenant_Name, User_Name, First_Name, Last_Name, Email_Address, Password, Role;
	}

	
	public CvsRow(String line) 
	{
		String[] tokens = line.split(",");
		Column[] column = Column.values();
		Preconditions.checkNotNull(tokens);
		Preconditions.checkArgument(tokens.length == column.length);
		
		for (int i=0; i < tokens.length; i++)
		{
			data.put(column[i], tokens[i].trim());
		}
		
	}
	
	public static boolean validateHeader(String line)
	{
		if (line==null)
			return false;
		
		String[] tokens = line.split(",");
		Column[] column = Column.values();
		Preconditions.checkNotNull(tokens);
		Preconditions.checkArgument(tokens.length == column.length);
		
		for (int i=0; i < tokens.length; i++)
		{
			if (!(tokens[i].trim().equals(column[i].toString())))
			{
				return false;
			}
		}
		
		return true;
	}
	
	public String getProperty(Column columnName)
	{
		return data.get(columnName);
	}

}
