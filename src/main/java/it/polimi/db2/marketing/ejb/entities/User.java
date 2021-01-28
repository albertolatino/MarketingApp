package it.polimi.db2.marketing.ejb.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * The persistent class for the usertable database table.
 */
@Entity
@Table(name = "user", schema = "db_marketing")
@NamedQueries({
        @NamedQuery(name = "User.getUsersWhoSubmitted", query = "SELECT u FROM User u JOIN u.isSubmitted s WHERE KEY(s) = ?1 AND s = true"),
        @NamedQuery(name = "User.getUsersWhoCanceled", query = "SELECT u FROM User u JOIN u.isSubmitted s WHERE KEY(s) = ?1 AND s = false"),
        @NamedQuery(name = "User.checkCredentials", query = "SELECT r FROM User r  WHERE r.username = ?1 and r.password = ?2"),
        @NamedQuery(name = "User.getNonAdminUsers", query = "SELECT r FROM User r WHERE r.is_admin = FALSE ORDER BY r.score DESC"),
        @NamedQuery(name = "User.checkUnique", query = "SELECT r FROM User r WHERE r.username = ?1 or r.email = ?2")
})
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String password;

    private String surname;

    private String username;

    private boolean is_admin;

    private boolean is_blocked;

    private String email;

    private Integer score;

    @ElementCollection
    @CollectionTable(name = "user_questionnaire_submitted", schema = "db_marketing", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    @MapKeyColumn(name = "date")
    @Column(name = "is_submitted")
    private Map<Date, Boolean> isSubmitted;

    @ElementCollection(fetch=FetchType.LAZY)
    @CollectionTable(name = "log", schema = "db_marketing", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    @Column(name = "datetime")
    private Set<Date> logs;

    @ElementCollection
    @CollectionTable(name = "answer", schema = "db_marketing", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    @MapKeyColumn(name = "question_id")
    @Column(name = "answer")
    private Map<Integer, String> answers;

    public User() {
    }

    public User(String username, String name, String surname, String password, String email) {
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.email = email;
        this.score = 0;
        this.is_admin = false;
        this.is_blocked = false;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSurname() {
        return this.surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isAdmin() {
        return is_admin;
    }

    public void setIsAdmin(boolean is_admin) {
        this.is_admin = is_admin;
    }

    public boolean isBlocked() {
        return is_blocked;
    }

    public void setIsBlocked(boolean is_blocked) {
        this.is_blocked = is_blocked;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getScore() {
        return this.score;
    }

    public Map<Date, Boolean> getIsSubmitted() {
        return isSubmitted;
    }

    public void setIsSubmitted(Map<Date, Boolean> isSubmitted) {
        this.isSubmitted = isSubmitted;
    }

    public Set<Date> getLogs() {
        return logs;
    }

    public void setLogs(Set<Date> logs) {
        this.logs = logs;
    }

    public void logAccess() {
        this.logs.add(new Date());
    }

    public Map<Integer, String> getAnswers() {
        return answers;
    }

    public void setAnswers(Map<Integer, String> answers) {
        this.answers = answers;
    }
}