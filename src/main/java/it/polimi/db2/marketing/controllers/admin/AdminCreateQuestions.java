package it.polimi.db2.marketing.controllers.admin;

import it.polimi.db2.marketing.controllers.ServletBase;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;


@WebServlet("/AdminCreateQuestions")
public class AdminCreateQuestions extends ServletBase {
    private static final long serialVersionUID = 1L;
    @EJB(name = "it.polimi.db2.marketing.services/QuestionnaireService")
    private QuestionnaireService questionnaireService;

    public AdminCreateQuestions() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (redirectIfNotLogged(request, response)) return;
        if (redirectIfNotAdmin(request, response)) return;


        // Redirect to the Admin create questions page
        String path = "/WEB-INF/AdminCreateQuestions.html";
        ServletContext servletContext = getServletContext();
        final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
        getTemplateEngine().process(path, ctx, response.getWriter());
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (redirectIfNotLogged(request, response)) return;
        if (redirectIfNotAdmin(request, response)) return;

        boolean isBadRequest = false;
        Date questionnaireDate = null;
        String title = null;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            questionnaireDate = (Date) sdf.parse(request.getParameter("date"));
            title = (String) StringEscapeUtils.escapeJava(request.getParameter("title"));
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
        //Increment by one
        questionnaireDate = incrementDate(questionnaireDate,1);

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
        getTemplateEngine().process(path, ctx, response.getWriter());
    }

    private boolean isBeforeToday(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = dateFormat.format(date);
        String strToday = dateFormat.format(new Date());

        return strDate.compareTo(strToday) < 0;
    }


    public void destroy() {
    }

    public Date incrementDate(Date date, int days){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, 5);
        calendar.add(Calendar.DATE, 1);
        return calendar.getTime();
    }



}
