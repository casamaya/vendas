/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vendas.dao;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import vendas.beans.AniversarioFilter;
import vendas.entity.ComissaoVendedor;
import vendas.entity.ComissaoVendedorPK;
import vendas.entity.ContatoVendedor;
import vendas.entity.Repres;
import vendas.entity.User;
import vendas.entity.Vendedor;
import vendas.entity.manager.ManagerFactory;

/**
 *
 * @author Sam
 */
public class VendedorDao extends BaseDao {

    public List<ContatoVendedor> findAniversariantes(AniversarioFilter filtro) {
        Session session = ManagerFactory.getSession();
        Criteria criteria = session.createCriteria(ContatoVendedor.class);
        if (filtro.getNome() != null && filtro.getNome().length() > 0)
            criteria.add(Restrictions.like("contato", filtro.getNome(), MatchMode.ANYWHERE));
        else {
            criteria.add(Restrictions.ge("aniversario", filtro.getMesInicial()));
            criteria.add(Restrictions.le("aniversario", filtro.getMesFinal()));
        }
        Criteria criteriaVendedor = criteria.createCriteria("vendedor");
        criteriaVendedor.add(Restrictions.eq("ativo", "1"));
        return criteria.list();
    }

    public List<Vendedor> findVendedorAniversariante(AniversarioFilter filtro) {
        Session session = ManagerFactory.getSession();
        Criteria criteria = session.createCriteria(Vendedor.class);
        if (filtro.getNome() != null && filtro.getNome().length() > 0)
            criteria.add(Restrictions.like("nome", filtro.getNome(), MatchMode.ANYWHERE));
        else {
            criteria.add(Restrictions.ge("dtAniver", filtro.getMesInicial()));
            criteria.add(Restrictions.le("dtAniver", filtro.getMesFinal()));
        }
        criteria.add(Restrictions.eq("ativo", "1"));
        return criteria.list();
    }
        
    @Override
    public List findAll() {
        return ManagerFactory.getEntityManager().createQuery("select object(o) from Vendedor as o order by o.nome").getResultList();
    }
    
    public List findAllAtivos() {
        EntityManager em = ManagerFactory.getEntityManager();
        Query q = em.createQuery("select object(o) from Vendedor as o where ativo = :ativo order by o.nome");
        q.setParameter("ativo", "1");
        return q.getResultList();
    }

    public List findAllVendedorComissao() {
        Session session = ManagerFactory.getSession();
        Criteria criteria = session.createCriteria(Vendedor.class).addOrder(Order.asc("nome"));
        criteria.add(Restrictions.eq("comissionado", "1"));
        return criteria.list();
    }

    @Override
    public List findByExample(Object obj) {
        String value = (String) obj;
        Session session = ManagerFactory.getSession();
        Criteria criteria = session.createCriteria(Vendedor.class).addOrder(Order.asc("nome"));
        criteria.add(Restrictions.like("nome", "%" + value.toUpperCase() + "%"));
        return criteria.list();
    }
    
    public List findVendedorPgto() {
        return ManagerFactory.getEntityManager().createQuery("select object(v) from Vendedor as v where v.idVendedor in (select p.vendedor.idVendedor from Pedido as p, AtendimentoPedido as a where p = a.pedido and p.vendedor = v and a.dtPgtoComissao is null) order by v.nome").getResultList();
    }
    
    public BigDecimal findComissao(Vendedor vendedor, Repres repres) {
        ComissaoVendedorPK pk = new ComissaoVendedorPK();
        pk.setIdrepres(repres.getIdRepres());
        pk.setIdvendedor(vendedor.getIdVendedor());
        ComissaoVendedor comissao;
        try {
            comissao = (ComissaoVendedor)findById(ComissaoVendedor.class, pk);
        } catch (Exception e) {
            comissao = null;
        }
        if (comissao == null)
            return null;
        else
            return comissao.getComissao();
    }

    public String getEmailUser(Integer idVendedor) {
        User user = (User) ManagerFactory.getEntityManager().createQuery("select o from User as o where o.idvendedor = " + idVendedor).getSingleResult();
        return user.getEmail();
    }
    
}
