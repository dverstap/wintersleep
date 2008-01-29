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
package org.wintersleep.usermgmt.servlet;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.beans.BeansException;
import org.wintersleep.usermgmt.model.RoleRepository;
import org.wintersleep.usermgmt.model.UserManagementService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class MyServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter w = response.getWriter();
        try {
            w.println("hello davy");

            WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
            if (context == null) {
                w.println("no context found");
            } else {
                w.println(context);

                w.println("names: " + context.getBeanDefinitionNames().length);
                for (String name : context.getBeanDefinitionNames()) {
                    w.println(name + ": " + context.getBean(name).getClass());
                }

                RoleRepository pr = (RoleRepository) context.getBean("roleRepository");
                w.println(pr.getClass());

                UserManagementService ums = (UserManagementService) context.getBean("userManagementService");
                w.println(ums.getClass());
                w.println(ums.findRole(1L));
            }
        } catch (Exception e) {
            e.printStackTrace(w);
        }
    }
}
