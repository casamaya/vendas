/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas.dao;

import java.util.Date;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import vendas.beans.PendenciaFilter;
import vendas.entity.Pendencia;
import vendas.entity.manager.ManagerFactory;
import vendas.exception.DAOException;

/**
 *
 * @author jaime
 */
public class PendenciaDao extends BaseDao {
    @Override
    public List findAll() {
        return ManagerFactory.getEntityManager().createQuery("select object(o) from Pendencia as o order by dtInclusao").getResultList();
    }

    @Override
    public List findByExample(Object obj) {
        getLogger().info("findByExample");
        PendenciaFilter filter = (PendenciaFilter) obj;
        Session session = ManagerFactory.getSession();
        Criteria criteria = session.createCriteria(PendenciaFilter.class).addOrder(Order.asc("dtInclusao"));
        if (filter.getUsername() != null) {
            criteria.add(Restrictions.eq("username", filter.getUsername()));
        }
        if (filter.getDtInicio() != null) {
            criteria.add(Restrictions.ge("dtInclusao", filter.getDtInicio()));
        }
        if (filter.getDtFim() != null) {
            criteria.add(Restrictions.le("dtInclusao", filter.getDtFim()));
        }
        if (filter.getStatus() != null && filter.getStatus().compareTo("T") != 0) {
            criteria.add(Restrictions.eq("inResolvido", filter.getStatus()));
        }
        
        return criteria.list();
    }

    @Override
    public void insertRecord(Object object) throws DAOException {
        Pendencia pendencia = (Pendencia) object;
        pendencia.setDtInclusao(new Date());
        pendencia.setInResolvido("N");
        super.insertRecord(object);
    }

    @Override
    public void updateRow(Object object) throws DAOException {
        Pendencia pendencia = (Pendencia) object;
        pendencia.setDtAlteracao(new Date());
        super.updateRow(object);
    }
}
