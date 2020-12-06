package it.polimi.db2.marketing.controllers;

import it.polimi.db2.marketing.ejb.entities.User;
import it.polimi.db2.marketing.ejb.services.QuestionnaireService;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;


@WebServlet("/AdminCreateQuestions")
public class AdminCreateQuestions extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private TemplateEngine templateEngine;
    @EJB(name = "it.polimi.db2.marketing.services/QuestionnaireService")
    private QuestionnaireService questionnaireService;

    public AdminCreateQuestions() {
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
        String loginpath = getServletContext().getContextPath() + "/index.html";
        HttpSession session = request.getSession();
        if (session.isNew() || session.getAttribute("user") == null) {
            response.sendRedirect(loginpath);
            return;
        }


        // Redirect to the Admin create questions page
        String path = "/WEB-INF/AdminCreateQuestions.html";
        ServletContext servletContext = getServletContext();
        final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
        templateEngine.process(path, ctx, response.getWriter());
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // If the user is not logged in (not present in session) redirect to the login
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (session.isNew() || user == null) {
            String loginpath = getServletContext().getContextPath() + "/index.html";
            response.sendRedirect(loginpath);
            return;
        }

        if (!user.isAdmin()) {
            String path = getServletContext().getContextPath() + "/Home";
            response.sendRedirect(path);
            return;
        }

        boolean isBadRequest = false;
        Date questionnaireDate = null;
        String title = null;

        try {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            questionnaireDate = (Date) sdf.parse(request.getParameter("date"));
            title = (String) StringEscapeUtils.escapeJava(request.getParameter("title"));
            questionnaireDate = normalizeDate(questionnaireDate);
            isBadRequest = isBeforeToday(questionnaireDate);
        } catch (NumberFormatException | NullPointerException | ParseException e) {
            isBadRequest = true;
            e.printStackTrace();
        }
        if (isBadRequest) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect or missing param values");
            return;
        }

        String message;

        //check there aren't other questionnaires in this date
        //TODO EVENTUALMENTE SOSTITUIRE INVECE DI NAMED QUERY CON EM.FIND
        if (questionnaireService.questionnaireAlreadyExist(questionnaireDate)) {
            message = "A questionnaire for this date already exists";
        } else {

            message = "Questionnaire correctly created";


            // Take questions from form
            ArrayList<String> questions = new ArrayList<>();

            try {

                Enumeration<String> parameters = request.getParameterNames();
                String parameterName = null;

                while (parameters.hasMoreElements()) {


                    parameterName = (String) parameters.nextElement();
                    if (!parameterName.equals("title") && !parameterName.equals("date"))
                        questions.add(StringEscapeUtils.escapeJava(request.getParameter(parameterName)));
                }

            } catch (NumberFormatException | NullPointerException e) {
                e.printStackTrace();
            }


            questionnaireService.createQuestionnaire(questions, questionnaireDate, title);

        }


        //TODO check that there are no questionnaires planned for questionnaireDate in db


        // Redirect to the Admin create page
        String path = "/WEB-INF/AdminHome.html";//TODO add a successful message as a parameter to be shown in home
        ServletContext servletContext = getServletContext();
        final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
        ctx.setVariable("message", message);
        templateEngine.process(path, ctx, response.getWriter());

    }

    private boolean isBeforeToday(Date date) {
        return date.before(normalizeDate(new Date()));
    }

    private Date normalizeDate(Date date) {
        long time = new Date().getTime();

        return new Date(time - time % (24 * 60 * 60 * 1000));
    }

    public void destroy() {
    }

}
