/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas.swing.model;

import java.util.Date;
import java.util.List;
import vendas.entity.ReciboComissao;
import vendas.swing.core.ServiceTableModel;

/**
 *
 * @author joliveira
 */
public class ReciboVendedorModel extends ServiceTableModel {

    public ReciboVendedorModel() throws Exception {
        super();
    }

    public ReciboVendedorModel(List values) {
        super(values);
    }

    @Override
    public void setColumns() {
        addColumn("Data");
    }

    @Override
    public Class getColumnClass(int column) {
        return Date.class;
    }

    @Override
    public Object getValueAt(int row, int col) {
        ReciboComissao recibo = (ReciboComissao) getObject(row);
        return recibo.getDtRecibo();
    }

}
