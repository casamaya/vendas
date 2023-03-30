/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas.dao;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import vendas.beans.AgendaFilter;
import vendas.entity.Agenda;
import vendas.entity.manager.ManagerFactory;
import vendas.exception.DAOException;

/**
 *
 * @author sam
 */
public class AgendaDao extends BaseDao {

    @Override
    public List findAll(Object t) throws DAOException {
        return super.findByExample(t);
    }

    @Override
    public List findAll() throws DAOException {
        AgendaFilter filter = new AgendaFilter();
        return findByExample(filter);
    }

    @Override
    public List findByExample(Object obj) {
        getLogger().info("findByExample ini");
        Session session = ManagerFactory.getSession();
        AgendaFilter filter = (AgendaFilter) obj;
        Criteria criteria = session.createCriteria(Agenda.class);
        Criteria usuario = criteria.createCriteria("usuario");
        usuario.add(Restrictions.eq("userName", filter.getUsuario()));
        
        if (filter.getDtFim() != null) {
            criteria.add(Restrictions.between("dtevento", filter.getDtInicio(), filter.getDtFim()));
        } else {
            criteria.add(Restrictions.ge("dtevento", filter.getDtInicio()));
        }

        criteria.addOrder(Order.asc("dtevento"));
        return criteria.list();
    }

}
