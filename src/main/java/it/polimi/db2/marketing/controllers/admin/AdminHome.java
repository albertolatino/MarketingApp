package it.polimi.db2.marketing.controllers.admin;

import it.polimi.db2.marketing.controllers.ServletBase;
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
import java.util.HashMap;
import java.util.Map;

@WebServlet("/AdminHome")
public class AdminHome extends ServletBase {
	private static final long serialVersionUID = 1L;

	public AdminHome() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		if (redirectIfNotLogged(request, response)) return;
		if (redirectIfNotAdmin(request, response)) return;

		String message = StringEscapeUtils.escapeJava(request.getParameter("message"));

		Map<String, Object> variables = new HashMap<>();
		variables.put("message", message);
		renderPage(request, response, "/WEB-INF/AdminHome.html", variables);
	}
}
