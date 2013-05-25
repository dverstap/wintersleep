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

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.HeadersToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigationToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilteredAbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.TextFilteredPropertyColumn;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.OddEvenItem;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.hibernate.Criteria;
import org.wintersleep.usermgmt.model.User;
import org.wintersleep.usermgmt.wicket.base.ActionsPanel;
import org.wintersleep.usermgmt.wicket.base.BasePage;
import org.wintersleep.usermgmt.wicket.base.GoAndClearAndNewFilter;
import org.wintersleep.wicket.hibernate.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserListPage extends BasePage {

    public UserListPage() {
        super();

        List<IColumn<User, String>> columns = new ArrayList<>();
        columns.add(new FilteredAbstractColumn<User, String>(new Model<>("Actions")) {

            public void populateItem(Item<ICellPopulator<User>> cellItem, String componentId, final IModel<User> model) {
                cellItem.add(new ActionsPanel(componentId,
                        new PageSourceLink<>("showLink", UserEditPage.class, model),
                        new IPageLink() {
                            public Page getPage() {
                                return new UserEditPage(UserListPage.this, (HibernateObjectModel<Long, User>) model);
                            }

                            public Class<UserEditPage> getPageIdentity() {
                                return UserEditPage.class;
                            }
                        },
                        new IPageLink() {
                            public Page getPage() {
                                User user = model.getObject();
                                return new DeletePage<>(UserListPage.this, model, "User: " + user.getFullName());
                            }

                            public Class<DeletePage> getPageIdentity() {
                                return DeletePage.class;
                            }
                        }
                )
                );
            }

            public Component getFilter(String componentId, FilterForm<?> form) {
                return new GoAndClearAndNewFilter(componentId, form) {
                    public Page createNewPage() {
                        return new UserEditPage(UserListPage.this, new HibernateObjectModel<Long, User>(new User(WicketHibernateUtil.EMPTY_STRING)));
                    }
                };
            }
        });
        columns.add(new TextFilteredPropertyColumn<User, User, String>(new Model<>("Login"), "login", "login"));
        columns.add(new TextFilteredPropertyColumn<User, User, String>(new Model<>("Name"), "fullName", "fullName"));
/*
                // custom column to enable countries be be in the list and labeled correctly
                new MyChoiceFilteredPropertyColumn(new Model("Birth country"), "country.name", "country.name", "country", "name", countries),
                new MyChoiceFilteredPropertyColumn(new Model("Birth city"), "birthCity.name", "birthCity.name", "birthCity", "name", cities),
                new PropertyColumn(new Model("Final game"), "finalGame", "finalGame")
*/

        UserFilter filter = new UserFilter();
        final FilterForm<UserFilterState> filterForm = new FilterForm<>("form", filter);
        CriteriaSorter sorter = new CriteriaSorter();
        HibernateProvider<Long, User> provider = new HibernateProvider<>(User.class, filter, sorter);
        DataTable<User, String> table = new DataTable<User, String>("table", columns, provider, 25) {
            protected Item<User> newRowItem(String id, int index, IModel<User> model) {
                return new OddEvenItem<>(id, index, model);
            }
        };
        table.addTopToolbar(new NavigationToolbar(table));
        table.addTopToolbar(new HeadersToolbar<>(table, sorter));
        table.addTopToolbar(new FilterToolbar(table, filterForm, filter));

        add(filterForm.add(table));
    }

    static class UserFilterState implements Serializable {

        private String login;
        private String fullName;

        String getLogin() {
            return login;
        }

        void setLogin(String login) {
            this.login = login;
        }

        String getFullName() {
            return fullName;
        }

        void setFullName(String fullName) {
            this.fullName = fullName;
        }
    }

    static class UserFilter extends AbstractCriteriaFilter<UserFilterState> {

        public UserFilter() {
            super(new UserFilterState());
        }

        public void build(Criteria criteria) {
            if (filterState.getLogin() != null) {
                criteria.add(hilike("login", filterState.getLogin()));
            }
            if (filterState.getFullName() != null) {
                criteria.add(hilike("fullName", filterState.getFullName()));
            }
        }

    }

/*

    // This doesn't work, because:
    // - the concrete FilterStateLocator must really store a reference to the object returned from getFilterState()
    // - and because that actual object is updated
    // - and it seems that setFilterState is never actually called, except when you hit the clear filter button.
    //
    // This has some nasty consequences:
    // - The FilterToolbar<T, S> forces the same T for DataTable<T, S>, FilterForm<T> and IFilterStateLocator<T>
    // - The filterState is cloned using serialization by GoAndClearFilter, so it must be Serializable
    // - And since the filterState T has to be the entity, this forces our entity class to implement Serializable
    // - Which is something we'd like to avoid, because that would help us to prevent us from accidentally
    // - serializing it in the wicket stores etc

    static class UserFilter implements IFilterStateLocator<User>, CriteriaBuilder {
        private String login;
        private String fullName;

        public void build(Criteria criteria) {
            if (!Strings.isNullOrEmpty(login)) {
                criteria.add(Restrictions.ilike("login", login, MatchMode.START));
            }
            if (!Strings.isNullOrEmpty(fullName)) {
                criteria.add(Restrictions.ilike("fullName", fullName, MatchMode.START));
            }
        }

        @Override
        public User getFilterState() {
            return new User(login, null, fullName, null);
        }

        @Override
        public void setFilterState(User filterState) {
            this.login = filterState.getLogin();
            this.fullName = filterState.getFullName();
        }
    }
*/

}