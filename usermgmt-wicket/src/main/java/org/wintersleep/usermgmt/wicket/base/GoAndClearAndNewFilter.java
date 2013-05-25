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

package org.wintersleep.usermgmt.wicket.base;

import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.GoAndClearFilter;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.model.IModel;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.Page;

public abstract class GoAndClearAndNewFilter extends GoAndClearFilter {

    private Button newButton;

    public GoAndClearAndNewFilter(String id, FilterForm<?> form) {
        super(id, form);
        newButton = new Button("new") {
            public void onSubmit() {
                getForm().setResponsePage(createNewPage());
            }
        };
        add(newButton);
        form.setDefaultButton(getGoButton());
    }

    public GoAndClearAndNewFilter(String id, FilterForm<?> form, IModel<String> goModel, IModel<String> clearModel, IModel<String> newModel) {
        super(id, form, goModel, clearModel);

        newButton = new Button("new", newModel) {
            public void onSubmit() {
                getForm().setResponsePage(createNewPage());
            }
        };
        add(newButton);
    }

    public abstract Page createNewPage();

    public Button getNewButton() {
        return newButton;
    }

}
