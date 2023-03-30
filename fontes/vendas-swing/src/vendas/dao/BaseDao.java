/*
 * BaseDao.java
 * 
 * Created on 24/06/2007, 15:20:56
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package vendas.dao;

import vendas.entity.manager.ManagerFactory;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.apache.log4j.Logger;
import vendas.exception.DAOException;

/**
 *
 * @author Sam
 */
public class BaseDao<T> {

    private Logger logger;

    public BaseDao() {
        logger = Logger.getLogger(getClass());
    }

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public EntityManager getEntityManager() {
        return ManagerFactory.getEntityManager();
    }
    
    public void insertRecord(T object) throws DAOException {
        logger.info("insertRecord");
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(object);
            em.getTransaction().commit();
        } catch (Exception e) {
            try {
            em.getTransaction().rollback();
            } catch(Exception re) {
                logger.error(re);
            }
            throw new DAOException(e);
        } finally {
            em.close();
        }
    }
    public void insertRecord(T object, EntityManager em) throws DAOException {
        logger.info("insertRecord");
        try {
            em.persist(object);
        } catch (Exception e) {
            throw new DAOException(e);
        }
    }

    public void updateRow(T object) throws DAOException {
        logger.info("updateRow");
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            object = em.merge(object);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();            
            throw new DAOException(e);
        } finally {
            em.close();
        }
    }
    public void updateRow(T object, EntityManager em) throws DAOException {
        logger.info("updateRow");
        try {
            object = em.merge(object);
        } catch (Exception e) {
            e.printStackTrace();
            throw new DAOException(e);
        }
    }
    
    public void updateSingleRow(T object) throws DAOException {
        logger.info("updateSingleRow");
        EntityManager em = getEntityManager();
        try {

            object = em.merge(object);


        } catch (Exception e) {
            throw new DAOException(e);
        } finally {
            em.close();
        }
    }

    public void deleteRow(T object) throws DAOException {
        logger.info("deleteRow " + object.toString());
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            object = em.merge(object);
            em.remove(object);
            em.getTransaction().commit();
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            em.getTransaction().rollback();
            throw new DAOException(e);
        } finally {
            em.close();
        }        
    }
    
    public void deleteRow(Class clazz, Object id) throws DAOException {
        logger.info("deleteRow " + id);
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.remove(em.getReference(clazz, id));
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new DAOException(e);
        } finally {
            em.close();
        }
    }

    public List findAll() throws DAOException {
        return null;
    }

    public List<T> findAll(Class<T> clazz)  throws DAOException {
        StringBuilder sb = new StringBuilder(clazz.getSimpleName()).append(".findAll");
        EntityManager em = getEntityManager();
        Query q = em.createNamedQuery(sb.toString());
        return q.getResultList();
    }
    
    public List<T> findAll(T t)  throws DAOException {
        return null;
    }

    public List<T> findByName(Class<T> clazz, String name) throws DAOException {
        StringBuilder sb = new StringBuilder(clazz.getSimpleName()).append(".findByName");
        StringBuilder sbName = new StringBuilder("%").append(name).append("%");
        EntityManager em = getEntityManager();
        Query q = em.createNamedQuery(sb.toString());
        q.setParameter("name", sbName.toString());
        return q.getResultList();
    }

    public List findByExample(T obj) throws DAOException {
        return null;
    }

    public Object findById(Class<T> clazz, Object id) throws DAOException {
        EntityManager em = getEntityManager();
        Object value = null;
        try{
            value = em.find(clazz, id);
        } catch (Exception e) {
            throw new DAOException(e);
//        } finally {
//            em.close();
        }
        return value;
    }

}
