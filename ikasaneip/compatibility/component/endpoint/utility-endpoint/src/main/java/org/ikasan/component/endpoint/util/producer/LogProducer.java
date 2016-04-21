/* 
 * $Id$
 * $URL$
 *
 * ====================================================================
 * Ikasan Enterprise Integration Platform
 * 
 * Distributed under the Modified BSD License.
 * Copyright notice: The copyright for this software and a full listing 
 * of individual contributors are as shown in the packaged copyright.txt 
 * file. 
 * 
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 *
 *  - Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 *
 *  - Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 *
 *  - Neither the name of the ORGANIZATION nor the names of its contributors may
 *    be used to endorse or promote products derived from this software without 
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE 
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL 
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR 
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER 
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE 
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * ====================================================================
 */
package org.ikasan.component.endpoint.util.producer;

import org.apache.log4j.Logger;
import org.ikasan.spec.component.endpoint.EndpointException;
import org.ikasan.spec.component.endpoint.Producer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implementation of a producer as a logger.
 *
 * @author Ikasan Development Team
 */
public class LogProducer<T>
        implements Producer<T>
{
    /** logger instance */
    Logger logger = Logger.getLogger(LogProducer.class);

    /** text to accompany the payload logging */
    String text;

    /** regexp pattern to apply */
    Pattern pattern;

    /**
     * Constructor
     * @param text
     * @param eventPlaceholder
     */
    public LogProducer(String text, String eventPlaceholder)
    {
        this.text = text;
        if(text == null)
        {
            throw new IllegalArgumentException("text cannot be 'null'");
        }

        if(eventPlaceholder == null)
        {
            throw new IllegalArgumentException("eventPlaceholder cannot be 'null'");
        }

        this.pattern = Pattern.compile(eventPlaceholder);
    }

    /**
     * Constructor
     */
    public LogProducer()
    {
        // nothing here
    }

    @Override
    public void invoke(T payload) throws EndpointException
    {
        if(logger.isInfoEnabled())
        {
            if(pattern == null)
            {
                logger.info(payload.toString());
            }
            else
            {
                Matcher matcher = pattern.matcher(text);
                logger.info(matcher.replaceAll(payload.toString()));
            }
        }
    }
}
