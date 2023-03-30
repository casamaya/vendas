/*
 * ClienteFornecedorModel.java
 * 
 * Created on 30/06/2007, 12:12:39
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package vendas.swing.model;

import java.util.List;
import vendas.beans.ClientesFilter;
import vendas.dao.RepresDao;
import vendas.entity.ClienteRepres;
import vendas.swing.core.ServiceTableModel;

/**
 *
 * @author Sam
 */
public class ClienteFornecedorModel extends ServiceTableModel {
    
    public static final int FORNECEDOR = 0;
    public static final int CLIENTE = 1;
    public static final int CODIGO = 2;
    public static final int IDENTIFICADOR = 3;
    public static final int LIMITE = 4;
    public static final int EMAIL = 5;

    public ClienteFornecedorModel() {
        super();
    }

    public ClienteFornecedorModel(List values) {
        super(values);
    }
    
    @Override
    public void setColumns() {
        addColumn("Fornecedor");
        addColumn("Cliente");
        addColumn("Código");
        addColumn("Identificador");
        addColumn("Limite");
        addColumn("Email");
    }
    
    @Override
    public Class getColumnClass(int col) {
        return String.class;
    }
    
    @Override
    public Object getValueAt(int row, int col) {
        ClienteRepres repres = (ClienteRepres)getObject(row);
        Object obj = null;
        switch (col) {
            case FORNECEDOR: obj = repres.getRepres().getRazao(); break;
            case CLIENTE: obj = repres.getCliente().getRazao(); break;
            case CODIGO: obj = repres.getCodRepres(); break;
            case IDENTIFICADOR: obj = repres.getCodIdentificador(); break;
            case LIMITE: obj = repres.getLimiteCredito(); break;
            case EMAIL: obj = repres.getCliente().getEmailMalaDireta(); break;
        }
        return obj;
    }

    @Override
    public void select(Object obj) throws Exception {
        RepresDao dao = (RepresDao)getDao();
        setItemList(dao.findClientes((ClientesFilter)obj));
        fireTableDataChanged();
    }
}
