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

import org.springframework.test.AbstractTransactionalSpringContextTests;

import java.util.List;
import java.util.TreeSet;

// TODO figure out decent testing strategy
public class UserRepositoryTest extends AbstractTransactionalSpringContextTests {

    private RoleRepository roleRepository;
    private UserProfileRepository userProfileRepository;
    private UserRepository userRepository;

    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public void setUserProfileRepository(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    protected String getConfigPath() {
        return "/testApplicationContext.xml";
    }

    public void testFindById() {
        Role role = roleRepository.findById(1L);
        assertNull(role);
    }

    public void testFindAll() {
        List<Role> roles = roleRepository.findAll();
        assertEquals(0, roles.size());
    }

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