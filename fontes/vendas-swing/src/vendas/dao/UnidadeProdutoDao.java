/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vendas.dao;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import vendas.entity.UnidadeProduto;
import vendas.entity.manager.ManagerFactory;

/**
 *
 * @author Sam
 */
public class UnidadeProdutoDao extends BaseDao {
    @Override
    public List findAll() {
        return ManagerFactory.getEntityManager().createQuery("select object(o) from UnidadeProduto as o order by o.idUnidade").getResultList();
    }
    @Override
    public List findByExample(Object obj) {
        String value = (String) obj;
        Session session = ManagerFactory.getSession();
        Criteria criteria = session.createCriteria(UnidadeProduto.class).addOrder(Order.asc("nome"));
        criteria.add(Restrictions.like("nome", "%" + value.toUpperCase() + "%"));
        return criteria.list();
    }
}
