package it.polimi.db2.marketing.ejb.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "stat_answers", schema = "db_marketing")
@IdClass(StatAnswers.Key.class)
public class StatAnswers implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private Date date;

    @Id
    private Integer user_id;

    private Integer age;

    private String sex;

    private String expertise;

    public StatAnswers() {
    }

    public StatAnswers(Date date, Integer user_id, Integer age, String sex, String expertise) {
        this.date = date;
        this.user_id = user_id;
        this.age = age;
        this.sex = sex;
        this.expertise = expertise;
    }

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

        public Key() {
        }

        ;

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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getExpertise() {
        return expertise;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }
}

