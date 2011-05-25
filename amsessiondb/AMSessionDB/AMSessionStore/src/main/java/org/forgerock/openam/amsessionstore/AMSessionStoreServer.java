/**
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2011 ForgeRock AS. All Rights Reserved
 *
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can obtain a copy of the License at
 * http://forgerock.org/license/CDDLv1.0.html
 * See the License for the specific language governing
 * permission and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at http://forgerock.org/license/CDDLv1.0.html
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 */
package org.forgerock.openam.amsessionstore;

import java.util.logging.Level;
import org.forgerock.openam.amsessionstore.common.Constants;
import org.forgerock.openam.amsessionstore.common.Log;
import org.forgerock.openam.amsessionstore.common.SystemProperties;
import org.forgerock.openam.amsessionstore.db.DBStatistics;
import org.forgerock.openam.amsessionstore.db.PersistentStoreFactory;
import org.restlet.Component;
import org.restlet.data.Protocol;

/**
 * This is the main class of the amsessiondb server. Starts the RESTlet server
 * component and initialises the underlying persistent store implementation.
 * 
 * @author steve
 */

public class AMSessionStoreServer {
    private static final int DEFAULT_PORT = 8182;
    private final static String DEFAULT_URI = "/amsessiondb";
    
    public static void main( String[] args ) {
        // Create a new Component.  
        Component component = new Component();  
  
        int port = SystemProperties.getAsInt(Constants.PORT, DEFAULT_PORT);
        String uri = SystemProperties.get(Constants.URI, DEFAULT_URI);
        
        // Add a new HTTP server listening.  
        component.getServers().add(Protocol.HTTP, port);  
  
        // Attach the sample application.  
        component.getDefaultHost().attach(uri, new AmSessionDbApplication());  
  
        // Start the component, persistents store and statistics framework.  
        try {
            component.start(); 
            PersistentStoreFactory.getPersistentStore();
            DBStatistics.getInstance();
        } catch (Exception ex) {
            Log.logger.log(Level.WARNING, "Unable to start amsessiondb", ex);
        }
        
        Log.logger.log(Level.FINE, "amsessiondb started on port {0}", port);
    }
}
