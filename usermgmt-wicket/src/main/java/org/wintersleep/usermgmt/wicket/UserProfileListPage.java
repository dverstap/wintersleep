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

import com.google.common.base.Strings;
import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.HeadersToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigationToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.*;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.OddEvenItem;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.hibernate.Criteria;
import org.wintersleep.usermgmt.model.UserProfile;
import org.wintersleep.usermgmt.wicket.base.ActionsPanel;
import org.wintersleep.usermgmt.wicket.base.BasePage;
import org.wintersleep.usermgmt.wicket.base.GoAndClearAndNewFilter;
import org.wintersleep.wicket.hibernate.*;
import org.wintersleep.wicket.hibernate.FilterToolbar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserProfileListPage extends BasePage {

    public UserProfileListPage() {
        super();

        UserProfileFilter filter = new UserProfileFilter();
        final FilterForm<UserProfileFilterState> form = new FilterForm<>("form", filter);

        List<IColumn<UserProfile, String>> columns = new ArrayList<>();
        columns.add(new FilteredAbstractColumn<UserProfile, String>(new Model<>("Actions")) {

            public void populateItem(Item<ICellPopulator<UserProfile>> cellItem, String componentId, final IModel<UserProfile> model) {
                cellItem.add(new ActionsPanel(componentId,
                        new PageSourceLink<>("showLink", UserProfileEditPage.class, model),
                        new IPageLink() {
                            public Page getPage() {
                                return new UserProfileEditPage(UserProfileListPage.this, model);
                            }

                            public Class<UserProfileEditPage> getPageIdentity() {
                                return UserProfileEditPage.class;
                            }
                        },
                        new IPageLink() {
                            public Page getPage() {
                                UserProfile userProfile = model.getObject();
                                return new DeletePage<>(UserProfileListPage.this, model, "User Profile: " + userProfile.getName());
                            }

                            public Class<DeletePage> getPageIdentity() {
                                return DeletePage.class;
                            }
                        }
                )
                );
            }

            public Component getFilter(String componentId, FilterForm form) {
                return new GoAndClearAndNewFilter(componentId, form) {
                    public Page createNewPage() {
                        return new UserProfileEditPage(UserProfileListPage.this, new HibernateObjectModel<Long, UserProfile>(new UserProfile(WicketHibernateUtil.EMPTY_STRING)));
                    }
                };
            }
        });
        columns.add(new TextFilteredPropertyColumn<UserProfile, UserProfile, String>(new Model<>("Name"), "name", "name"));
        //new MyChoiceFilteredPropertyColumn(new Model("Role"), "role.name", "role.name", "role", "name", roles),
                /*
                new MyChoiceFilteredPropertyColumn(new Model("Birth city"), "birthCity.name", "birthCity.name", "birthCity", "name", cities),
                new PropertyColumn(new Model("Final game"), "finalGame", "finalGame")
                */
        CriteriaSorter sorter = new CriteriaSorter();
        HibernateProvider<Long, UserProfile> provider = new HibernateProvider<>(UserProfile.class, filter, sorter);
        DataTable<UserProfile, String> table = new DataTable<UserProfile, String>("table", columns, provider, 25) {
            protected Item<UserProfile> newRowItem(String id, int index, IModel<UserProfile> model) {
                return new OddEvenItem<>(id, index, model);
            }
        };
        table.addTopToolbar(new NavigationToolbar(table));
        table.addTopToolbar(new HeadersToolbar<>(table, sorter));
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

    static class UserProfileFilterState implements Serializable {

        private String name;

        String getName() {
            return name;
        }

        void setName(String name) {
            this.name = name;
        }
    }


    static class UserProfileFilter extends AbstractCriteriaFilter<UserProfileFilterState> {

        public UserProfileFilter() {
            super(new UserProfileFilterState());
        }

        public void build(Criteria criteria) {
            if (!Strings.isNullOrEmpty(filterState.getName())) {
                criteria.add(hilike("name", filterState.getName()));
            }
        }

    }

}