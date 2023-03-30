/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vendas.dao;

import vendas.beans.CompromissoFilter;
import java.util.List;
import vendas.entity.APagar;
import vendas.entity.Compromisso;
import vendas.exception.DAOException;

/**
 *
 * @author Sam
 */
public class APagarDao extends CompromissoDao {

    @Override
    public List findByExample(Object obj) throws DAOException {
        return findCompromisso(APagar.class, (CompromissoFilter)obj, false);
    }

    @Override
    public List findByExample(Object obj, boolean orderByMaster) throws Exception {
        return findCompromisso(APagar.class, (CompromissoFilter)obj, orderByMaster);
    }

    @Override
    public void exportarLancamento(List <Compromisso> lista, int month) throws Exception {
        exportarLancamento(lista, APagar.class, month);
    }
}
