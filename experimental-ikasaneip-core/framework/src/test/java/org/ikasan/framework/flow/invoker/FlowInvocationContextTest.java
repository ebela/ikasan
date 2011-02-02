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
package org.ikasan.framework.flow.invoker;

import java.util.List;

import org.ikasan.core.flow.invoker.FlowInvocationContext;
import org.junit.Assert;
import org.junit.Test;


public class FlowInvocationContextTest {

	String componentName1 = "componentName1";
	String componentName2 = "componentName2";
	/**
	 * Test method for {@link org.ikasan.core.flow.invoker.FlowInvocationContext#getLastComponentName()}.
	 */
	@Test
	public void testGetLastComponentName_willReturnNullWhenNoComponentsAdded() {
		FlowInvocationContext flowInvocationContext = new FlowInvocationContext();
		Assert.assertNull(flowInvocationContext.getLastComponentName());
	}
	
	/**
	 * Test method for {@link org.ikasan.core.flow.invoker.FlowInvocationContext#getLastComponentName()}.
	 */
	@Test
	public void testGetLastComponentName_willReturnMostRecentlyAddedComponentName() {

		
		FlowInvocationContext flowInvocationContext = new FlowInvocationContext();
		Assert.assertNull(flowInvocationContext.getLastComponentName());
		
		flowInvocationContext.addInvokedComponentName(componentName1);
		Assert.assertEquals(componentName1, flowInvocationContext.getLastComponentName());
		
		flowInvocationContext.addInvokedComponentName(componentName2);
		Assert.assertEquals(componentName2, flowInvocationContext.getLastComponentName());

	}


	/**
	 * Test method for {@link org.ikasan.core.flow.invoker.FlowInvocationContext#getInvokedComponents()}.
	 */
	@Test
	public void testGetInvokedComponents() {
		FlowInvocationContext flowInvocationContext = new FlowInvocationContext();
		flowInvocationContext.addInvokedComponentName(componentName1);
		flowInvocationContext.addInvokedComponentName(componentName2);
		
		List<String> invokedComponents = flowInvocationContext.getInvokedComponents();
		Assert.assertEquals(componentName1, invokedComponents.get(0));
		Assert.assertEquals(componentName2, invokedComponents.get(1));
		
		//check the safety of the returned list - ensure it is a representation only and not the real thing
		invokedComponents.add("componentName3");
		
		Assert.assertFalse(invokedComponents.equals(flowInvocationContext.getInvokedComponents()));

	}
}
