package it.polimi.db2.marketing.controllers;

import it.polimi.db2.marketing.ejb.entities.Answer;
import it.polimi.db2.marketing.ejb.entities.Question;
import it.polimi.db2.marketing.ejb.entities.Questionnaire;
import it.polimi.db2.marketing.ejb.entities.User;
import it.polimi.db2.marketing.ejb.exceptions.FormException;
import it.polimi.db2.marketing.ejb.exceptions.QuestionnaireException;
import it.polimi.db2.marketing.ejb.exceptions.QuestionnaireNotFoundException;
import it.polimi.db2.marketing.ejb.services.QuestionnaireService;
import it.polimi.db2.marketing.ejb.services.UserService;
import org.apache.commons.lang.StringEscapeUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.ejb.EJB;
import javax.persistence.NoResultException;
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

@WebServlet("/TodaysQuestionnaire")
public class TodaysQuestionnaire extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	@EJB(name = "it.polimi.db2.marketing.services/QuestionnaireService")
	private QuestionnaireService qstService;

	public TodaysQuestionnaire() {
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

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String loginpath = getServletContext().getContextPath() + "/index.html";
		HttpSession session = request.getSession();
		if (session.isNew() || session.getAttribute("user") == null) {
			response.sendRedirect(loginpath);
			return;
		}

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

		String path = "/WEB-INF/TodaysQuestionnaire.html";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		ctx.setVariable("questionnaireName", qst.getTitle());
		ctx.setVariable("questions", qst.getQuestions());
		templateEngine.process(path, ctx, response.getWriter());
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

		// contains all indices of all questions in the current questionnaire
		List<Integer> allQuestionnaireNumbers = qst.getQuestions().stream().map(Question::getId).collect(Collectors.toList());

		ArrayList<Answer> answers = new ArrayList<>();
		try {

			Enumeration<String> parameters = request.getParameterNames();
			String parameterName = null;

			while (parameters.hasMoreElements()) {
				parameterName = (String) parameters.nextElement();
				int questionNumber = Integer.parseInt(parameterName);
				String strAnswer = (StringEscapeUtils.escapeJava(request.getParameter(parameterName)));
				if (strAnswer == null || strAnswer.length() == 0) {
					throw new FormException();
				}

				answers.add(new Answer(
					Integer.parseInt(parameterName), user.getId(), strAnswer
				));

				// removes the id of the answered question from the list, indicating that the question has been answered
				allQuestionnaireNumbers.remove(Integer.valueOf(questionNumber));
			}
		} catch (NumberFormatException | NullPointerException | FormException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect or missing param values");
			return;
		}

		// checks that all questions have been answered
		if (allQuestionnaireNumbers.size() > 0) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Not all questions answered");
			return;
		}


		qstService.addAnswers(answers);

		String path = "/WEB-INF/TodaysQuestionnaireStatistics.html";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		ctx.setVariable("questionnaireName", qst.getTitle());
		templateEngine.process(path, ctx, response.getWriter());
	}

	public void destroy() {
	}

}
