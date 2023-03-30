/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vendas.dao;

import java.util.List;
import javax.persistence.Query;
import vendas.entity.Correio;
import vendas.entity.manager.ManagerFactory;
import vendas.exception.DAOException;

/**
 *
 * @author Sam
 */
public class CorreioDao extends BaseDao {

    @Override
    public List findByExample(Object t) throws DAOException {
        Correio c= (Correio)t;
        StringBuilder sb = new StringBuilder("select o from Correio o where o.userDestino.userName = :user");
        //sb.append(" and o.dataLeitura is null or o.dataLeitura >= :dt");
        Query q = ManagerFactory.getEntityManager().createQuery(sb.toString());
        q.setParameter("user", c.getUserDestino().getUserName());
        //q.setParameter("dt", c.getDataLeitura());
        return q.getResultList();
    }

    

}

