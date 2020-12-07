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

@WebServlet("/AdminHome")
public class AdminHome extends ServletBase {
	private static final long serialVersionUID = 1L;

	public AdminHome() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String userhomepath = getServletContext().getContextPath() + "/Home";

		if (redirectIfNotLogged(request, response)) return;

		HttpSession session = request.getSession();

		User user = (User) session.getAttribute("user");
		
		//is user but not admin 
		if(!user.isAdmin()) {
			response.sendRedirect(userhomepath);
			return;
		}

		// Redirect to the Home page
		String path = "/WEB-INF/AdminHome.html";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());

		getTemplateEngine().process(path, ctx, response.getWriter());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public void destroy() {
	}

}
