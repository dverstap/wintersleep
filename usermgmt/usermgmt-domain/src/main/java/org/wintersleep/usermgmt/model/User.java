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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Autowire;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Configurable(autowire = Autowire.BY_TYPE, preConstruction = true)
public class User implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, updatable = false, unique = true, length = 8)
    private String login;

    @Column(nullable = false, length = 16)
    // TODO null is allowed when using external password
    private String password;

    @Column(nullable = false, length = 32)
    private String fullName;

    @ManyToOne(optional = false)
    private UserProfile userProfile;

    @Transient
    @Autowired
    private transient UserManagementService userManagementService;

    public User() {
    }

    public User(String login, String password, String fullName, UserProfile userProfile) {
        this.login = login;
        this.password = password;
        this.fullName = fullName;
        this.userProfile = userProfile;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public UserManagementService getUserManagementService() {
        return userManagementService;
    }

    public String getMsg() {
        String msg = "loaded by " + this.getClass().getClassLoader() + ": ";

        if (userManagementService == null) {
            return msg + "it's null";
        } else {
            return msg + "it's not null";
        }
    }

    public String toString() {
        return "User{" +
                "userManagementService=" + userManagementService +
                '}';
    }
}