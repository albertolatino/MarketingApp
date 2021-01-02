package it.polimi.db2.marketing.ejb.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;


@Entity
@Table(name = "user_questionnaire", schema = "db_marketing")
@Cacheable(false)
@NamedQueries({
        @NamedQuery(name = "UserQuestionnaire.getReviewsByQst", query = "SELECT uq FROM UserQuestionnaire uq WHERE uq.date = ?1")
})
@IdClass(UserQuestionnaire.Key.class)
public class UserQuestionnaire implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private Integer user_id;

    @Id
    @Temporal(TemporalType.DATE)
    private Date date;

    private boolean is_submitted;

    private String review;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "date", insertable = false, updatable = false)
    private Questionnaire questionnaire;


    public UserQuestionnaire() {
    }

    public UserQuestionnaire(Integer user_id, Date date) {
        this.user_id = user_id;
        this.date = date;
        this.is_submitted = false;
    }

    public Integer getUserId() {
        return user_id;
    }

    public void setUserId(Integer user_id) {
        this.user_id = user_id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean getIsSubmitted() {
        return is_submitted;
    }

    public void setIsSubmitted(boolean is_submitted) {
        this.is_submitted = is_submitted;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public static class Key implements Serializable {

        protected Integer user_id;
        protected Date date;

        public Integer getUserId() {
            return user_id;
        }

        public void setUserId(Integer user_id) {
            this.user_id = user_id;
        }

        public Date getDate() {
            return this.date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public Key(Integer user_id, Date date) {
            this.user_id = user_id;
            this.date = date;
        }

        public Key() {
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || o.getClass() != getClass()) return false;

            UserQuestionnaire.Key qk = (UserQuestionnaire.Key) o;
            return qk.user_id.equals(user_id) && qk.date.equals(date);
        }

        @Override
        public int hashCode() {
            return Objects.hash(user_id, date);
        }

    }

}
