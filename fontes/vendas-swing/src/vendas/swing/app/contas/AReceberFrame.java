/*
 * AReceberFrame.java
 *
 * Created on 1 de Novembro de 2007, 18:05
 */
package vendas.swing.app.contas;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.ListSelectionModel;
import ritual.swing.DateCellEditor;
import ritual.swing.TApplication;
import ritual.util.DateUtils;
import vendas.beans.CompromissoFilter;
import vendas.dao.CompromissoDao;
import vendas.entity.AReceber;
import vendas.entity.Compromisso;
import vendas.entity.Conta;
import vendas.entity.Vendedor;
import vendas.swing.core.ServiceTableModel;
import vendas.swing.core.TableViewFrame;
import vendas.util.EditDialog;
import vendas.util.Messages;
import vendas.util.Reports;
import vendas.util.VendasUtil;

/**
 *
 * @author  p993702
 */
public class AReceberFrame extends TableViewFrame {
    
    Vendedor vendedor;

    public AReceberFrame(ServiceTableModel tableModel) throws Exception {
        super("Contas a receber", tableModel);
    }

    @Override
    public void initComponents() {
        super.initComponents();
        createPopupMenu();
        formatColumns();
        getTable().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        Integer id = TApplication.getInstance().getUser().getIdvendedor();
        if (id == null)
            id = 1;
        try {
            CompromissoDao dao = (CompromissoDao) getTableModel().getDao();
            vendedor = (Vendedor)dao.findById(Vendedor.class, id);
        } catch (Exception e) {
            throw new RuntimeException("Falha geral");
        }
        resetFilterObject();
    }
    
    @Override
    public void insert() {
        if (TApplication.getInstance().isGrant("INCLUIR_CONTA_ARECEBER"))
            super.insert();
    }
    
    @Override
    public void remove() {
        if (!TApplication.getInstance().isGrant("EXCLUIR_CONTA_ARECEBER"))
            return;


        int i = getSelectedRow();
        if (i < 0) {
            Messages.warningMessage(getBundle().getString("selectOne"));
            return;
        }
        if (!Messages.deleteQuestion())
            return;
        removeRow();
    }

    @Override
    public void resetFilterObject() {
        CompromissoFilter filter = new CompromissoFilter();
        Conta conta = new Conta();
        conta.setVendedor(vendedor);
        filter.setConta(conta);
        Date dtIni = DateUtils.getFirstDate(DateUtils.parse(DateUtils.format(new Date())));
        //Date dtEnd = DateUtils.getLastDate(DateUtils.parse(DateUtils.format(new Date())));
        filter.setDtEnd(null);
        filter.setDtIni(dtIni);
        StringBuilder filterTitle = new StringBuilder();
        filterTitle.append("Vencimentos à partir de ");
        filterTitle.append(DateUtils.format(dtIni));
        filter.setTitle(filterTitle.toString());
        setFilterObject(filter);
    }

    @Override
    public void viewRow() {
        edit();
    }

    @Override
    public void insertRow() {
        AReceber conta = new AReceber();
        conta.setDtVencimento(new Date());
        conta.setValor(new BigDecimal(0));
        AReceberEditPanel editPanel = new AReceberEditPanel();
        EditDialog edtDlg = new EditDialog(getBundle().getString("addCompromissoTitle"));
        edtDlg.setEditPanel(editPanel);
        while (edtDlg.edit(conta)) {
            try {
                getTableModel().insertRecord(conta);
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
    }

    @Override
    public void editRow() {
        ServiceTableModel vtm = getTableModel();
        AReceber conta = (AReceber) vtm.getObject(getSelectedRow());
        //if (conta.getDtPgto() == null) {
        //    conta.setDtPgto(new Date());
        //}
        AReceberEditPanel editPanel = new AReceberEditPanel();
        EditDialog edtDlg = new EditDialog(getBundle().getString("editCompromissoTitle"));
        edtDlg.setEditPanel(editPanel, !TApplication.getInstance().isGrant("ALTERAR_CONTA_ARECEBER"));
        while (edtDlg.edit(conta)) {
            try {
                vtm.updateRow(conta);
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("saveErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("saveErrorMessage"));
            }
        }
    }
    @Override
    public void filter() {
        getLogger().info("filter");
        CompromissoFilterPanel filterPanel = new CompromissoFilterPanel();
                
            filterPanel.setContas(VendasUtil.getAReceberListModel());
            filterPanel.setGrupo(VendasUtil.getAReceberMasterListModel());
        EditDialog edtDlg = new EditDialog(getBundle().getString("findHintLabel"));
        edtDlg.setEditPanel(filterPanel);
        CompromissoFilter filtro = new CompromissoFilter();
        Conta conta = new Conta();
        conta.setVendedor(vendedor);
        filtro.setConta(conta);

        while (edtDlg.edit(filtro)) {
            try {
                filtro.getConta().setVendedor(vendedor);
                getTableModel().select(filtro);
                setFilterObject(filtro);
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("findErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("findErrorMessage"));
            }
        }
    }

    @Override
    public void showReport() {
        URL url;

        //Map hm = new HashMap();
        CompromissoFilter filter = (CompromissoFilter)getFilterObject();
        //hm.put("ReportTitle", getTitle());
        //hm.put("SubTitle", filter.getTitle());
        //hm.put("LogoURL", getClass().getResource("/vendas/resources/logo.gif"));
        Object[] options = {getBundle().getString("geral"), getBundle().getString("detalhado"), getBundle().getString("cancelar")};
        int n = Messages.queryQuestion(options, getBundle().getString("opcoesImpressao"));
        List lista = null;
        
        if (n == 2) {
            return;
        } else if (n == 0) {
            lista = getTableModel().getItemList();
            url = getClass().getResource("/vendas/report/AReceberReport.jasper");
        } else {
            url = getClass().getResource("/vendas/report/AReceberDetalhesReport.jasper");
            try {
                CompromissoDao dao = (CompromissoDao) getTableModel().getDao();
                lista = dao.findByExample(getFilterObject(), true);
            } catch (Exception e) {
                getLogger().error(e.getMessage(), e);
                Messages.errorMessage(getBundle().getString("reportError"));
                return;
            }
        }
        try {
            Reports.showReport(getTitle(), filter.getTitle(), url, lista);
        } catch (Exception e) {
            getLogger().error(e.getMessage(), e);
            Messages.errorMessage(getBundle().getString("reportError"));
        }
    }

    public void formatColumns() {
        getColumn(0).setPreferredWidth(80);
        getColumn(1).setPreferredWidth(140);
        getColumn(2).setPreferredWidth(140);
        getColumn(3).setPreferredWidth(88);
        getColumn(4).setPreferredWidth(140);
        getColumn(5).setPreferredWidth(95);
        getColumn(5).setCellEditor(new DateCellEditor());
        getColumn(6).setPreferredWidth(600);
    }

    private void createPopupMenu() {
        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem todosMenuItem = new JMenuItem();
        todosMenuItem.setText(getBundle().getString("selecionarTodos"));
        todosMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                getTable().selectAll();
            }
        });
        popupMenu.add(todosMenuItem);
        
        JMenuItem inverterMenuItem = new JMenuItem();
        inverterMenuItem.setText(getBundle().getString("inverterSelecao"));
        inverterMenuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                int[] selectedIndexs = getTable().getSelectedRows();
                getTable().selectAll();
                for (int i : selectedIndexs) {
                    getTable().removeRowSelectionInterval(i, i);
                }
            }
        });
        
        popupMenu.add(inverterMenuItem);
        popupMenu.addSeparator();

        JMenuItem baixarMenuItem = new JMenuItem();
        baixarMenuItem.setText(getBundle().getString("repetirLancamento"));
        baixarMenuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                int rows[] = getTable().getSelectedRows();
                if (rows.length <= 0) {
                    Messages.warningMessage(getBundle().getString("selectMany"));
                    return;
                }

                repetirLancamento();
            }
        });
        popupMenu.add(baixarMenuItem);
        setPopupMenu(popupMenu);
    }

    private void repetirLancamento() {
        String[] options = {getBundle().getString("repetir"), getBundle().getString("cancelar")};
        int month;
        RepetirPanel panel = new RepetirPanel();
        if (JOptionPane.showOptionDialog(this, panel, getBundle().getString("repetirLancamento"), JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]) == 0) {
            month = ((Integer) panel.getValue());
            List<Compromisso> lista = new ArrayList();
            int rows[] = getTable().getSelectedRows();
            for (int i : rows) {
                lista.add((Compromisso) getTableModel().getObject(i));
            }
            try {
                CompromissoDao dao = (CompromissoDao) getTableModel().getDao();
                dao.exportarLancamento(lista, month);
                refresh();
            } catch (Exception e) {
                getLogger().error(getBundle().getString("exportError"), e);
                JOptionPane.showMessageDialog(this, getBundle().getString("exportError"), getBundle().getString("errorTitle"), JOptionPane.ERROR_MESSAGE);
            }
        }

    }
}
