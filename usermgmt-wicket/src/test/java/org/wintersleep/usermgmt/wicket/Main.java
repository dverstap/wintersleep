/*
 * Copyright 2008 Davy Verstappen.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wintersleep.usermgmt.wicket;

import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.webapp.WebAppContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * You need to run this with JVM option:
 * <p/>
 * -javaagent:${settings.localRepository}/org/springframework/spring-instrument/3.1.3.RELEASE/spring-instrument-3.1.3.RELEASE.jar
 */
public class Main {

    private static final Logger log = Logger.getLogger(Main.class);


    public static void main(String[] args) {

        // start spring container
        String[] configLocations = new String[]{"/spring-jetty.xml"};

        ApplicationContext context = new ClassPathXmlApplicationContext(configLocations);
        Server server = context.getBean("jettyServer", Server.class);
        ServerConnector connector = (ServerConnector) server.getConnectors()[0];
        String host = connector.getHost();
        int port = connector.getLocalPort();
        WebAppContext webAppContext = (WebAppContext) server.getHandler();
        String contextPath = webAppContext.getContextPath();
        if (log.isInfoEnabled()) {
            log.info("server started - 'http://" + host + ":" + port + contextPath + "'");
        }

    }
}
