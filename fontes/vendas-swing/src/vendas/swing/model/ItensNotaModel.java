/**
 * ClienteContatoModel.java
 * 
 * Created on 30/06/2007, 12:12:18
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package vendas.swing.model;

import java.math.BigDecimal;
import java.util.List;
import vendas.dao.ProdutoDao;
import vendas.entity.ItemPedidoAtend;
import vendas.entity.Produto;
import vendas.entity.RepresProduto;
import vendas.swing.core.BaseTableModel;

/**
 *
 * @author Sam
 */
public class ItensNotaModel extends BaseTableModel {
    
    public static final int CODIGO = 0;
    public static final int DESCRICAO = 1;
    public static final int PERC = 2;
    public static final int QUANT = 3;
    public static final int SALDO = 4;
    public static final int EMBALAGEM = 5;
    public static final int UNIDADE = 6;
    public static final int VALOR = 7;

    private final ProdutoDao produtoDao = new ProdutoDao();

    public ItensNotaModel() throws Exception {
        super();
    }
    
    public ItensNotaModel(List values) {
        super(values);
    }
    
    @Override
    public void setColumns() {
        addColumn("Código");
        addColumn("Descri\u00E7\u00E3o");
        addColumn("Comissão");
        addColumn("Quant.");
        addColumn("Saldo");
        addColumn("Emb.");
        addColumn("Unid.");
        addColumn("Valor");
    }
    
    @Override
    public boolean isCellEditable(int row, int column) {
        boolean result = false;
        if (isReadOnly()) return false;
        switch (column) {
        case PERC: result = true; break;
        case QUANT: result = true; break;
        case SALDO: result = true; break;
        case EMBALAGEM: result = true; break;
        case VALOR: result = true; break;
        default: result = false;
        }
        return result;
    }
    
    @Override
    public Class getColumnClass(int column) {
        Class aClass = null;
        switch (column) {
        case CODIGO: aClass = String.class; break;
        case DESCRICAO: aClass = String.class; break;
        case PERC: aClass = BigDecimal.class; break;
        case QUANT: aClass = BigDecimal.class; break;
        case SALDO: aClass = BigDecimal.class; break;
        case EMBALAGEM: aClass = BigDecimal.class; break;
        case UNIDADE: aClass = String.class; break;
        case VALOR: aClass = BigDecimal.class; break;
        }
        return aClass;
    }
    
    @Override
    public Object getValueAt(int row, int col) {
        ItemPedidoAtend itemPedido = (ItemPedidoAtend)getObject(row);
        Produto produto;
        Object obj = null;
        switch (col) {
        case CODIGO: produto = itemPedido.getProduto();
                    if (produto != null)
                        obj = produto.getIdProduto();
                    else
                        obj = null;
                    break;
        case DESCRICAO: produto = itemPedido.getProduto();
                    if (produto != null)
                        obj = produto.getDescricao();
                    else
                        obj = null;
                    break;
        case PERC: obj = itemPedido.getPercComissao(); break;
        case QUANT: obj = itemPedido.getQtd(); break;
        case SALDO: obj = itemPedido.getSaldo(); break;
        case EMBALAGEM: obj = itemPedido.getEmbalagem(); break;
        case UNIDADE: produto = itemPedido.getProduto();
                if (produto != null)
                    obj = produto.getUnidade().getIdUnidade();
                else
                    obj = null;
                break;
        case VALOR: obj = itemPedido.getValorCliente();
        }
        return obj;
    }

    @Override
    public void setValueAt(Object aValue, int row, int column) {
        ItemPedidoAtend itemPedido = (ItemPedidoAtend)getObject(row);
        getLogger().info(aValue + ": " + aValue.getClass());
        Produto produto = null;
        
        switch (column) {
        case CODIGO:    try {
                            produto = (Produto)produtoDao.findById(Produto.class, aValue);
                        } catch (Exception e) {
                            getLogger().error(e);
                        }
                        itemPedido.setProduto(produto);
                        itemPedido.getItemPedidoAtendPK().setIdProduto(produto.getIdProduto());
                        break;
        case DESCRICAO: produto = ((RepresProduto)aValue).getProduto();
                        itemPedido.setProduto(produto);
                        itemPedido.setValor(((RepresProduto)aValue).getPreco());
                        itemPedido.getItemPedidoAtendPK().setIdProduto(produto.getIdProduto());
                        break;
        case PERC:      itemPedido.setPercComissao(new BigDecimal((String)aValue)); break;
        case QUANT:     itemPedido.setQtd(new BigDecimal((String)aValue)); break;
        case EMBALAGEM: itemPedido.setEmbalagem(new BigDecimal((String)aValue)); break;
        case VALOR:     itemPedido.setValor(new BigDecimal((String)aValue)); break;
        }
        fireTableRowsUpdated(row, row);
    }
    
}
