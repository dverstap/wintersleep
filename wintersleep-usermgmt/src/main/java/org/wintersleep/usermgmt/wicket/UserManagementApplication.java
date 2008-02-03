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

import net.databinder.DataApplication;

import org.hibernate.SessionFactory;
import org.hibernate.mapping.PersistentClass;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.orm.hibernate3.LocalSessionFactoryBean;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.log4j.Logger;

import java.util.Iterator;

public class UserManagementApplication extends DataApplication {

    private static final Logger m_log = Logger.getLogger(UserManagementApplication.class);

    @Override
	public Class getHomePage() {
		return UserListPage.class;
	}

    @Override
    protected void init() {
        super.init();
        addComponentInstantiationListener(new SpringComponentInjector(this));

        try {
            mountBookmarkablePages();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        // disable cookieless interaction to improve search engine crawlability
		setCookielessSupported(false);
    }

    // I can't really get this REST-thingy to work: it seems databinder supports it for panels and such,
    // but not for plain pages. Futhermore, the example app (cookbook) seems to do this only for showing
    // recipes, not for editing them.
    private void mountBookmarkablePages() throws ClassNotFoundException {
        WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());

        LocalSessionFactoryBean sessionFactoryBean = (LocalSessionFactoryBean) context.getBean("&sessionFactory");
        Iterator<PersistentClass> classMappings = sessionFactoryBean.getConfiguration().getClassMappings();
        while (classMappings.hasNext()) {
            PersistentClass persistentClass = classMappings.next();
            Class entityClass = Class.forName(persistentClass.getClassName());
            m_log.info("Adding bookmark support for: " + persistentClass.getEntityName() + ": " + entityClass);
            mountBookmarkablePage(entityClass.getSimpleName(), entityClass);
        }

    }

    @Override
    public void buildHibernateSessionFactory(Object key) {
        WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        SessionFactory sessionFactory = (SessionFactory) context.getBean("sessionFactory");
        setHibernateSessionFactory(null, sessionFactory);

    }
}