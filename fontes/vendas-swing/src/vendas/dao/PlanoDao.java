/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vendas.dao;

import java.util.List;
import javax.persistence.Query;
import vendas.exception.DAOException;

/**
 *
 * @author Sam
 */
public class PlanoDao extends BaseDao {

    public List findAll(Integer vendedor) throws DAOException {
        getLogger().info("findAll");
        Query query = getEntityManager().createQuery("select t from Conta as t where t.contaMaster is not null and t.vendedor.idVendedor = :vendedor order by t.tipo, t.contaMaster.nome, t.nome");
        query.setParameter("vendedor", vendedor);
        return query.getResultList();
    }

    public List findAPagar(Integer vendedor) throws DAOException {
        getLogger().info("findAPagar");
        Query query = getEntityManager().createQuery("select t from Conta as t where t.contaMaster is not null and t.tipo = 1 and t.vendedor.idVendedor = :vendedor order by t.nome");
        query.setParameter("vendedor", vendedor);
        return query.getResultList();
    }
    public List findAPagarMaster(Integer vendedor) throws DAOException {
        getLogger().info("findAPagarMaster");
        Query query = getEntityManager().createQuery("select t from Conta as t where t.contaMaster is null and t.tipo = 1 and t.vendedor.idVendedor = :vendedor order by t.nome");
        query.setParameter("vendedor", vendedor);
        return query.getResultList();
    }
    
    public List findAReceber(Integer vendedor) throws DAOException {
        getLogger().info("findAReceber");
        Query query = getEntityManager().createQuery("select t from Conta as t where t.contaMaster is not null and t.tipo = 2 and t.vendedor.idVendedor = :vendedor order by t.nome");
        query.setParameter("vendedor", vendedor);
        return query.getResultList();
    }
    public List findAReceberMaster(Integer vendedor) throws DAOException {
        getLogger().info("findAReceberMaster");
        Query query = getEntityManager().createQuery("select t from Conta as t where t.contaMaster is null and t.tipo = 2 and t.vendedor.idVendedor = :vendedor order by t.nome");
        query.setParameter("vendedor", vendedor);
        return query.getResultList();
    }

    public List findAllMaster(Integer vendedor) throws DAOException {
        getLogger().info("findAllMaster");
        Query query = getEntityManager().createQuery("select t from Conta as t where t.contaMaster is null and t.vendedor.idVendedor = :vendedor order by t.nome");
        query.setParameter("vendedor", vendedor);
        return query.getResultList();
    }
    

}
