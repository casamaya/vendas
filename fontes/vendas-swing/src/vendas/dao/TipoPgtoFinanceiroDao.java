/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vendas.dao;

import java.util.List;
import javax.persistence.Query;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import vendas.entity.TipoPgto;
import vendas.entity.TipoPgtoFinanceiro;
import vendas.entity.Vendedor;
import vendas.entity.manager.ManagerFactory;

/**
 *
 * @author Sam
 */
public class TipoPgtoFinanceiroDao extends BaseDao {

    public List findAll(Integer vendedor) {
        Query query = ManagerFactory.getEntityManager().createQuery("select object(o) from TipoPgtoFinanceiro as o where o.vendedor.idVendedor = :vendedor order by o.nome");
        query.setParameter("vendedor", vendedor);
        return query.getResultList();
    }
    
    @Override
    public List findByExample(Object obj) {
        Vendedor value = (Vendedor) obj;
        Session session = ManagerFactory.getSession();
        Criteria criteria = session.createCriteria(TipoPgtoFinanceiro.class).addOrder(Order.asc("nome"));
        criteria.add(Restrictions.eq("vendedor.idVendedor", value.getIdVendedor()));
        return criteria.list();
    }
}
