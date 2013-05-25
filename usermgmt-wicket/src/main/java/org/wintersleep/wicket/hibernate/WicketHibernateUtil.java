/*
 * Copyright 2013 Davy Verstappen.
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
package org.wintersleep.wicket.hibernate;

import org.apache.wicket.protocol.http.WebApplication;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;

public class WicketHibernateUtil {

    /**
     * This is typically used to pass to the constructor of new Hibernate entities:
     * <ul>
     *     <li>
     *         For the natural key property/properties, so that the equals/hashCode methods don't throw NPE.
     *     </li>
     *     <li>
     *         For filter state objects, again so that equals/hashCode does not throw NPE.
     *     </li>
     * </ul>
     */
    public static final String EMPTY_STRING = "";

    public static SessionFactory getSessionFactory() {
        ServletContext servletContext = WebApplication.get().getServletContext();
        WebApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
        return applicationContext.getBean(SessionFactory.class);
    }

    public static Session getCurrentSession() {
        return getSessionFactory().getCurrentSession();
    }

}
