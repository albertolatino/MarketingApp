package it.polimi.db2.marketing.controllers.admin;

import it.polimi.db2.marketing.controllers.ServletBase;
import org.apache.commons.lang.StringEscapeUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/AdminHome")
public class AdminHome extends ServletBase {
    private static final long serialVersionUID = 1L;

    public AdminHome() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        if (redirectIfNotLogged(request, response)) return;
        if (redirectIfNotAdmin(request, response)) return;

        String message = StringEscapeUtils.escapeJava(request.getParameter("message"));
        String error = StringEscapeUtils.escapeJava(request.getParameter("error"));

        Map<String, Object> variables = new HashMap<>();
        variables.put("message", message);
        variables.put("error", error);
        renderPage(request, response, "/WEB-INF/AdminHome.html", variables);
    }
}
