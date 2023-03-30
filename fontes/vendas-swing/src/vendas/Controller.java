/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas;

import java.util.List;
import vendas.dao.BaseDao;
import vendas.entity.Banco;
import vendas.exception.BOException;
import vendas.exception.DAOException;
import vendas.exception.InternalErrorException;

/**
 *
 * @author sam
 */
public class Controller {

    private BaseDao dao;

    public BaseDao getDao() {
        return dao;
    }

    public void setDao(BaseDao dao) {
        this.dao = dao;
    }



    public Object persist(Object obj) throws BOException, InternalErrorException {
        try {
            dao.insertRecord(obj);
        } catch (DAOException e) {
            throw new InternalErrorException(e);
        }
        return obj;
    }

    public Object update(Object obj) throws BOException, InternalErrorException {
        try {
            dao.updateRow(obj);
        } catch (DAOException e) {
            throw new InternalErrorException(e);
        }
        return obj;
    }

    public void delete(Object obj) throws BOException, InternalErrorException {
        try {
            dao.deleteRow(obj);
        } catch (DAOException e) {
            throw new InternalErrorException(e);
        }
    }

    public List findAll(Object obj) throws BOException, InternalErrorException {
        try {
            return dao.findByExample(obj);
        } catch (DAOException e) {
            throw new InternalErrorException(e);
        }
    }

    public Object findById(Class clazz, Object id) throws InternalErrorException {
        try {
            return dao.findById(clazz, id);
        } catch (DAOException e) {
            throw new InternalErrorException(e);
        }
    }

    public boolean validate(Object obj) {
        Banco banco = (Banco)obj;
        return banco.getNome() != null && banco.getNome().length() >= 3;
    }


}
