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

import org.apache.wicket.model.LoadableDetachableModel;

import java.io.Serializable;
import java.util.List;

public class HibernateListModel<ID extends Serializable, T> extends LoadableDetachableModel<List<T>> {

    private final String query;

    public HibernateListModel(String query) {
        this.query = query;
    }

    @Override
    protected List<T> load() {
        return WicketHibernateUtil.getCurrentSession().createQuery(query).list();
    }

}
