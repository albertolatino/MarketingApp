package it.polimi.db2.marketing.controllers.user;

import it.polimi.db2.marketing.controllers.ServletBase;
import it.polimi.db2.marketing.ejb.entities.User;
import it.polimi.db2.marketing.ejb.services.UserQuestionnaireService;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@WebServlet("/QuestionnaireDetails")
public class QuestionnaireDetails extends ServletBase {
    private static final long serialVersionUID = 1L;
    @EJB(name = "it.polimi.db2.marketing.ejb.services/UserQuestionnaireService")
    private UserQuestionnaireService uqService;

    public QuestionnaireDetails() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        if (redirectIfNotLogged(request, response)) return;

        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            date = sdf.parse(request.getParameter("date"));

            //TODO see style
            //cannot see history for future questionnaire
            if (getToday().before(date)) {
                throw new Exception("Missing or empty credential value");
            }

        } catch (ParseException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Date inserted has wrong format");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing credential value");
            return;
        }

        /* To show in questionnaire-details.html:
         * List of users who submitted the questionnaire,
         * List of users who canceled the questionnaire.
         *
         * Questionnaire answers of each user. [New page linked to users who submitted]
         */

        //get users who submitted and canceled the questionnaire of date
        List<User> usersSubmitted, usersCanceled;

        try {
            usersSubmitted = uqService.getUsersWhoSubmitted(date);
            usersCanceled = uqService.getUsersWhoCanceled(date);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to get questionnaires data");
            return;
        }
        System.out.println(usersSubmitted.get(0).getUsername());
        //System.out.println(usersCanceled.get(0).getUsername());


        String path = "/WEB-INF/questionnaire-details.html";
        ServletContext servletContext = getServletContext();
        final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
        ctx.setVariable("usersSubmitted", usersSubmitted);
        getTemplateEngine().process(path, ctx, response.getWriter());

    }

    private Date getToday() {
        return new Date(System.currentTimeMillis());
    }


    public void destroy() {
    }

}