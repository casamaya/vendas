/**
 * BancoInternalFrame.java
 *
 * Created on 22/06/2007, 15:16:29
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package vendas.swing.app.repres;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;
import vendas.swing.core.ServiceTableModel;
import vendas.swing.core.TableViewFrame;
import ritual.swing.DateCellRenderer;
import ritual.swing.FractionCellRenderer;
import ritual.swing.TApplication;
import vendas.beans.ClientesFilter;
import vendas.beans.CobrancaFilter;
import vendas.beans.EmailBean;
import vendas.dao.EmpresaDao;
import vendas.dao.UserDao;
import vendas.entity.Cliente;
import vendas.entity.ClienteRepres;
import vendas.entity.Params;
import vendas.entity.User;
import vendas.entity.Vendedor;
import vendas.swing.model.ClienteFornecedorModel;
import vendas.swing.model.CobrancaTableModel;
import vendas.util.Constants;
import vendas.util.EditDialog;
import vendas.util.EmailClientePanel;
import vendas.util.EmailUtil;
import vendas.util.Messages;
import vendas.util.Reports;

/**
 *
 * @author p993702
 */
public class ClienteRepresInternalFrame extends TableViewFrame {

    TApplication app = TApplication.getInstance();

    public ClienteRepresInternalFrame(ServiceTableModel tableModel) throws Exception {
        super(tableModel);
        setTitle(getBundle().getString("clientesRepresentadaTitle"));
    }

    @Override
    public void initComponents() {
        super.initComponents();

        JTable jt;
        final ServiceTableModel stm = getTableModel();
        jt = new JTable(stm) {

            @Override
            public Component prepareRenderer(TableCellRenderer renderer,
                    int rowIndex, int vColIndex) {
                BigDecimal value = new BigDecimal(0);
                Component c = super.prepareRenderer(renderer, rowIndex, vColIndex);
                c.setForeground(getForeground());
                ClienteRepres p = (ClienteRepres) stm.getObject(rowIndex);
                if ((vColIndex == CobrancaTableModel.CLIENTE) && "FINANCEIRO RUIM,INATIVO,FECHOU".contains(p.getCliente().getSituacaoCliente().getNome())) {
                    c.setForeground(Color.RED);
                }
                return c;
            }
        };
        jt.setDefaultRenderer(BigDecimal.class, new FractionCellRenderer(8, 2, SwingConstants.RIGHT));
        jt.setDefaultRenderer(Date.class, new DateCellRenderer());
        jt.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jt.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    viewRow();
                }
            }
        });
        setTable(jt);
        JScrollPane jsp = new JScrollPane(jt);
        getContentPane().add(jsp, BorderLayout.CENTER);
        // setSize(400, 300);
        setPreferredSize(new Dimension(800, 400));
        setFilterObject(createFilter());
        getColumn(ClienteFornecedorModel.FORNECEDOR).setPreferredWidth(220);
        getColumn(ClienteFornecedorModel.CLIENTE).setPreferredWidth(300);
        getColumn(ClienteFornecedorModel.CODIGO).setPreferredWidth(70);
        getColumn(ClienteFornecedorModel.IDENTIFICADOR).setPreferredWidth(90);
        getColumn(ClienteFornecedorModel.LIMITE).setPreferredWidth(70);
        getColumn(ClienteFornecedorModel.EMAIL).setPreferredWidth(300);

        createPopupMenu();
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
        popupMenu.addSeparator();

        JMenuItem exportMenuItem = new JMenuItem();
        exportMenuItem.setText("Exportar email");
        exportMenuItem.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                int rows[] = getTable().getSelectedRows();
                if (rows.length <= 0) {
                    Messages.warningMessage(getBundle().getString("selectMany"));
                    return;
                }

                List<String> lista = new ArrayList<>();
                ClienteRepres cliRepres;
                Cliente cli;
                List<String> tmp;

                for (int i : rows) {
                    cliRepres = (ClienteRepres) getTableModel().getObject(i);
                    cli = cliRepres.getCliente();
                    tmp = cli.getEmailMalaDireta2();

                    for (String s : tmp) {
                        lista.add(cli.getRazao() + "," + s);
                    }
                }

                StringBuilder sb = new StringBuilder();
                sb.append("Name, E-mail\n");

                for (String s : lista) {
                    sb.append(s).append("\n");
                }

                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setName("emailclientes.csv");
                if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                  
                    try {
                        PrintWriter writer = new PrintWriter(file.getPath(), "UTF-8");
                        writer.println(sb.toString());
                        writer.close();
                        Messages.infoMessage("Email exportado.");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // save to file
                }
            }
        });

        exportMenuItem.setEnabled(app.isGrant("ENVIAR_EMAIL_CLIENTE"));
        popupMenu.add(exportMenuItem);

        JMenuItem emailMenuItem = new JMenuItem();
        emailMenuItem.setText("Enviar email");
        emailMenuItem.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                int rows[] = getTable().getSelectedRows();
                if (rows.length <= 0) {
                    Messages.warningMessage(getBundle().getString("selectMany"));
                    return;
                }

                EmailBean email = new EmailBean();
                TApplication app = TApplication.getInstance();
                EditDialog edtDlg = new EditDialog(app.getResourceString("sendEmail"));
                EmailClientePanel editPanel = new EmailClientePanel();
                edtDlg.setEditPanel(editPanel);
                User user = (User) TApplication.getInstance().getUser();

                String from = user.getEmail();
                String server = user.getServerName();
                email.setServer(server);
                email.setFrom(from);
                email.setPort(user.getPort());
                email.setPassword(user.getEmailPasswd());
                email.setAssinatura(user.getAssinatura());
                List<String> erros = new ArrayList<String>();
                int enviados = 0;
                Set<Vendedor> vendedores = new HashSet<Vendedor>();
                while (edtDlg.edit(email)) {
                    email.setSubject(email.getSubject());
                    email.setText(email.getText());
                    EmailUtil eu = new EmailUtil();
                    for (int i : rows) {
                        ClienteRepres cliRepres = (ClienteRepres) getTableModel().getObject(i);
                        Cliente cli = cliRepres.getCliente();
                        email.setTo(cli.getEmailMalaDireta2());
                        vendedores.addAll(cli.getVendedores());
                        try {
                            eu.sendMail(email);
                            enviados++;
                        } catch (Exception e) {
                            erros.add(cli.getRazao());
                        }
                    }

                    UserDao ud = (UserDao) TApplication.getInstance().lookupService("userDao");
                    email.setSubject("Email para cliente: " + email.getSubject());
                    for (Vendedor v : vendedores) {
                        User u = ud.getUserByVendedor(v.getIdVendedor());
                        email.setTo(u.getEmail());
                        try {
                            eu.sendMail(email);
                            enviados++;
                        } catch (Exception e) {
                            e.printStackTrace();
                            erros.add(v.getNome());
                        }
                    }
                    EmpresaDao empresaDao = (EmpresaDao) TApplication.getInstance().lookupService("empresaDao");
                    Object o = new Integer(-1);
                    Params value = null;
                    try {
                        value = (Params) empresaDao.findById(Params.class, o);
                        email.setTo(value.getEmail());
                        eu.sendMail(email);
                    } catch (Exception e) {
                        e.printStackTrace();
                        erros.add(value.getEmail());
                    }
                    break;

                }
                if (enviados > 0) {
                    Messages.infoMessage("Mensagem enviada.");
                }
                if (erros.size() > 0) {
                    Messages.errorMessage("Erro com o email de " + erros);
                }

            }
        });
        emailMenuItem.setEnabled(app.isGrant("ENVIAR_EMAIL_CLIENTE"));
        popupMenu.add(emailMenuItem);
        setPopupMenu(popupMenu);
    }

    private ClientesFilter createFilter() {
        ClientesFilter clienteFilter = new ClientesFilter();
        clienteFilter.setOrder(1);
        return clienteFilter;
    }

    @Override
    public void cancelFilter() {
        setFilterObject(createFilter());
        doRefresh();
    }

    public void executeFilter(CobrancaFilter filtro) {
        try {
            getTableModel().select(filtro);
            setFilterObject(filtro);
            getTableModel().fireTableDataChanged();
        } catch (Exception e) {
            getLogger().error(getBundle().getString("findErrorMessage"), e);
            Messages.errorMessage(getBundle().getString("findErrorMessage"));
        }
    }

    @Override
    public void filter() {
        getLogger().info("filter");
        ClienteRepresPanel filterPanel = new ClienteRepresPanel();
        filterPanel.init();
        EditDialog edtDlg = new EditDialog(getBundle().getString("clientesRepresentadaTitle"));
        edtDlg.setEditPanel(filterPanel);
        ClientesFilter filtro = new ClientesFilter();

        while (edtDlg.edit(filtro)) {
            try {
                getTableModel().select(filtro);
                setFilterObject(filtro);
                getTableModel().fireTableDataChanged();
                break;
            } catch (Exception e) {
                getLogger().error(getBundle().getString("findErrorMessage"), e);
                Messages.errorMessage(getBundle().getString("findErrorMessage"));
            }
        }
    }

    @Override
    public void viewRow() {
    }

    @Override
    public void remove() {
    }

    @Override
    public void report() {
        Object[] options = {"Rela\u00E7\u00E3o", "Email", "Ficha cliente", "Cancelar"};
        int n = Messages.queryQuestion(options, getBundle().getString("opcoesImpressao"));

        if (n == 3) {
            return;
        }

        List lista;
        int rows[] = getTable().getSelectedRows();

        if (rows.length <= 1) {
            lista = getTableModel().getItemList();
        } else {
            lista = new ArrayList();
            ClienteRepres cliRepres;
            for (int i : rows) {
                cliRepres = (ClienteRepres) getTableModel().getObject(i);
                lista.add(cliRepres);
            }
        }

        switch (n) {
            case 0:
                relacao(lista);
                break;
            case 1:
                relacaoEmail(lista);
                break;
            default:
                fichaCliente(lista);
        }
    }

    private void relacao(List lista) {
        try {
            ClienteRepresDataSource ds = new ClienteRepresDataSource(lista);
            Map model = TApplication.getInstance().getDefaultMap(app.getResourceString("clientesRepresentadaTitle"));
            Reports.showReport(getClass().getResource(Constants.JRCLIENTESREPRES), model, ds);
        } catch (Exception e) {
            getLogger().error(e.getMessage(), e);
            Messages.errorMessage(app.getResourceString("reportError"));
        }
    }

    private void relacaoEmail(List lista) {
        URL url = getClass().getResource(Constants.JRCLIENTEREPRESEMAIL);
        String reportTitle = this.getTitle();

        try {
            Reports.showReport(reportTitle, "", url, lista);
        } catch (Exception e) {
            getLogger().error(e.getMessage(), e);
            Messages.errorMessage(app.getResourceString("reportError"));
        }
    }

    private void fichaCliente(List lista) {
        URL url = getClass().getResource(Constants.JRFICHACLIENTE2);
        Map model = TApplication.getInstance().getDefaultMap(app.getResourceString("clientesRepresentadaTitle"));
        ClienteRepresDataSource ds = new ClienteRepresDataSource(lista);
        ClientesFilter filter = (ClientesFilter) getFilterObject();
        try {
            Reports.showReport(app.getResourceString("clientesRepresentadaTitle"), filter.getTitle(), url, ds); //showReport(url, model, ds);
        } catch (Exception e) {
            getLogger().error(e.getMessage(), e);
            Messages.errorMessage(app.getResourceString("reportError"));
        }
    }
}
