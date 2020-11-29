package it.polimi.db2.marketing.ejb.services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.NonUniqueResultException;
import it.polimi.db2.marketing.ejb.entities.User;
import it.polimi.db2.marketing.ejb.exceptions.*;
import java.util.List;

@Stateless
public class UserService {
	@PersistenceContext(unitName = "MarketingEJB")
	private EntityManager em;

	public UserService() {
	}

	public User checkCredentials(String usrn, String pwd) throws CredentialsException, NonUniqueResultException {
		List<User> uList = null;
		try {
			uList = em.createNamedQuery("User.checkCredentials", User.class).setParameter(1, usrn).setParameter(2, pwd)
					.getResultList();
		} catch (PersistenceException e) {
			throw new CredentialsException("Could not verify credentals");
		}
		if (uList.isEmpty())
			return null;
		else if (uList.size() == 1)
			return uList.get(0);
		throw new NonUniqueResultException("More than one user registered with same credentials");
	}

	public boolean checkUnique(String usrn, String mail) throws CredentialsException {
		List<User> uList = null;
		try {
			uList = em.createNamedQuery("User.checkUnique", User.class).setParameter(1, usrn).setParameter(2, mail)
					.getResultList();
		} catch (PersistenceException e) {
			throw new CredentialsException("Could not verify credentals");
		}
		if (uList.isEmpty())
			return true;
		else
			return false;
	}

	public List<User> getAllUsers() {
		List<User> allUsers = null;
		allUsers = em.createNamedQuery("User.getAll", User.class).getResultList();

		return allUsers;
	}

	public void addUser(String usrn, String name, String surname, String pwd, String mail) {
		User user = new User(usrn, name, surname, pwd, mail);
		em.persist(user);
	}
}
