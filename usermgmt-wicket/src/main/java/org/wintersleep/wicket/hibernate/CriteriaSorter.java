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

import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortState;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortStateLocator;
import org.apache.wicket.extensions.markup.html.repeater.util.SingleSortState;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;

public class CriteriaSorter implements ISortStateLocator<String>, CriteriaBuilder {

    private SingleSortState<String> sortState = new SingleSortState<>();

    public void build(Criteria criteria) {
        SortParam<String> sort = sortState.getSort();
        if (sort != null) {
            criteria.addOrder(sort.isAscending() ? Order.asc(sort.getProperty()) : Order.desc(sort.getProperty()));
        }
    }

    public ISortState<String> getSortState() {
        return sortState;
    }

}
