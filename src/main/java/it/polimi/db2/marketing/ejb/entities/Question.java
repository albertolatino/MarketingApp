package it.polimi.db2.marketing.ejb.entities;

import java.io.Serializable;
import javax.persistence.*;
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
@IdClass(Question.QuestionKey.class)
public class Question implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer question_id;

    @Id
    @Temporal(TemporalType.DATE)
    private Date date;

    @ManyToOne
    @JoinColumn(name="date", insertable=false, updatable=false)
    private Questionnaire questionnaire;

    private String text;

    public static class QuestionKey implements Serializable {

        protected Integer question_id;
        protected Date date;
        public Integer getQuestionId() {
            return question_id;
        }

        public void setQuestionId(Integer question_id) {
            this.question_id = question_id;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public QuestionKey(Integer question_id, Date date) {
            this.question_id = question_id;
            this.date = date;
        }

        public QuestionKey() {};

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || o.getClass() != getClass()) return false;

            QuestionKey qk = (QuestionKey) o;
            return qk.question_id.equals(question_id) && qk.date.equals(date);
        }

        @Override
        public int hashCode() {
            return Objects.hash(question_id, date);
        }

    }


    public Question() {
    }

    public Questionnaire getQuestionnaire() {
        return questionnaire;
    }

    public void setQuestionnaire(Questionnaire questionnaire) {
        this.questionnaire = questionnaire;
    }


    public Integer getQuestionId() {
        return this.question_id;
    }

    public void setQuestionId(Integer question_id) {
        this.question_id = question_id;
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

}