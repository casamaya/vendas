/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas.dao;

import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import vendas.entity.GrupoProduto;
import vendas.entity.SubGrupoProduto;
import vendas.entity.manager.ManagerFactory;
import vendas.exception.DAOException;

/**
 *
 * @author Sam
 */
public class GrupoProdutoDao extends BaseDao {

    @Override
    public List findAll() {
        return ManagerFactory.getEntityManager().createQuery("select object(o) from GrupoProduto as o order by o.nomeGrupo").getResultList();
    }

    @Override
    public List findByExample(Object obj) {
        String value = (String) obj;
        Session session = ManagerFactory.getSession();
        Criteria criteria = session.createCriteria(GrupoProduto.class).addOrder(Order.asc("nomeGrupo"));
        criteria.add(Restrictions.like("nomeGrupo", "%" + value.toUpperCase() + "%"));
        return criteria.list();
    }

    public GrupoProduto updateGrupoProduto(GrupoProduto grupo, List<SubGrupoProduto> deletedItens) throws DAOException {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            if (deletedItens != null) {
                Query q = em.createQuery("delete from SubGrupoProduto where idCodSubGrupo = :grupo");
                for (SubGrupoProduto subGrupo : deletedItens) {
                    q.setParameter("grupo", subGrupo.getIdCodSubGrupo());
                    q.executeUpdate();
                }
            }
            grupo = em.merge(grupo);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new DAOException(e);
        } finally {
            em.close();
        }
        return grupo;
    }

    public Collection findAllSubGrupo(Integer grupo) {
        Query query = ManagerFactory.getEntityManager().createQuery("select object(o) from SubGrupoProduto as o where o.idCodSubGrupo = :grupo order by o.nomeGrupo");
        query.setParameter("grupo", grupo);
        return query.getResultList();
    }
}
