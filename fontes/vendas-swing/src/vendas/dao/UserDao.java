/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vendas.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Query;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import vendas.entity.Perfil;
import vendas.entity.PerfilRecurso;
import vendas.entity.Recurso;
import vendas.entity.User;
import vendas.entity.manager.ManagerFactory;

/**
 *
 * @author Sam
 */
public class UserDao extends BaseDao {
    @Override
    public List findAll() {
        return ManagerFactory.getEntityManager().createQuery("select u from User u order by u.userName").getResultList();
    }

    public User getUserByVendedor(Integer i) {
        List<User> users = ManagerFactory.getEntityManager().createQuery("select u from User u where u.idvendedor = " + i).getResultList();
        if (users == null || users.isEmpty())
            return null;
        return users.get(0);
    }
    
    public List<Perfil> getPerfis() {
        getLogger().info("getPerfis");
        Query query = ManagerFactory.getEntityManager().createQuery("select u from Perfil u where u.inStatusPerfil = :situacao");
        query.setParameter("situacao", 'A');
        return query.getResultList();
    }

    public List<Recurso> findAllMaster(Perfil perfil) {
        getLogger().info("findAllMaster");
        Query query = getEntityManager().createQuery("select t from Recurso as t inner join t.perfilList pl where t.inStatusRecurso = 'A' and t.recursoPai is null and pl.perfil1.perfil = :perfil");
        query.setParameter("perfil", perfil.getPerfil());
        return query.getResultList();
    }

    public PerfilRecurso getPerfilRecurso(Perfil perfil, Recurso recurso) {
        Query query = getEntityManager().createQuery("select t from PerfilRecurso as t where t.perfil1.perfil = :perfil and t.recurso.idRecurso = :recurso and t.recurso.inStatusRecurso = 'A'");
        query.setParameter("perfil", perfil.getPerfil());
        query.setParameter("recurso", recurso.getIdRecurso());
        return (PerfilRecurso)query.getSingleResult();
    }
    
    public Map<String, Boolean> getPermissoes(List<Perfil> perfis) {
        getLogger().info("getPermissoes");
        Session session = ManagerFactory.getSession();
        Criteria criteria = session.createCriteria(PerfilRecurso.class);
        List lista = new ArrayList();
        
        for (Perfil p : perfis) {
            lista.add(p.getPerfil());
        }
        
        criteria.add(Restrictions.in("perfil1.perfil", lista));
        
        Criteria recursoCri = criteria.createCriteria("recurso");
        recursoCri.add(Restrictions.eq("inStatusRecurso", 'A'));

        List<PerfilRecurso>values = criteria.list();
        Map<String, Boolean> result = new HashMap<>();
        
        for (PerfilRecurso pr : values) {
            if ("C".equals(pr.getChecked().toString())) {
                result.put(pr.getRecurso().getMnemonico(), Boolean.TRUE);
            }
        }
        return result;
    }
    
}
