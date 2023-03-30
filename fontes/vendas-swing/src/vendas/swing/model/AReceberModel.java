/*
 * ContaTableModel.java
 * 
 * Created on 09/07/2007, 21:33:42
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package vendas.swing.model;

/**
 *
 * @author Sam
 */
public class AReceberModel extends CompromissoModel {

    @Override
    public void setColumns() {
        addColumn("Vencimento");
        addColumn("Fornecedor");
        addColumn("Grupo");
        addColumn("Valor");
        addColumn("Tipo pagamento");
        addColumn("Data pagamento");
        addColumn("Observa\u00E7\u00E3o");
    }

}
