package it.polimi.db2.marketing.ejb.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "answer", schema = "db_marketing")
@IdClass(StatAnswers.Key.class)
public class StatAnswers implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private Date date;

    @Id
    private Integer user_id;

    private String answer;

    @ManyToOne
    @JoinColumn(name="date", insertable=false, updatable=false)
    private Questionnaire questionnaire;

    @ManyToOne
    @JoinColumn(name="user_id", insertable=false, updatable=false)
    private User user;

    public static class Key implements Serializable {

        protected Date date;
        protected Integer user_id;

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public Integer getUserId() {
            return user_id;
        }

        public void setUserId(Integer user_id) {
            this.user_id = user_id;
        }

        public Key(Date date, Integer user_id) {
            this.date = date;
            this.user_id = user_id;
        }

        public Key() {};

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || o.getClass() != getClass()) return false;

            StatAnswers.Key qk = (StatAnswers.Key) o;
            return qk.date.equals(date) && qk.user_id.equals(user_id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(date, user_id);
        }

    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getUserId() {
        return user_id;
    }

    public void setUserId(Integer user_id) {
        this.user_id = user_id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}

