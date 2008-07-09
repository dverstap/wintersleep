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

package org.wintersleep.usermgmt.wicket.base;

import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.springframework.transaction.annotation.Transactional;
import org.wintersleep.usermgmt.wicket.base.Saver;

@Transactional
public class SaverImpl implements Saver {

    private SessionFactory sessionFactory;

    public SaverImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void save(Object... entities) {
        Session session = sessionFactory.getCurrentSession();
        for (Object entity : entities) {
            session.saveOrUpdate(entity);
        }
    }

    public void delete(Object... entities) {
        Session session = sessionFactory.getCurrentSession();
        for (Object entity : entities) {
            session.delete(entity);
        }
    }
}
