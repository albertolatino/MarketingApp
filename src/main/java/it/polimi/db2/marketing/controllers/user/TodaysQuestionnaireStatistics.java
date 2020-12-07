package it.polimi.db2.marketing.controllers.user;

import it.polimi.db2.marketing.controllers.ServletBase;
import it.polimi.db2.marketing.ejb.entities.*;
import it.polimi.db2.marketing.ejb.exceptions.FormException;
import it.polimi.db2.marketing.ejb.exceptions.QuestionnaireException;
import it.polimi.db2.marketing.ejb.exceptions.QuestionnaireNotFoundException;
import it.polimi.db2.marketing.ejb.services.QuestionnaireService;
import it.polimi.db2.marketing.ejb.services.UserQuestionnaireService;
import org.apache.commons.lang.StringEscapeUtils;
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
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/TodaysQuestionnaireStatistics")
public class TodaysQuestionnaireStatistics extends ServletBase {
	private static final long serialVersionUID = 1L;
	@EJB(name = "it.polimi.db2.marketing.services/QuestionnaireService")
	private QuestionnaireService qstService;
	@EJB(name = "it.polimi.db2.marketing.services/UserQuestionnaireService")
	private UserQuestionnaireService uqService;

	public TodaysQuestionnaireStatistics() {
		super();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		if (redirectIfNotLogged(request, response)) return;

		HttpSession session = request.getSession();

		User user = (User) session.getAttribute("user");

		Questionnaire qst;
		try {
			qst = qstService.getToday();
		} catch (QuestionnaireNotFoundException e) {
			String path = getServletContext().getContextPath() + "/Home";
			response.sendRedirect(path);
			return;
		} catch (QuestionnaireException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
			return;
		}

		if (uqService.hasSubmitted(user, qst)) {
			System.out.println("ALREADY SUBMITTED!");
			String path = getServletContext().getContextPath() + "/Home";
			response.sendRedirect(path);
			return;
		}

		if (!uqService.checkAlreadyExists(user, qst)) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad action sequence!");
			return;
		}

		// insert statistical questions
        Integer age = null;
        String sex = null;
        String expertise = null;
        String unparsedAge;
        unparsedAge = StringEscapeUtils.escapeJava(request.getParameter("age"));
        if (unparsedAge != null && !unparsedAge.isEmpty()) {
            try {
                age = Integer.parseInt(unparsedAge);
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad integer input for age");
                return;
            }
        }

        sex = StringEscapeUtils.escapeJava(request.getParameter("sex"));
        expertise = StringEscapeUtils.escapeJava(request.getParameter("expertise"));
        if (sex.isEmpty()) sex = null;
        if (expertise.isEmpty()) expertise = null;

        StatAnswers statAnswers = new StatAnswers(qst.getDate(), user.getId(), age, sex, expertise);

        uqService.addStatAnswers(statAnswers);

        uqService.submitQuestionnaire(user, qst);


		String path = "/WEB-INF/Home.html";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		ctx.setVariable("message", "Questionnaire responses successfully submitted!");
		getTemplateEngine().process(path, ctx, response.getWriter());
	}

	public void destroy() {
	}

}
