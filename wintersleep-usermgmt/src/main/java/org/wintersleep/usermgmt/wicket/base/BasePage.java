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

import net.databinder.components.DataStyleLink;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.PageLink;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.wintersleep.usermgmt.wicket.UserListPage;
import org.wintersleep.usermgmt.wicket.UserProfileListPage;
import org.wintersleep.usermgmt.wicket.UserReportPage;

public class BasePage extends WebPage {
    public BasePage() {
        add(new DataStyleLink("css"));
        add(new FeedbackPanel("status"));
        add(new PageLink("userListPageLink", UserListPage.class));
        add(new PageLink("userProfileListPageLink", UserProfileListPage.class));
        add(new PageLink("userReportPageLink", UserReportPage.class));
    }

}