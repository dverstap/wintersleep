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

package org.wintersleep.usermgmt;

import org.apache.log4j.Logger;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.ContextHandlerCollection;
import org.mortbay.jetty.webapp.WebAppContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * You need to run this with JVM option:
 *
 * -javaagent:/udir/stappend/maven/repositories/local/org/springframework/spring-agent/2.5.5/spring-agent-2.5.5.jar
 */
public class Main {

    private static final Logger log = Logger.getLogger(Main.class);


    public static void main(String[] args) {

        // start spring container
        String[] configLocations = new String[]{"/spring-jetty.xml"};

        ApplicationContext context = new ClassPathXmlApplicationContext(configLocations);
        Server server = (Server) context.getBean("jettyServer");
        // get host
        String host = server.getConnectors()[0].getHost();
        if (host == null) {
            host = "localhost";
        }
        // get port
        int port = server.getConnectors()[0].getPort();
        // get context path
        Handler[] handlers = ((ContextHandlerCollection) server.getHandlers()[0]).getHandlers();
        String contextPath = ((WebAppContext) handlers[0]).getContextPath();
        if (log.isInfoEnabled()) {
            log.info("server started - 'http://" + host + ":" + port + contextPath + "'");
        }

    }
}
