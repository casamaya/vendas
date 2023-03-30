/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DateFormatter;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jdesktop.application.Action;
import org.jdesktop.application.SingleFrameApplication;
import ritual.swing.BaseFrame;
import ritual.swing.MDIDesktopPane;
import ritual.swing.TApplication;
import ritual.swing.ViewFrame;
import ritual.util.DateUtils;
import ritual.util.NumberUtils;
import vendas.beans.AgendaFilter;
import vendas.beans.AniversarioFilter;
import vendas.beans.ClientesFilter;
import vendas.beans.CobrancaFilter;
import vendas.beans.PedidoFilter;
import vendas.beans.ResumoComissao;
import vendas.dao.ClienteDao;
import vendas.dao.PedidoDao;
import vendas.dao.ProdutoDao;
import vendas.dao.UserDao;
import vendas.entity.Cliente;
import vendas.entity.Perfil;
import vendas.entity.Produto;
import vendas.entity.User;
import vendas.entity.Vendedor;
import vendas.exception.DAOException;
import vendas.security.Crypt;
import vendas.security.EmailProfileForm;
import vendas.security.TrocaSenha;
import vendas.security.UserProfileForm;
import vendas.swing.app.auxiliar.AgendaInternalFrame;
import vendas.swing.app.auxiliar.AniversarioView;
import vendas.swing.app.auxiliar.ParamsHelper;
import vendas.swing.app.cliente.ClienteInternalFrame;
import vendas.swing.app.cliente.ClientePgtoPendenteInternalFrame;
import vendas.swing.app.cliente.ClienteViewFrame;
import vendas.swing.app.cliente.MapaVisitasView;
import vendas.swing.app.cliente.PgtosPendentesView;
import vendas.swing.app.cliente.ResumoVisitasView;
import vendas.swing.app.cliente.RotatividadeView;
import vendas.swing.app.cliente.SaldoClienteInternalFrame;
import vendas.swing.app.cliente.VisitasView;
import vendas.swing.app.contas.ContabilFrame;
import vendas.swing.app.contas.FechamentoGeralView;
import vendas.swing.app.contas.FechamentoGrupoView;
import vendas.swing.app.main.AboutBox;
import vendas.swing.app.pedido.ClienteSemVendaView;
import vendas.swing.app.pedido.ClientesGrupoView;
import vendas.swing.app.pedido.ClientesProdutoView;
import vendas.swing.app.pedido.CobrancaInternalFrame;
import vendas.swing.app.pedido.ComissaoView;
import vendas.swing.app.pedido.DescontosView;
import vendas.swing.app.pedido.MapaComissaoView;
import vendas.swing.app.pedido.NotasPedidoView;
import vendas.swing.app.pedido.PedidoDataSource;
import vendas.swing.app.pedido.PedidoInternalFrame;
import vendas.swing.app.pedido.PedidoUtil;
import vendas.swing.app.pedido.PosicaoAtendView;
import vendas.swing.app.pedido.ProdutosCompradosView;
import vendas.swing.app.pedido.ProdutosVendidosView;
import vendas.swing.app.pedido.ReciboComissaoView;
import vendas.swing.app.pedido.TotaisOPView;
import vendas.swing.app.pedido.VendasClientesView;
import vendas.swing.app.pedido.VendasGrupoProdutoView;
import vendas.swing.app.pedido.VendasSegmentoView;
import vendas.swing.app.pedido.VendasUnidadeView;
import vendas.swing.app.produto.ProdutoInternalFrame;
import vendas.swing.app.repres.RecebimentosView;
import vendas.swing.app.repres.RepresInternalFrame;
import vendas.swing.core.TableViewFrame;
import vendas.util.Constants;
import vendas.util.EditDialog;
import vendas.util.Messages;
import vendas.util.ReportViewFrame;
import vendas.util.Reports;

/**
 *
 * @author Sam
 */
public class Main extends SingleFrameApplication {

    private JEditorPane textPane;
    private MDIDesktopPane desktopPane;
    private TApplication app;
    private Logger logger;
    private Senha janela;
    private SearchBar searchBar;
    private UserDao ud;
    private JLabel comissaoLabel;
    private JLabel comissaoMeta;
    private JLabel comissaoVendedor;

    private javax.swing.Action getAction(String actionName) {

        return getContext().getActionMap().get(actionName);
    }

    private JMenu createMenu(String menuName, String[][] actionNames) {
        JMenu menu = new JMenu();
        JMenuItem menuItem;
        
        menu.setName(menuName);
        menu.setText(menuName);

        for (String[] actionName : actionNames) {
            if (actionName[0].equals("---")) {
                menu.add(new JSeparator());
            } else {
                if ((actionName[1] == null) || (app.isGrant(actionName[1]))) {
                    menuItem = new JMenuItem();
                    menuItem.setAction(getAction(actionName[0]));
                    menu.add(menuItem);
                }
            }
        }
        return menu;
    }

    private JComponent createToolBar() {
        String[] toolbarActionNames = {
            "add",
            "---",
            "open",
            "delete",
            "---",
            "refresh",
            "find",
            "cancel",
            "---",
            "print",
            "---",
            "email"
        };
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        for (String actionName : toolbarActionNames) {
            if (actionName.equals("---")) {
                toolBar.addSeparator();
            } else {
                JButton button = new JButton();
                button.setAction(getAction(actionName));
                //button.setFocusable(false);
                button.setText(null);
                toolBar.add(button);
            }
        }
        toolBar.addSeparator();
        searchBar = new SearchBar();
        searchBar.addListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                findPedido();
            }
        });

        toolBar.add(searchBar);

        toolBar.addSeparator();
        comissaoLabel = new JLabel();
        comissaoMeta = new JLabel();
        comissaoMeta.setForeground(Color.RED);
        comissaoVendedor = new JLabel();
        comissaoLabel.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    iniciarComissao();
                }
            }
        });
        comissaoVendedor.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    iniciarComissao();
                }
            }
        });

        toolBar.add(comissaoMeta);
        toolBar.addSeparator();
        toolBar.add(comissaoVendedor);
        toolBar.addSeparator();
        toolBar.add(comissaoLabel);
        toolBar.addSeparator();
        if (app.getUser().getUserName().equals("JAIME")) {
            JLabel a = new JLabel("Atualiza");
            a.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    if (evt.getClickCount() == 2) {
                        ClienteDao dao = new ClienteDao();
                        dao.atualizaAtrasos();
                    }
                }
            });
            toolBar.add(a);
        }
        return toolBar;
    }

    private JMenuBar createMenuBar() {
        String[][] fileMenuActionNames = {
            {"config", "DADOS_EMPRESA"},
            {"emailProfile", "CONFIGURAR_EMAIL"},
            {"trocaSenha", "TROCAR_SENHA"},
            {"usuarios", "USUARIOS"},
            {"permissoes", "USUARIOS"},
            {"---", null},
            {"resumo", "RESUMO"},
            {"email", "ENVIAR_EMAIL"},
            {"print", null},
            {"---", null},
            {"quit", null}
        };

        String[][] filterMenuActionNames = {{"find", null}, {"cancel", null}, {"---", null}, {"refresh", null}};
        String[][] pedidoMenuActionNames = {
            {"pedidoNew", "NOVO_PEDIDO"},
            {"pedidoFind", null},
            {"orcamento", "ORCAMENTO"},
            {"---", null},
            {"pedidoControle", "CONTROLE_PEDIDOS"},
            {"meta", "METAS_VENDAS"},
            {"---", null},
            {"pedidoComissao", "CONTROLE_COMISSOES"},
            {"mapaComissao", "MAPA_COMISSOES"},
            {"pedidoPgtoComissao", "PAGAMENTO_COMISSAO"},
            {"pedidoRecibo", "RECIBO_PAGAMENTO_COMISSAO"},
            {"---", null},
            {"pedidoAtendimento", "POSICAO_ATENDIMENTO"},
            {"totaisOp", "TOTAIS_OP"},
            {"totaisDesconto", "TOTAIS_DESCONTO"},
            {"notasPedido", "NOTAS_DE_ATENDIMENTO"}
        };
        String[][] auxiliarMenuActionNames = {
            {"bancos", "BANCOS"},
            {"transportadores", "TRANSPORTADORES"},
            {"vendedores", "VENDEDORES"},
            {"---", null},
            {"clienteSituacao", "SITUACOES_CLIENTE"},
            {"grupoClientes", "GRUPOS_DE_CLIENTES"},
            {"referencias", "REFERENCIAS"},
            {"---", null},
            {"produtoUnidade", "UNIDADES_DE_PRODUTO"},
            {"produtoGrupo", "GRUPOS_DE_PRODUTO"},
            {"---", null},
            {"segmentos", "SEGMENTOS_DE_MERCADO"},
            {"formasPgto", "FORMAS_DE_PAGAMENTO"},
            {"colaborador", "ESPECIFICADORES"},
            {"---", null},
            {"aniversario", "ANIVERSARIOS"},
            {"agenda", "AGENDA"},
            {"correio", "MENSAGENS"}
        };
        String[][] carteiraMenuActionNames = {
            {"clienteCadastro", "CARTEIRA"},
            {"clientesGrupo", "GRUPO_DE_CLIENTES"},
            {"---", null},
            {"vendasCliente", "VENDAS_POR_CLIENTE"},
            {"vendasSegmento", "VENDAS_POR_SEGMENTO"},
            {"vendasGrupoProduto", "VENDAS_POR_GRUPO_PRODUTO"},
            {"---", null},
            {"clienteSemVenda", "CLIENTES_SEM_VENDAS"},
            {"produtosComprados", "PRODUTOS_COMPRADOS"},
            {"rotatividade", "ROTATIVIDADE_DE_VENDAS"},
            {"---", null},
            {"visitaMapa", "MAPA_DE_VISITAS"},
            {"visitaReport", "RELATORIO_DE_VISITAS"},
            //{"visitaResumo", "RESUMO_DE_VISITAS"},
            {"visitaRoteiro", "ROTEIRO_DE_VENDAS"},
            {"---", null},
            {"pgtosPendentes", "PAGAMENTOS_PENDENTES"},
            {"pgtoCliente", "CONTROLE_DE_COBRANCA"},
            {"saldoCliente", "SALDO_CLIENTE"}
        };
        String[][] financeiroMenuActionNames = {
            {"planoContas", "PLANO_CONTAS"},
            {"tiposPgto", "TIPOS_DE_PAGAMENTO"},
            {"aPagar", "CONTAS_APAGAR"},
            {"aReceber", "CONTAS_ARECEBER"},
            {"---", null},
            {"grupoMovimento", "GRUPO_MOVIMENTO"},
            {"fluxo", "MOVIMENTO_FINANCEIRO"},
            {"movimentoGrupo", "MOVIMENTO_GRUPO"},
            {"fechamentoGrupo", "FECHAMENTO_GRUPO"},
            {"---", null},
            {"contabil", "CONTABIL"},
            {"fechamentoGeral", "FECHAMENTO_GERAL"}
        };
        
        String[][] represMenuActionNames = {
            {"fornecedores", "CADASTRO_DE_FORNECEDORES"},
            {"recebimentos", "RELATORIO_RECEBIMENTOS"},
            {"---", null},
            {"produtos", "PRODUTOS"},
            {"imprimirprodutos", "PRODUTOS"},
            {"maisvendidos", "PRODUTOS_MAIS_VENDIDOS"},
            {"produtosRepres", "PRODUTOS_POR_FORNECEDOR"},
            {"---", null},
            {"vendasUnidade", "VENDAS_POR_UNIDADE"},
            {"---", null},
            {"clientesProdutos", "CLIENTES_POR_PRODUTO"},
            {"clientesRepres", "CLIENTES_POR_FORNECEDOR"},
            {"clientesGrupoProdutos", "CLIENTES_POR_GRUPO_PRODUTO"}
        };
        String[][] windowMenuActionNames = {{"cascata", null}, {"tile", null}};
        String[][] helpMenuActionNames = {{"about", null}};
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createMenu("fileMenu", fileMenuActionNames));
        //menuBar.add(createMenu("findMenu", filterMenuActionNames));
        menuBar.add(createMenu("pedidoMenu", pedidoMenuActionNames));
        menuBar.add(createMenu("auxiliar", auxiliarMenuActionNames));
        menuBar.add(createMenu("carteira", carteiraMenuActionNames));
        menuBar.add(createMenu("repres", represMenuActionNames));
        menuBar.add(createMenu("financeiro", financeiroMenuActionNames));
        menuBar.add(createMenu("window", windowMenuActionNames));
        //menuBar.add(createMenu("help", helpMenuActionNames));

        return menuBar;
    }

    private JComponent createMainPanel() {
        textPane = new JTextPane();
        textPane.setName("textPane");
        JPanel panel = new JPanel(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane();
        desktopPane = new MDIDesktopPane();
        app.setDesktopPane(desktopPane);
        scrollPane.getViewport().add(desktopPane);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(createToolBar(), BorderLayout.NORTH);
        panel.setBorder(new EmptyBorder(0, 2, 2, 2)); // top, left, bottom, right
        return panel;
    }
    
    @Action
    public void fluxo() {
        if (app.getUser().isAdmin() || app.getUser().isVendedor()) {
            newInternalFrame("fluxoInternalFrame");
        }
    }
    
    @Action
    public void movimentoGrupo() {
        if (app.getUser().isAdmin() || app.getUser().isVendedor()) {
            newInternalFrame("movimentoGrupoInternalFrame");
        }
    }
    
    @Action
    public void grupoMovimento() {
        if (app.getUser().isAdmin() || app.getUser().isVendedor()) {
            newInternalFrame("grupoMovimentoInternalFrame");
        }
    }

    @Action
    public void aPagar() {
        newInternalFrame("aPagarInternalFrame");
    }

    @Action
    public void aReceber() {
        newInternalFrame("aReceberInternalFrame");
    }

    @Action
    public void planoContas() {
        newInternalFrame("contasInternalFrame");
    }

    @Action
    public void meta() {
        newInternalFrame("metasFrame");

    }

    @Action
    public void open() {
        JInternalFrame frame = desktopPane.getSelectedFrame();
        if (frame instanceof ViewFrame) {
            ((ViewFrame) frame).view();
        }
    }
    
    @Action
    public void imprimirprodutos() {
        newInternalFrame("imprimirProdutosFrame");
    }

    @Action
    public void edit() {
        JInternalFrame frame = desktopPane.getSelectedFrame();
        if (frame instanceof ViewFrame) {
            ((ViewFrame) frame).edit();
        }
    }

    @Action
    public void add() {
        JInternalFrame frame = desktopPane.getSelectedFrame();
        
        if (frame instanceof ViewFrame) {
            String title = frame.getTitle();
            
            if (app.getUser().isVendedor() || app.getUser().isPromotor()) {
                if (title.equals("Produtos por fornecedor") || title.equals("Clientes") || title.equals("Produtos") || title.equals("Produtos por fornecedor") || title.equals("Visitas a clientes") || title.equals("Agenda") || title.equals("Tipos de pagamento") || title.equals("Contas a receber") || title.equals("Contas a pagar") || title.equals("Plano de contas") || title.equals("Controle de pedidos") || frame instanceof ClienteViewFrame || title.equals("Or�amentos") || title.equals("Movimento financeiro")) {
                    ((ViewFrame) frame).insert();
                }
                if (app.getUser().isPromotor()) {
                    if (title.equals("Especificadores")) {
                        ((ViewFrame) frame).insert();
                    }
                }
            } else {
                ((ViewFrame) frame).insert();
            }
        }
    }

    @Action
    public void refresh() {
        JInternalFrame frame = desktopPane.getSelectedFrame();
        if (frame instanceof ViewFrame) {
            ((ViewFrame) frame).refresh();
        } else if (frame instanceof TableViewFrame) {
            ((TableViewFrame) frame).refresh();
        }
    }

    @Action
    public void delete() {

        JInternalFrame frame = desktopPane.getSelectedFrame();
        if (frame instanceof ViewFrame) {
            ((ViewFrame) frame).remove();
        }
    }

    @Action
    public void print() {
        JInternalFrame frame = desktopPane.getSelectedFrame();
        if (frame instanceof ViewFrame) {
            ((ViewFrame) frame).report();
        }
    }

    @Action
    public void find() {
        JInternalFrame frame = desktopPane.getSelectedFrame();
        if (frame instanceof ViewFrame) {
            ((ViewFrame) frame).filter();
        }
    }

    @Action
    public void cancel() {
        JInternalFrame frame = desktopPane.getSelectedFrame();
        if (frame instanceof ViewFrame) {
            ((ViewFrame) frame).cancelFilter();
        }
    }

    @Action
    public void email() throws Exception {
        JInternalFrame frame = desktopPane.getSelectedFrame();
        if (frame instanceof ReportViewFrame) {
            try {
                ((ReportViewFrame) frame).sendMail();
            } catch (Exception e) {
                logger.error(e);
                Messages.errorMessage("Falha ao enviar mensagem: " + e.getMessage());
            }
        } else {
            Messages.warningMessage(app.getResourceString("gerarRelatorio"));
        }
    }

    @Action
    public void quit() {
        exitApp();
    }
    
    @Action
    public void fechamentoGeral() {
        newInternalFrame(new FechamentoGeralView());
    }
    
    @Action
    public void fechamentoGrupo() {
        newInternalFrame(new FechamentoGrupoView());
    }

    @Action
    public void user() {
        UserProfileForm form = new UserProfileForm();
        boolean result = false;
        EditDialog edtDlg = new EditDialog(TApplication.getInstance().getBundle().getString("newPassword"));
        User user = (User) TApplication.getInstance().getUser();
        edtDlg.setEditPanel(form);
        while (edtDlg.edit(user)) {
            if (user.getPasswd().equals(user.getRepeatPasswd())) {
                try {
                    ud.updateRow(user);
                    app.setUser(user);
                    break;
                } catch (Exception e) {
                    Messages.errorMessage("Falha ao salvar");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Senha n\u00E3o confere");
            }
        }
    }

    @Action
    public void config() {
        ParamsHelper ph = new ParamsHelper();
        ph.showParams();
    }

    @Action
    public void pedidoNew() {
        PedidoUtil util = new PedidoUtil();
        util.novoPedido();
    }

    @Action
    public void pedidoFind() {
        PedidoUtil util = new PedidoUtil();
        util.viewPedido();
    }

    @Action
    public void findPedido() {
        String value = searchBar.getValue();
        if (value == null) {
            return;
        }
        if (searchBar.getType() == 1) {
            PedidoUtil util = new PedidoUtil();
            try {
                util.viewPedido(Integer.decode(value), this);
            } catch (Exception e) {
                Messages.errorMessage("Falha ao carregar pedido");
            }
            searchBar.setValue(null);
        }
        if (searchBar.getType() == 2) {
            PedidoInternalFrame frame = (PedidoInternalFrame) app.lookupService("pedidoInternalFrame");
            newInternalFrame(frame);
            frame.searchNota(value.toUpperCase());
            frame.doRefresh();
        }
        if (searchBar.getType() == 3) {
            ClienteInternalFrame frame = (ClienteInternalFrame) app.lookupService("clienteInternalFrame");
            newInternalFrame(frame);
            frame.executeFilter(value.toUpperCase());
        }
        if (searchBar.getType() == 4) {
            ProdutoInternalFrame frame = (ProdutoInternalFrame) app.lookupService("produtoInternalFrame");
            newInternalFrame(frame);
            Produto produto = new Produto();
            try {
                produto.setIdProduto(new Integer(value));
            } catch (Exception e) {
                produto.setDescricao(value.toUpperCase());
            }
            frame.executeFilter(produto);
        }
        if (searchBar.getType() == 5) {
            RepresInternalFrame frame = (RepresInternalFrame) app.lookupService("represInternalFrame");
            newInternalFrame(frame);
            frame.executeFilter(value.toUpperCase());
        }
    }
    
    @Action
    public void agenda() {
        newInternalFrame("agendaInternalFrame");
    }

    @Action
    public void aniversario() {
        newInternalFrame("aniversarianteInternalFrame");
    }
    @Action
    public void orcamento() {
        newInternalFrame("orcamentoInternalFrame");
    }

    @Action
    public void pedidoControle() {
        newInternalFrame("pedidoInternalFrame");
    }

    @Action
    public void pedidoComissao() {
        newInternalFrame(new ComissaoView());
    }

    @Action
    public void pedidoPgtoComissao() {
        if (app.getUser().isAdmin())
            newInternalFrame("pgtoComissaoFrame");
    }

    @Action
    public void mapaComissao() {
        if (app.getUser().isAdmin())
            newInternalFrame(new MapaComissaoView());
    }

    @Action
    public void pedidoRecibo() {
        if (app.getUser().isAdmin())
            newInternalFrame(new ReciboComissaoView());
    }

    @Action
    public void pedidoAtendimento() {
        newInternalFrame(new PosicaoAtendView());
    }

    @Action
    public void vendasSegmento() {
        newInternalFrame(new VendasSegmentoView());
    }
    @Action
    public void vendasGrupoProduto() {
        newInternalFrame(new VendasGrupoProdutoView());
    }

    @Action
    public void notasPedido() {
        newInternalFrame(new NotasPedidoView());
    }

    @Action
    public void bancos() {
        newInternalFrame("bancoInternalFrame");
    }

    @Action
    public void transportadores() {
        newInternalFrame("transportadorInternalFrame");
    }

    @Action
    public void vendedores() {
        newInternalFrame("vendedorInternalFrame");
    }
    @Action
    public void usuarios() {
        if (app.getUser().isAdmin())
            newInternalFrame("userInternalFrame");
    }

    @Action
    public void permissoes() {
        if (app.getUser().isAdmin())
            newInternalFrame("recursosInternalFrame");
    }
    
    @Action
    public void clienteSituacao() {
        newInternalFrame("situacaoClienteInternalFrame");
    }
    
    @Action
    public void colaborador() {
        newInternalFrame("colaboradorInternalFrame");
    }

    @Action
    public void grupoClientes() {
        newInternalFrame("grupoClienteInternalFrame");
    }

    @Action
    public void produtoUnidade() {
        newInternalFrame("undProdutoInternalFrame");
    }

    @Action
    public void produtoGrupo() {
        newInternalFrame("grupoProdutoInternalFrame");
    }

    @Action
    public void segmentos() {
        newInternalFrame("segMercadoInternalFrame");
    }
    @Action
    public void referencias() {
        newInternalFrame("referenciaInternalFrame");
    }

    @Action
    public void formasPgto() {
        newInternalFrame("formaPgtoInternalFrame");
    }

    @Action
    public void tiposPgto() {
        newInternalFrame("tipoPgtoFinancInternalFrame");
    }

    @Action
    public void clienteCadastro() {
        newInternalFrame("clienteInternalFrame");
    }

    @Action
    public void correio() {
        newInternalFrame("correioInternalFrame");
    }

    @Action
    public void clientesGrupo() {
        newInternalFrame("grupoClienteFrame");
    }

    @Action
    public void vendasCliente() {
        newInternalFrame(new VendasClientesView());
    }

    @Action
    public void totaisOp() {
        newInternalFrame(new TotaisOPView());
    }
    @Action
    public void totaisDesconto() {
        newInternalFrame(new DescontosView());
    }

    @Action
    public void clienteSemVenda() {
        newInternalFrame(new ClienteSemVendaView());
    }

    @Action
    public void contabil() {
        if (app.getUser().isAdmin() || app.getUser().isEscritorio())
            newInternalFrame(new ContabilFrame());
    }

    @Action
    public void produtosComprados() {
        newInternalFrame(ProdutosCompradosView.class);
    }

    @Action
    public void maisvendidos() {
        newInternalFrame(ProdutosVendidosView.class);
    }

    @Action
    public void rotatividade() {
        newInternalFrame(new RotatividadeView());
    }

    @Action
    public void visitaMapa() {
        newInternalFrame(new MapaVisitasView());
    }

    @Action
    public void visitaReport() {
        newInternalFrame("visitasView");
    }

    @Action
    public void visitaResumo() {
        newInternalFrame(new ResumoVisitasView());
    }
    
    @Action
    public void visitaRoteiro() {
        newInternalFrame("roteiroClienteFrame");
    }

    @Action
    public void pgtoCliente() {
        newInternalFrame("cobrancaInternalFrame");

    }

    @Action
    public void saldoCliente() {
        newInternalFrame("saldoClienteInternalFrame");

    }
    
    @Action
    public void pgtosPendentes() {
        newInternalFrame(new PgtosPendentesView());
    }

    @Action
    public void fornecedores() {
        newInternalFrame("represInternalFrame");
    }

    @Action
    public void recebimentos() {
        newInternalFrame(new RecebimentosView());
    }

    @Action
    public void produtos() {
        newInternalFrame("produtoInternalFrame");
    }

    @Action
    public void produtosRepres() {
        newInternalFrame("produtoRepresInternalFrame");
    }

    @Action
    public void vendasUnidade() {
        newInternalFrame(new VendasUnidadeView());
    }

    @Action
    public void clientesProdutos() {
        newInternalFrame(new ClientesProdutoView());
    }

    @Action
    public void clientesRepres() {
        //newInternalFrame(new ClienteRepresView());
        newInternalFrame("clienteRepresInternalFrame");
    }

    @Action
    public void clientesGrupoProdutos() {
        newInternalFrame(new ClientesGrupoView());
    }

    @Action
    public void cascata() {
        desktopPane.cascadeFrames();
    }

    @Action
    public void tile() {
        desktopPane.tileFrames();
    }

    @Action
    public void about() {
        showAboutBox();
    }

    @Action
    public void trocaSenha() {
        TrocaSenha senha = new TrocaSenha();
        senha.trocaSenha();
    }

    @Action
    public void emailProfile() {
        EmailProfileForm form = new EmailProfileForm();
        EditDialog edtDlg = new EditDialog(TApplication.getInstance().getBundle().getString("emailConfig"));
        User user = (User) TApplication.getInstance().getUser();
        edtDlg.setEditPanel(form);
        while (edtDlg.edit(user)) {
            try {
                ud.updateRow(user);
                app.setUser(user);
                break;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Falha ao salvar");
            }
        }
    }

    public void newInternalFrame(final String frameName) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                final ViewFrame frame = (ViewFrame) app.lookupService(frameName);
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        if (!app.locateFrame(frame.getTitle())) {
                           desktopPane.add(frame);
                           frame.setLocation(0,0);
                           if ("Movimento financeiro".equals(frame.getTitle())) {
                               frame.setSize(app.getDesktopPane().getBounds().width, app.getDesktopPane().getBounds().height);
                           }
                                   
                        }
                        frame.setVisible(true);
                    }
                });
            }
        }).start();
    }

    private void newInternalFrame(final Class a) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    final BaseFrame frame = (BaseFrame) a.newInstance();
                    SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            desktopPane.add(frame);
                            frame.setLocation(0,0);
                            frame.setVisible(true);
                        }
                    });
                } catch (Exception e) {
                }
            }
        }).start();
    }

    public void newInternalFrame(final BaseFrame frame) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!app.locateFrame(frame.getTitle())) {
                    desktopPane.add(frame);
                    frame.setLocation(0,0);
                }
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        frame.setVisible(true);
                    }
                });
            }
        }).start();
    }

    private void exitApp() {
        System.exit(0);
    }

    public void showAboutBox() {
        AboutBox dlg = new AboutBox(getMainFrame());
        Dimension dlgSize = dlg.getPreferredSize();
        Dimension frmSize = getMainFrame().getSize();
        Point loc = getMainFrame().getLocation();
        dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
        dlg.setModal(true);
        dlg.pack();
        dlg.setVisible(true);
    }

    private void initApp() {
        ud = (UserDao) TApplication.getInstance().lookupService("userDao");
        try {
            ud.findById(User.class, "USER");
        } catch (Exception e) {
            logger.error(e);
        }
    }

    @Override
    protected void startup() {
        URL url = getClass().getResource("/vendas/log4j.properties");
        PropertyConfigurator.configure(url);
        app = TApplication.getInstance();
        logger = Logger.getLogger(getClass());
        app.setResourcePath(Constants.BUNDLE);
        app.setLogoPath(Constants.LOGOFILE);
        app.setFactoryPath(Constants.FACTORYXML);
        initApp();
        PedidoUtil pedidoUtil = new PedidoUtil();
        //pedidoUtil.enviarEmailClienteSemPedidoPorPrazo();
        pedidoUtil.excluirAnexosPedido();
        try {
            for (LookAndFeelInfo laf : UIManager.getInstalledLookAndFeels()) {
                //logger.info(laf.getName());
//                if ("mac os x".equals(laf.getName().toLowerCase())) {
                if ("metal".equals(laf.getName().toLowerCase())) {
//                if ("Nimbus".equals(laf.getName().toLowerCase())) {
                    UIManager.setLookAndFeel(laf.getClassName());
                    break;
                }
            }
        } catch (UnsupportedLookAndFeelException e) {
            // handle exception
        } catch (ClassNotFoundException e) {
            // handle exception
        } catch (InstantiationException e) {
            // handle exception
        } catch (IllegalAccessException e) {
            // handle exception
        }
        app.setMainFrame(getMainFrame());
        autenticar();
        getMainFrame().setJMenuBar(createMenuBar());
        show(createMainPanel());
        
        if (!app.getUser().getUserName().equals("JAIME")) {
            resumo(); 
        }
        
        if (app.getUser().isAdmin()) {
            verBloqueados();
        }
    }
    
    private void verBloqueados() {
        StringBuilder sb = new StringBuilder();
        ProdutoDao dao = (ProdutoDao)TApplication.getInstance().lookupService("produtoDao");
        boolean temProduto = dao.temProdutosBloqueados();
        
        if (temProduto) {
            sb.append("H\u00E1 produtos aguardando autoriza\u00E7\u00E3o.");
        }
        
        ClienteDao clidao = (ClienteDao)TApplication.getInstance().lookupService("clienteDao");
        boolean temCliente = clidao.temClientesBloqueados();
        
        if (temCliente) {
            sb.append("H\\u00E1 clientes aguardando autoriza\u00E7\u00E3o.");
        }
        
        if (temProduto || temCliente) {
            Messages.warningMessage(sb.toString());
        }
    }
    
    private boolean registraVisita() {
        VisitasView frame = (VisitasView) app.lookupService("visitasView");
        boolean result;
        try {
            result = frame.check();
        } catch (Exception e) {
            result = false;
        }
        return result;
    }

    /*private void iniciarVisitas() {
        VisitasUtil vu = new VisitasUtil();
        Date d = DateUtils.getDate();
            if (d == null)
                d = new Date();
        String s = DateUtils.format(d);
        vu.showVisitasDia(DateUtils.parse(s));
    }*/

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        launch(Main.class, args);
    }

    private void autenticar() {
        janela = new Senha(null, "Nome de Usu\u00E1rio e Senha", true);
        janela.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        janela.setVisible(true);
    }

    public void verificar(String u, String s) {
        boolean result = true;
        String msg = "Dados Incorretos.";
        if ((u == null || u.length() == 0) || (s == null || s.length() == 0)) {
            msg = "Usu\u00E1rio e senha devem ser informados";
            result = false;
        } else {
            try {
                User user = (User) ud.findById(User.class, u);
                
                if (user == null) {
                    result = false;
                    msg = "Usu\u00E1rio inv\u00E1lido";
                } else if (user.getAtivo().equals("I")) {
                    result = false;
                    msg = "Usu\u00E1rio inativo";
                } else if (user.getPasswd() == null) {
                    result = newPassword(user, s);
                }
                if (result && Crypt.encrypt(u, s).equals(user.getPasswd())) {
                    result = true;
                    List<Perfil> perfil = user.getPerfis();
                    
                    if ( perfil == null || perfil.isEmpty()) {
                        result = false;
                        msg = "Usu\u00E1rio sem perfil.";
                    }
                    if (result) {
                        janela.setVisible(false);
                        app.setUser(user);
                    }
                } else {
                    msg = "Usu\u00E1rio e/ou Senha inv\u00E1lidos";
                    result = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                result = false;
            }
        }
        if (!result) {
            JOptionPane.showMessageDialog(null, msg);
            janela.txtUsuario.requestFocus();
        } 

    }

    private boolean newPassword(User user, String s) throws Exception {
        UserProfileForm form = new UserProfileForm();
        boolean result = false;
        EditDialog edtDlg = new EditDialog("Nova senha");
        user.setPasswd(s);
        edtDlg.setEditPanel(form);
        while (edtDlg.edit(user)) {
            if (user.getPasswd().equals(user.getRepeatPasswd())) {
                ud.updateRow(user);
                result = true;
                break;
            } else {
                JOptionPane.showMessageDialog(null, "Senha n\u00E3o confere");
            }
        }
        return result;
    }

    @Action
    public void resumo() {
        try {
        for (JInternalFrame frame : desktopPane.getAllFrames()) {
            frame.setClosed(true);
        }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        List<JInternalFrame> frames = new ArrayList<>();
        
//        if (app.getUser().isEscritorio() || app.getUser().isAdmin()) {
//            PedidoDao dao = (PedidoDao) TApplication.getInstance().lookupService("pedidoDao");
//            dao.baixaPgtoCliente("BO");
//        }
        
        try {
            frames.add(iniciarPedidosNaoEnviados()); //e em atraso
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Messages.errorMessage("Falha ao iniciar pedidos n\u00E3o enviados");
        }

        try {
            iniciarComissao();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Messages.errorMessage("Falha ao iniciar comiss�o");
        }

        try {
            frames.add(iniciarAniversarios());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Messages.errorMessage("Falha ao iniciar anivers�rios");
        }
        
        /* try {
            frames.add(iniciarVisitas());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Messages.errorMessage("Falha ao iniciar visitas");
        } */
        
        try {
            iniciarMeta();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Messages.errorMessage("Falha ao iniciar metas");
        }
/*
        try {
            frames.add(iniciarClientesPgtoPendente());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Messages.errorMessage("Falha ao iniciar clientes pendentes");
        }
        if (Constants.ESCRITORIO.equals(app.getUser().getPerfil())) {
            try {
                frames.add(iniciarPendentes());
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                Messages.errorMessage("Falha ao iniciar posi\u00E7\u00E3o de atendimento");
            }
        }*/
        
        try {
            //iniciarPendentes();
            frames.add(iniciarCobranca());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Messages.errorMessage("Falha ao iniciar cobran�a");
        }

        try {
            frames.add(iniciarSaldoCliente());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Messages.errorMessage("Falha ao iniciar saldo de cobran�a");
        }
        
        try {
            frames.add(iniciarAgenda());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Messages.errorMessage("Falha ao iniciar agenda");
        }
        
        desktopPane.tileFrames(frames);

    }
    
    private void iniciarMensagem() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    correio();
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    Messages.errorMessage("Falha ao iniciar correio");
                }
            }
        }).start();
    }

    private void iniciarMeta() {
        PedidoDao dao = (PedidoDao) TApplication.getInstance().lookupService("pedidoDao");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM");
        DateFormatter df = new DateFormatter(formatter);
        Date d = DateUtils.getDate();
            if (d == null)
                d = new Date();
        String anoMes = formatter.format(d);
        BigDecimal meta = dao.getMeta(anoMes);
        if (meta != null) {
            comissaoMeta.setText(NumberUtils.format(meta, 10, 2) + "(M)");
            comissaoMeta.invalidate();
            comissaoMeta.repaint();
        }

    }
    
    private JInternalFrame iniciarClientesPgtoPendente() {
        ClientePgtoPendenteInternalFrame frame = (ClientePgtoPendenteInternalFrame) app.lookupService("clientePgtoPendenteInternalFrame");
        newInternalFrame(frame);
        frame.refresh();
        return frame;
    }

    private BaseFrame iniciarAniversarios() {
        AniversarioFilter filter = new AniversarioFilter();
        AniversarioView frame = (AniversarioView) app.lookupService("aniversarianteInternalFrame");
        newInternalFrame(frame);
        if (app.getUser().isVendedor()) {
            Vendedor vendedor = new Vendedor();
            vendedor.setIdVendedor(app.getUser().getIdvendedor());
            filter.setVendedor(vendedor);
        }
        frame.executeFilter(filter);
        return frame;

    }
    
    private BaseFrame iniciarVisitas() {
        ClientesFilter filter = new ClientesFilter();
        VisitasView frame = (VisitasView) app.lookupService("visitasView");
        ClienteDao dao = (ClienteDao)app.lookupService("clienteDao");
        Vendedor vendedor = new Vendedor();
        
        newInternalFrame(frame);

        if (app.getUser().isVendedor()) {
            try {
                vendedor = (Vendedor)dao.findById(Vendedor.class, app.getUser().getIdvendedor());
            } catch (DAOException ex) {
                java.util.logging.Logger.getLogger(ClienteInternalFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            vendedor.setNome("TODOS");
        }
        
        filter.setDtInclusaoIni(DateUtils.getCurrentDate());
        filter.setDtInclusaoEnd(DateUtils.getCurrentDate());
        filter.setVendedor(vendedor);
        frame.executeFilter(filter);
        
        return frame;
    }

    private void iniciarPrePedidos() {
        PedidoInternalFrame frame = (PedidoInternalFrame) app.lookupService("pedidoInternalFrame");
        newInternalFrame(frame);
        PedidoFilter filtro = new PedidoFilter();
        filtro.setSituacao("T");
        filtro.setPrePedido(Boolean.TRUE);
        frame.executeFilter(filtro);

    }

    private JInternalFrame iniciarPedidosNaoEnviados() {
        logger.info("iniciarPedidosNaoEnviados");
        PedidoInternalFrame frame = (PedidoInternalFrame) app.lookupService("pedidoInternalFrame");
        newInternalFrame(frame);
        PedidoFilter filtro = new PedidoFilter();
        filtro.setSituacao("E");
        filtro.setPrePedido(true);
        filtro.setEmissao(2);
        if (app.getUser().isVendedor()) {
            Vendedor vendedor = new Vendedor();
            vendedor.setIdVendedor(app.getUser().getIdvendedor());
            filtro.setVendedor(vendedor);
        }
        frame.executeFilter(filtro);
        return frame;
    }
    
    private JInternalFrame iniciarEmAtraso() {
        logger.info("iniciarEmAtraso");
        PedidoInternalFrame frame = (PedidoInternalFrame) app.lookupService("pedidoInternalFrame");
        newInternalFrame(frame);
        PedidoFilter filtro = new PedidoFilter();
        filtro.setSituacao("E");
        filtro.setPrePedido(true);
        frame.executeFilter(filtro);
        return frame;
    }
    
    private JInternalFrame iniciarAgenda() {
        logger.info("iniciarAgenda");
        AgendaInternalFrame frame = (AgendaInternalFrame) app.lookupService("agendaInternalFrame");
        newInternalFrame(frame);
        AgendaFilter filtro = new AgendaFilter();
        frame.executeFilter(filtro);
        return frame;
    }

    private void iniciarComissao() {
//        if (!app.getUser().isVendedor() && !app.getUser().getPerfil().equals(Constants.ADMIN)) {
//            return;
//        }
        PedidoFilter pedidoFilter = new PedidoFilter();
        pedidoFilter.setVendedor(null);
        try {
            Date d = DateUtils.getDate();
            if (d == null)
                d = new Date();
            String s = DateUtils.format(d);
            pedidoFilter.setDtEmissaoIni(DateUtils.getFirstDate(DateUtils.parse(s)));
            pedidoFilter.setDtEmissaoEnd(DateUtils.parse(DateUtils.format(d)));
        } catch (Exception e) {
            e.printStackTrace();
            pedidoFilter.setDtEmissaoIni(new Date());
            pedidoFilter.setDtEmissaoEnd(new Date());
        }
        pedidoFilter.setSituacao("T");
        //if (app.getUser().isVendedor()) {
        //    Vendedor vendedor = new Vendedor();
        //    vendedor.setIdVendedor(app.getUser().getIdvendedor());
        //    pedidoFilter.setVendedor(vendedor);
        //}
        PedidoDao pedidoDao = (PedidoDao) app.lookupService("pedidoDao");
        List[] demonstrativo = pedidoDao.findComissaoResumo(pedidoFilter);
        List<ResumoComissao> lista = demonstrativo[0];
        BigDecimal valor = new BigDecimal(0);
        BigDecimal valorVendedor;
        HashMap<String, BigDecimal> maps = new HashMap<>();
        
        if (app.getUser().isAdmin() || app.getUser().isEscritorio()) {
            for (ResumoComissao resumo : lista) {
                if (maps.containsKey(resumo.getVendedor())) {
                    valorVendedor = maps.get(resumo.getVendedor());
                    valorVendedor = valorVendedor.add(resumo.getComissaoVendedor());
                    maps.put(resumo.getVendedor(), valorVendedor);
                } else {
                    maps.put(resumo.getVendedor(), resumo.getComissaoVendedor());
                }
                valor = valor.add(resumo.getComissaoTotal());
            }
            Iterator iterator = maps.keySet().iterator();
            StringBuilder sb = new StringBuilder();
            if (app.getUser().isAdmin()) {
                while (iterator.hasNext()) {
                    String s = (String) iterator.next();
                    sb.append(NumberUtils.format(maps.get(s), 10, 2)).append("(").append(s.substring(0, 2)).append(") ");
                }
            }
            comissaoVendedor.setText(sb.toString());
        } else {
            String nome = "";
            if (app.getUser().getIdvendedor() != null) {
                try {
                    Vendedor vendedor = (Vendedor) pedidoDao.findById(Vendedor.class, app.getUser().getIdvendedor());
                    nome = vendedor.getNome();
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
            valorVendedor = new BigDecimal(0);
            for (ResumoComissao resumo : lista) {
                if (nome.equals(resumo.getVendedor())) {
                    valorVendedor = valorVendedor.add(resumo.getComissaoVendedor());
                }
                valor = valor.add(resumo.getComissaoTotal());
            }
            comissaoVendedor.setText(NumberUtils.format(valorVendedor, 10, 2));
        }

        if (app.getUser().isAdmin() || app.getUser().isEscritorio()) {
        comissaoLabel.setText(NumberUtils.format(valor, 10, 2));
        comissaoLabel.invalidate();
        comissaoLabel.repaint();
        }
        comissaoVendedor.invalidate();
        comissaoVendedor.repaint();
    }

    private void iniciarVisitasClientes() {
        ClientesFilter clienteFilter = new ClientesFilter();
        clienteFilter.setDtInclusaoIni(DateUtils.getCurrentDate());
        clienteFilter.setDtInclusaoEnd(DateUtils.getCurrentDate());
        StringBuilder sb = new StringBuilder();
        sb.append("Per\u00EDodo: ");
        sb.append(DateUtils.format(clienteFilter.getDtInclusaoIni()));
        sb.append(" - ");
        sb.append(DateUtils.format(clienteFilter.getDtInclusaoEnd()));
        clienteFilter.setTitle(sb.toString());
        ClienteDao pedidoDao = (ClienteDao) TApplication.getInstance().lookupService("clienteDao");
        if (app.getUser().isVendedor()) {
            Cliente cliente = new Cliente();
            Vendedor vendedor = new Vendedor();
            vendedor.setIdVendedor(app.getUser().getIdvendedor());
            clienteFilter.setVendedor(vendedor);
            clienteFilter.setCliente(cliente);
        }
        clienteFilter.setSemVisita(false);
        //String reportTitle = app.getResourceString("semVisitasTitle");
        String reportTitle = app.getResourceString("visitasTitle");

        //String reportFile = Constants.JRSEMVISITAS;
        String reportFile = Constants.JRVISITAS;
        try {
            List lista;
            //lista = pedidoDao.findSemVisitas(pedidoFilter);
            lista = pedidoDao.findVisitas(clienteFilter);
            if ((lista == null) || lista.isEmpty()) {
                //Messages.errorMessage(app.getResourceString("clientesSemVisitas"));
            } else {
                showReport(reportTitle, clienteFilter.getTitle(), reportFile, lista);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Messages.errorMessage(app.getResourceString("reportError"));
        }
    }

    private void showReport(String title, String subTitle, String reportFile, List lista) throws Exception {
        URL url = getClass().getResource(reportFile);
        Map model = app.getDefaultMap(title, subTitle);
        int mes = DateUtils.getMonth(DateUtils.getDate());
        model.put("titles", DateUtils.getLastMonths(mes));
        Reports.showReport(url, model, lista);
    }

    private JInternalFrame iniciarPendentes() throws DAOException {
        PedidoFilter pedidoFilter = new PedidoFilter();
        pedidoFilter.setSituacao("P");
        pedidoFilter.setOrdem(2);

        PedidoDao pedidoDao = (PedidoDao) app.lookupService("pedidoDao");
        if (!app.getUser().isEscritorio()) {
            if (app.getUser().isVendedor()) {
                Vendedor vendedor = (Vendedor) pedidoDao.findById(Vendedor.class, app.getUser().getIdvendedor());
                pedidoFilter.setVendedor(vendedor);
                StringBuilder sb = new StringBuilder();
                sb.append("Vendedor ").append(vendedor.getNome()).append(". ");
                pedidoFilter.setTitle(sb.toString());
            }

        }
        try {
            List lista = pedidoDao.findPosicaoAtend(pedidoFilter);
            if ((lista != null) && !lista.isEmpty()) {
                PedidoDataSource ds = new PedidoDataSource(lista);
                Map model = TApplication.getInstance().getDefaultMap(app.getResourceString("posicaoAtendTitle"), pedidoFilter.getTitle());
                model.put("quebraDtEntrega", pedidoFilter.getTotalizaEntrega().booleanValue());
                return Reports.showReport(getClass().getResource(Constants.JRATENDIMENTOS), model, ds);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Messages.errorMessage(app.getResourceString("reportError"));
        }
        return null;
    }

    private JInternalFrame iniciarCobranca() throws DAOException {
        CobrancaInternalFrame frame = (CobrancaInternalFrame) app.lookupService("cobrancaInternalFrame");
        newInternalFrame(frame);
        CobrancaFilter cobrancaFilter = new CobrancaFilter();
        cobrancaFilter.setSituacao("E");
        cobrancaFilter.setOrdem(0);
        PedidoDao pedidoDao = (PedidoDao) app.lookupService("pedidoDao");

        StringBuilder sb = new StringBuilder();
        sb.append("Em atraso. ");
        if (app.getUser().isVendedor()) {
            Vendedor vendedor = (Vendedor) pedidoDao.findById(Vendedor.class, app.getUser().getIdvendedor());
            cobrancaFilter.setVendedor(vendedor);
        }

        cobrancaFilter.setTitle(sb.toString());
        frame.executeFilter(cobrancaFilter);
        return frame;
    }

    private JInternalFrame iniciarSaldoCliente() throws DAOException {
        SaldoClienteInternalFrame frame = (SaldoClienteInternalFrame) app.lookupService("saldoClienteInternalFrame");
        newInternalFrame(frame);
        CobrancaFilter cobrancaFilter = new CobrancaFilter();
        cobrancaFilter.setSituacao("N");
        cobrancaFilter.setOrdem(0);
        PedidoDao pedidoDao = (PedidoDao) app.lookupService("pedidoDao");

        if (app.getUser().isVendedor()) {
            Vendedor vendedor = (Vendedor) pedidoDao.findById(Vendedor.class, app.getUser().getIdvendedor());
            cobrancaFilter.setVendedor(vendedor);
        }

        cobrancaFilter.setTitle("Saldo de cobran�as");
        frame.executeFilter(cobrancaFilter);
        return frame;
    }

    private class Senha extends JDialog {

        JTextField txtUsuario;
        JPasswordField txtSenha;
        JButton entrar, cancelar;

        public Senha(Frame owner, String title, boolean modal) {
            super(owner, title, modal);

            Container tela = getContentPane();

            BorderLayout layout = new BorderLayout();
            tela.setLayout(layout);

            JLabel lblUsuario = new JLabel("Nome do Usu\u00E1rio:");
            JLabel lblSenha = new JLabel("Senha:");
            txtUsuario = new JTextField(10);
            txtSenha = new JPasswordField(10);

            JPanel superior = new JPanel();
            superior.setLayout(new GridLayout(2, 2, 5, 5));
            superior.add(lblUsuario);
            superior.add(txtUsuario);
            superior.add(lblSenha);
            superior.add(txtSenha);

            JPanel superior2 = new JPanel();

            String titulo = "Informe o nome de usu\u00E1rio e senha";
            Border etched = BorderFactory.createEtchedBorder();
            Border borda = BorderFactory.createTitledBorder(etched, titulo);

            superior2.setBorder(borda);
            superior2.setLayout(new FlowLayout(FlowLayout.LEFT));
            superior2.add(superior);

            Tratador trat = new Tratador();

            entrar = new JButton("Entrar");
            entrar.addActionListener(trat);
            getRootPane().setDefaultButton(entrar);

            cancelar = new JButton("Cancelar");
            cancelar.addActionListener(trat);

            JPanel inferior = new JPanel();
            inferior.setLayout(new FlowLayout(FlowLayout.RIGHT));
            inferior.add(entrar);
            inferior.add(cancelar);

            tela.add(BorderLayout.NORTH, superior2);
            tela.add(BorderLayout.SOUTH, inferior);

            setSize(280, 150);
            setLocationRelativeTo(null);
        }

        private class Tratador implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent e) {
                String senha = new String(txtSenha.getPassword());

                if (e.getSource() == entrar) {
                    verificar(txtUsuario.getText().toUpperCase(), senha);
                } else {
                    System.exit(0);
                }
            }
        }
    }
}
