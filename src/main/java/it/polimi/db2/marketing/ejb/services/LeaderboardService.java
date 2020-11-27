package it.polimi.db2.marketing.ejb.services;

import it.polimi.db2.marketing.ejb.entities.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import java.util.List;

@Stateless
public class LeaderboardService {
	@PersistenceContext(unitName = "MarketingEJB")
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
