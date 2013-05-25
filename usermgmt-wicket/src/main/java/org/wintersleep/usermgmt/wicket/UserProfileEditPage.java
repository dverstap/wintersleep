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

import org.apache.wicket.Page;
import org.apache.wicket.extensions.markup.html.form.palette.Palette;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;
import org.hibernate.SessionFactory;
import org.wintersleep.usermgmt.model.Role;
import org.wintersleep.usermgmt.model.UserProfile;
import org.wintersleep.usermgmt.wicket.base.BasePage;
import org.wintersleep.wicket.hibernate.HibernateListModel;
import org.wintersleep.wicket.hibernate.HibernateObjectModel;
import org.wintersleep.wicket.hibernate.Saver;

import java.util.List;


public class UserProfileEditPage extends BasePage {
    private static final long serialVersionUID = 1;

    @SpringBean
    private SessionFactory sessionFactory;

    @SpringBean
    private Saver saver;

    public UserProfileEditPage(PageParameters parameters) {
        this(new UserProfileListPage(), new HibernateObjectModel<>(UserProfile.class, parameters.get("id").toLong()));
    }

    public UserProfileEditPage(final Page backPage, IModel<UserProfile> model) {
        HibernateListModel<Long, Role> allRolesModel = new HibernateListModel<>("from Role order by name");

        final Form<UserProfile> form = new Form<UserProfile>("form", model) {
            protected void onSubmit() {
                UserProfile userProfile = getModelObject();
                saver.save(userProfile);
                //getSession().info(String.format("Saved userProfile %s %s", userProfile.getNameFirst(), userProfile.getNameLast()));
                setResponsePage(backPage);
            }
        };
        add(form);

        // TODO allow model-based validation
        form.add(new RequiredTextField<>("name", new PropertyModel<String>(model, "name")).add(StringValidator.maximumLength(8)));
        form.add(new Palette<>("roles", new PropertyModel<List<Role>>(model, "roles"), allRolesModel, new ChoiceRenderer<Role>("name", "id"), 10, false));

//        form.add(new Button("new") {
//            public void onSubmit() {
//                form.clearPersistentObject();
//            }
//        }.setDefaultFormProcessing(false));

        form.add(new Button("cancel") {
            public void onSubmit() {
                getSession().info("Cancelled edit");
                sessionFactory.getCurrentSession().clear();
                setResponsePage(backPage);
            }
        }.setDefaultFormProcessing(false));
    }
}