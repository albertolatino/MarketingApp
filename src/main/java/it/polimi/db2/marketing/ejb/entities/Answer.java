package it.polimi.db2.marketing.ejb.entities;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "answer", schema = "db_marketing")
@IdClass(Answer.Key.class)
public class Answer implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private Integer question_id;

    @Id
    private Integer user_id;

    private String answer;

    @ManyToOne
    @JoinColumn(name="question_id", insertable=false, updatable=false)
    private Question question;

    @ManyToOne
    @JoinColumn(name="user_id", insertable=false, updatable=false)
    private User user;

    public static class Key implements Serializable {

        protected Integer question_id;
        protected Integer user_id;

        public Integer getQuestionId() {
            return question_id;
        }

        public void setQuestionId(Integer question_id) {
            this.question_id = question_id;
        }

        public Integer getUserId() {
            return user_id;
        }

        public void setUserId(Integer user_id) {
            this.user_id = user_id;
        }

        public Key(Integer question_id, Integer user_id) {
            this.question_id = question_id;
            this.user_id = user_id;
        }

        public Key() {};

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || o.getClass() != getClass()) return false;

            Answer.Key qk = (Answer.Key) o;
            return qk.question_id.equals(question_id) && qk.user_id.equals(user_id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(question_id, user_id);
        }

    }

    public Integer getQuestionId() {
        return question_id;
    }

    public void setQuestionId(Integer question_id) {
        this.question_id = question_id;
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
