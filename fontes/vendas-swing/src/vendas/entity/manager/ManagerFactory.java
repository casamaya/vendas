/*
 * ManagerFactory.java
 * 
 * Created on 24/06/2007, 16:55:58
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package vendas.entity.manager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.hibernate.impl.SessionImpl;
import org.hibernate.Session;

/**
 *
 * @author Sam
 */
public class ManagerFactory {
    
    static EntityManagerFactory emf;
    static ManagerFactory mf;

    static {
        mf = new ManagerFactory();
    }
    
    private ManagerFactory() {
        emf = Persistence.createEntityManagerFactory("vendasPU");
    }

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    public static Session getSession() {
        SessionImpl si = (SessionImpl)ManagerFactory.getEntityManager().getDelegate();
        Session session = si.getSession(si.getEntityMode());
        return session;
    }
}
