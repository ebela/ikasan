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
package org.ikasan.framework.component.routing;

/**
 * Exception representing a problem calculating a routing
 * 
 * This should only ever be thrown from within a Router's onEvent method, signifying that router's failure to calculate
 * route
 * 
 * @author Ikasan Development Team
 */
public class RouterException extends Exception
{
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 3203142027287174087L;

    /**
     * Constructor
     * 
     * @param cause The original cause of the exception
     */
    public RouterException(Throwable cause)
    {
        super(cause);
    }

    /**
     * Constructor
     * 
     * @param message The exception message
     */
    public RouterException(String message)
    {
        super(message);
    }
}
