/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vendas.dao;

import java.util.List;
import vendas.entity.ContaEmpresa;
import vendas.exception.DAOException;

/**
 *
 * @author Sam
 */
public class ContaEmpresaDao extends BaseDao {

    @Override
    public List findAll() throws DAOException {
        return findAll(ContaEmpresa.class);
    }

}
