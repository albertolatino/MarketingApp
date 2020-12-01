package it.polimi.db2.marketing.controllers;

import it.polimi.db2.marketing.ejb.entities.User;
import org.apache.commons.lang.StringEscapeUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;


@WebServlet("/AdminCreateQuestions")
public class AdminCreateQuestions extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private TemplateEngine templateEngine;

    public AdminCreateQuestions() {
        super();
    }

    public void init() throws ServletException {
        ServletContext servletContext = getServletContext();
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
        templateResolver.setTemplateMode(TemplateMode.HTML);
        this.templateEngine = new TemplateEngine();
        this.templateEngine.setTemplateResolver(templateResolver);
        templateResolver.setSuffix(".html");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // If the user is not logged in (not present in session) redirect to the login
        String loginpath = getServletContext().getContextPath() + "/index.html";
        HttpSession session = request.getSession();
        if (session.isNew() || session.getAttribute("user") == null) {
            response.sendRedirect(loginpath);
            return;
        }


        // Redirect to the Admin create questions page
        String path = "/WEB-INF/AdminCreateQuestions.html";
        ServletContext servletContext = getServletContext();
        final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
        templateEngine.process(path, ctx, response.getWriter());
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // If the user is not logged in (not present in session) redirect to the login
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (session.isNew() || user == null) {
            String loginpath = getServletContext().getContextPath() + "/index.html";
            response.sendRedirect(loginpath);
            return;
        }

        if (!user.isAdmin()) {
            String path = getServletContext().getContextPath() + "/Home";
            response.sendRedirect(path);
            return;
        }

        ArrayList<String> questions = new ArrayList<String>();

        try {

            Enumeration<String> parameters = request.getParameterNames();
            String parameterName = null;

            while (parameters.hasMoreElements()) {

                parameterName = (String) parameters.nextElement();
                questions.add(StringEscapeUtils.escapeJava(request.getParameter(parameterName)));
            }

        } catch (NumberFormatException | NullPointerException e) {
            e.printStackTrace();
        }






            //TODO check that there are no questionnaires planned for questionnaireDate in db


            // Redirect to the Admin create page
            String path = "/WEB-INF/AdminHome.html";//TODO add a successful message as a parameter to be shown in home
            ServletContext servletContext = getServletContext();
            final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
            templateEngine.process(path, ctx, response.getWriter());

    }

    public void destroy() {
    }

}
