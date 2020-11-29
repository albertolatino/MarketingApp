package it.polimi.db2.marketing.ejb.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * The persistent class for the usertable database table.
 */
@Entity
@Table(name = "questionnaire", schema = "db_marketing")
@NamedQueries({
        @NamedQuery(name = "Questionnaire.getAll", query = "SELECT q FROM Questionnaire q")
})

public class Questionnaire implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Temporal(TemporalType.DATE)
    private Date date;

    private String title;

    public Questionnaire() {
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


}