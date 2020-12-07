package it.polimi.db2.marketing.controllers.user;

import it.polimi.db2.marketing.controllers.ServletBase;
import it.polimi.db2.marketing.ejb.entities.User;
import it.polimi.db2.marketing.ejb.services.UserService;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/Leaderboard")
public class Leaderboard extends ServletBase {
	private static final long serialVersionUID = 1L;
	@EJB(name = "it.polimi.db2.marketing.ejb.services/UserService")
	private UserService uService;

	public Leaderboard() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		if (redirectIfNotLogged(request, response)) return;
		
		List<User> allUsers = null;

		try {
			  allUsers = uService.getAllUsers();
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to get data");
			return;
		}

		String path = "/WEB-INF/Leaderboard.html";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		ctx.setVariable("allUsers", allUsers);

		getTemplateEngine().process(path, ctx, response.getWriter());
	}

	public void destroy() {
	}

}
