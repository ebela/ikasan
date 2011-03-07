/*
 * $Id$
 * $URL$
 * 
 * ====================================================================
 * Ikasan Enterprise Integration Platform
 * Copyright (c) 2003-2008 Mizuho International plc. and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the 
 * Free Software Foundation Europe e.V. Talstrasse 110, 40217 Dusseldorf, Germany 
 * or see the FSF site: http://www.fsfeurope.org/.
 * ====================================================================
 */
package org.ikasan.common.persistence;

import java.util.List;
import java.util.Properties;

import javax.naming.NamingException;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import org.ikasan.common.CommonContext;
import org.ikasan.common.CommonPersistenceFactory;
import org.ikasan.common.CommonRuntimeException;
import org.ikasan.common.ResourceLoader;

/**
 * Provides a basic implementation of Hibernate persistence behind 
 * the Persistence interface.
 * 
 * @author Jeff Mitchell
 */
public class HibernatePersistence
    implements CommonPersistenceFactory
{
    /** standard class logger */
    private Logger logger = Logger.getLogger(HibernatePersistence.class);
    
    /** Non transactional DS Session factory */
    protected SessionFactory dsSessionFactory;

    /** Local transactional DS Session factory */
    protected SessionFactory localDSSessionFactory;

    /** XA transactional DS Session factory */
    protected SessionFactory xaDSSessionFactory;

    /** Common context is hidden behind an interface */
    protected CommonContext context = ResourceLoader.getInstance().newContext();

    /** instance of the singleton */
    protected static CommonPersistenceFactory instance = null;

    /**
     * Singleton constructor
     * @return CommonPersistence
     */
    public static CommonPersistenceFactory getInstance()
    {
        if(instance == null) 
            instance = new HibernatePersistence();
        return instance;
    }
    
    /** 
     * Constructor 
     */
    private HibernatePersistence()
    {
    	// TODO - re-implement
    	// cannot currently separate the sessionFactories from the mapping configs
    	// this has been left for now as we need to get something in for TRAX2
//        try
//        {
//            this.dsSessionFactory = (SessionFactory)this.context.lookup(CommonContext.DS_SESSION_FACTORY);
//            this.localDSSessionFactory = (SessionFactory)this.context.lookup(CommonContext.LOCALDS_SESSION_FACTORY);
//            this.xaDSSessionFactory = (SessionFactory)this.context.lookup(CommonContext.XADS_SESSION_FACTORY);
//        }
//        catch(NamingException e)
//        {
//            throw new CommonRuntimeException(e);
//        }
    }
    
	// TODO - re-implement
	// cannot currently separate the sessionFactories from the mapping configs
	// this has been left for now as we need to get something in for TRAX2
//    /**
//     * Get the non-transactional persistence session factory
//     * @return SessionFactory
//     */
//    public SessionFactory getDefaultDSSessionFactory()
//    {
//        return this.dsSessionFactory;
//    }
//    
//    /**
//     * Get the local-transactional persistence session factory
//     * @return SessionFactory
//     */
//    public SessionFactory getDefaultLocalDSSessionFactory()
//    {
//        return this.localDSSessionFactory;
//    }
//    
//    /**
//     * Get the xa-transactional persistence session factory
//     * @return SessionFactory
//     */
//    public SessionFactory getDefaultXADSSessionFactory()
//    {
//        return this.xaDSSessionFactory;
//    }
//    
    /**
     * Get a session factory other than the defaults supplied
     * @param sessionFactoryName 
     * @return SessionFactory
     * @throws NamingException 
     */
    public SessionFactory getCustomSessionFactory(final String sessionFactoryName)
        throws NamingException
    {
    	Object obj = this.context.lookup(sessionFactoryName);
    	if(obj instanceof SessionFactory)
    		return (SessionFactory)obj;
    	
    	throw new NamingException("SessionFactory expected class is ["
    			+ SessionFactory.class.getName() + "]. "
    			+ "Lookup returned class ["
    			+ obj.getClass().getName() + "].");
    }

    /**
     * Create a session factory based on the incoming resource list. This
     * creates and returns a session factory rather than simply returning 
     * a pre-built one from the JNDI.
     * 
     * @param resources 
     * @return SessionFactory
     */
    public SessionFactory getCustomSessionFactory(final List<String> resources)
    {
        return this.getCustomSessionFactory(resources, null);
    }

    /**
     * Create a session factory based on the incoming resource list. This
     * creates and returns a session factory rather than simply returning 
     * a pre-built one from the JNDI.
     * 
     * @param resources 
     * @param properties 
     * @return SessionFactory
     */
    public SessionFactory getCustomSessionFactory(final List<String> resources, final Properties properties)
    {
        if(resources == null)
        {
            throw new CommonRuntimeException("Persistence resources param cannot be null!");
        }

        if(resources.size() == 0)
        {
            logger.warn("Persistence resources passed as an empty list!");
        }
        
        Configuration cfg = new Configuration();
        for(String resource : resources)
        {
            logger.debug("Persistence resource [" + resource + "] in list.");
            cfg.addResource(resource);
            logger.debug("Persistence resource [" + resource + "] added.");
        }

        // if we have properties then set them
        if(properties != null)
            cfg.setProperties(properties);

        // return a built session factory
        return cfg.buildSessionFactory();
    }

}