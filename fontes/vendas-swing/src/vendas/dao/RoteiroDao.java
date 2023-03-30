/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vendas.dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import vendas.entity.Cliente;
import vendas.entity.RoteiroVendedor;
import vendas.entity.manager.ManagerFactory;
import vendas.exception.DAOException;

/**
 *
 * @author Sam
 */
public class RoteiroDao extends BaseDao {

    @Override
    public List findAll() {
        return ManagerFactory.getEntityManager().createQuery("select object(o) from RoteiroVendedor as o order by descricao").getResultList();
    }

    @Override
    public List findByExample(Object obj) {
        getLogger().info("findByExample");
        RoteiroVendedor roteiro = (RoteiroVendedor) obj;
        Session session = ManagerFactory.getSession();
        Criteria criteria = session.createCriteria(RoteiroVendedor.class).addOrder(Order.asc("descricao"));
        criteria.createCriteria("vendedor").add(Restrictions.eq("idVendedor", roteiro.getVendedor().getIdVendedor()));
        return criteria.list();
    }

    public List findNotInRoteiro(RoteiroVendedor roteiro) {
        getLogger().info("findNotInRoteiro ini");
        Session session = ManagerFactory.getSession();
        StringBuilder sb = new StringBuilder();
        sb.append("select {c.*} from tbcliente as c, tbsitcliente as s, tbvendedorcliente v"); 
        sb.append(" where c.idsitcliente = s.idsitcliente and v.idcliente = c.idcliente");
        sb.append(" and s.pedido = '1'");
        sb.append(" and v.idvendedor = ").append(roteiro.getVendedor().getIdVendedor().toString());
        sb.append(" and c.idcliente not in (select idcliente from tbroteirocliente where idroteiro = ").append(roteiro.getIdRoteiro().toString()).append( ")");
        sb.append(" order by c.razao");
        
        return session.createSQLQuery(sb.toString()).addEntity("c", Cliente.class).list();
    }

    public void inserirCliente(RoteiroVendedor roteiro, Cliente cliente) throws Exception {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Query q = em.createNativeQuery(String.format("insert into tbroteirocliente (idroteiro, idcliente) values (%s, %s)", roteiro.getIdRoteiro(), cliente.getIdCliente()));
            q.executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new DAOException(e);
        } finally {
            em.close();
        }
    }

    public void deleteCliente(RoteiroVendedor roteiro, Cliente cliente) throws Exception  {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Query q = em.createNativeQuery(String.format("delete from tbroteirocliente where idroteiro = %s and idcliente = %s", roteiro.getIdRoteiro(), cliente.getIdCliente()));
            q.executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new DAOException(e);
        } finally {
            em.close();
        }
    }

    public void deleteRoteiro(RoteiroVendedor roteiro) throws Exception {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Query q = em.createNativeQuery(String.format("delete from tbroteirocliente where idroteiro = %s", roteiro.getIdRoteiro()));
            q.executeUpdate();
            q = em.createNativeQuery(String.format("delete from tbroteirovendedor where idroteiro = %s", roteiro.getIdRoteiro()));
            q.executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new DAOException(e);
        } finally {
            em.close();
        }
    }

}
