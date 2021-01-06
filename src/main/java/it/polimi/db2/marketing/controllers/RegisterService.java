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
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet("/Register")
public class RegisterService extends ServletBase {
    private static final long serialVersionUID = 1L;
    @EJB(name = "it.polimi.db2.marketing.services/UserService")
    private UserService uService;

    public RegisterService() {
        super();
    }

    final Pattern mailPattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        renderPage(request, response, "/WEB-INF/Register.html");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        ServletContext servletContext = getServletContext();
        final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
        String errorPath = "/WEB-INF/Register.html";


        String usrn = StringEscapeUtils.escapeJava(request.getParameter("username"));
        String pwd = StringEscapeUtils.escapeJava(request.getParameter("pwd"));
        String pwd1 = StringEscapeUtils.escapeJava(request.getParameter("pwd1"));
        String mail = StringEscapeUtils.escapeJava(request.getParameter("mail"));
        String name = StringEscapeUtils.escapeJava(request.getParameter("name"));
        String surname = StringEscapeUtils.escapeJava(request.getParameter("surname"));

        if (usrn == null || pwd == null || pwd1 == null || mail == null || name == null || surname == null ||
                usrn.isEmpty() || pwd.isEmpty() || pwd1.isEmpty() || mail.isEmpty() || name.isEmpty() || surname.isEmpty()) {
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
            Map<String, Object> variables = new HashMap<>();
            variables.put("errorMsg", "Invalid email!");
            renderPage(request, response, "/WEB-INF/Register.html", variables);
            return;
        }

        try {
            if (!uService.checkUnique(usrn, mail)) {
                Map<String, Object> variables = new HashMap<>();
                variables.put("errorMsg", "User already exists!");
                renderPage(request, response, "/WEB-INF/Register.html", variables);
                return;
            }
        } catch (CredentialsException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Could not check credentials");
            return;
        }

        uService.addUser(usrn, name, surname, pwd, mail);

        String path = getServletContext().getContextPath() + "/Home";
        response.sendRedirect(path);
    }
}