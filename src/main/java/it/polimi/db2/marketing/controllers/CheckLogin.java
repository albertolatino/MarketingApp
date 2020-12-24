package it.polimi.db2.marketing.controllers;

import it.polimi.db2.marketing.ejb.entities.User;
import it.polimi.db2.marketing.ejb.exceptions.CredentialsException;
import it.polimi.db2.marketing.ejb.services.QuestionnaireService;
import it.polimi.db2.marketing.ejb.services.UserService;
import org.apache.commons.lang.StringEscapeUtils;

import javax.ejb.EJB;
import javax.persistence.NonUniqueResultException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/CheckLogin")
public class CheckLogin extends ServletBase {
    private static final long serialVersionUID = 1L;
    @EJB(name = "it.polimi.db2.marketing.services/UserService")
    private UserService usrService;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // obtain and escape params
        String usrn = null;
        String pwd = null;
        try {
            usrn = StringEscapeUtils.escapeJava(request.getParameter("username"));
            pwd = StringEscapeUtils.escapeJava(request.getParameter("pwd"));
            if (usrn == null || pwd == null || usrn.isEmpty() || pwd.isEmpty()) {
                throw new Exception("Missing or empty credential value");
            }

        } catch (Exception e) {
            // for debugging only e.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing credential value");
            return;
        }
        User user;
        try {
            // query db to authenticate for user
            user = usrService.checkCredentials(usrn, pwd);
        } catch (CredentialsException | NonUniqueResultException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Could not check credentials");
            return;
        }



        // If the user exists, add info to the session and go to home page, otherwise
        // show login page with error message

        String path;
        if (user == null) {
            Map<String, Object> variables = new HashMap<>();
            variables.put("errorMsg", "Incorrect username or password");
            renderPage(request, response, "/index.html", variables);
        } else {

            usrService.registerAccess(user);

            request.getSession().setAttribute("user", user);
            if (user.isAdmin()) {
                path = getServletContext().getContextPath() + "/AdminHome";
            } else {
                path = getServletContext().getContextPath() + "/Home";
            }
            response.sendRedirect(path);
        }

    }
}