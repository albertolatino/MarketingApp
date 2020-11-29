package it.polimi.db2.marketing.controllers;
/*
import it.polimi.db2.marketing.ejb.entities.Question;
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


@WebServlet("/QuestionDetails")
public class QuestionDetails extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private TemplateEngine templateEngine;
    @EJB(name = "it.polimi.db2.marketing.ejb.services/QuestionService")
    private QuestionService qService;

    public QuestionDetails() {
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

        // If the user is not logged in (not present in session) redirect to the login
        String loginPath = getServletContext().getContextPath() + "/index.html";
        HttpSession session = request.getSession();
        if (session.isNew() || session.getAttribute("user") == null) {
            response.sendRedirect(loginPath);
            return;
        }

        //retrieve all question for a given date
        Question question = null;

        try {
            question = qService.getQuestion(date);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to get questionnaires data");
            return;
        }
*/
        /* To show in question.html:
         * List of users who submitted the questionnaire.
         * List of users who cancelled the questionnaire.
         * Questionnaire answers of each user.
         */
/*
        String path = "/WEB-INF/question.html";
        ServletContext servletContext = getServletContext();
        final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
        ctx.setVariable("question", question);
        templateEngine.process(path, ctx, response.getWriter());

    }


    public void destroy() {
    }

}*/