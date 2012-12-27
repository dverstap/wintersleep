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

import net.databinder.components.hib.DataForm;
import net.databinder.models.hib.HibernateListModel;
import net.databinder.models.hib.HibernateObjectModel;
import org.apache.wicket.Page;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.ListChoice;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;
import org.hibernate.SessionFactory;
import org.wintersleep.usermgmt.model.User;
import org.wintersleep.usermgmt.wicket.base.BasePage;
import org.wintersleep.usermgmt.wicket.base.Saver;


public class UserEditPage extends BasePage {
    private static final long serialVersionUID = 1;

    @SpringBean
    private SessionFactory sessionFactory;

    @SpringBean
    private Saver saver;

    public UserEditPage(PageParameters parameters) {
        this(new UserListPage(), new HibernateObjectModel<User>(User.class, Long.parseLong((String) parameters.get("id"))));
    }

    public UserEditPage(final Page backPage, HibernateObjectModel<User> model) {
        // TODO replace queries with calls to Repository
        HibernateListModel userProfiles = new HibernateListModel("from UserProfile order by name");

        final DataForm form = new DataForm<User>("editform", model) {
            protected void onSubmit() {
                User user = getModelObject();
                setPersistentObject(user);
                saver.save(user);
                //getSession().info(String.format("Saved user %s %s", user.getNameFirst(), user.getNameLast()));
                setResponsePage(backPage);
            }
        };
        add(form);

        // TODO allow model-based validation
        form.add(new RequiredTextField("login").add(StringValidator.maximumLength(8)));
        form.add(new PasswordTextField("password").add(StringValidator.maximumLength(16)));
        form.add(new RequiredTextField("fullName").add(StringValidator.maximumLength(32)));
        form.add(new ListChoice("userProfile", new PropertyModel(model.getObject(), "userProfile"), userProfiles, new ChoiceRenderer("name", "id")));

/*
        form.add(new Button("new") {
            public void onSubmit() {
                form.clearPersistentObject();
            }
        }.setDefaultFormProcessing(false));
*/

        form.add(new Button("cancel") {
            public void onSubmit() {
                getSession().info("Cancelled edit");
                sessionFactory.getCurrentSession().clear();
                setResponsePage(backPage);
            }
        }.setDefaultFormProcessing(false));

        form.add(new Label("userManagementService", new PropertyModel(model.getObject(), "msg")));
    }
}