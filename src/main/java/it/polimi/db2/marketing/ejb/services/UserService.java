package it.polimi.db2.marketing.ejb.services;

import it.polimi.db2.marketing.ejb.entities.Log;
import it.polimi.db2.marketing.ejb.entities.User;
import it.polimi.db2.marketing.ejb.exceptions.CredentialsException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import java.util.List;

@Stateless
public class UserService {
    @PersistenceContext(unitName = "MarketingEJB")
    private EntityManager em;

    public UserService() {
    }

    public User getUser(int userid) {
        return em.find(User.class, userid);
    }

    public User checkCredentials(String usrn, String pwd) throws CredentialsException, NonUniqueResultException {
        List<User> users;
        try {
            users = em.createNamedQuery("User.checkCredentials", User.class).setParameter(1, usrn).setParameter(2, pwd)
                    .getResultList();
        } catch (PersistenceException e) {
            throw new CredentialsException("Could not verify credentals");
        }
        if (users.isEmpty())
            return null;
        else if (users.size() == 1)
            return users.get(0);
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
        return uList.isEmpty();
    }

    public List<User> getNonAdminUsers() {
        List<User> allUsers = null;
        allUsers = em.createNamedQuery("User.getNonAdminUsers", User.class).getResultList();

        return allUsers;
    }

    public void addUser(String usrn, String name, String surname, String pwd, String mail) {
        User user = new User(usrn, name, surname, pwd, mail);
        em.persist(user);
    }

    public void registerAccess(User user) {
        Log log = new Log(user);
        em.persist(log);
    }


}
