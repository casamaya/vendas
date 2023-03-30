/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas.swing.app.contas;

import vendas.entity.Conta;

/**
 *
 * @author Sam
 */
public class PlanoContaEditPanel extends ItemEditPanel {

    @Override
    public void object2Field(Object obj) {
        Conta tipo = (Conta) obj;
        setNome(tipo.getNome());
    }

    @Override
    public void field2Object(Object obj) {
        Conta tipo = (Conta) obj;
        tipo.setNome(getNome());
    }
}
