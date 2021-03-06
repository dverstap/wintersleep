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
package org.wintersleep.usermgmt.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.TreeSet;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"../../../../testApplicationContext.xml"})
@TransactionConfiguration
@Transactional
public class UserRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserProfileRepository userProfileRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindById() {
        Role role = roleRepository.findById(1L);
        assertNull(role);
    }

    @Test
    public void testFindAll() {
        List<Role> roles = roleRepository.findAll();
        assertEquals(0, roles.size());
    }

    @Test
    public void testFindAllSortedByProfile() {
        UserProfile profile1 = new UserProfile("profile1", new TreeSet<Role>());
        userProfileRepository.makePersistent(profile1);
        UserProfile profile2 = new UserProfile("profile2", new TreeSet<Role>());
        userProfileRepository.makePersistent(profile2);

        User user1 = new User("user1", "user1", "User 1", profile1);
        userRepository.makePersistent(user1);

        User user2 = new User("user2", "user2", "User 2", profile2);
        userRepository.makePersistent(user2);

        User user3 = new User("user3", "user3", "User 3", profile1);
        userRepository.makePersistent(user3);

        User user4 = new User("user4", "user4", "User 4", profile2);
        userRepository.makePersistent(user4);

        List<User> users = userRepository.findAllSortedByProfile();
        assertEquals(4, users.size());
        assertSame(user1, users.get(0));
        assertSame(user3, users.get(1));
        assertSame(user2, users.get(2));
        assertSame(user4, users.get(3));
    }

}