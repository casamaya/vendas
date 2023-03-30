/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import ritual.swing.ListComboBoxModel;
import vendas.dao.ClienteDao;
import vendas.dao.GrupoProdutoDao;
import vendas.dao.RepresDao;
import vendas.dao.RoteiroDao;
import vendas.dao.SituacaoClienteDao;
import ritual.swing.TApplication;
import vendas.beans.ClientesFilter;
import vendas.beans.RepresFilter;
import vendas.dao.ColaboradorDao;
import vendas.dao.FormaVendaDao;
import vendas.dao.GrupoClienteDao;
import vendas.dao.PlanoDao;
import vendas.dao.SegMercadoDao;
import vendas.dao.UnidadeProdutoDao;
import vendas.dao.VendedorDao;
import vendas.entity.Cliente;
import vendas.entity.Colaborador;
import vendas.entity.Conta;
import vendas.entity.FormaVenda;
import vendas.entity.FuncaoColaborador;
import vendas.entity.GrupoCliente;
import vendas.entity.GrupoProduto;
import vendas.entity.Repres;
import vendas.entity.RoteiroVendedor;
import vendas.entity.SegMercado;
import vendas.entity.SituacaoCliente;
import vendas.entity.SubGrupoProduto;
import vendas.entity.UnidadeProduto;
import vendas.entity.Vendedor;

/**
 *
 * @author Sam
 */
public class VendasUtil {

    //static String todosString = TApplication.getInstance().getResourceString("comboTodos");
    static String todosString = " -- Selecione -- ";

    private VendasUtil() {
    }

    public static ListComboBoxModel getFormaVendaListModel() {

        List lista = new ArrayList();
        FormaVendaDao plano = (FormaVendaDao)TApplication.getInstance().lookupService("formaVendaDao");
        FormaVenda vendedor = new FormaVenda();
        vendedor.setNome(todosString);
        lista.add(vendedor);
        lista.addAll(plano.findAll());
        return new ListComboBoxModel(lista);
    }

    public static ListComboBoxModel getAPagarListModel() {
        List lista = new ArrayList();
        PlanoDao plano = (PlanoDao)TApplication.getInstance().lookupService("planoDao");
        try {
            Conta conta = new Conta();
            conta.setNome(todosString);
            lista.add(conta);
            lista.addAll(plano.findAPagar(TApplication.getInstance().getUser().getIdvendedor()));
        } catch (Exception e) {
        }
        return new ListComboBoxModel(lista);
    }
    public static ListComboBoxModel getAPagarMasterListModel() {
        List lista = new ArrayList();
        PlanoDao plano = (PlanoDao)TApplication.getInstance().lookupService("planoDao");
        try {
            Conta conta = new Conta();
            conta.setNome(todosString);
            lista.add(conta);
            lista.addAll(plano.findAPagarMaster(TApplication.getInstance().getUser().getIdvendedor()));
        } catch (Exception e) {
        }
        return new ListComboBoxModel(lista);
    }

    public static ListComboBoxModel getAReceberListModel() {
        List lista = new ArrayList();
        PlanoDao plano = (PlanoDao)TApplication.getInstance().lookupService("planoDao");
        try {
            Conta conta = new Conta();
            conta.setNome(todosString);
            lista.add(conta);
            lista.addAll(plano.findAReceber(TApplication.getInstance().getUser().getIdvendedor()));
        } catch (Exception e) {
        }
        return new ListComboBoxModel(lista);
    }
    public static ListComboBoxModel getAReceberMasterListModel() {
        List lista = new ArrayList();
        PlanoDao plano = (PlanoDao)TApplication.getInstance().lookupService("planoDao");
        try {
            Conta conta = new Conta();
            conta.setNome(todosString);
            lista.add(conta);
            lista.addAll(plano.findAReceberMaster(TApplication.getInstance().getUser().getIdvendedor()));
        } catch (Exception e) {
        }
        return new ListComboBoxModel(lista);
    }

    public static ListComboBoxModel getVendedoresListModel() {
        VendedorDao vendedorDao = (VendedorDao) TApplication.getInstance().lookupService("vendedorDao");
        List lista = new ArrayList();
        Vendedor vendedor = new Vendedor();
        vendedor.setNome(todosString);
        lista.add(vendedor);
        lista.addAll(vendedorDao.findAllAtivos());
        return new ListComboBoxModel(lista);
    }
    
    public static ListComboBoxModel getFuncaoColabListModel() {
        ColaboradorDao vendedorDao = (ColaboradorDao) TApplication.getInstance().lookupService("colaboradorDao");
        List lista = new ArrayList();
        FuncaoColaborador vendedor = new FuncaoColaborador();
        vendedor.setNome(todosString);
        lista.add(vendedor);
        Colaborador colab = new Colaborador();
        colab.setFuncaoColaborador(new FuncaoColaborador());
        lista.addAll(vendedorDao.getFuncoes());
        return new ListComboBoxModel(lista);
    }

    public static ListComboBoxModel getSitClienteListModel() {
        SituacaoClienteDao situacaoDao = (SituacaoClienteDao) TApplication.getInstance().lookupService("situacaoClienteDao");
        List lista = new ArrayList();
        SituacaoCliente situacao = new SituacaoCliente();
        situacao.setNome(todosString);
        lista.add(situacao);
        lista.addAll(situacaoDao.findAll());
        return new ListComboBoxModel(lista);
    }

    public static ListComboBoxModel getClienteListModel() {
        ClienteDao clienteDao = (ClienteDao) TApplication.getInstance().lookupService("clienteDao");
        List lista = new ArrayList();
        Cliente cliente = new Cliente();
        cliente.setRazao(todosString);
        lista.add(cliente);
        lista.addAll(clienteDao.findAllAtivos());
        return new ListComboBoxModel(lista);
    }

    public static ListComboBoxModel getClienteListModel(boolean todos) {
        ClienteDao clienteDao = (ClienteDao) TApplication.getInstance().lookupService("clienteDao");
        List lista = new ArrayList();
        Cliente cliente = new Cliente();
        Cliente cliente2 = new Cliente();

        cliente.setRazao(todosString);
        lista.add(cliente);
        ClientesFilter filter = new ClientesFilter();
        SituacaoCliente situacao = new SituacaoCliente();
        if (todos) {
            situacao.setPedido("T");
        } else {
            situacao.setPedido("1");
        }
        
        Vendedor vendedor = new Vendedor();
        
        if (TApplication.getInstance().getUser().isVendedor()) {
            vendedor.setIdVendedor(TApplication.getInstance().getUser().getIdvendedor());
            vendedor.setNome(TApplication.getInstance().getUser().getUserName());
        } else
            vendedor.setNome("TODOS"); 
        
        cliente2.setVendedor(vendedor);
        
        cliente2.setSituacaoCliente(situacao);
        filter.setCliente(cliente2);
        lista.addAll(clienteDao.findByExample(filter));
        return new ListComboBoxModel(lista);
    }

    public static ListComboBoxModel getRepresListModel(boolean ativos) {
        RepresDao represDao = (RepresDao) TApplication.getInstance().lookupService("represDao");
        List lista = new ArrayList();
        Repres repres = new Repres();
        repres.setRazao(todosString);
        lista.add(repres);
        repres = new Repres();
        if (ativos) {
            repres.setInativo('A');
        } else {
            repres.setInativo('T');
        }
        RepresFilter filtro = new RepresFilter();
        filtro.setRepres(repres);
        lista.addAll(represDao.findByExample(filtro));
        return new ListComboBoxModel(lista);
    }

    public static ListComboBoxModel getGrupoProdutoListModel() {
        GrupoProdutoDao grupoDao = (GrupoProdutoDao) TApplication.getInstance().lookupService("grupoProdutoDao");
        List lista = new ArrayList();
        GrupoProduto cliente = new GrupoProduto();
        cliente.setNomeGrupo(todosString);
        lista.add(cliente);
        lista.addAll(grupoDao.findAll());
        return new ListComboBoxModel(lista);
    }

    public static ListComboBoxModel getSubGrupoProdutoListModel(GrupoProduto grupo) {
        GrupoProdutoDao grupoDao = (GrupoProdutoDao) TApplication.getInstance().lookupService("grupoProdutoDao");
        List lista = new ArrayList();
        SubGrupoProduto cliente = new SubGrupoProduto();
        cliente.setNomeGrupo(todosString);
        lista.add(cliente);
        lista.addAll(grupo.getSubGrupoProdutoList());
        Collections.sort(lista, new NomeGrupoComparator());
        return new ListComboBoxModel(lista);
    }

    public static ListComboBoxModel getGrupoClienteListModel() {
        GrupoClienteDao grupoDao = (GrupoClienteDao) TApplication.getInstance().lookupService("grupoClienteDao");
        List lista = new ArrayList();
        GrupoCliente grupo = new GrupoCliente();
        grupo.setNomeGrupo(todosString);
        lista.add(grupo);
        lista.addAll(grupoDao.findAll());
        return new ListComboBoxModel(lista);
    }

    public static ListComboBoxModel getSegMercadoListModel() {
        SegMercadoDao segDao = (SegMercadoDao) TApplication.getInstance().lookupService("segMercadoDao");
        List lista = new ArrayList();
        SegMercado unidade = new SegMercado();
        unidade.setNome(todosString);
        lista.add(unidade);
        lista.addAll(segDao.findAll());
        return new ListComboBoxModel(lista);
    }

    public static ListComboBoxModel getUndProdutoListModel() {
        UnidadeProdutoDao unidadeDao = (UnidadeProdutoDao) TApplication.getInstance().lookupService("undProdutoDao");
        List lista = new ArrayList();
        UnidadeProduto unidade = new UnidadeProduto();
        unidade.setNome(todosString);
        lista.add(unidade);
        lista.addAll(unidadeDao.findAll());
        return new ListComboBoxModel(lista);
    }

    public static ListComboBoxModel getRoteiroListModel() {
        RoteiroDao unidadeDao = (RoteiroDao) TApplication.getInstance().lookupService("roteiroDao");
        List lista = new ArrayList();
        RoteiroVendedor unidade = new RoteiroVendedor();
        unidade.setDescricao(todosString);
        lista.add(unidade);
        lista.addAll(unidadeDao.findAll());
        return new ListComboBoxModel(lista);
    }
}

class NomeGrupoComparator implements Comparator {

    public int compare(Object obj1, Object obj2) {
        SubGrupoProduto grupo1 = (SubGrupoProduto) obj1;
        SubGrupoProduto grupo2 = (SubGrupoProduto) obj2;
        return grupo1.getNomeGrupo().compareTo(grupo2.getNomeGrupo());

    }
}
