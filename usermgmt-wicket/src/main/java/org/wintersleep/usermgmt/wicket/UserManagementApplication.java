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

import org.apache.log4j.Logger;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.target.coding.BookmarkablePageRequestTargetUrlCodingStrategy;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.wintersleep.usermgmt.model.User;
import org.wintersleep.usermgmt.model.UserProfile;

public class UserManagementApplication extends WebApplication {

    private static final Logger m_log = Logger.getLogger(UserManagementApplication.class);

    @Override
    public Class<UserListPage> getHomePage() {
        return UserListPage.class;
    }

    @Override
    protected void init() {
        super.init();
        addComponentInstantiationListener(new SpringComponentInjector(this));

        //mount(new HybridUrlCodingStrategy("/user", UserEditPage.class));
        mount(new BookmarkablePageRequestTargetUrlCodingStrategy(User.class.getSimpleName().toLowerCase(),
                UserEditPage.class, null));
        mount(new BookmarkablePageRequestTargetUrlCodingStrategy(UserProfile.class.getSimpleName().toLowerCase(),
                UserProfileEditPage.class, null));

        // mount(new IndexedParamUrlCodingStrategy("/user", UserEditPage.class));
        //mount(new IndexedParamUrlCodingStrategy("/userprofile", UserProfileEditPage.class));

        //mountBookmarkablePage(User.class.getSimpleName(), UserEditPage.class);
        //mountBookmarkablePage(UserProfile.class.getSimpleName(), UserProfileEditPage.class);
    }

}