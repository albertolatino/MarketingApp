package it.polimi.db2.marketing.controllers.admin;

import it.polimi.db2.marketing.controllers.ServletBase;
import it.polimi.db2.marketing.ejb.entities.User;
import it.polimi.db2.marketing.ejb.services.QuestionnaireService;
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@WebServlet("/AdminDelete")
public class AdminDelete extends ServletBase {
    private static final long serialVersionUID = 1L;
    @EJB(name = "it.polimi.db2.marketing.services/QuestionnaireService")
    private QuestionnaireService questionnaireService;


    public AdminDelete() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (redirectIfNotLogged(request, response)) return;
        if (redirectIfNotAdmin(request, response)) return;

        renderPage(request, response, "/WEB-INF/AdminDelete.html");
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (redirectIfNotLogged(request, response)) return;
        if (redirectIfNotAdmin(request, response)) return;

        Date deletionDate = null;
        String message;
        boolean isBadRequest = false;


        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            deletionDate = (Date) sdf.parse(request.getParameter("date"));
            isBadRequest = !isBeforeToday(deletionDate);

        } catch (NumberFormatException | NullPointerException | ParseException e) {
            isBadRequest = true;
            e.printStackTrace();
        }
        if (isBadRequest) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect or missing param values");
            return;
        }


       // deletionDate = incrementDate(deletionDate,1);

        if(questionnaireService.questionnaireAlreadyExist(deletionDate)){

            message = "Questionnaire correctly deleted";
            questionnaireService.deleteQuestionnaire(deletionDate);

        }else{
            message = "Cannot retrieve questionnaire for this date";
        }

        Map<String, Object> variables = new HashMap<>();
        variables.put("message", message);
        renderPage(request, response, "/WEB-INF/AdminHome.html", variables);
    }

    private boolean isBeforeToday(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = dateFormat.format(date);
        String strToday = dateFormat.format(new Date());

        return strDate.compareTo(strToday) < 0;
    }

}