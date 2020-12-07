package it.polimi.db2.marketing.ejb.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

/**
 * The persistent class for the usertable database table.
 *
 */
@Entity
@Table(name = "usertable", schema = "db_marketing")
@Cacheable(false)
@NamedQueries({
		@NamedQuery(name = "User.checkCredentials", query = "SELECT r FROM User r  WHERE r.username = ?1 and r.password = ?2"),
		@NamedQuery(name = "User.getAll", query = "SELECT r FROM User r"),
		@NamedQuery(name = "User.checkUnique", query = "SELECT r FROM User r WHERE r.username = ?1 or r.email = ?2"),
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

	@OneToMany(mappedBy="user")
	private Collection<Answer> answers;

	@OneToMany(mappedBy="user")
	private Collection<StatAnswers> statAnswers;

	@OneToMany(mappedBy="user")
	private Collection<UserQuestionnaire> questionnaires;


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

	public Collection<Answer> getAnswers() {
		return answers;
	}

	public void setAnswers(Collection<Answer> answers) {
		this.answers = answers;
	}

	public Collection<StatAnswers> getStatAnswers() {
		return statAnswers;
	}

	public void setStatAnswers(Collection<StatAnswers> statAnswers) {
		this.statAnswers = statAnswers;
	}
}