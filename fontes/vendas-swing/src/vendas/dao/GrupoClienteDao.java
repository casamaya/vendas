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
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import vendas.entity.GrupoCliente;

/**
 *
 * @author Sam
 */
public class GrupoClienteDao extends BaseDao {

    @Override
    public List findAll() {
        return ManagerFactory.getEntityManager().createQuery("select object(o) from GrupoCliente as o order by o.nomeGrupo").getResultList();
    }
    @Override
    public List findByExample(Object obj) {
        String value = (String) obj;
        Session session = ManagerFactory.getSession();
        Criteria criteria = session.createCriteria(GrupoCliente.class).addOrder(Order.asc("nomeGrupo"));
        criteria.add(Restrictions.like("nomeGrupo", "%" + value.toUpperCase() + "%"));
        return criteria.list();
    }
}
