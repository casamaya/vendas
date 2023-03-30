/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas.swing.model;

import java.math.BigDecimal;
import java.util.List;
import ritual.swing.TApplication;
import vendas.beans.VendasCliente;
import vendas.dao.ClienteDao;
import vendas.dao.PedidoDao;
import vendas.entity.Cliente;
import vendas.swing.core.ServiceTableModel;

/**
 *
 * @author jaimeoliveira
 */
public class ClientePgtoPendenteTableModel  extends ServiceTableModel {
    
        public static int RAZAO = 0;
    public static int NAOATENDIDO = 1;
    public static int VENCIDOS = 2;
    public static int AVENCER = 3;
    public static int ATRASO = 4;
    
    public ClientePgtoPendenteTableModel() throws Exception {
        super();
    }
    
    public ClientePgtoPendenteTableModel(List values) {
        super(values);
    }

    @Override
    public void setColumns() {
        addColumn("Razão social");
        addColumn("Pedidos n\u00E3o atendidos");
        addColumn("Valores vencidos");
        addColumn("Valores a vencer");
        addColumn("Dias atraso");
    }

    @Override
    public Class getColumnClass(int column) {
        if (column == 0)
            return String.class;
        else
            return BigDecimal.class;
    }
    @Override
    public Object getValueAt(int row, int col) {
        VendasCliente cliente = (VendasCliente)getObject(row);
        if (col == RAZAO)
            return cliente.getRazao();
        else if (col == NAOATENDIDO)
            return cliente.getValorVenda();
        else if (col == VENCIDOS)
            return cliente.getValorVencido();
        else if (col == AVENCER)
            return cliente.getValorPgto();
        else
            return cliente.getNumDiasAtraso();
    }

    @Override
    public void select(Object obj) throws Exception {
        ClienteDao dao = (ClienteDao) TApplication.getInstance().lookupService("clienteDao");
        PedidoDao pedidodao = (PedidoDao) TApplication.getInstance().lookupService("pedidoDao");
        BigDecimal value;
        Cliente c;
        BigDecimal b;
        List<VendasCliente> lista = dao.getClientesPgtosPendentes();
        for (VendasCliente o : lista) {
            value = pedidodao.getVendasClientePendende(o.getId());
            o.setVALORVENDA(value);
            c = (Cliente)dao.findById(Cliente.class, o.getId());
            o.setNumDiasAtraso(c.getNumDiasAtraso());
        }
        setItemList(lista);
        fireTableDataChanged();
    }
    
    
}
