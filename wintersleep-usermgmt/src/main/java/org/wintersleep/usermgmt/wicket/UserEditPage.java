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

import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.Page;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.extensions.markup.html.form.palette.Palette;
import org.apache.wicket.validation.validator.StringValidator;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.wintersleep.usermgmt.model.User;
import org.wintersleep.usermgmt.wicket.base.Saver;
import org.wintersleep.usermgmt.wicket.base.BasePage;
import org.hibernate.SessionFactory;
import net.databinder.models.HibernateObjectModel;
import net.databinder.models.HibernateListModel;
import net.databinder.components.hibernate.DataForm;


public class UserEditPage extends BasePage {
    private static final long serialVersionUID = 1;

    @SpringBean
    private SessionFactory sessionFactory;

    @SpringBean
    private Saver saver;

    public UserEditPage(final Page backPage, HibernateObjectModel model) {
        HibernateListModel roles = new HibernateListModel("from Role order by name");

        final DataForm form = new DataForm("editform", model) {
            protected void onSubmit() {
                User user = (User) getModelObject();
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
        form.add(new Palette("roles", new PropertyModel(model, "roles"), roles, new ChoiceRenderer("name", "id"), 10, false));

        form.add(new Button("new") {
            public void onSubmit() {
                form.clearPersistentObject();
            }
        }.setDefaultFormProcessing(false));

        form.add(new Button("cancel") {
            public void onSubmit() {
                getSession().info("Cancelled edit");
                sessionFactory.getCurrentSession().clear();
                setResponsePage(backPage);
            }
        }.setDefaultFormProcessing(false));
    }
}