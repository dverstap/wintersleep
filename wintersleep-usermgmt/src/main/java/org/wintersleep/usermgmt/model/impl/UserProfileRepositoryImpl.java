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

package org.wintersleep.usermgmt.model.impl;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Property;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.wintersleep.repository.RepositoryImpl;
import org.wintersleep.usermgmt.model.UserProfile;
import org.wintersleep.usermgmt.model.UserProfileRepository;

@Repository
public class UserProfileRepositoryImpl extends RepositoryImpl<UserProfile, Long> implements UserProfileRepository {

    private static final Property NAME = findProperty(UserProfile.class, "name");

    @Autowired
    public UserProfileRepositoryImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public UserProfile findByName(String name) {
        return uniqueResult(NAME.eq(name));
    }
}
