package it.polimi.db2.marketing.controllers.admin;

import it.polimi.db2.marketing.controllers.ServletBase;
import it.polimi.db2.marketing.ejb.entities.User;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


@WebServlet("/AdminDelete")
public class AdminDelete extends ServletBase {
    private static final long serialVersionUID = 1L;


    public AdminDelete() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (redirectIfNotLogged(request, response)) return;



        // Redirect to the Admin create page
        String path = "/WEB-INF/AdminDelete.html";
        ServletContext servletContext = getServletContext();
        final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
        getTemplateEngine().process(path, ctx, response.getWriter());
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

        Integer questionsNumber = null;
        boolean isBadRequest = false;


        try {
            questionsNumber = Integer. parseInt(request.getParameter("number"));
            isBadRequest = questionsNumber <= 0;
        } catch (NumberFormatException | NullPointerException e) {
            isBadRequest = true;
            e.printStackTrace();
        }
        if (isBadRequest) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect or missing param values");
            return;
        }


        //System.out.println(questionnaireDate);
        System.out.println(questionsNumber);




        String path = "/WEB-INF/AdminCreateQuestions.html";
        ServletContext servletContext = getServletContext();
        final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
        ctx.setVariable("number", questionsNumber);
        getTemplateEngine().process(path, ctx, response.getWriter());
    }


    public void destroy() {
    }

}