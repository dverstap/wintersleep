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
import net.databinder.models.hib.CriteriaBuilder;
import net.databinder.models.hib.HibernateObjectModel;
import org.wintersleep.databinder.HibernateProvider;
import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortState;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortStateLocator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.HeadersToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigationToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.ChoiceFilter;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.ChoiceFilteredPropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilteredAbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.IFilterStateLocator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.TextFilteredPropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.util.SingleSortState;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.OddEvenItem;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.wintersleep.usermgmt.model.UserProfile;
import org.wintersleep.usermgmt.wicket.base.ActionsPanel;
import org.wintersleep.usermgmt.wicket.base.BasePage;
import org.wintersleep.usermgmt.wicket.base.GoAndClearAndNewFilter;

public class UserProfileListPage extends BasePage {

    public UserProfileListPage() {
        super();

        UserProfileFilter filter = new UserProfileFilter();
        final FilterForm form = new FilterForm("form", filter);

        // list of columns allows table to render itself without any markup from us
        IColumn[] columns = new IColumn[]{

                new FilteredAbstractColumn(new Model<String>("Actions")) {

                    // add the ActionsPanel to the cell item
                    public void populateItem(Item cellItem, String componentId, final IModel model) {
                        cellItem.add(new ActionsPanel(componentId,
                                new PageSourceLink("showLink", UserProfileEditPage.class, model),
                                new IPageLink() {
                                    public Page getPage() {
                                        return new UserProfileEditPage(UserProfileListPage.this, (HibernateObjectModel<UserProfile>) model);
                                    }

                                    public Class getPageIdentity() {
                                        return UserProfileEditPage.class;
                                    }
                                },
                                new IPageLink() {
                                    public Page getPage() {
                                        UserProfile userProfile = (UserProfile) model.getObject();
                                        return new DeletePage<UserProfile>(UserProfileListPage.this, (HibernateObjectModel) model, "User Profile: " + userProfile.getName());
                                    }

                                    public Class getPageIdentity() {
                                        return DeletePage.class;
                                    }
                                }
                        )
                        );
                    }

                    // return the go-and-clear filter for the filter toolbar
                    public Component getFilter(String componentId, FilterForm form) {
                        return new GoAndClearAndNewFilter(componentId, form) {
                            public Page createNewPage() {
                                return new UserProfileEditPage(UserProfileListPage.this, new HibernateObjectModel<UserProfile>(new UserProfile()));
                            }
                        };
                    }
                },
                new TextFilteredPropertyColumn(new Model<String>("Name"), "name", "name"),
                //new MyChoiceFilteredPropertyColumn(new Model("Role"), "role.name", "role.name", "role", "name", roles),
                /*
                new MyChoiceFilteredPropertyColumn(new Model("Birth city"), "birthCity.name", "birthCity.name", "birthCity", "name", cities),
                new PropertyColumn(new Model("Final game"), "finalGame", "finalGame")
                */
        };
        UserProfileSorter sorter = new UserProfileSorter();
        HibernateProvider<UserProfile> provider = new HibernateProvider<UserProfile>(UserProfile.class, filter, sorter);
        provider.setWrapWithPropertyModel(false);
        DataTable<UserProfile> table = new DataTable<UserProfile>("table", columns, provider, 25) {
            protected Item<UserProfile> newRowItem(String id, int index, IModel<UserProfile> model) {
                return new OddEvenItem<UserProfile>(id, index, model);
            }
        };
        table.addTopToolbar(new NavigationToolbar(table));
        table.addTopToolbar(new HeadersToolbar(table, sorter));
        table.addTopToolbar(new FilterToolbar(table, form, filter));
        add(form.add(table));
    }

    class MyChoiceFilteredPropertyColumn extends ChoiceFilteredPropertyColumn {
        private ChoiceRenderer choiceRenderer;
        private String displayProperty;

        public MyChoiceFilteredPropertyColumn(IModel<String> displayModel, String sortProperty, String displayProperty, String propertyExpression, String filterLabelProperty, IModel filterChoices) {
            super(displayModel, sortProperty, propertyExpression, filterChoices);
            choiceRenderer = new ChoiceRenderer(filterLabelProperty);
            this.displayProperty = displayProperty;
        }

        protected IChoiceRenderer getChoiceRenderer() {
            return choiceRenderer;
        }

        protected IModel createLabelModel(IModel embeddedModel) {
            return new PropertyModel(embeddedModel, displayProperty);
        }

        public Component getFilter(String componentId, FilterForm form) {
            ChoiceFilter cf = (ChoiceFilter) super.getFilter(componentId, form);
            cf.getChoice().setNullValid(true);
            return cf;
        }
    }

    class UserProfileFilter implements IFilterStateLocator, CriteriaBuilder {
        private UserProfile filterState = new UserProfile();

        /**
         * Apply filter to criteria.
         */
        public void build(Criteria criteria) {
            if (filterState.getName() != null && filterState.getName().length() > 0) {
                criteria.add(Restrictions.ilike("name", filterState.getName(), MatchMode.START));
            }
        }

        public UserProfile getFilterState() {
            // TODO ugly stuff
            if (filterState.getName() == null) {
                filterState.setName("");
            }
            return filterState;
        }

        public void setFilterState(Object filterState) {
            this.filterState = (UserProfile) filterState;
        }
    }

    class UserProfileSorter implements ISortStateLocator, CriteriaBuilder {
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