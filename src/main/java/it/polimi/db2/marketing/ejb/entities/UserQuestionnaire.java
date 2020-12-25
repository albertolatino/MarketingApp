package it.polimi.db2.marketing.ejb.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;


@Entity
@Table(name = "user_questionnaire", schema = "db_marketing")
@Cacheable(false)
@NamedQueries({
        @NamedQuery(name = "UserQuestionnaire.getUsersWhoSubmitted", query = "SELECT u FROM User u, UserQuestionnaire uq WHERE u = uq.user AND uq.date = ?1 AND uq.has_submitted = true"),
        @NamedQuery(name = "UserQuestionnaire.getUsersWhoCanceled", query = "SELECT u FROM User u, UserQuestionnaire uq WHERE u = uq.user AND uq.date = ?1 AND uq.has_submitted = false")
})
@IdClass(UserQuestionnaire.Key.class)
public class UserQuestionnaire implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private Integer user_id;

    @Id
    @Temporal(TemporalType.DATE)
    private Date date;

    private boolean has_submitted;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    public UserQuestionnaire() {
    }

    public UserQuestionnaire(Integer user_id, Date date) {
        this.user_id = user_id;
        this.date = date;
        this.has_submitted = false;
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

    public boolean getHasSubmitted() {
        return has_submitted;
    }

    public void setHasSubmitted(boolean has_submitted) {
        this.has_submitted = has_submitted;
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

        ;

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
