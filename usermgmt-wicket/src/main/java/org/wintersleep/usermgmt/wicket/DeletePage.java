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
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wintersleep.usermgmt.wicket.base.BasePage;
import org.wintersleep.wicket.hibernate.Saver;

public class DeletePage<T> extends BasePage {
    private static final long serialVersionUID = 1;

    @SpringBean
    private Saver saver;

    public DeletePage(final Page backPage, IModel<T> model, String objectDescription) {
        super();

        add(new Label("name", objectDescription));

        Form form = new Form<T>("confirmForm", model) {
            protected void onSubmit() {
                T entity = getModelObject();
                saver.delete(entity);
                // TODO audit log
                // getSession().info(String.format("Deleted %s %s,",player.getNameFirst(),player.getNameLast()));
                setResponsePage(backPage);
            }
        };

        form.add(new Button("cancel") {
            public void onSubmit() {
                setResponsePage(backPage);
            }
        }.setDefaultFormProcessing(false));

        add(form);
    }
}