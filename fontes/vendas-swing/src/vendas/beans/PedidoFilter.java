/*
 * PedidoFilter.java
 * 
 * Created on 15/08/2007, 22:17:05
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package vendas.beans;

import java.util.Date;
import java.util.List;
import ritual.swing.TApplication;
import vendas.entity.Cliente;
import vendas.entity.FormaVenda;
import vendas.entity.GrupoCliente;
import vendas.entity.GrupoProduto;
import vendas.entity.Produto;
import vendas.entity.Repres;
import vendas.entity.RoteiroVendedor;
import vendas.entity.SegMercado;
import vendas.entity.SubGrupoProduto;
import vendas.entity.Vendedor;
import vendas.util.Constants;

/**
 *
 * @author Sam
 */
public class PedidoFilter extends FilterBean {

    private Date dtEmissaoIni;
    private Date dtEmissaoEnd;
    private Date dtNotaIni;
    private Date dtNotaEnd;
    private Date dtEntregaIni;
    private Date dtEntregaEnd;
    private Date dtPgtoNotaIni;
    private Date dtPgtoNotaEnd;
    private Vendedor vendedor;
    private Repres repres;
    private Cliente cliente;
    private Cliente responsavel;
    private String cidade;
    private String bairro;
    private String uf;
    private Integer pedido;
    private Integer pedidoRepres;
    private String situacao;
    private String atendimento;
    private Boolean reposicao;
    private Boolean inativos;
    private Boolean precoCliente;
    private Boolean emAtraso;
    private Boolean produtosInativos;
    private Boolean op;
    private Boolean filtrarPgtos;
    private Boolean totalGrupoCliente;
    private String nota;
    private Produto produto;
    private int ordem;
    private int emissao;
    private int demonstrativo;
    private int tipoRelatorio;
    private GrupoCliente grupo;
    private SegMercado segmento;
    private GrupoProduto grupoProduto;
    private SubGrupoProduto subGrupoProduto;
    private List<Integer> subGrupos;
    private RoteiroVendedor roteiro;
    private FormaVenda formaVenda;
    private Boolean totalizaEntrega;
    private Boolean quebrarSegmento;
    private Boolean quebrarRepres;
    private Boolean prePedido;
    private Boolean preCobranca;
    private Boolean filtrarPedidosFornecedor;

    public PedidoFilter() {
        TApplication app = TApplication.getInstance();
        situacao = "P";
        atendimento = "T";
        ordem = 2;
        tipoRelatorio = 1;
        totalGrupoCliente = false;
        
        repres = new Repres();
        repres.setRazao("TODOS");
        cliente = new Cliente();
        cliente.setRazao("TODOS");
        responsavel = new Cliente();
        responsavel.setRazao("TODOS");
        produto = new Produto();
        grupo = new GrupoCliente();
        grupo.setNomeGrupo("TODOS");
        grupoProduto = new GrupoProduto();
        grupoProduto.setNomeGrupo("TODOS");
        segmento = new SegMercado();
        segmento.setNome("TODOS");
        formaVenda = new FormaVenda();
        formaVenda.setNome("TODOS");
        reposicao = false;
        inativos = false;
        produtosInativos = false;
        quebrarSegmento = false;
        quebrarRepres = false;
        prePedido = false;
        preCobranca = false;
        emAtraso = false;
        op = false;
        emissao = 0;
        demonstrativo = 0;
        prePedido = false;
        precoCliente = false;
        filtrarPedidosFornecedor = false;
        setTitle("Pendentes");
        totalizaEntrega = false;
        vendedor = new Vendedor();
        if (app.getUser().isVendedor()) {
            vendedor.setIdVendedor(app.getUser().getIdvendedor());
            vendedor.setNome(app.getUser().getUserName());
        } else
            vendedor.setNome("TODOS");       

    }

    public Boolean getFiltrarPedidosFornecedor() {
        return filtrarPedidosFornecedor;
    }

    public void setFiltrarPedidosFornecedor(Boolean filtrarPedidosFornecedor) {
        this.filtrarPedidosFornecedor = filtrarPedidosFornecedor;
    }

    public Boolean getTotalGrupoCliente() {
        return totalGrupoCliente;
    }

    public void setTotalGrupoCliente(Boolean totalGrupoCliente) {
        this.totalGrupoCliente = totalGrupoCliente;
    }

    public SegMercado getSegmento() {
        return segmento;
    }

    public void setSegmento(SegMercado segmento) {
        this.segmento = segmento;
    }

    public Boolean isPrePedido() {
        return prePedido;
    }

    public void setPrePedido(Boolean prePedido) {
        this.prePedido = prePedido;
    }

    public Boolean getQuebrarRepres() {
        return quebrarRepres;
    }

    public void setQuebrarRepres(Boolean quebrarRepres) {
        this.quebrarRepres = quebrarRepres;
    }

    public Boolean isPreCobranca() {
        return preCobranca;
    }

    public void setPreCobranca(Boolean preCobranca) {
        this.preCobranca = preCobranca;
    }

    public Boolean getQuebrarSegmento() {
        return quebrarSegmento;
    }

    public void setQuebrarSegmento(Boolean quebrarSegmento) {
        this.quebrarSegmento = quebrarSegmento;
    }

    public Boolean isOp() {
        return op;
    }

    public void setOp(Boolean op) {
        this.op = op;
    }

    public int getEmissao() {
        return emissao;
    }

    public void setEmissao(int emissao) {
        this.emissao = emissao;
    }

    public Boolean getTotalizaEntrega() {
        return totalizaEntrega;
    }

    public void setTotalizaEntrega(Boolean totalizaEntrega) {
        this.totalizaEntrega = totalizaEntrega;
    }

    public Boolean getProdutosInativos() {
        return produtosInativos;
    }

    public void setProdutosInativos(Boolean produtosInativos) {
        this.produtosInativos = produtosInativos;
    }

    public RoteiroVendedor getRoteiro() {
        return roteiro;
    }

    public void setRoteiro(RoteiroVendedor roteiro) {
        this.roteiro = roteiro;
    }

    public int getTipoRelatorio() {
        return tipoRelatorio;
    }

    public void setTipoRelatorio(int tipoRelatorio) {
        this.tipoRelatorio = tipoRelatorio;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public Date getDtEmissaoIni() {
        return dtEmissaoIni;
    }

    public void setDtEmissaoIni(Date dtEmissaoIni) {
        this.dtEmissaoIni = dtEmissaoIni;
    }

    public Date getDtEmissaoEnd() {
        return dtEmissaoEnd;
    }

    public void setDtEmissaoEnd(Date dtEmissaoEnd) {
        this.dtEmissaoEnd = dtEmissaoEnd;
    }

    public Date getDtNotaIni() {
        return dtNotaIni;
    }

    public void setDtNotaIni(Date dtNotaIni) {
        this.dtNotaIni = dtNotaIni;
    }

    public Date getDtNotaEnd() {
        return dtNotaEnd;
    }

    public void setDtNotaEnd(Date dtNotaEnd) {
        this.dtNotaEnd = dtNotaEnd;
    }

    public Date getDtEntregaIni() {
        return dtEntregaIni;
    }

    public void setDtEntregaIni(Date dtEntregaIni) {
        this.dtEntregaIni = dtEntregaIni;
    }

    public Date getDtEntregaEnd() {
        return dtEntregaEnd;
    }

    public void setDtEntregaEnd(Date dtEntregaEnd) {
        this.dtEntregaEnd = dtEntregaEnd;
    }

    public Vendedor getVendedor() {
        return vendedor;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }

    public Repres getRepres() {
        return repres;
    }

    public void setRepres(Repres repres) {
        this.repres = repres;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Cliente getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(Cliente responsavel) {
        this.responsavel = responsavel;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public String getAtendimento() {
        return atendimento;
    }

    public void setAtendimento(String atendimento) {
        this.atendimento = atendimento;
    }

    public Boolean getReposicao() {
        return reposicao;
    }

    public void setReposicao(Boolean reposicao) {
        this.reposicao = reposicao;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public int getOrdem() {
        return ordem;
    }

    public void setOrdem(int ordem) {
        this.ordem = ordem;
    }

    public int getDemonstrativo() {
        return demonstrativo;
    }

    public void setDemonstrativo(int demonstrativo) {
        this.demonstrativo = demonstrativo;
    }

    public Boolean getPrecoCliente() {
        return precoCliente;
    }

    public void setPrecoCliente(Boolean precoCliente) {
        this.precoCliente = precoCliente;
    }

    public GrupoCliente getGrupo() {
        return grupo;
    }

    public void setGrupo(GrupoCliente grupo) {
        this.grupo = grupo;
    }

    public SubGrupoProduto getSubGrupoProduto() {
        return subGrupoProduto;
    }

    public void setSubGrupoProduto(SubGrupoProduto subGrupoProduto) {
        this.subGrupoProduto = subGrupoProduto;
    }

    public Integer getPedido() {
        return pedido;
    }

    public void setPedido(Integer pedido) {
        this.pedido = pedido;
    }

    public Date getDtPgtoNotaIni() {
        return dtPgtoNotaIni;
    }

    public void setDtPgtoNotaIni(Date dtPgtoNotaIni) {
        this.dtPgtoNotaIni = dtPgtoNotaIni;
    }

    public Date getDtPgtoNotaEnd() {
        return dtPgtoNotaEnd;
    }

    public void setDtPgtoNotaEnd(Date dtPgtoNotaEnd) {
        this.dtPgtoNotaEnd = dtPgtoNotaEnd;
    }

    public GrupoProduto getGrupoProduto() {
        return grupoProduto;
    }

    public void setGrupoProduto(GrupoProduto grupoProduto) {
        this.grupoProduto = grupoProduto;
    }

    public Boolean getInativos() {
        return inativos;
    }

    public void setInativos(Boolean inativos) {
        this.inativos = inativos;
    }

    public Boolean getEmAtraso() {
        return emAtraso;
    }

    public void setEmAtraso(Boolean emAtraso) {
        this.emAtraso = emAtraso;
    }

    public Boolean getFiltrarPgtos() {
        return filtrarPgtos;
    }

    public void setFiltrarPgtos(Boolean filtrarPgtos) {
        this.filtrarPgtos = filtrarPgtos;
    }

    public Integer getPedidoRepres() {
        return pedidoRepres;
    }

    public void setPedidoRepres(Integer pedidoRepres) {
        this.pedidoRepres = pedidoRepres;
    }

    public void setFormaVenda(FormaVenda formaVenda) {
        this.formaVenda = formaVenda;
    }

    public FormaVenda getFormaVenda() {
        return formaVenda;
    }

    public List<Integer> getSubGrupos() {
        return subGrupos;
    }

    public void setSubGrupos(List<Integer> subGrupos) {
        this.subGrupos = subGrupos;
    }
    
    
}
