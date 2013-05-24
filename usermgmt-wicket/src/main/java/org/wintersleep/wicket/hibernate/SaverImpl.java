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

package org.wintersleep.wicket.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class SaverImpl implements Saver {

    private static final Logger log = LoggerFactory.getLogger(SaverImpl.class);

    private SessionFactory sessionFactory;

    public SaverImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    public void save(Object... entities) {
        Session session = sessionFactory.getCurrentSession();
        log.debug("flushMode={}", session.getFlushMode());
        for (Object entity : entities) {
            log.debug("Saving {}", entity);
            session.saveOrUpdate(entity);
            session.flush();
        }
    }

    @Transactional
    public void delete(Object... entities) {
        Session session = sessionFactory.getCurrentSession();
        for (Object entity : entities) {
            log.debug("Deleting {}", entity);
            session.delete(entity);
        }
    }
}
