package it.polimi.db2.marketing.ejb.entities;

import javax.persistence.*;
import java.io.Serializable;

/**
 * The persistent class for the question database table.
 */
@Entity
@Table(name = "offensive_word", schema = "db_marketing")
@NamedQueries({
        @NamedQuery(name = "OffensiveWord.containsWord",
                query = "SELECT COUNT(o) FROM OffensiveWord o WHERE ?1 LIKE CONCAT('%',o.word ,'%')")
})
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