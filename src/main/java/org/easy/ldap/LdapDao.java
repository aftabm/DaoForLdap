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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.easy.ldap.model.LdapUser;


/**
 * @author  mahmood.aftab
 * 
 */
public class LdapDao
{
    private static Log log = LogFactory.getLog(LdapDao.class);
    private LdapContextFactory contextFactory = null;

    /**
     * @return the contextFactory
     */
    public LdapContextFactory getContextFactory()
    {
    	return contextFactory;
    }

	/**
     * @param contextFactory the contextFactory to set
     */
    public void setContextFactory(LdapContextFactory contextFactory)
    {
    	this.contextFactory = contextFactory;
    }

	/**
     * @param properties - LDAP connection and structure properties.
     */
    public LdapDao(LdapEnvironment newProperties)
    {
        contextFactory = new LdapContextFactory(newProperties);
    }
    
    /**
     * @param properties - LDAP connection and structure properties.
     */
    public LdapDao()
    {
    }    
    

    /**
     * @param tenantId
     * @param userId
     * @param password
     * @return
     * @throws NamingException 
     */
    public boolean isValidUser(String tenantId, String userId, String password)
    {
        boolean out = false;
        Context ctx=null;

        try
        {
        	LdapName rootDn = contextFactory.getNamingFactory().createUserDn(tenantId, userId);
            ctx = contextFactory.createSecureContext(rootDn, rootDn, password, LdapEnvironment.USER_AUTHENTICATION_METHOD);

            if (ctx != null)
                out = true;

            ctx.close();
        }
        catch (NamingException e)
        {
        	log.debug("FYI", e);
        }
        finally
        {
        	if (ctx!=null)
	            try
                {
	                ctx.close();
                }
                catch (NamingException e)
                {
	                log.debug("FYI",e);
                }
        }
        
        return out;
    }

	
	/**
	 * @param user
	 * @return
	 */
	public static Attributes toAttributes(LdapUser user)
	{
		Attributes attributes = new BasicAttributes();
		
		if (user.getCommonName()!=null)
			attributes.put(new BasicAttribute(RdnType.CN.toString(),
					user.getCommonName()));

		if (user.getGivenName()!=null)
			attributes.put(new BasicAttribute(RdnType.GIVEN_NAME.toString(),
	        		user.getGivenName()));

		if (user.getSurname()!=null)
			attributes.put(new BasicAttribute(RdnType.SN.toString(),
	        		user.getSurname()));
		
		if (user.getUserId()!=null)
			attributes.put(new BasicAttribute(RdnType.UID.toString(),
	        		user.getUserId()));

		if (user.getEmail()!=null)
			attributes.put(new BasicAttribute(RdnType.MAIL.toString(),
	        		user.getEmail()));
		
		if (user.getPassword()!=null)
			attributes.put(new BasicAttribute(RdnType.PASSWORD.toString(),
	        		user.getPassword()));
		
		return attributes;
	}
	
	
	/**
	 * @param attributes
	 * @return
	 */
	public static LdapUser toModel(String tenantId, Attributes attributes)
	{
		LdapUser out = null;
		
		try
        {
			out = new LdapUser(tenantId, attributes.get(RdnType.UID.toString()).get().toString());
			out.setCommonName(attributes.get(RdnType.CN.toString()).get().toString());
			out.setGivenName(attributes.get(RdnType.GIVEN_NAME.toString()).get().toString());
			out.setSurname(attributes.get(RdnType.SN.toString()).get().toString());
			out.setEmail(attributes.get(RdnType.MAIL.toString()).get().toString());
        }
        catch (NamingException e)
        {
	        log.error(e.getMessage(), e);
        }
		
		return out;
	}

	
	public void updateRdn(LdapName rootDn, RdnType type, String rdnValue)
	{
		DirContext ctx=null;
		
		try
        {	
            ctx = contextFactory.createContext(rootDn.toString());
	        
	        ModificationItem[] modifications = new ModificationItem[1];	        
	        
	        Attribute attribute = new BasicAttribute(type.toString(), rdnValue);
	        
	        modifications[0] =new ModificationItem(DirContext.REPLACE_ATTRIBUTE,attribute);
	        
	        ctx.modifyAttributes("", modifications);
	        
        }
        
        catch (NamingException e)
        {
        	throw new RuntimeException(type.toString()+"="+rdnValue+","+rootDn.toString(),e);
        }
	        
        finally
        {
        	if (ctx!=null)
        	{
	            try
                {
	                ctx.close();
                }
                catch (NamingException e)
                {
	                log.debug(e);
                }
        	}
        }		
	}
	
	/**
	 * @param rootDn
	 * @param type
	 * @param rdnValue
	 */
	public void addRdn(LdapName rootDn, RdnType type, String rdnValue)
	{
		DirContext ctx=null;
		
		try
        {	
            ctx = contextFactory.createContext(rootDn.toString());
	        
	        ModificationItem[] modifications = new ModificationItem[1];	        
	        
	        Attribute attribute = new BasicAttribute(type.toString(), rdnValue);
	        
	        modifications[0] = new ModificationItem(DirContext.ADD_ATTRIBUTE,attribute);
	        
	        ctx.modifyAttributes("", modifications);
	        
        }
        
        catch (NamingException e)
        {
        	throw new RuntimeException(e);
        }
	        
        finally
        {
        	if (ctx!=null)
        	{
	            try
                {
	                ctx.close();
                }
                catch (NamingException e)
                {
	                log.debug(e);
                }
        	}
        }
		
	}	
	
	/**
	 * @param rootDn
	 * @param type
	 * @param rdnValue
	 */
	public void removeRdn(LdapName rootDn, RdnType type, String rdnValue)
	{
		DirContext ctx=null;
		
		try
        {	
            ctx = contextFactory.createContext(rootDn.toString());
	        
	        ModificationItem[] modifications = new ModificationItem[1];	        
	        
	        Attribute attribute = new BasicAttribute(type.toString(), rdnValue);
	        
	        modifications[0] =new ModificationItem(DirContext.REMOVE_ATTRIBUTE,attribute);
	        
	        ctx.modifyAttributes("", modifications);
	        
        }
        
        catch (NamingException e)
        {
        	throw new RuntimeException(type.toString()+"="+rdnValue+","+rootDn.toString(),e);
        }
	        
        finally
        {
        	if (ctx!=null)
        	{
	            try
                {
	                ctx.close();
                }
                catch (NamingException e)
                {
	                log.debug(e);
                }
        	}
        }		
		
	}	
	
	/**
	 * @param parentDn
	 * @param subContextName
	 * @param attributes
	 */
	public void createSubContext(final LdapName parentDn, final Rdn subContextRdn, final Attributes attributes) 
	{
		DirContext ctx=null;
		
		
		try
        {
			LdapName subContextName = NamingFactory.createName(subContextRdn);
	        ctx = contextFactory.createContext(parentDn.toString());
	        ctx.createSubcontext(subContextName.toString(), attributes);
        }
        catch (NamingException e)
        {
        	throw new java.lang.RuntimeException(subContextRdn.toString()+","+parentDn.toString(),e);
        }
	}
	
	
	/**
	 * @param rdsn
	 * @return
	 */
	private static Attributes toAttributes(Map<RdnType, String> rdsn)
	{
		Attributes attributes = new BasicAttributes();
		
        for (RdnType attributeName:rdsn.keySet())
        {
            attributes.put( new BasicAttribute(attributeName.toString(), rdsn.get(attributeName)));
        }
		
		return attributes;
	}

	
	/**
	 * @param parentDn
	 * @param subContextRdn
	 */
	public void deleteSubContext(LdapName parentDn, Rdn subContextToDelete)
	{
        DirContext ctx = null;
        
        try
        {
            ctx = contextFactory.createContext(parentDn.toString());
            ctx.destroySubcontext(subContextToDelete.toString());
        }
        catch (NamingException e)
        {
        	throw new RuntimeException(subContextToDelete.toString()+","+parentDn.toString(),e);
        }        
        finally
        {
        	if (contextFactory!=null)
        		contextFactory.closeContext(ctx);
        }
		
	}
	
	public void deleteSubContext(LdapName parentDn, LdapName subContextName)
	{
        DirContext ctx = null;
        
        try
        {
            ctx = contextFactory.createContext(parentDn.toString());
            ctx.destroySubcontext(subContextName);
        }
        catch (NamingException e)
        {
        	throw new RuntimeException(subContextName.toString()+","+parentDn.toString(),e);
        }        
        finally
        {
            contextFactory.closeContext(ctx);
        }
		
	}	
	
	/**
	 * @param dn
	 * @return
	 */
	public boolean isDnExists(LdapName dn)
	{
		
        boolean result = false;
        Context ctx = null;

        try
        {
            ctx = contextFactory.createContext(dn.toString());
            
            if (ctx!=null)
            	result = true;
            else
            	result = false;            	
        }
        catch (NamingException e)
        {
        	throw new RuntimeException(dn.toString(),e);
        }
        finally
        {
        	if (contextFactory!=null)
        		contextFactory.closeContext(ctx);
        }
        
		return result;
	}
	
	/**
	 * @param rootDn
	 * @param type
	 * @param value
	 * @return
	 */
	public boolean isRdnExists(LdapName rootDn, RdnType type, String value)
	{
        Object result = null;
        Context ctx = null;

        try
        {
            ctx = contextFactory.createContext(rootDn.toString());
            result = ctx.lookup(NamingFactory.createRdn(type, value).toString());

        }
        catch (NamingException e)
        {
	        throw new RuntimeException(type.toString()+"="+value+","+rootDn.toString(),e);
        }
        finally
        {
            contextFactory.closeContext(ctx);
        }
        
        if (result!=null)
        	return true;
        else
        	return false;
	}
	
	
	public boolean isRdnExists(LdapName rootDn, LdapName rdnName)
	{
        Object result = null;
        Context ctx = null;

        try
        {
            ctx = contextFactory.createContext(rootDn.toString());
            result = ctx.lookup(rdnName);

        }
        catch (NamingException e)
        {
	        throw new RuntimeException(rdnName.toString()+","+rootDn.toString(),e);
        }
        finally
        {
            contextFactory.closeContext(ctx);
        }
        
        if (result!=null)
        	return true;
        else
        	return false;
	}	
	
	/**
	 * @param rootDn
	 * @param type
	 * @return
	 */
	public List<String> findRdnValue(LdapName rootDn, RdnType type)
	{
    	NamingEnumeration<SearchResult> result = null;
        List<String> out = new ArrayList<String>(0);
        
    	DirContext ctx = null;
        
        try
        {
            ctx = contextFactory.createContext(rootDn.toString());
    		Attributes attributes = new BasicAttributes();    		
    		attributes.put(new BasicAttribute(type.toString()));
    		
            result = ctx.search("", attributes);
            
	        while(result.hasMore())
	        {
	        	attributes = result.next().getAttributes();
	        	out.add(attributes.get(type.toString()).get().toString());
	        }            
            
        }
        catch (NamingException e)
        {
        	throw new RuntimeException(type.toString()+","+rootDn.toString(),e);
        }
        finally
        {
        	if (contextFactory!=null)
        		contextFactory.closeContext(ctx);
        }
        
        return out;
	}
	
	
	/**
	 * @param rootDn
	 * @param type
	 * @return
	 */
	public List<String> findRdnValues(LdapName rootDn, Attributes attributesToMatch, RdnType returnType)
	{
    	NamingEnumeration<SearchResult> result = null;
        List<String> out = new ArrayList<String>(0);
        
    	DirContext ctx = null;
        
        try
        {
            ctx = contextFactory.createContext(rootDn.toString());
            result = ctx.search("", attributesToMatch);
            
            Attributes attributes;    
	        
            while(result.hasMore())
	        {
	        	attributes = result.next().getAttributes();
	        	out.add(attributes.get(returnType.toString()).get().toString());
	        }            
            
        }
        catch (NamingException e)
        {
        	throw new RuntimeException(returnType.toString()+","+rootDn.toString(),e);
        }
        finally
        {
        	if (contextFactory!=null)
        		contextFactory.closeContext(ctx);
        }
        
        return out;
	}	
	
	
	
    /**
     * @param rootDn
     * @param subContextName
     * @param modifications
     */
    public void updateSubContext(LdapName rootDn, LdapName subContextName, ModificationItem[] modifications)
    {
        DirContext ctx = null;

        try
        {
            ctx = contextFactory.createContext(rootDn.toString());
            ctx.modifyAttributes(subContextName, modifications);
        }
        catch (NamingException e)
        {
	       throw new RuntimeException(subContextName.toString()+","+rootDn,e);
        }
        finally
        {
        	if (contextFactory!=null)
        		contextFactory.closeContext(ctx);
        }        

	    
    }
    
    
    /**
     * @param rootDn
     * @param attributesToMatch
     * @return
     */
    public NamingEnumeration<SearchResult> findAll(LdapName rootDn, Attributes attributesToMatch)  
    {    	
    	NamingEnumeration<SearchResult> result = null;
    	
        DirContext ctx = null;
        try
        {
            ctx = contextFactory.createContext(rootDn.toString());
            result = ctx.search("", attributesToMatch);
        }
        catch (NamingException e)
        {
        	throw new RuntimeException(rootDn.toString(),e);
        }
        finally
        {
        	if (contextFactory!=null)
        		contextFactory.closeContext(ctx);
        }
        
        return result;
    }    
    
    
	//Finders
	//search
    
}
