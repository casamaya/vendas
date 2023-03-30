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
import vendas.entity.ItemOrcamento;
import vendas.entity.Produto;
import vendas.entity.RepresProduto;
import vendas.swing.core.BaseTableModel;
import vendas.util.Messages;

/**
 *
 * @author Sam
 */
public class ItemOrcamentoModel extends BaseTableModel {
    
    public static final int CODIGO = 0;
    public static final int DESCRICAO = 1;
    public static final int EMBALAGEM = 2;
    public static final int QUANT = 3;
    public static final int UNIDADE = 4;
    public static final int VALOR = 5;
    public static final int TOTAL = 6;

    private ProdutoDao produtoDao = new ProdutoDao();

    public ItemOrcamentoModel() throws Exception {
        super();
    }
    
    public ItemOrcamentoModel(List values) {
        super(values);
    }
    
    @Override
    public void setColumns() {
        addColumn("Código");
        addColumn("Descri\u00E7\u00E3o");
        addColumn("Emb.");
        addColumn("Quant.");
        addColumn("Unid.");
        addColumn("Preço");
        addColumn("Total");
    }
    
    @Override
    public boolean isCellEditable(int row, int column) {
        boolean result = false;
        if (isReadOnly())
            return false;
        switch (column) {
        case CODIGO: result = true; break;
        case DESCRICAO: result = true; break;
        case EMBALAGEM: result = true; break;
        case QUANT: result = true; break;
        case VALOR: result = true; break;
        }
        return result;
    }
    
    
    @Override
    public Class getColumnClass(int column) {
        Class aClass = null;
        switch (column) {
        case CODIGO: aClass = Integer.class; break;
        case DESCRICAO: aClass = String.class; break;
        case EMBALAGEM: aClass = Integer.class; break;
        case QUANT: aClass = BigDecimal.class; break;
        case UNIDADE: aClass = String.class; break;
        case VALOR: aClass = BigDecimal.class; break;
        case TOTAL: aClass = BigDecimal.class; break;
        }
        return aClass;
    }
    
    @Override
    public Object getValueAt(int row, int col) {
        ItemOrcamento itemPedido = (ItemOrcamento)getObject(row);
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
        case EMBALAGEM: obj = itemPedido.getEmbalagem(); break;
        case QUANT: obj = itemPedido.getQtd(); break;
        case UNIDADE: produto = itemPedido.getProduto();
                    if (produto != null)
                        obj = produto.getUnidade().getIdUnidade();
                    else
                        obj = null;
                    break;
        case VALOR: obj = itemPedido.getValorCliente(); break;
        case TOTAL: obj = itemPedido.getTotal(); break;
        }
        return obj;
    }

    @Override
    public void setValueAt(Object aValue, int row, int column) {
        ItemOrcamento itemPedido = (ItemOrcamento)getObject(row);
        getLogger().info(aValue + ": " + aValue.getClass());
        Produto produto = null;
        
        switch (column) {
        case CODIGO:    try {
                            produto = (Produto)produtoDao.findById(Produto.class, aValue);
                        } catch (Exception e) {
                            getLogger().error(e);
                        }
                        if (produto == null)
                            Messages.errorMessage("Produto n\u00E3o localizado");
                        else {
                            itemPedido.setProduto(produto);
                            itemPedido.getItemOrcamentoPK().setIdproduto(produto.getIdProduto());
                        }
                        break;
        case DESCRICAO: produto = ((RepresProduto)aValue).getProduto();
                        itemPedido.setProduto(produto);
                        itemPedido.getItemOrcamentoPK().setIdproduto(produto.getIdProduto());
                        itemPedido.setValor(((RepresProduto)aValue).getPreco());
                        BigDecimal valor = ((RepresProduto)aValue).getPrecoFinal();
                        if (valor == null || valor.intValue() == 0) {
                            valor = ((RepresProduto)aValue).getPreco();
                        }
                        itemPedido.setValorCliente(valor);
                        itemPedido.setIpi(((RepresProduto)aValue).getIpi());
                        itemPedido.setPercComissao(((RepresProduto)aValue).getPercComissao());
                        
                        if (((RepresProduto)aValue).getEmbalagem() == null)
                            itemPedido.setQtd(BigDecimal.ZERO);
                        else
                            itemPedido.setQtd(((RepresProduto)aValue).getEmbalagem());
                        
                        itemPedido.setQtd(((RepresProduto)aValue).getEmbalagem());
                        break;
        case QUANT:     itemPedido.setQtd(new BigDecimal((String)aValue));
                        break;
        case EMBALAGEM:     itemPedido.setEmbalagem(new Integer((String)aValue));
                        break;
        case VALOR:     itemPedido.setValorCliente(new BigDecimal((String)aValue)); break;
        }
        fireTableRowsUpdated(row, row);
    }
    
}