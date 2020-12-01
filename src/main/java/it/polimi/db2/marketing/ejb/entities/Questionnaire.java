package it.polimi.db2.marketing.ejb.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "questionnaire", schema = "db_marketing")
@NamedQueries({
        @NamedQuery(name = "Questionnaire.getAll", query = "SELECT q FROM Questionnaire q"),
        @NamedQuery(name = "Questionnaire.getByDate", query = "SELECT q FROM Questionnaire q WHERE q.date = ?1"),
        @NamedQuery(name = "Questionnaire.getToday", query = "SELECT q FROM Questionnaire q WHERE q.date = CURRENT_DATE")
})

public class Questionnaire implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Temporal(TemporalType.DATE)
    private Date date;

    private String title;

    @OneToMany(mappedBy="questionnaire")
    private Collection<Question> questions;

    @OneToMany(mappedBy="questionnaire")
    private Collection<StatAnswers> statAnswers;

    public Questionnaire() {
    }

    public Questionnaire(Collection<Question> questions, Date date, String title){
        this.questions = questions;
        this.date = date;
        this.title = title;
    }

    public  Questionnaire(Date date, String title){
        this.date = date;
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Collection<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public Collection<StatAnswers> getStatAnswers() {
        return statAnswers;
    }

    public void setStatAnswers(Collection<StatAnswers> statAnswers) {
        this.statAnswers = statAnswers;
    }
}