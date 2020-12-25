package it.polimi.db2.marketing.controllers;

import it.polimi.db2.marketing.ejb.exceptions.CredentialsException;
import it.polimi.db2.marketing.ejb.services.UserService;
import org.apache.commons.lang.StringEscapeUtils;
import org.thymeleaf.context.WebContext;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet("/Register")
public class RegisterService extends ServletBase {
    private static final long serialVersionUID = 1L;
    @EJB(name = "it.polimi.db2.marketing.services/UserService")
    private UserService usrService;

    public RegisterService() {
        super();
    }

    final Pattern mailPattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String path = "/WEB-INF/Register.html";
        ServletContext servletContext = getServletContext();
        final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
        getTemplateEngine().process(path, ctx, response.getWriter());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        ServletContext servletContext = getServletContext();
        final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
        String errorPath = "/WEB-INF/Register.html";
        String okPath = "/WEB-INF/Home.html";


        // obtain and escape params
        String usrn = null;
        String pwd = null;
        String pwd1 = null;
        String mail = null;
        String name = null;
        String surname = null;
        try {
            usrn = StringEscapeUtils.escapeJava(request.getParameter("username"));
            pwd = StringEscapeUtils.escapeJava(request.getParameter("pwd"));
            pwd1 = StringEscapeUtils.escapeJava(request.getParameter("pwd1"));
            mail = StringEscapeUtils.escapeJava(request.getParameter("mail"));
            name = StringEscapeUtils.escapeJava(request.getParameter("name"));
            surname = StringEscapeUtils.escapeJava(request.getParameter("surname"));

            if (usrn == null || pwd == null || pwd1 == null || mail == null || name == null || surname == null ||
                    usrn.isEmpty() || pwd.isEmpty() || pwd1.isEmpty() || mail.isEmpty() || name.isEmpty() || surname.isEmpty()) {
                throw new Exception("Missing or empty credential value");
            }

        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing credential value");
            return;
        }

        if (!pwd.equals(pwd1)) {
            ctx.setVariable("errorMsg", "The two passwords are different!");
            getTemplateEngine().process(errorPath, ctx, response.getWriter());
            return;
        }

        Matcher mailMatch = mailPattern.matcher(mail);

        if (!mailMatch.matches()) {
            ctx.setVariable("errorMsg", "Invalid email!");
            getTemplateEngine().process(errorPath, ctx, response.getWriter());
            return;
        }

        try {
            if (!usrService.checkUnique(usrn, mail)) {
                ctx.setVariable("errorMsg", "User already exists!");
                getTemplateEngine().process(errorPath, ctx, response.getWriter());
                return;
            }
        } catch (CredentialsException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Could not check credentials");
            return;
        }

        usrService.addUser(usrn, name, surname, pwd, mail);

        String path = getServletContext().getContextPath() + "/Home";
        response.sendRedirect(path);
    }
}