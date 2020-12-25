package it.polimi.db2.marketing.controllers;

import it.polimi.db2.marketing.ejb.entities.User;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

public class ServletBase extends HttpServlet {
    private TemplateEngine templateEngine;

    public void init() {
        ServletContext servletContext = getServletContext();
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
        templateResolver.setTemplateMode(TemplateMode.HTML);
        this.templateEngine = new TemplateEngine();
        this.templateEngine.setTemplateResolver(templateResolver);
        templateResolver.setSuffix(".html");
    }

    public TemplateEngine getTemplateEngine() {
        return templateEngine;
    }

    public boolean redirectIfNotLogged(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String loginpath = getServletContext().getContextPath() + "/index.html";
        HttpSession session = request.getSession();
        if (session.isNew() || session.getAttribute("user") == null) {
            response.sendRedirect(loginpath);
            return true;
        }

        return false;
    }

    public boolean redirectIfNotAdmin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = getServletContext().getContextPath() + "/Home";
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (!user.isAdmin()) {
            response.sendRedirect(path);
            return true;
        }

        return false;
    }

    public void renderPage(HttpServletRequest request, HttpServletResponse response, String path) throws IOException {
        ServletContext servletContext = getServletContext();
        WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
        getTemplateEngine().process(path, ctx, response.getWriter());
    }

    public void renderPage(HttpServletRequest request, HttpServletResponse response, String path, Map<String, Object> variables) throws IOException {
        ServletContext servletContext = getServletContext();
        WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
        ctx.setVariables(variables);
        getTemplateEngine().process(path, ctx, response.getWriter());
    }
}
