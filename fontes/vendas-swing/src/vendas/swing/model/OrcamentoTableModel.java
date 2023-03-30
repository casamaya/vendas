/*
 * OrcamentoTableModel.java
 * 
 * Created on 09/07/2007, 21:33:42
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package vendas.swing.model;

import java.math.BigDecimal;
import java.util.Date;
import ritual.swing.TApplication;
import vendas.dao.OrcamentoDao;
import vendas.swing.core.ServiceTableModel;
import vendas.entity.Orcamento;
import vendas.util.Constants;
import vendas.util.Messages;

/**
 *
 * @author Sam
 */
public class OrcamentoTableModel extends ServiceTableModel {

    public static final int ORCAMENTO = 0;
    public static final int REPRES = 1;
    public static final int CLIENTE = 2;
    public static final int VENDEDOR = 3;
    public static final int EMISSAO = 4;
    public static final int FORMAPGTO = 5;
    public static final int VALOR = 6;

    public OrcamentoTableModel() throws Exception {
        super();
    }

    @Override
    public void setColumns() {
        addColumn("Orcamento");
        addColumn("Fornecedor");
        addColumn("Cliente");
        addColumn("Vendedor");
        addColumn("Data");
        addColumn("Forma pagamento");
        addColumn("Valor");
    }

    @Override
    public Class getColumnClass(int column) {
        Class aClass = null;
        switch (column) {
            case ORCAMENTO:
                aClass = Integer.class;
                break;
            case REPRES:
                aClass = String.class;
                break;
            case CLIENTE:
                aClass = String.class;
                break;
            case VENDEDOR:
                aClass = String.class;
                break;
            case EMISSAO:
                aClass = Date.class;
                break;
            case FORMAPGTO:
                aClass = String.class;
                break;
            case VALOR:
                aClass = BigDecimal.class;
                break;
        }
        return aClass;
    }

    @Override
    public Object getValueAt(int row, int col) {
        Orcamento orcamento = (Orcamento) getObject(row);
        if (orcamento == null) {
            return null;
        }
        Object obj = null;
        switch (col) {
            case ORCAMENTO:
                obj = orcamento.getIdorcamento();
                break;
            case REPRES:
                obj = orcamento.getRepres().getRazao();
                break;
            case CLIENTE:
                obj = orcamento.getCliente().getRazao();
                break;
            case VENDEDOR:
                obj = orcamento.getVendedor().getNome();
                break;
            case EMISSAO:
                obj = orcamento.getDtorcamento();
                break;
            case FORMAPGTO:
                obj = orcamento.getFormaPgto().getNome();
                break;
            case VALOR:
                obj = orcamento.getValor();
                break;
        }
        return obj;
    }

    @Override
    public void deleteRow(int row) throws Exception {
        infoLog("deleteRow");
        try {
            Orcamento orcamento = (Orcamento) getObject(row);
            OrcamentoDao dao = (OrcamentoDao) getDao();
            dao.deleteRow(orcamento);
            infoLog("atualizando model");
            removeObject(row);
            infoLog("atualizado");
        } catch (Exception e) {
            errorLog(e);
            throw e;
        }
    }
}
