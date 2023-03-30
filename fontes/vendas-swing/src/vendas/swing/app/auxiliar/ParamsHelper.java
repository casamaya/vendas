/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vendas.swing.app.auxiliar;

import vendas.dao.EmpresaDao;
import ritual.swing.TApplication;
import vendas.entity.Params;
import vendas.swing.core.DesktopHelper;

/**
 *
 * @author Sam
 */
public class ParamsHelper extends DesktopHelper {
    
    public ParamsHelper() {
        super();
    }
    
    public void showParams() {
        TApplication app = TApplication.getInstance();
        EmpresaDao dao = (EmpresaDao)app.lookupService("empresaDao");
        Object o = new Integer(-1);
        Params params = null;
        try {
            params = (Params)dao.findById(Params.class, o);
        } catch (Exception e) {
            getLogger().error(e);
        }
        ConfigEditPanel editPanel = new ConfigEditPanel("Dados da empresa");
        TApplication.getInstance().getDesktopPane().add(editPanel);
        editPanel.setVisible(true);
        editPanel.setLocation(0, 0);
        editPanel.execute(params);
    }

}
