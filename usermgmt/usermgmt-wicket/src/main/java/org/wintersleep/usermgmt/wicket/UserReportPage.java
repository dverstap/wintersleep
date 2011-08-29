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

package org.wintersleep.usermgmt.wicket;

import net.databinder.components.hib.PageSourceLink;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.wintersleep.databinder.HibernateProvider;
import net.databinder.models.hib.HibernateObjectModel;
import net.databinder.models.hib.CriteriaBuilder;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortState;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortStateLocator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.HeadersToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigationToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.*;
import org.apache.wicket.extensions.markup.html.repeater.util.SingleSortState;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.OddEvenItem;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.wintersleep.usermgmt.model.User;
import org.wintersleep.usermgmt.wicket.base.ActionsPanel;
import org.wintersleep.usermgmt.wicket.base.BasePage;
import org.wintersleep.usermgmt.wicket.base.GoAndClearAndNewFilter;

// This class is just here to demonstrate how to do tables with a rowspan using wicket
public class UserReportPage extends BasePage {

    static int counter = 0;

    public UserReportPage() {
        super();

        UserFilter filter = new UserFilter();
        final FilterForm filterForm = new FilterForm("form", filter);

        // list of columns allows table to render itself without any markup from us
        IColumn[] columns = new IColumn[]{

                new FilteredAbstractColumn<User>(new Model<String>("Actions")) {

                    // add the ActionsPanel to the cell item
                    public void populateItem(Item<ICellPopulator<User>> cellItem, String componentId, final IModel<User> model) {
                        if (counter % 2 == 0) {
                            cellItem.add(new AttributeModifier("rowspan", true, new Model<String>("2")));
                            cellItem.add(new ActionsPanel(componentId,
                                    new PageSourceLink<User>("showLink", UserEditPage.class, model),
                                    new IPageLink() {
                                        public Page getPage() {
                                            return new UserEditPage(UserReportPage.this, (HibernateObjectModel<User>) model);
                                        }

                                        public Class<UserEditPage> getPageIdentity() {
                                            return UserEditPage.class;
                                        }
                                    },
                                    new IPageLink() {
                                        public Page getPage() {
                                            User user = model.getObject();
                                            return new DeletePage<User>(UserReportPage.this, (HibernateObjectModel<User>) model, "User: " + user.getFullName());
                                        }

                                        public Class<DeletePage> getPageIdentity() {
                                            return DeletePage.class;
                                        }
                                    }
                            )
                            );
                        } else {
                            cellItem.add(new Label(componentId, ""));
                            //cellItem.add(new AttributeModifier("colspan", true, new Model("0")));
                            cellItem.setVisible(false);
                        }
                        counter++;
                    }

                    // return the go-and-clear filter for the filter toolbar
                    public Component getFilter(String componentId, FilterForm form) {
                        return new GoAndClearAndNewFilter(componentId, form) {
                            public Page createNewPage() {
                                return new UserEditPage(UserReportPage.this, new HibernateObjectModel<User>(new User()));
                            }
                        };
                    }
                },
                new TextFilteredPropertyColumn(new Model<String>("Login"), "login", "login"),
                new TextFilteredPropertyColumn(new Model<String>("Name"), "fullName", "fullName"),
/*
                // custom column to enable countries be be in the list and labeled correctly
                new MyChoiceFilteredPropertyColumn(new Model("Birth country"), "country.name", "country.name", "country", "name", countries),
                new MyChoiceFilteredPropertyColumn(new Model("Birth city"), "birthCity.name", "birthCity.name", "birthCity", "name", cities),
                new PropertyColumn(new Model("Final game"), "finalGame", "finalGame")
*/
        };
        UserSorter sorter = new UserSorter();
        HibernateProvider<User> provider = new HibernateProvider<User>(User.class, filter, sorter);
        provider.setWrapWithPropertyModel(false);
        DataTable<User> table = new DataTable<User>("table", columns, provider, 25) {
            protected Item<User> newRowItem(String id, int index, IModel<User> model) {
                return new OddEvenItem<User>(id, index, model);
            }
        };
        table.addTopToolbar(new NavigationToolbar(table));
        table.addTopToolbar(new HeadersToolbar(table, sorter));
        table.addTopToolbar(new FilterToolbar(table, filterForm, filter));
        add(filterForm.add(table));
    }

    class UserFilter implements IFilterStateLocator, CriteriaBuilder {
        private User filterState = new User();

        /**
         * Apply filter to criteria.
         */
        public void build(Criteria criteria) {
            if (filterState.getLogin() != null) {
                criteria.add(Restrictions.ilike("login", filterState.getLogin(), MatchMode.START));
            }
            if (filterState.getFullName() != null) {
                criteria.add(Restrictions.ilike("fullName", filterState.getFullName(), MatchMode.START));
            }
        }

        public User getFilterState() {
            return filterState;
        }

        public void setFilterState(Object filterState) {
            this.filterState = (User) filterState;
        }
    }

    class UserSorter implements ISortStateLocator, CriteriaBuilder {
        private SingleSortState sortState = new SingleSortState();

        public void build(Criteria criteria) {
            SortParam sort = sortState.getSort();
            if (sort != null) {
                criteria.addOrder(sort.isAscending() ? Order.asc(sort.getProperty()) : Order.desc(sort.getProperty()));
            }
        }

        public ISortState getSortState() {
            return sortState;
        }

        public void setSortState(ISortState state) {
            sortState = (SingleSortState) state;
        }
    }
}