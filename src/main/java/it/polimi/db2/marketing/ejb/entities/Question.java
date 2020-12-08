package it.polimi.db2.marketing.ejb.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import it.polimi.db2.marketing.ejb.entities.Questionnaire;

/**
 * The persistent class for the question database table.
 *
 */
@Entity
@Table(name = "question", schema = "db_marketing")
public class Question implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Temporal(TemporalType.DATE)
    private Date date;

    private String text;

    @ManyToOne
    @JoinColumn(name="date", insertable=false, updatable=false)
    private Questionnaire questionnaire;

    @OneToMany(mappedBy="question")
    private Collection<Answer> answers;

    public Question(Date date, String text ) {
        this.date = date;
        this.text = text;
    }

    public Question(){

    }

    public Questionnaire getQuestionnaire() {
        return questionnaire;
    }

    public void setQuestionnaire(Questionnaire questionnaire) {
        this.questionnaire = questionnaire;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Collection<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(Collection<Answer> answers) {
        this.answers = answers;
    }
}