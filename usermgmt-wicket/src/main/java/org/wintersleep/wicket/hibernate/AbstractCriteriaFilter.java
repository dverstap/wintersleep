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

import com.google.common.base.Preconditions;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.IFilterStateLocator;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

public abstract class AbstractCriteriaFilter<T> implements IFilterStateLocator<T>, CriteriaBuilder {

    protected T filterState;

    public AbstractCriteriaFilter(T filterState) {
        Preconditions.checkNotNull(filterState);
        this.filterState = filterState;
    }

    public T getFilterState() {
        return filterState;
    }

    public void setFilterState(T filterState) {
        this.filterState = filterState;
    }

    /**
     * 'Human-friendly' case insensitive match: accepts expression with '*' wildcards,
     * and escapes SQL wildcard characters.
     *
     * @param propertyName The property of the hibernate entity to filter on
     * @param humanExpression Expression which can contain 0 or more '*' wildcard characters
     * @return The Hibernate Criterion
     */
    protected Criterion hilike(String propertyName, String humanExpression) {
        String sqlExpression = humanExpression.replace("%", "\\%").replace('*', '%').replace("_", "\\_");
        return Restrictions.ilike(propertyName, sqlExpression);
    }

}
