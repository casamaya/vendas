/*
 * BaseTableModel.java
 * 
 * Created on 24/06/2007, 11:39:01
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package vendas.swing.core;

import java.util.List;
import vendas.dao.BaseDao;

/**
 *
 * @author Sam
 */
public abstract class ServiceTableModel extends BaseTableModel {

    private BaseDao dao;
    
    public ServiceTableModel() {
        super();
    }

    public ServiceTableModel(List values) {
        super(values);
    }

    public void setDao(BaseDao bs) {
        dao = bs;
    }
    
    public BaseDao getDao() {
        return dao;
    }
    
    public void insertRecord(Object object) throws Exception {
        try {
            dao.insertRecord(object);
            addObject(object);
        } catch (Exception e) {
            errorLog(e);
            throw e;
        }
    }
    
    public void updateRow(Object object) throws Exception {
        try {
            dao.updateRow(object);
            fireTableDataChanged();
        } catch (Exception e) {
            errorLog(e);
            throw e;
        }
    }
    
    public void deleteRow(int row) throws Exception {
        infoLog("deleteRow");
        try {
            dao.deleteRow(getObject(row));
            infoLog("atualizando model");
            removeObject(row);
            infoLog("atualizado");
        } catch (Exception e) {
            errorLog(e);
            throw e;
        }
    }

    public void select(Object obj) throws Exception {
        infoLog("select");
        if (obj == null) {
            infoLog("findAll");
            setItemList(dao.findAll());
        } else {
            infoLog("findByExample");
            if (dao != null)
            setItemList(dao.findByExample(obj));
        }
        fireTableDataChanged();
    }
    
}
