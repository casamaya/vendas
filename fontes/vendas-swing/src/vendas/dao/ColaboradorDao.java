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
import vendas.beans.ColaboradorFilter;
import vendas.entity.Colaborador;
import vendas.entity.FuncaoColaborador;
import vendas.entity.manager.ManagerFactory;

/**
 *
 * @author jaimeoliveira
 */
public class ColaboradorDao extends BaseDao {
    
        @Override
    public List findAll() {
        getLogger().info("findAll");
        ColaboradorFilter filtro = new ColaboradorFilter();
        
        return findByExample(filtro);
    }

    @Override
    public List findByExample(Object obj) {
        getLogger().info("findByExample");
        
        ColaboradorFilter filter = (ColaboradorFilter) obj;
        Colaborador colab = filter.getColaborador();
        Session session = ManagerFactory.getSession();
        Criteria criteria = session.createCriteria(Colaborador.class);

        if (colab.getNome() != null && !colab.getNome().isEmpty()) {
            criteria.add(Restrictions.like("nome", "%" + colab.getNome() + "%"));
        }

        if (colab.getCidade() != null && !colab.getCidade().isEmpty()) {
            criteria.add(Restrictions.like("cidade", "%" + colab.getNome() + "%"));
        }

        if (colab.getUf() != null && !colab.getUf().isEmpty()) {
            criteria.add(Restrictions.eq("uf", colab.getUf()));
        }

        if (colab.getRazao() != null && !colab.getRazao().isEmpty()) {
            criteria.add(Restrictions.like("razao", "%" + colab.getUf() + "%"));
        }

        if (colab.getFuncaoColaborador().getIdfuncao() != null) {
            criteria.createCriteria("funcaoColaborador").add(Restrictions.eq("idfuncao", colab.getFuncaoColaborador().getIdfuncao()));
        }
        
        criteria.addOrder(Order.asc("nome"));
        
        return criteria.list();
    }
    
    public List getFuncoes() {
        Session session = ManagerFactory.getSession();
        Criteria criteria = session.createCriteria(FuncaoColaborador.class);
        criteria.addOrder(Order.asc("nome"));
        return criteria.list();
    }

}
