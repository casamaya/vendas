/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vendas.dao;

import java.util.List;
import vendas.entity.manager.ManagerFactory;
import vendas.exception.DAOException;

/**
 *
 * @author Sam
 */
public class GrupoMovimentoDao extends BaseDao {   
    @Override
    public List findByExample(Object obj) throws DAOException {
        Integer vendedor = (Integer) obj;
        return ManagerFactory.getEntityManager().createQuery("select object(o) from GrupoMovimento as o where o.vendedor.idVendedor = " + vendedor + " order by o.nomeGrupo").getResultList();
    }
}
