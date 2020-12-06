package it.polimi.db2.marketing.controllers;

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
public class TodaysQuestionnaireStatistics extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	@EJB(name = "it.polimi.db2.marketing.services/QuestionnaireService")
	private QuestionnaireService qstService;
	@EJB(name = "it.polimi.db2.marketing.services/UserQuestionnaireService")
	private UserQuestionnaireService uqService;

	public TodaysQuestionnaireStatistics() {
		super();
	}

	public void init() throws ServletException {
		ServletContext servletContext = getServletContext();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(templateResolver);
		templateResolver.setSuffix(".html");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String loginpath = getServletContext().getContextPath() + "/index.html";
		HttpSession session = request.getSession();
		if (session.isNew() || session.getAttribute("user") == null) {
			response.sendRedirect(loginpath);
			return;
		}

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

		if (!uqService.checkAlreadyExists(user, qst) || !uqService.checkRespondedToMarketingQuestions(user, qst)) {
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
		templateEngine.process(path, ctx, response.getWriter());
	}

	public void destroy() {
	}

}
