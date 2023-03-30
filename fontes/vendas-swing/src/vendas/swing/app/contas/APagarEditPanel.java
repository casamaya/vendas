/*
 * EditarAPagarPanel.java
 *
 * Created on November 19, 2006, 4:20 PM
 */
package vendas.swing.app.contas;

import ritual.swing.TApplication;
import vendas.dao.PlanoDao;
import vendas.dao.TipoPgtoFinanceiroDao;

/**
 *
 * @author  jaime
 */
public class APagarEditPanel extends CompromissoEditPanel {

    /** Creates new form EditarAPagarPanel */
    public APagarEditPanel() {
        super();
        try {
            PlanoDao tp = new PlanoDao();
            TipoPgtoFinanceiroDao tpfd = new TipoPgtoFinanceiroDao();
            Integer id = TApplication.getInstance().getUser().getIdvendedor();
            if (id == null)
                id = 1;
            loadPlano(tp.findAPagar(id));
            loadTipoPgto(tpfd.findAll(id));
        } catch (Exception e) {
            e.printStackTrace();
        }        
    }
}

