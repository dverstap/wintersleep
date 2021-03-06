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

import com.google.common.base.Preconditions;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.SortedSet;
import java.util.TreeSet;

@Entity
public class UserProfile implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @NaturalId
    @Column(nullable = false, length = 16, unique = true)
    private String name;

    @Sort(type = SortType.NATURAL)
    @ManyToMany
    private SortedSet<Role> roles = new TreeSet<>();

/*
    @Basic(optional = false)
    @Column(updatable = false)
    private boolean editable;

    @Basic(optional = false)
    @Column(updatable = false)
    private boolean hidden;
*/

    protected UserProfile() {
    }

    public UserProfile(String name) {
        this(name, new TreeSet<Role>());
    }

    public UserProfile(String name, SortedSet < Role > roles) {
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(roles);
        this.name = name;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SortedSet<Role> getRoles() {
        return roles;
    }

    public void setRoles(SortedSet<Role> roles) {
        this.roles = roles;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserProfile that = (UserProfile) o;
        return getName().equals(that.getName());
    }

    public int hashCode() {
        return getName().hashCode();
    }

    
}
