package it.polimi.db2.marketing.ejb.services;

import java.util.List;
import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.NonUniqueResultException;

import it.polimi.db2.marketing.ejb.entities.User;
import it.polimi.db2.marketing.ejb.exceptions.*;

@Stateless
public class LeaderboardService {
	@PersistenceContext(unitName = "MissionExpensesEJB")
	private EntityManager em;

	public LeaderboardService() {
	}
	
	public List<User> getAllUsers() {
		List<User> allUsers = null;
		
		try {
			allUsers = em.createNamedQuery("User.getAll", User.class).getResultList();
		} catch (PersistenceException e) {
			//throw new Exception("Error retrieving users");
		}
		
		return allUsers;
	}
}
