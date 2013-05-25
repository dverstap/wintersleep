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

import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.IFilterStateLocator;

public class FilterToolbar extends org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterToolbar {

    /**
     * The Wicket FilterToolbar constructor unnecessarily enforces that the type of the filter state
     * is the same as the type of the row model. When using Hibernate entities whose equals/hashCode
     * is implemented using the mandatory business key (which is the right way), this often leads to
     * NPE's because the business key properties in the filter state will often be empty in the textbox,
     * which Wicket then sets as null.
     *
     * This artificial restriction was actually not there in 1.4, but was introduced in 1.5 as part of WICKET-3295.
     *
     * The FilterToolbar itself however works just fine when the types are different. This class just
     *
     * @param table        data table this toolbar will be added to
     * @param form         the filter form
     * @param stateLocator locator responsible for finding object used to store filter's state
     * @param <T>          type of the filter state object
     * @param <S>          type of the DataTable sorting parameter
     */
    public <T, S> FilterToolbar(DataTable<?, S> table, FilterForm<T> form, IFilterStateLocator<T> stateLocator) {
        super(table, (FilterForm) form, (IFilterStateLocator) stateLocator);
    }

}
