/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas.swing.app.auxiliar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import ritual.swing.TApplication;
import vendas.beans.Aniversariante;
import vendas.beans.AniversarioFilter;
import vendas.dao.ClienteDao;
import vendas.dao.RepresDao;
import vendas.dao.UserDao;
import vendas.dao.VendedorDao;
import vendas.entity.Comprador;
import vendas.entity.ContatoVendedor;
import vendas.entity.User;
import vendas.entity.Vendedor;
import vendas.entity.VendedorRepres;
import vendas.swing.app.cliente.CompradorEditPanel;
import vendas.swing.app.cliente.ContatoVendedorEditPanel;
import vendas.swing.app.repres.VendedorRepresPanel;
import vendas.util.EditDialog;
import vendas.util.Messages;

/**
 *
 * @author sam
 */
public class AniversarioController {

    VendedorDao vendedorDao;
    ClienteDao clienteDao;
    RepresDao represDao;

    public AniversarioController() {
        vendedorDao = (VendedorDao) TApplication.getInstance().lookupService("vendedorDao");
        represDao = (RepresDao) TApplication.getInstance().lookupService("represDao");
        clienteDao = (ClienteDao) TApplication.getInstance().lookupService("clienteDao");
    }

    public List<Aniversariante> getAniversariantes(AniversarioFilter filter) {
        List<ContatoVendedor> contatosVendedor = vendedorDao.findAniversariantes(filter);
        List<Comprador> compradores = clienteDao.findAniversariantes(filter);
        List<VendedorRepres> vendRepres = represDao.findAniversariantes(filter);
        List<Vendedor> vendedores = vendedorDao.findVendedorAniversariante(filter);
        List<Aniversariante> lista = new ArrayList();
        
        for (ContatoVendedor contato : contatosVendedor) {
            lista.add(convert(contato));
        }
        for (Comprador contato : compradores) {
            lista.add(convert(contato));
        }
        for (VendedorRepres contato : vendRepres) {
            lista.add(convert(contato));
        }
        for (Vendedor contato : vendedores) {
            lista.add(convert(contato));
        }
        
        Collections.sort(lista, new AniversarianteComparator());
        return lista;
    }

    public void remove (Aniversariante contato) throws Exception {
        if (Aniversariante.CONTATOVENDEDOR == contato.getClasse()) {
            clienteDao.deleteRow(ContatoVendedor.class, contato.getId());
        }
        if (Aniversariante.CLIENTE == contato.getClasse()) {
            clienteDao.deleteRow(Comprador.class, contato.getId());
        }
        if (Aniversariante.REPRES == contato.getClasse()) {
            clienteDao.deleteRow(VendedorRepres.class, contato.getId());
        }
    }
    
    public Aniversariante edit(Aniversariante contato) {
        if (Aniversariante.CONTATOVENDEDOR == contato.getClasse()) {
            contato = contatoVendedorEdit(getContatoVendedor(contato.getId()));
        }
        if (Aniversariante.CLIENTE == contato.getClasse()) {
            contato = contatoClienteEdit(getContatoCliente(contato.getId()));
        }
        if (Aniversariante.REPRES == contato.getClasse()) {
            contato = contatoRepresEdit(getContatoRepres(contato.getId()));
        }

        return contato;
    }

    private Aniversariante contatoVendedorEdit(ContatoVendedor contato) {
        ContatoVendedorEditPanel editPanel = new ContatoVendedorEditPanel();
        EditDialog edtDlg = new EditDialog(TApplication.getInstance().getBundle().getString("editCompradorTitle"));

        edtDlg.setEditPanel(editPanel, !TApplication.getInstance().isGrant("ALTERAR_ANIVERSARIO"));

        while (edtDlg.edit(contato)) {
            try {
                vendedorDao.updateRow(contato);
                return convert(contato);
            } catch (Exception e) {
                Messages.errorMessage(TApplication.getInstance().getBundle().getString("saveErrorMessage"));
            }
        }
        return convert(contato);
    }
    
    private Aniversariante contatoClienteEdit(Comprador comprador) {
        CompradorEditPanel editPanel = new CompradorEditPanel();
        EditDialog edtDlg = new EditDialog(TApplication.getInstance().getBundle().getString("editCompradorTitle"));

        edtDlg.setEditPanel(editPanel, !TApplication.getInstance().isGrant("ALTERAR_ANIVERSARIO"));
        
        while (edtDlg.edit(comprador)) {
            try {
                clienteDao.updateRow(comprador);
                return convert(comprador);
            } catch (Exception e) {
                Messages.errorMessage(TApplication.getInstance().getBundle().getString("saveErrorMessage"));
            }
        }
        return convert(comprador);
    }
    
        private Aniversariante contatoRepresEdit(VendedorRepres vendedor) {
        VendedorRepresPanel editPanel = new VendedorRepresPanel();
        EditDialog edtDlg = new EditDialog(TApplication.getInstance().getBundle().getString("editVendedorTitle"));

        edtDlg.setEditPanel(editPanel, !TApplication.getInstance().isGrant("ALTERAR_ANIVERSARIO"));

        while (edtDlg.edit(vendedor)) {
            try {
                represDao.updateRow(vendedor);
                return convert(vendedor);
            } catch (Exception e) {
                Messages.errorMessage(TApplication.getInstance().getBundle().getString("saveErrorMessage"));
            }
        }
        return convert(vendedor);
    }
    
    private Aniversariante convert(ContatoVendedor contato) {
        Aniversariante aniversariante = new Aniversariante();
        aniversariante.setClasse(Aniversariante.CONTATOVENDEDOR);
        aniversariante.setDtAniver(contato.getAniversario());
        aniversariante.setId(contato.getidContato());
        aniversariante.setMsn(contato.getMsn());
        aniversariante.setNome(contato.getContato());
        aniversariante.setEmail(contato.getMsn());
        aniversariante.setObservacao(contato.getObservacao());
        if ("P".equals(contato.getTipoContatoVendedor())) {
            aniversariante.setTipo("Pessoal");
        } else if ("F".equals(contato.getTipoContatoVendedor())) {
            aniversariante.setTipo("Família");
        } else {
            aniversariante.setTipo("Outros");
        }
        aniversariante.setResponsavel(contato.getVendedor().getNome());
        return aniversariante;
    }
    private Aniversariante convert(Vendedor contato) {
        Aniversariante aniversariante = new Aniversariante();
        UserDao userDao = (UserDao) TApplication.getInstance().lookupService("userDao");
        User user = userDao.getUserByVendedor(contato.getIdVendedor());
        aniversariante.setClasse(Aniversariante.VENDEDOR);
        aniversariante.setDtAniver(contato.getDtAniver());
        aniversariante.setId(contato.getIdVendedor());
        aniversariante.setMsn(user.getEmail());
        aniversariante.setNome(contato.getNome());
        aniversariante.setEmail(user.getEmail());
        aniversariante.setTipo("Vendedor");
        return aniversariante;
    }

    private Aniversariante convert(Comprador contato) {
        Aniversariante aniversariante = new Aniversariante();
        aniversariante.setClasse(Aniversariante.CLIENTE);
        aniversariante.setDtAniver(contato.getDtAniver());
        aniversariante.setId(contato.getIdComprador());
        aniversariante.setMsn(contato.getMsn());
        aniversariante.setEmail(contato.getMsn());
        aniversariante.setNome(contato.getContato());
        aniversariante.setObservacao(contato.getObservacao());
        aniversariante.setTipo("Cliente");
        aniversariante.setResponsavel(contato.getCliente().getRazao());
        return aniversariante;
    }

    private Aniversariante convert(VendedorRepres contato) {
        Aniversariante aniversariante = new Aniversariante();
        aniversariante.setClasse(Aniversariante.REPRES);
        aniversariante.setDtAniver(contato.getDtAniversario());
        aniversariante.setId(contato.getIdVendedor());
        aniversariante.setMsn(contato.getMsn());
        aniversariante.setEmail(contato.getMsn());
        aniversariante.setNome(contato.getContato());
        aniversariante.setObservacao(contato.getObservacao());
        aniversariante.setTipo("Fornecedor");
        aniversariante.setResponsavel(contato.getRepres().getRazao());
        return aniversariante;
    }

    private ContatoVendedor getContatoVendedor(Integer id) {
        try {
            return (ContatoVendedor) vendedorDao.findById(ContatoVendedor.class, id);
        } catch (Exception e) {
            return null;
        }
    }
    
    private Comprador getContatoCliente(Integer id) {
        try {
            return (Comprador) vendedorDao.findById(Comprador.class, id);
        } catch (Exception e) {
            return null;
        }
    }
    private Vendedor getVendedor(Integer id) {
        try {
            return (Vendedor) vendedorDao.findById(Vendedor.class, id);
        } catch (Exception e) {
            return null;
        }
    }
    
    private VendedorRepres getContatoRepres(Integer id) {
        try {
            return (VendedorRepres) vendedorDao.findById(VendedorRepres.class, id);
        } catch (Exception e) {
            return null;
        }
    }
}

class AniversarianteComparator implements Comparator {

    public int compare(Object obj1, Object obj2) {
        Aniversariante grupo1 = (Aniversariante) obj1;
        Aniversariante grupo2 = (Aniversariante) obj2;
        if (grupo1.getDtAniver() == null || grupo2.getDtAniver() == null)
            return 0;
        else
            return grupo1.getDtAniver().compareTo(grupo2.getDtAniver());

    }
}
