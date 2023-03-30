/*
 * BaseTableModel.java
 * 
 * Created on 14/07/2007, 14:18:52
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package vendas.swing.core;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import org.apache.log4j.Logger;

/**
 *
 * @author Sam
 */
public class BaseTableModel extends DefaultTableModel {

    private List itens = null;
    private Logger logger;
    private boolean readOnly;

    public BaseTableModel() {
        logger = Logger.getLogger(getClass());
        setColumns();
        readOnly = true;
    }
    
    public BaseTableModel(List values) {
        this();
        if (values == null)
            itens = new ArrayList();
        else
            itens = values;
    }

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public void setColumns() {}

    public Object getObject(int row) {
        if (row < 0 || row >= itens.size())
            return null;
        return itens.get(row);
    }
    
    @Override
    public int getRowCount() {
        if (itens == null)
            return 0;
        return itens.size();
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    public List getItemList() {
        return itens;
    }
    
    public void setItemList(List lista) {
        itens = lista;
    }
    
    public void addObject(Object object) {
        if (itens == null) {
            itens = new ArrayList();
        }
       itens.add(object);
       fireTableDataChanged();
    }

    public void removeObject(int row) {
        itens.remove(row);
        fireTableDataChanged();
    }
    
    public void errorLog(Object value) {
        logger.error(value);
    }
    
    public void errorLog(Object value, Throwable e) {
        logger.error(value, e);
    }

    public void infoLog(Object value) {
        logger.info(value);
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }
}
