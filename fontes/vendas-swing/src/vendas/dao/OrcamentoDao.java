/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vendas.dao;

import java.math.BigInteger;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import vendas.beans.PedidoFilter;
import vendas.entity.Orcamento;
import vendas.entity.manager.ManagerFactory;
import vendas.exception.DAOException;

/**
 *
 * @author sam
 */
public class OrcamentoDao extends BaseDao {

    @Override
    public List findAll() throws DAOException {
        return findByExample(null);
    }

    @Override
    public void deleteRow(Object object) throws DAOException {
        Orcamento orcamento = (Orcamento) object;
                EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Query q = em.createQuery("delete from ItemOrcamento where itemOrcamentoPK.idorcamento = :orcamento");
            q.setParameter("orcamento", orcamento.getIdorcamento());
            q.executeUpdate();
            q = em.createQuery("delete from ArquivoOrcamento where orcamento.idorcamento = :orcamento");
            q.setParameter("orcamento", orcamento.getIdorcamento());
            q.executeUpdate();
            q = em.createQuery("delete from Orcamento where idorcamento = :orcamento)");
            q.setParameter("orcamento", orcamento.getIdorcamento());
            q.executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
    
    @Override
    public List findByExample(Object obj) throws DAOException {
        getLogger().info("findByExample");
        PedidoFilter filter = (PedidoFilter) obj;
        Session session = ManagerFactory.getSession();
        Criteria criteria = session.createCriteria(Orcamento.class).addOrder(Order.asc("idorcamento"));
        if (filter.getCliente().getIdCliente() != null) {
            Criteria criteriaCliente = criteria.createCriteria("cliente");
            criteriaCliente.add(Restrictions.eq("idCliente", filter.getCliente().getIdCliente()));
        }
        if (filter.getRepres().getIdRepres() != null) {
            Criteria criteriaRepres = criteria.createCriteria("repres");
            criteriaRepres.add(Restrictions.eq("idRepres", filter.getRepres().getIdRepres()));
        }
        if (filter.getVendedor().getIdVendedor() != null) {
            Criteria criteriaVendedor = criteria.createCriteria("vendedor");
            criteriaVendedor.add(Restrictions.eq("idVendedor", filter.getVendedor().getIdVendedor()));
        }
        if (filter.getDtEmissaoIni() != null)
            criteria.add(Restrictions.between("dtorcamento", filter.getDtEmissaoIni(), filter.getDtEmissaoEnd()));
        return criteria.list();
    }


    public Integer getNextValue() {
        String s = "select gen_id( TBORCAMENTO_IDORCAMENTO_SEQ, 1 ) from RDB$DATABASE";
        Session session = ManagerFactory.getSession();
        SQLQuery q = (SQLQuery) session.createSQLQuery(s);
        BigInteger bi = (BigInteger) q.uniqueResult();

        return bi.intValue();

    }
    public void setNextValue(Integer idPedido) {
        //String s = "select max(idpedido) from tbpedido";
        Session session = ManagerFactory.getSession();
        //SQLQuery q = (SQLQuery) session.createSQLQuery(s);
        //Integer bi = (Integer) q.uniqueResult();
        String s = "set generator TBORCAMENTO_IDORCAMENTO_SEQ to " + idPedido.toString();
        SQLQuery q = (SQLQuery) session.createSQLQuery(s);
        //q.setParameter("num", bi.intValue());
        q.executeUpdate();
    }

        public void updateOrcamento(Object object) throws DAOException {
        Orcamento pedido = (Orcamento) object;
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Query q = em.createQuery("delete from ItemOrcamento where orcamento.idorcamento = :pedido");
            q.setParameter("pedido", pedido.getIdorcamento());
            int n = q.executeUpdate();
            //getLogger().info(n);
            object = em.merge(object);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new DAOException(e);
        } finally {
            em.close();
        }
    }

    public Collection findUnidadesByOrcamento(Integer idorcamento) {
        Query q = ManagerFactory.getEntityManager().createQuery("select new vendas.beans.UnidadeItem(p.undCumulativa.idUnidade, sum(i.qtd * p.fatorConversao)) from ItemOrcamento i, Produto p where i.orcamento.idorcamento = :pedido and i.produto.idProduto = p.idProduto group by p.undCumulativa.idUnidade");
        q.setParameter("pedido", idorcamento);
        List lista = q.getResultList();
        return lista;    }

    public Collection findItensOrcamento(Integer idorcamento) {
        getLogger().info("findItensOrcamento ini");
        Query q = ManagerFactory.getEntityManager().createQuery("select o from ItemOrcamento as o where o.orcamento.idorcamento = :pedido order by o.produto.descricao ASC");
        q.setParameter("pedido", idorcamento);
        List lista = q.getResultList();
        return lista;
    }
}
