/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas.swing.app.cliente;

import java.net.URL;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.TableColumn;
import ritual.util.DateUtils;
import vendas.beans.ClientesFilter;
import vendas.dao.ClienteDao;
import ritual.swing.TApplication;
import vendas.dao.VendedorDao;
import vendas.entity.Cliente;
import vendas.entity.Vendedor;
import vendas.entity.VisitaCliente;
import vendas.exception.DAOException;
import vendas.swing.core.ServiceTableModel;
import vendas.swing.core.TableViewFrame;
import vendas.swing.model.VisitasModel;
import vendas.util.Constants;
import vendas.util.EditDialog;
import vendas.util.Messages;
import vendas.util.Reports;

/**
 *
 * @author Sam
 */
public class VisitasView extends TableViewFrame {

    TApplication app = TApplication.getInstance();
    ClienteDao dao = (ClienteDao) app.lookupService("clienteDao");

    public VisitasView() throws Exception {
        super("Visitas a clientes");
    }
    
    @Override
    public void init() {       
        setFilterObject(createFilter());
        VisitasModel model = new VisitasModel(dao.findVisitas((ClientesFilter)getFilterObject()));
        setTableModel(model);
        super.init();
        TableColumn col = getColumn(VisitasModel.CLIENTE);
        col.setPreferredWidth(1024);
        col = getColumn(VisitasModel.VENDEDOR);
        col.setPreferredWidth(180);
        col = getColumn(VisitasModel.DT_VISITA);
        col.setPreferredWidth(100);
    }

    private ClientesFilter createFilter() {
        ClientesFilter cliente = new ClientesFilter();
        
        Vendedor vendedor = new Vendedor();
        
        cliente.setDtInclusaoIni(DateUtils.getCurrentDate());
        cliente.setDtInclusaoEnd(DateUtils.getCurrentDate());
        
        if (app.getUser().isVendedor()) {
            try {
                vendedor = (Vendedor)dao.findById(Vendedor.class, app.getUser().getIdvendedor());
            } catch (DAOException ex) {
                Logger.getLogger(ClienteInternalFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            vendedor = new Vendedor();
            vendedor.setNome("TODOS");
        }
        
        cliente.setVendedor(vendedor);
        
        return cliente;
    }

    @Override
    public void doRefresh() {
        ClientesFilter filter = (ClientesFilter)getFilterObject();
        getTableModel().setItemList(filter.isSemVisita() ? dao.findSemVisitas(filter) : dao.findVisitas(filter));
        getTableModel().fireTableDataChanged();
    }

    @Override
    public void cancelFilter() {
        createFilter();
        doRefresh();
    }
   
    @Override
    public void filter() {
        getLogger().info("filter"); 
        EditDialog edtDlg = new EditDialog(getBundle().getString("filterAniversarioTitle")); 
        VisitasFilterPanel visitaPanel = new VisitasFilterPanel(); 
        visitaPanel.init(); 
        edtDlg.setEditPanel(visitaPanel); 
        ClientesFilter filter = (ClientesFilter) getFilterObject();
        while (edtDlg.edit(filter)) { 
            try { 
                getTableModel().setItemList(filter.isSemVisita() ? dao.findSemVisitas(filter) : dao.findVisitas(filter)); 
                getTableModel().fireTableDataChanged(); 
                setFilterObject(filter); 
                break; 
            } catch (Exception e) { 
                getLogger().error(getBundle().getString("findErrorMessage"), e); 
                Messages.errorMessage(getBundle().getString("findErrorMessage")); 
            } 
        }
    }

    public void executeFilter(ClientesFilter filter) {
        setFilterObject(filter);
        doRefresh();
    }
    
    @Override
    public void insert() {
        if (TApplication.getInstance().isGrant("INCLUIR_VISITA")) {
            super.insert();
        }
    }
    
    public boolean check() throws Exception {
        Date dtVisita = DateUtils.getNextDate(new Date(), -1);
        dtVisita = DateUtils.parse(DateUtils.format(dtVisita));
        
        int dia = DateUtils.getDiaSemana(dtVisita);
        
        if (dia == 1 || dia == 7) {
            return true;
        }
        
        TApplication app = TApplication.getInstance();
        ClienteDao clienteDao = (ClienteDao) app.lookupService("clienteDao");
        VisitaCliente visita = new VisitaCliente();
        Vendedor vendedor = (Vendedor)dao.findById(Vendedor.class, app.getUser().getIdvendedor());
        visita.setVendedor(vendedor);
        visita.setDtVisita(dtVisita);
        
        
        while (clienteDao.getNumVisitas(visita) == 0) {
            Object[] options = {"Justificar", "Inserir", "Cancelar"};
            int n = Messages.queryQuestion(options, "Não h\u00E1 visita registrada ontem. Você quer justificar ou inserir uma visita?");
            
            switch (n) {
                case 0:
                    registrarNaoVisita(dtVisita);
                    break;
                case 1:
                    insertRow();
                    break;
                default:
                    return false;
            }
        }

        return true;
    }
  
    public void registrarNaoVisita(Date dtVisita) {
        NaoVisitaPanel editPanel = new NaoVisitaPanel();
        editPanel.setNaoVisita(Boolean.TRUE);
        ClienteDao clienteDao = (ClienteDao) app.lookupService("clienteDao");
        VendedorDao vendedorDao = (VendedorDao) app.lookupService("vendedorDao");
        Vendedor vendedor = null;
        
        try {
            editPanel.setVendedores(vendedorDao.findAllAtivos());
            editPanel.setClientes(clienteDao.findAllAtivos());
        } catch (Exception e) {
            getLogger().error(getBundle().getString("findErrorMessage"), e);
            Messages.errorMessage(getBundle().getString("findErrorMessage"));
            return;
        }
        
        editPanel.init();
        editPanel.habilitarCliente(true);
        EditDialog edtDlg = new EditDialog("Justificar n\u00E3o visita");
        edtDlg.setEditPanel(editPanel, !TApplication.getInstance().isGrant("INCLUIR_VISITA"));

        VisitaCliente visita = new VisitaCliente();
        visita.setDtVisita(dtVisita);
        visita.setTipoVisita("N");
        
        TApplication app = TApplication.getInstance();
        
        if (app.getUser().isVendedor() || app.getUser().isAdmin()) {
            VendedorDao dao = (VendedorDao) app.lookupService("vendedorDao");
            try {
                vendedor = (Vendedor)dao.findById(Vendedor.class, app.getUser().getIdvendedor());
            } catch (Exception e) {
                getLogger().error(e);
            }
        }
        
        visita.setVendedor(vendedor);
        
        while (edtDlg.edit(visita)) {
            try {
                clienteDao.insertRecord(visita);
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }

    }
    
    @Override
    protected void insertRow() {
        NaoVisitaPanel editPanel = new NaoVisitaPanel();
        ClienteDao clienteDao = (ClienteDao) app.lookupService("clienteDao");
        VendedorDao vendedorDao = (VendedorDao) app.lookupService("vendedorDao");
        Vendedor vendedor = null;
        
        try {
            editPanel.setVendedores(vendedorDao.findAllAtivos());
            editPanel.setClientes(clienteDao.findAllAtivos());
        } catch (Exception e) {
            getLogger().error(getBundle().getString("findErrorMessage"), e);
            Messages.errorMessage(getBundle().getString("findErrorMessage"));
            return;
        }
        
        EditDialog edtDlg = new EditDialog(getBundle().getString("addVisitaTitle"));
        edtDlg.setEditPanel(editPanel, !TApplication.getInstance().isGrant("INCLUIR_VISITA"));
        //Cliente visita = new Cliente();

        VisitaCliente visita = new VisitaCliente();
        //visita.setCliente(visita);
        visita.setDtVisita(DateUtils.getCurrentDate());
        visita.setDtVisitaRealizada(visita.getDtVisita());
        
        TApplication app = TApplication.getInstance();
        
        if (app.getUser().isVendedor() || app.getUser().isAdmin()) {
            VendedorDao dao = (VendedorDao) app.lookupService("vendedorDao");
            try {
                vendedor = (Vendedor)dao.findById(Vendedor.class, app.getUser().getIdvendedor());
            } catch (Exception e) {
                getLogger().error(e);
            }
        }
        
        visita.setVendedor(vendedor);
        
        while (edtDlg.edit(visita)) {
            try {
                clienteDao.incluirVisita(visita);
                doRefresh();
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
    }

    @Override
    protected void viewRow() {
        
        NaoVisitaPanel editPanel = new NaoVisitaPanel();
        ClienteDao clienteDao = (ClienteDao) app.lookupService("clienteDao");
        VendedorDao vendedorDao = (VendedorDao) app.lookupService("vendedorDao");
        Vendedor vendedor = null;
        
        try {
            editPanel.setVendedores(vendedorDao.findAllAtivos());
            editPanel.setClientes(clienteDao.findAllAtivos());
        } catch (Exception e) {
            getLogger().error(getBundle().getString("findErrorMessage"), e);
            Messages.errorMessage(getBundle().getString("findErrorMessage"));
            return;
        }
        
        editPanel.disableDtVisita();
        EditDialog edtDlg = new EditDialog(getBundle().getString("editVisitaTitle"));
        edtDlg.setEditPanel(editPanel, !TApplication.getInstance().isGrant("ALTERAR_VISITA"));
        ServiceTableModel vtm = getTableModel();
        VisitaCliente visita = (VisitaCliente)vtm.getObject(getSelectedRow());
        //editPanel.setNaoVisita(visita.getTipoVisita().equals("N"));
        
        while (edtDlg.edit(visita)) {
            try {
                clienteDao.removeVisitas(visita);
                clienteDao.incluirVisita(visita);
                doRefresh();
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }

    }

    @Override
    public void remove() {
        ClienteDao clienteDao = (ClienteDao) app.lookupService("clienteDao");
        ServiceTableModel vtm = getTableModel();
        VisitaCliente visita = (VisitaCliente)vtm.getObject(getSelectedRow());
        if (visita.getDtVisita().compareTo(DateUtils.getCurrentDate()) >= 0) { 
            clienteDao.removeVisitas(visita);
            doRefresh();
        }
    }

    @Override
    public void showReport() {
        ClientesFilter filter = (ClientesFilter) getFilterObject();
        String reportTitle;
        String reportFile;
        if (filter.isSemVisita()) {
            reportTitle = app.getResourceString("semVisitasTitle");
            reportFile = Constants.JRSEMVISITAS;
        } else {
            reportTitle = app.getResourceString("visitasTitle");
            reportFile = Constants.JRVISITAS;
        }
        URL url;
        url = getClass().getResource(reportFile);
        
        String sub = "";
        
        if (filter.getDtInclusaoIni() != null) {
            sub += String.format("Periodo: %s a %s", DateUtils.format(filter.getDtInclusaoIni()), DateUtils.format(filter.getDtInclusaoEnd()));
        }

        try {
            Reports.showReport(reportTitle, sub, url, getTableModel().getItemList());
        } catch (Exception ex) {
            Messages.errorMessage(app.getResourceString("reportError"));
            Logger.getLogger(VisitasView.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
}
