/*
 * ContaRepresModel.java
 * 
 * Created on 30/06/2007, 12:12:39
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package vendas.swing.model;

import java.util.List;
import vendas.entity.ContaRepres;
import vendas.swing.core.BaseTableModel;

/**
 *
 * @author Sam
 */
public class ContaRepresModel extends BaseTableModel {
    
    public static final int TIPOCONTA = 0;
    public static final int BANCO = 1;
    public static final int AGENCIA = 2;
    public static final int CONTA = 3;
    public static final int NOME = 4;
    public static final int CPFCNPJ = 5;
    public static final int ATIVO = 6;

    public ContaRepresModel(List values) {
        super(values);
    }
    
    @Override
    public void setColumns() {
        addColumn("Tipo");
        addColumn("Banco");
        addColumn("Agência");
        addColumn("Conta");
        addColumn("Nome");
        addColumn("CPF/CNPJ");
        addColumn("Situa\u00E7\u00E3o");
    }
    
    @Override
    public Class getColumnClass(int column) {
        return String.class;
    }
    
    @Override
    public Object getValueAt(int row, int col) {
        ContaRepres entrada = (ContaRepres)getObject(row);
        Object obj = null;
        switch (col) {
        case BANCO: if (entrada.getBanco() != null) {
                        obj = entrada.getBanco().getNome();
                    }; break;
        case AGENCIA: obj = entrada.getAgencia(); break;
        case CONTA: obj = entrada.getContaCorrente(); break;
        case NOME: obj = entrada.getNome(); break;
        case CPFCNPJ: obj = entrada.getCpfCnpj(); break;
        case ATIVO: obj = "A".equals(entrada.getAtivo()) ? "Ativo" : "Inativo"; break;
        default: obj = entrada.getTipoConta();
        }

        return obj;
    }
}
