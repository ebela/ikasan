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
package org.ikasan.demo;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.resource.ResourceException;

import org.ikasan.common.Payload;
import org.ikasan.common.component.Spec;
import org.ikasan.common.factory.PayloadFactory;
import org.ikasan.framework.payload.service.PayloadProvider;

import org.apache.log4j.Logger;

public class RandomWordPayloadProvider implements PayloadProvider{

	private List<String> words;
	
	private PayloadFactory payloadFactory;

	
	/** Logger */
    private static Logger logger = Logger.getLogger(RandomWordPayloadProvider.class);
	
	

    public RandomWordPayloadProvider(List<String> words, PayloadFactory payloadFactory) {
		super();
		this.words = words;
		this.payloadFactory = payloadFactory;
	}

	public List<Payload> getNextRelatedPayloads() throws ResourceException {
	    List<Payload> result = new ArrayList<Payload>();
		
		Random random = new Random();
		int nextInt = random.nextInt(words.size()-1);
		String randomWord = words.get(nextInt);
		
		int payloadId = randomWord.hashCode();
		payloadId = (int) ((37*payloadId)+ System.currentTimeMillis());
		
		Payload newPayload = payloadFactory.newPayload(""+payloadId, randomWord.getBytes());

		newPayload.setContent(randomWord.getBytes());
		newPayload.setAttribute("description", "A randomly generated element");
		result.add(newPayload);
		return result;
	}

}
