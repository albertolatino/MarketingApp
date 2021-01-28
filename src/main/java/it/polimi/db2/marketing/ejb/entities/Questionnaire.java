package it.polimi.db2.marketing.ejb.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "questionnaire", schema = "db_marketing")
@NamedQueries({
        @NamedQuery(name = "Questionnaire.getAll", query = "SELECT q FROM Questionnaire q"),

})

public class Questionnaire implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Temporal(TemporalType.DATE)
    private Date date;

    private String title;

    @Basic(fetch = FetchType.LAZY)
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] image;

    @OneToMany(mappedBy = "questionnaire")
    private List<Question> questions;

    @ElementCollection
    @CollectionTable(name = "review", schema = "db_marketing", joinColumns = @JoinColumn(name = "date"))
    @Column(name = "review")
    private Set<String> reviews;

    public Questionnaire() {
    }

    public Questionnaire(List<Question> questions, Date date, String title, byte[] image) {
        this.questions = questions;
        this.date = date;
        this.title = title;
        this.image = image;
    }

    public Questionnaire(Date date, String title, byte[] image) {
        this.date = date;
        this.title = title;
        this.image = image;
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

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Set<String> getReviews() {
        return reviews;
    }

    public void setReviews(Set<String> reviews) {
        this.reviews = reviews;
    }
}