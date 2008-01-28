package org.wintersleep.springtryout;

import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;

public class MyServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter w = response.getWriter();
        w.println("hello davy");

        WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
        if (context == null) {
            w.println("no context found");
        } else {
            w.println(context);

            PersonRepository pr = (PersonRepository) context.getBean("personRepositoryImpl");
            w.println(pr.getClass());

            PersonService ps = (PersonService) context.getBean("personService");
            w.println(ps.getClass());
            w.println(ps.find(1L));
        }

    }
}
