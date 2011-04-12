/* 
 * $Id$
 * $URL$
 *
 * ====================================================================
 * Ikasan Enterprise Integration Platform
 * Copyright (c) 2007-2008 Ikasan Ltd and individual contributors as indicated
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
package org.ikasan.framework.payload.service;

import java.util.HashMap;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.naming.NamingException;
import javax.resource.ResourceException;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.ikasan.common.Payload;
import org.ikasan.common.security.IkasanSecurityConf;
import org.ikasan.framework.messaging.jms.JndiDestinationFactory;
import org.ikasan.framework.payload.serialisation.JmsMessagePayloadSerialiser;
import org.jmock.Expectations;
import org.jmock.Mockery;
/**
 * JUnit test class for Database payload provider
 * 
 * @author Ikasan Development Teams
 */
public class JMSPayloadPublisherTest extends TestCase
{
    /**
     * Mockery for interfaces
     */
    private Mockery mockery = new Mockery();

    /**
     * jms username
     */
    private final String jmsUsername = "jmsUsername";

    /**
     * jms password
     */
    private final String jmsPassword = "jmsPassword";

    /**
     * mock of the destination
     */
    final Destination destination = mockery.mock(Destination.class);

    /**
     * mock of the connection factory
     */
    final ConnectionFactory jmsConnectionFactory = mockery.mock(ConnectionFactory.class);

    /**
     * mock of the serialiser
     */
    final JmsMessagePayloadSerialiser<Message> jmsMessagePayloadSerialiser = mockery.mock(JmsMessagePayloadSerialiser.class);

    /**
     * mock of the security conf
     */
    final IkasanSecurityConf ikasanSecurityConf = mockery.mock(IkasanSecurityConf.class);

    /**
     * mock of the payload
     */
    final Payload payload = mockery.mock(Payload.class);

    /**
     * mock of the connection
     */
    final Connection connection = mockery.mock(Connection.class);

    /**
     * mock of the jms session
     */
    final Session session = mockery.mock(Session.class);

    /**
     * mock of the map message
     */
    final MapMessage mapMessage = mockery.mock(MapMessage.class);

    /**
     * mock of the message producer
     */
    final MessageProducer messageProducer = mockery.mock(MessageProducer.class);

    /**
     * JMSException
     */
    final JMSException jmsException = new JMSException(null);


    
    /**
     * mock of the jndiDestinationFactory
     */
    final JndiDestinationFactory jndiDestinationFactory = mockery.mock(JndiDestinationFactory.class);

    /**
     * Test method for
     * {@link org.ikasan.framework.payload.service.JMSPayloadPublisher#publish(org.ikasan.common.Payload)}
     * .
     * @throws JMSException thrown when error publishing a message.
     * @throws ResourceException wrapper for all PayloadPublisher exceptions
     */
    public void testPublish_withSecurityPublishesWithSecureConnection() throws ResourceException, JMSException
    {
        mockery.checking(new Expectations()
        {
            {
                one(payload).getId();
                will(returnValue("dummy id list"));

                one(ikasanSecurityConf).getJMSUsername();
                will(returnValue(jmsUsername));
                one(ikasanSecurityConf).getJMSPassword();
                will(returnValue(jmsPassword));
                one(jmsConnectionFactory).createConnection(jmsUsername, jmsPassword);
                will(returnValue(connection));
                one(connection).createSession(true, javax.jms.Session.AUTO_ACKNOWLEDGE);
                will(returnValue(session));
                one(jmsMessagePayloadSerialiser).toMessage(payload, session);
                will(returnValue(mapMessage));
                one(session).createProducer(destination);
                will(returnValue(messageProducer));
                one(messageProducer).send(mapMessage);
                one(connection).close();
            }
        });
        final JMSPayloadPublisher publisher = new JMSPayloadPublisher(destination, jmsConnectionFactory, jmsMessagePayloadSerialiser, ikasanSecurityConf);
        publisher.publish(payload);
    }

    /**
     * Test method for
     * {@link org.ikasan.framework.payload.service.JMSPayloadPublisher#publish(org.ikasan.common.Payload)}
     * .
     * @throws JMSException thrown when error publishing a message.
     * @throws ResourceException wrapper for all PayloadPublisher exceptions
     */
    public void testPublish_withSecurityWithPrioritySetter() throws ResourceException, JMSException
    {
        mockery.checking(new Expectations()
        {
            {
                one(payload).getId();
                will(returnValue("dummy id list"));

                one(ikasanSecurityConf).getJMSUsername();
                will(returnValue(jmsUsername));
                one(ikasanSecurityConf).getJMSPassword();
                will(returnValue(jmsPassword));
                one(jmsConnectionFactory).createConnection(jmsUsername, jmsPassword);
                will(returnValue(connection));
                one(connection).createSession(true, javax.jms.Session.AUTO_ACKNOWLEDGE);
                will(returnValue(session));
                one(jmsMessagePayloadSerialiser).toMessage(payload, session);
                will(returnValue(mapMessage));
                one(session).createProducer(destination);
                will(returnValue(messageProducer));
                one(messageProducer).setPriority(with(any(Integer.class)));
                one(messageProducer).send(mapMessage);
                one(connection).close();
            }
        });
        final JMSPayloadPublisher publisher = new JMSPayloadPublisher(destination, jmsConnectionFactory, jmsMessagePayloadSerialiser,
            ikasanSecurityConf);
        final Integer priority = new Integer(1);
        publisher.setPriority(priority);
        publisher.publish(payload);
    }

    /**
     * Test method for
     * {@link org.ikasan.framework.payload.service.JMSPayloadPublisher#publish(org.ikasan.common.Payload)}
     * .
     * @throws JMSException thrown when error publishing a message.
     * @throws ResourceException wrapper for all PayloadPublisher exceptions
     */
    public void testPublish_withoutSecurityPublishesWithUnsecuredConnection() throws ResourceException, JMSException
    {
        mockery.checking(new Expectations()
        {
            {
                one(payload).getId();
                will(returnValue("dummy id list"));

                one(jmsConnectionFactory).createConnection();
                will(returnValue(connection));
                one(connection).createSession(true, javax.jms.Session.AUTO_ACKNOWLEDGE);
                will(returnValue(session));
                one(jmsMessagePayloadSerialiser).toMessage(payload, session);
                will(returnValue(mapMessage));
                one(session).createProducer(destination);
                will(returnValue(messageProducer));
                one(messageProducer).send(mapMessage);
                one(connection).close();
            }
        });
        final JMSPayloadPublisher publisher = new JMSPayloadPublisher(destination, jmsConnectionFactory, jmsMessagePayloadSerialiser, null);
        publisher.publish(payload);
    }

    /**
     * Test method for
     * {@link org.ikasan.framework.payload.service.JMSPayloadPublisher#publish(org.ikasan.common.Payload)}
     * .
     * @throws JMSException thrown when error publishing a message.
     */
    public void testPublish_throwsResourceExceptionWhenConnectionFactoryThrowsJMSException() throws JMSException
    {
        mockery.checking(new Expectations()
        {
            {
                one(jmsConnectionFactory).createConnection();
                will(throwException(jmsException));
                one(connection).close();
            }
        });
        final JMSPayloadPublisher publisher = new JMSPayloadPublisher(destination, jmsConnectionFactory, jmsMessagePayloadSerialiser, null);
        try
        {
            publisher.publish(payload);
            fail("Exception should have been thrown");
        }
        catch (ResourceException p)
        {
            assertTrue("underlyingException should be the JMSException", jmsException.equals(p.getCause()));
        }
    }

    /**
     * Test method for
     * {@link org.ikasan.framework.payload.service.JMSPayloadPublisher#publish(org.ikasan.common.Payload)}
     * .
     * @throws JMSException thrown when error publishing a message.
     */
    public void testPublish_throwsRespirceWhenConnectionThrowsJMSException() throws JMSException
    {
        mockery.checking(new Expectations()
        {
            {
                one(jmsConnectionFactory).createConnection();
                will(returnValue(connection));
                one(connection).createSession(true, javax.jms.Session.AUTO_ACKNOWLEDGE);
                will(throwException(jmsException));
                one(connection).close();
            }
        });
        final JMSPayloadPublisher publisher = new JMSPayloadPublisher(destination, jmsConnectionFactory, jmsMessagePayloadSerialiser, null);
        try
        {
            publisher.publish(payload);
            fail("Exception should have been thrown");
        }
        catch (ResourceException p)
        {
            assertTrue("underlyingException should be the JMSException", jmsException.equals(p.getCause()));
        }
    }



    /**
     * Test method for
     * {@link org.ikasan.framework.payload.service.JMSPayloadPublisher#publish(org.ikasan.common.Payload)}
     * .
     * @throws JMSException thrown when error publishing a message.
     */
    public void testPublish_throwsResourceExceptionWhenClosingConnectionThrowsJMSException() throws JMSException
    {
        mockery.checking(new Expectations()
        {
            {
                one(payload).getId();
                will(returnValue("dummy id list"));

                one(ikasanSecurityConf).getJMSUsername();
                will(returnValue(jmsUsername));
                one(ikasanSecurityConf).getJMSPassword();
                will(returnValue(jmsPassword));
                one(jmsConnectionFactory).createConnection(jmsUsername, jmsPassword);
                will(returnValue(connection));
                one(connection).createSession(true, javax.jms.Session.AUTO_ACKNOWLEDGE);
                will(returnValue(session));
                one(jmsMessagePayloadSerialiser).toMessage(payload, session);
                will(returnValue(mapMessage));
                one(session).createProducer(destination);
                will(returnValue(messageProducer));
                one(messageProducer).send(mapMessage);
                one(connection).close();
                will(throwException(jmsException));
            }
        });
        final JMSPayloadPublisher publisher = new JMSPayloadPublisher(destination, jmsConnectionFactory, jmsMessagePayloadSerialiser,
            ikasanSecurityConf);
        try
        {
            publisher.publish(payload);
            fail("Exception should have been thrown");
        }
        catch (ResourceException p)
        {
            assertTrue("underlyingException should be the JMSException", jmsException.equals(p.getCause()));
        }
    }
    
    public void testPublish_willUtiliseDestinationFactoryWhenSupplied() throws NamingException, JMSException, ResourceException {


        mockery.checking(new Expectations()
        {
            {
                one(payload).getId();
                will(returnValue("dummy id list"));
                
                one(jndiDestinationFactory).getDestination(true);
                will(returnValue(destination));
                one(jmsConnectionFactory).createConnection();
                will(returnValue(connection));
                one(connection).createSession(true, javax.jms.Session.AUTO_ACKNOWLEDGE);
                will(returnValue(session));
                one(jmsMessagePayloadSerialiser).toMessage(payload, session);
                will(returnValue(mapMessage));
                one(session).createProducer(destination);
                will(returnValue(messageProducer));
                one(messageProducer).send(mapMessage);
                one(connection).close();
            }
        });
        
        final JMSPayloadPublisher publisher = new JMSPayloadPublisher(jndiDestinationFactory, jmsConnectionFactory, jmsMessagePayloadSerialiser,
                null);
        publisher.publish(payload);
        
        mockery.assertIsSatisfied();
    }
    
    public void testPublish_willThrowResourceExceptionForNamingException() throws NamingException{
        final JMSPayloadPublisher publisher = new JMSPayloadPublisher(jndiDestinationFactory, jmsConnectionFactory, jmsMessagePayloadSerialiser,
                null);
        
        final NamingException namingException = new NamingException();
        final String jndiName = "jndiName";
        final Map<?,?> environment = new HashMap<String, String>();
        
        mockery.checking(new Expectations()
        {
            {
            	one(jndiDestinationFactory).getJndiName();
            	will(returnValue(jndiName));
            	
            	one(jndiDestinationFactory).getEnvironment();
            	will(returnValue(environment));
            	
                one(jndiDestinationFactory).getDestination(true);
                will(throwException(namingException));
            }
        });
        
        ResourceException thrownException = null;
        try {
			publisher.publish(payload);
			fail("ResourceException should have been thrown for NamingException");
		} catch (ResourceException e) {
			thrownException = e;
		}
		Assert.assertNotNull("ResourceException should have been thrown for NamingException", thrownException);
    	Assert.assertEquals("underlying exception should have been NamingException",namingException, thrownException.getCause());
    
    	mockery.assertIsSatisfied();
    }
}