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
public class OffensiveWord implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String word;

    private String language;

    public OffensiveWord() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}