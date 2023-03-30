/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import ritual.util.DateUtils;
import vendas.beans.ClientesFilter;
import vendas.beans.ClientesGrupo;
import vendas.beans.CobrancaFilter;
import vendas.beans.MapaComissao;
import vendas.beans.MapaFilterBean;
import vendas.beans.MovimentoFilter;
import vendas.beans.PedidoFilter;
import vendas.beans.ProdutosComprados;
import vendas.beans.ResumoComissao;
import vendas.beans.ResumoVendasUnidade;
import vendas.beans.UltimoPedido;
import vendas.beans.UnidadeItem;
import vendas.beans.VendasCliente;
import vendas.beans.VendasUnidadeFilter;
import vendas.entity.ArquivoPedido;
import vendas.entity.AtendimentoPedido;
import vendas.entity.EntradaRepres;
import vendas.entity.GrupoCliente;
import vendas.entity.GrupoProduto;
import vendas.entity.ItemPedido;
import vendas.entity.ItemPedidoAtend;
import vendas.entity.Meta;
import vendas.entity.Pedido;
import vendas.entity.PedidoEmbarque;
import vendas.entity.PgtoCliente;
import vendas.entity.Produto;
import vendas.entity.ReciboComissao;
import vendas.entity.Repres;
import vendas.entity.SubGrupoProduto;
import vendas.entity.Vendedor;
import vendas.entity.manager.ManagerFactory;
import vendas.exception.DAOException;
import vendas.util.StringUtils;


/**
 *
 * @author Sam
 */
public class PedidoDao extends BaseDao {

    public void excluirAnexosPedido() {
        EntityManager em = getEntityManager();
        EntityTransaction t = em.getTransaction();
        try {
            t.begin();
            Query q = em.createQuery("delete from ArquivoPedido where idPedido in (select p.idPedido from Pedido as p where p.dtPedido + 365 = :dtAtual)");
            q.setParameter("dtAtual", DateUtils.getCurrentDate());
            q.executeUpdate();
            t.commit();
        } catch (Exception e) {
            t.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public List findReciboComissao(Vendedor vendedor) {
        getLogger().info("findReciboComissao ini");
        Session session = ManagerFactory.getSession();
        StringBuilder sb = new StringBuilder();
        sb.append("select o from AtendimentoPedido o where o.dtPgtoComissao is not null and o.recibo = :recibo and o.atendimentoPedidoPK.idPedido in ");
        sb.append("  (select p.idPedido from Pedido p where p.idPedido = o.atendimentoPedidoPK.idPedido and p.vendedor.idVendedor = :vendedor");
        sb.append("  )");
        org.hibernate.Query q = session.createQuery(sb.toString());
        q.setParameter("recibo", "1");
        q.setParameter("vendedor", vendedor.getIdVendedor());
        return q.list();
    }

    public Integer getNextValue() {
        String s = "select gen_id( TBPEDIDO_IDPEDIDO_SEQ, 1 ) from RDB$DATABASE";
        Session session = ManagerFactory.getSession();
        SQLQuery q = (SQLQuery) session.createSQLQuery(s);
        BigInteger bi = (BigInteger) q.uniqueResult();

        return bi.intValue();

    }

    public List<Pedido> findAll(PedidoFilter t) throws DAOException {
        getLogger().info("findAll ini");
        return (List<Pedido>) findByExample(t);
    }

    @Override
    public List findByExample(Object obj) {
        getLogger().info("findByExample ini");
        PedidoFilter pedido = (PedidoFilter) obj;

        List<String> where = new ArrayList<>();
        StringBuilder sb = new StringBuilder("select p from Pedido p where ");
        HashMap<String, Object> params = new HashMap<>();

        if (pedido.getPedido() != null) {
            sb.append("(p.idPedido = :pedido or p.idPedidoRepres = :pedidoRepres)");
            params.put("pedido", pedido.getPedido());
            params.put("pedidoRepres", pedido.getPedido());
            Query q = ManagerFactory.getEntityManager().createQuery(sb.toString());
            q.setParameter("pedido", pedido.getPedido());
            q.setParameter("pedidoRepres", pedido.getPedido());
            return q.getResultList();
        }

        if (pedido.isPrePedido() && (!"E".equals(pedido.getSituacao()))) {
            where.add("p.prePedido = :prePedido");
            params.put("prePedido", "1");
        }

        if (pedido.isPreCobranca()) {
            //where.add("p.preCobranca = :preCobranca and (p.situacao = :atendido or p.atendimento = :atend)");
            where.add("p.preCobranca = :preCobranca and p.idPedido in (select co.atendimentoPedido.pedido.idPedido from PgtoCliente co where p.idPedido = co.atendimentoPedido.pedido.idPedido)");
            params.put("preCobranca", "1");
            //params.put("atendido", "A");
            //params.put("atend", "P");
        }

        if (pedido.getDemonstrativo() == 1) {
            where.add("(p.emitidoCobranca = :emitido1)");
            params.put("emitido1", "1");
        } else if (pedido.getDemonstrativo() == 2) {
            where.add("(p.emitidoCobranca = :emitido1)");
            params.put("emitido1", "0");
        }

        if (pedido.getDtEmissaoIni() != null) {
            where.add("p.dtPedido between :dtPedido1 and :dtPedido2");
            params.put("dtPedido1", pedido.getDtEmissaoIni());
            params.put("dtPedido2", pedido.getDtEmissaoEnd());
        }
        if (pedido.getVendedor().getIdVendedor() != null) {
            where.add("p.vendedor.idVendedor = :vendedor");
            params.put("vendedor", pedido.getVendedor().getIdVendedor());
        }
        if (pedido.getCliente().getIdCliente() == null && pedido.getResponsavel().getIdCliente() != null) {
            where.add("p.clienteResponsavel.idCliente = :responsavel");
            params.put("responsavel", pedido.getResponsavel().getIdCliente());
        } else if (pedido.getCliente().getIdCliente() != null && pedido.getResponsavel().getIdCliente() == null) {
            where.add("(p.cliente.idCliente = :cliente or p.clienteResponsavel.idCliente = :responsavel)");
            params.put("cliente", pedido.getCliente().getIdCliente());
            params.put("responsavel", pedido.getCliente().getIdCliente());
        } else if (pedido.getCliente().getIdCliente() != null && pedido.getResponsavel().getIdCliente() != null) {
            where.add("(p.cliente.idCliente = :cliente and p.clienteResponsavel.idCliente = :responsavel)");
            params.put("cliente", pedido.getCliente().getIdCliente());
            params.put("responsavel", pedido.getResponsavel().getIdCliente());
        }
        if (pedido.getFormaVenda().getIdformavenda() != null) {
            where.add("p.formaVenda.idformavenda = :forma");
            params.put("forma", pedido.getFormaVenda().getIdformavenda());
        }

        if (!pedido.getInativos()) {
            where.add("p.repres.inativo = '0'");
        }
        
        if (pedido.getRepres().getIdRepres() != null) {
            where.add("p.repres.idRepres = :repres");
            params.put("repres", pedido.getRepres().getIdRepres());
        }
        if ((pedido.getSubGrupoProduto() != null) && (pedido.getSubGrupoProduto().getIdCodSubGrupo() != null)) {
            where.add("p.idPedido in (select i.pedido.idPedido from ItemPedido i, Produto pr where p.idPedido = i.pedido.idPedido and i.produto.idProduto = pr.idProduto and pr.subGrupoProduto.idCodSubGrupo = :subgrupo)");
            params.put("subgrupo", pedido.getSubGrupoProduto().getIdCodSubGrupo());
        } else if (pedido.getGrupoProduto().getIdCodGrupo() != null) {
            where.add("p.idPedido in (select i.pedido.idPedido from ItemPedido i, Produto pr where p.idPedido = i.pedido.idPedido and i.produto.idProduto = pr.idProduto and pr.grupoProduto.idCodGrupo = :grupo)");
            params.put("grupo", pedido.getGrupoProduto().getIdCodGrupo());
        }
        if (pedido.getDtNotaIni() != null) {
            where.add("p.idPedido in (select a.atendimentoPedidoPK.idPedido from AtendimentoPedido a where p.idPedido = a.atendimentoPedidoPK.idPedido and a.dtNota between :dtNota1 and :dtNota2)");
            params.put("dtNota1", pedido.getDtNotaIni());
            params.put("dtNota2", pedido.getDtNotaEnd());
        }
        if (pedido.getDtPgtoNotaIni() != null) {
            where.add("p.idPedido in (select a.atendimentoPedidoPK.idPedido from AtendimentoPedido a where p.idPedido = a.atendimentoPedidoPK.idPedido and a.dtPgtoComissao between :dtPgto1 and :dtPgto2)");
            params.put("dtPgto1", pedido.getDtPgtoNotaIni());
            params.put("dtPgto2", pedido.getDtPgtoNotaEnd());
        }
        if (pedido.getGrupo().getIdGrupoCliente() != null) {
            where.add("p.cliente.idCliente in (select c.idCliente from Cliente c join c.gruposCliente s where exists (select t from GrupoCliente t where s = t and t.idGrupoCliente = :grupoCliente))");
            params.put("grupoCliente", pedido.getGrupo().getIdGrupoCliente());
        }
        if (pedido.getSegmento().getIdSegmento() != null) {
            where.add("p.cliente.idCliente in (select c.idCliente from Cliente c join c.segmentos s where exists (select t from SegMercado t where s = t and t.idSegmento = :segmento))");
            params.put("segmento", pedido.getSegmento().getIdSegmento());
        }
        if (("A".equals(pedido.getSituacao())) || ("N".equals(pedido.getSituacao()))) {
            where.add("p.situacao = :situacao");
            params.put("situacao", pedido.getSituacao());
        }
        if (!"T".equals(pedido.getAtendimento())) {
            where.add("p.atendimento = :atendimento");
            params.put("atendimento", pedido.getAtendimento());
        }

        if (pedido.isOp()) {
            where.add("((p.valorOp is not null) and (p.valorOp > 0)) ");
        }
        if ((pedido.getCidade() != null) && (pedido.getCidade().length() > 0)) {
            where.add("p.cliente.cidade = :cidade");
            params.put("cidade", pedido.getCidade());
        }
        if ((pedido.getBairro() != null) && (pedido.getBairro().length() > 0)) {
            where.add("p.cliente.bairro = :bairro");
            params.put("bairro", pedido.getBairro());
        }
        if ((pedido.getUf() != null) && (pedido.getUf().length() > 0)) {
            where.add("p.cliente.uf = :uf");
            params.put("uf", pedido.getUf());
        }
        if (pedido.getDtEntregaIni() != null) {
            where.add("(p.dtEntrega between :dtEntrega1 and :dtEntrega2)");
            params.put("dtEntrega1", pedido.getDtEntregaIni());
            params.put("dtEntrega2", pedido.getDtEntregaEnd());
        }
        if ((pedido.getProduto() != null) && (pedido.getProduto().getIdProduto() != null)) {
            where.add("p.idPedido in (select i.pedido.idPedido from ItemPedido i where p.idPedido = i.pedido.idPedido and i.produto.idProduto = :produto)");
            params.put("produto", pedido.getProduto().getIdProduto());
        }
        if ((pedido.getNota() != null) && (pedido.getNota().length() > 0)) {
            where.add("p.idPedido in (select a.atendimentoPedidoPK.idPedido from AtendimentoPedido a where a.atendimentoPedidoPK.nf = :nf)");
            params.put("nf", pedido.getNota());
        }
        
        if (pedido.getFiltrarPedidosFornecedor()) {
            where.add("p.idPedidoRepres is not null");
        }
        
        if ((pedido.getEmissao() == 2) && ("E".equals(pedido.getSituacao()))) {
            StringBuilder sb1 = new StringBuilder();
            sb1.append("(p.emitido = :emitido1 or p.emitidoCliente = :emitido2)");
            params.put("emitido1", "0");
            params.put("emitido2", "0"); 
             StringBuilder sb2 = new StringBuilder();
            if (pedido.isPrePedido())
                sb2.append("((");
            sb2.append("p.dtEntrega <= :dataAtual and p.situacao != :situacaoPedido");
            params.put("dataAtual", new Date());
            params.put("situacaoPedido", "A");
            if (pedido.isPrePedido()) {
                sb2.append(") or (p.prePedido = :prePedido))");
                sb1.append(" or ").append(sb2.toString());
                params.put("prePedido", "1");
            }   
            where.add("(" + sb1.toString() + ")");

        } else {    
            if (pedido.getEmissao() == 1) {
                where.add("(p.emitido = :emitido1 or p.emitidoCliente = :emitido2)");
                params.put("emitido1", "1");
                params.put("emitido2", "1");
            } else if (pedido.getEmissao() == 2) {
                where.add("(p.emitido = :emitido1 or p.emitidoCliente = :emitido2)");
                params.put("emitido1", "0");
                params.put("emitido2", "0");
            }

            if ("E".equals(pedido.getSituacao())) {
                StringBuilder sb2 = new StringBuilder();
                if (pedido.isPrePedido())
                    sb2.append("(");
                sb2.append("(p.dtEntrega <= :dataAtual and p.situacao != :situacaoPedido)");
                params.put("dataAtual", new Date());
                params.put("situacaoPedido", "A");
                if (pedido.isPrePedido()) {
                    sb2.append(") or (p.prePedido = :prePedido))");
                    params.put("prePedido", "1");
                }
                where.add("(" + sb2.toString() + ")");
            }
        }

        int i = 0;
        for (String item : where) {
            if (i++ > 0) {
                sb.append(" and ");
            }
            sb.append(item);
        }
        
        sb.append(" order by ");
        if (pedido.isPrePedido())
            sb.append(" p.prePedido desc, ");
        switch (pedido.getOrdem()) {
            case 0:
                sb.append(" p.repres.razao, p.cliente.razao, p.idPedido");
                break;
            case 1:
                sb.append(" p.cliente.razao, p.dtEntrega, p.repres.razao, p.idPedido");
                break;
            case 2:
                sb.append(" p.repres.razao, p.dtEntrega");
                break;
            case 3:
                sb.append(" p.repres.razao, p.idPedido");
                break;
            case 4:
                sb.append(" p.repres.razao, p.dtPedido");
                break;
            case 5:
                sb.append(" p.repres.razao, p.idPedidoRepres");
                break;
        }
        //getLogger().info(sb.toString());
        Query q = ManagerFactory.getEntityManager().createQuery(sb.toString());

        Iterator iterator = params.keySet().iterator();
        getLogger().info(sb.toString());
        while (iterator.hasNext()) {
            String s = (String) iterator.next();
            getLogger().info(s + "|" + params.get(s));
            q.setParameter(s, params.get(s));
        }

        return q.getResultList();
    }

    public List findPosicaoAtend(PedidoFilter pedido) {
        getLogger().info("findPosicaoAtend ini");
        Session session = ManagerFactory.getSession();
        Criteria criteria = session.createCriteria(Pedido.class).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        if (pedido.getPedido() != null) {
            criteria.add(Restrictions.eq("idPedido", pedido.getPedido()));
            return criteria.list();
        }
        Criteria represCriteria = criteria.createCriteria("repres");
        represCriteria.addOrder(Order.asc("razao"));
        if (pedido.getRepres().getIdRepres() != null) {
            represCriteria.add(Restrictions.eq("idRepres", pedido.getRepres().getIdRepres()));
        }
        if ("P".equals(pedido.getSituacao())) {
            criteria.add(Restrictions.eq("situacao", "N"));
            criteria.add(Restrictions.le("dtEntrega", new Date()));
        } else if ((!"T".equals(pedido.getSituacao())) && (!"P".equals(pedido.getSituacao()))) {
            criteria.add(Restrictions.eq("situacao", pedido.getSituacao()));
        }
        if (pedido.getDtEmissaoIni() != null) {
            criteria.add(Restrictions.between("dtPedido", pedido.getDtEmissaoIni(), pedido.getDtEmissaoEnd()));
        }
        if (pedido.getVendedor().getIdVendedor() != null) {
            criteria.createCriteria("vendedor").add(Restrictions.eq("idVendedor", pedido.getVendedor().getIdVendedor()));
        }
        if (pedido.getCliente().getIdCliente() != null) {
            criteria.createCriteria("cliente").add(Restrictions.eq("idCliente", pedido.getCliente().getIdCliente()));
        }
        if (pedido.getDtEntregaIni() != null && pedido.getDtEntregaEnd() != null) {
            criteria.add(Restrictions.between("dtEntrega", pedido.getDtEntregaIni(), pedido.getDtEntregaEnd()));
        } else if (pedido.getDtEntregaEnd() != null) {
            criteria.add(Restrictions.le("dtEntrega", pedido.getDtEntregaEnd()));
        }
        
        if (pedido.getFiltrarPedidosFornecedor()) {
            criteria.add(Restrictions.isNotNull("idPedidoRepres"));
        }
        
        Criteria itensCriteria = null;
        if (pedido.getGrupoProduto().getIdCodGrupo() != null) {
            itensCriteria = criteria.createCriteria("itens").createCriteria("produto");
            itensCriteria.createCriteria("grupoProduto").add(Restrictions.eq("idCodGrupo", pedido.getGrupoProduto().getIdCodGrupo()));
        }
        if ((pedido.getSubGrupoProduto() != null) && (pedido.getSubGrupoProduto().getIdCodSubGrupo() != null)) {
            if (itensCriteria == null) {
                itensCriteria = criteria.createCriteria("itens").createCriteria("produto");
            }
            itensCriteria.createCriteria("subGrupoProduto").add(Restrictions.eq("idCodSubGrupo", pedido.getSubGrupoProduto().getIdCodSubGrupo()));
        }        // ordem por pedido
        if (pedido.getOrdem() == 0) {
            criteria.addOrder(Order.asc("idPedido"));
        } else //ordem por emissao
        if (pedido.getOrdem() == 1) {
            criteria.addOrder(Order.asc("dtPedido"));
        } else //ordem por entrega
        {
            criteria.addOrder(Order.asc("dtEntrega"));
        }
        List lista = criteria.list();
        return lista;
    }

    public List findAPagar(Repres repres, Vendedor vendedor) {
        Session session = ManagerFactory.getSession();
        Criteria criteria = session.createCriteria(AtendimentoPedido.class).add(Restrictions.eq("recibo", "0"));
        criteria.add(Restrictions.isNotNull("dtNota"));
        criteria.add(Restrictions.isNull("dtPgtoComissao"));
        criteria.addOrder(Order.asc("dtNota"));
        Criteria pedidoCriteria = criteria.createCriteria("pedido");
        pedidoCriteria.createCriteria("repres").add(Restrictions.eq("idRepres", repres.getIdRepres()));
        if (vendedor.getIdVendedor() != null) {
            pedidoCriteria.createCriteria("vendedor").add(Restrictions.eq("idVendedor", vendedor.getIdVendedor()));
        }
        pedidoCriteria.addOrder(Order.asc("dtPedido"));
        return criteria.list();
    }

    public List findComissao(PedidoFilter pedido) {
        getLogger().info("findComissao ini");
        List<String> where = new ArrayList<String>();
        StringBuilder sb = new StringBuilder("select p from Pedido p where ");
        HashMap<String, Object> params = new HashMap<String, Object>();

        if (pedido.getDtEmissaoIni() != null) {
            where.add("p.dtPedido between :dtPedido1 and :dtPedido2");
            params.put("dtPedido1", pedido.getDtEmissaoIni());
            params.put("dtPedido2", pedido.getDtEmissaoEnd());
        }
        if (pedido.getVendedor().getIdVendedor() != null) {
            where.add("p.vendedor.idVendedor = :vendedor");
            params.put("vendedor", pedido.getVendedor().getIdVendedor());
        }
        if (pedido.getGrupo().getIdGrupoCliente() != null) {
            where.add("p.cliente.idCliente in (select c.idCliente from Cliente c join c.gruposCliente s where exists (select t from GrupoCliente t where s = t and t.idGrupoCliente = :grupoCliente))");
            params.put("grupoCliente", pedido.getGrupo().getIdGrupoCliente());
        }
        if (pedido.getCliente().getIdCliente() == null && pedido.getResponsavel().getIdCliente() != null) {
            where.add("p.clienteResponsavel.idCliente = :responsavel");
            params.put("responsavel", pedido.getResponsavel().getIdCliente());
        } else if (pedido.getCliente().getIdCliente() != null && pedido.getResponsavel().getIdCliente() == null) {
            where.add("(p.cliente.idCliente = :cliente or p.clienteResponsavel.idCliente = :responsavel)");
            params.put("cliente", pedido.getCliente().getIdCliente());
            params.put("responsavel", pedido.getCliente().getIdCliente());
        } else if (pedido.getCliente().getIdCliente() != null && pedido.getResponsavel().getIdCliente() != null) {
            where.add("(p.cliente.idCliente = :cliente and p.clienteResponsavel.idCliente = :responsavel)");
            params.put("cliente", pedido.getCliente().getIdCliente());
            params.put("responsavel", pedido.getResponsavel().getIdCliente());
        }

        if (pedido.getRepres().getIdRepres() != null) {
            where.add("p.repres.idRepres = :repres");
            params.put("repres", pedido.getRepres().getIdRepres());
        }
        if (pedido.getGrupoProduto().getIdCodGrupo() != null) {
            where.add("p.idPedido in (select i.pedido.idPedido from ItemPedido i, Produto pr, GrupoProduto g where p.idPedido = i.pedido.idPedido and i.produto.idProduto = pr.idProduto and g.idCodGrupo = pr.grupoProduto.idCodGrupo and g.idCodGrupo = :grupo)");
            params.put("grupo", pedido.getGrupoProduto().getIdCodGrupo());
        }
        if ((pedido.getSubGrupoProduto() != null) && (pedido.getSubGrupoProduto().getIdCodSubGrupo() != null)) {
            where.add("p.idPedido in (select i.pedido.idPedido from ItemPedido i, Produto pr where p.idPedido = i.pedido.idPedido and i.produto.idProduto = pr.idProduto and pr.subGrupoProduto.idCodSubGrupo = :subgrupo)");
            params.put("subgrupo", pedido.getSubGrupoProduto().getIdCodSubGrupo());
        }
        if (pedido.getDtNotaIni() != null) {
            where.add("p.idPedido in (select a.atendimentoPedidoPK.idPedido from AtendimentoPedido a where p.idPedido = a.atendimentoPedidoPK.idPedido and a.dtNota between :dtNota1 and :dtNota2)");
            params.put("dtNota1", pedido.getDtNotaIni());
            params.put("dtNota2", pedido.getDtNotaEnd());
        }
        if (pedido.getDtPgtoNotaIni() != null) {
            //where.add("p.idPedido in (select a.atendimentoPedidoPK.idPedido from AtendimentoPedido a where a.atendimentoPedidoPK.idPedido = p.idPedido and a.dtPgtoComissao between :dtPgto1 and :dtPgto2)");
            where.add("p.idPedido in (select a.pedido.idPedido from AtendimentoPedido a where a.pedido.idPedido = p.idPedido and a.dtPgtoComissao between :dtPgto1 and :dtPgto2)");
            params.put("dtPgto1", pedido.getDtPgtoNotaIni());
            params.put("dtPgto2", pedido.getDtPgtoNotaEnd());
        }
        if (null != pedido.getSituacao()) switch (pedido.getSituacao()) {
            case "P":
                where.add("p.idPedido in (select a.atendimentoPedidoPK.idPedido from AtendimentoPedido a where p.idPedido = a.pedido.idPedido and a.dtPgtoComissao is not null)");
                break;
            case "S":
                where.add("((p.idPedido in (select a.atendimentoPedidoPK.idPedido from AtendimentoPedido a where p.idPedido = a.atendimentoPedidoPK.idPedido and a.dtPgtoComissao is null)) or (p.situacao = :situacaoPedido))");
                params.put("situacaoPedido", "N");
                break;
            case "N":
                where.add("p.situacao = :situacaoPedido");
                params.put("situacaoPedido", "N");
                break;
        }
        int i = 0;
        for (String item : where) {
            if (i++ > 0) {
                sb.append(" and ");
            }
            sb.append(item);
        }
        sb.append(" order by ");
        switch (pedido.getOrdem()) {
            case 1: //embarque
                sb.append(" p.repres.razao, p.dtEntrega");
                break;
            case 2: //pedido
                sb.append(" p.repres.razao, p.idPedido");
                break;
            case 4: //dtpedido
                sb.append(" p.repres.razao, p.dtPedido, p.idPedido");
                break;             
            default:  //cliente
                sb.append(" p.repres.razao, p.cliente.razao");
                break;
        }
//        getLogger().info(sb.toString());
        Query q = ManagerFactory.getEntityManager().createQuery(sb.toString());

        Iterator iterator = params.keySet().iterator();
        while (iterator.hasNext()) {
            String s = (String) iterator.next();
            q.setParameter(s, params.get(s));

        }
        
        return q.getResultList();
    }

    public List findComissaoPgto(PedidoFilter pedido) {
        getLogger().info("findComissaoPgto ini");
        List<String> where = new ArrayList<String>();
                HashMap<String, Object> params = new HashMap<String, Object>();
        StringBuilder sb = new StringBuilder("select a from AtendimentoPedido a, Pedido p where a.atendimentoPedidoPK.idPedido = p.idPedido ");
        if (pedido.getDtEmissaoIni() != null) {
            where.add("p.dtPedido between :dtPedido1 and :dtPedido2");
            params.put("dtPedido1", pedido.getDtEmissaoIni());
            params.put("dtPedido2", pedido.getDtEmissaoEnd());
        }
        if (pedido.getVendedor().getIdVendedor() != null) {
            where.add("p.vendedor.idVendedor = :vendedor");
            params.put("vendedor", pedido.getVendedor().getIdVendedor());
        }
        if (pedido.getGrupo().getIdGrupoCliente() != null) {
            where.add("p.cliente.idCliente in (select c.idCliente from Cliente c join c.gruposCliente s where exists (select t from GrupoCliente t where s = t and t.idGrupoCliente = :grupoCliente))");
            params.put("grupoCliente", pedido.getGrupo().getIdGrupoCliente());
        }
        if (pedido.getCliente().getIdCliente() == null && pedido.getResponsavel().getIdCliente() != null) {
            where.add("p.clienteResponsavel.idCliente = :responsavel");
            params.put("responsavel", pedido.getResponsavel().getIdCliente());
        } else if (pedido.getCliente().getIdCliente() != null && pedido.getResponsavel().getIdCliente() == null) {
            where.add("(p.cliente.idCliente = :cliente or p.clienteResponsavel.idCliente = :responsavel)");
            params.put("cliente", pedido.getCliente().getIdCliente());
            params.put("responsavel", pedido.getCliente().getIdCliente());
        } else if (pedido.getCliente().getIdCliente() != null && pedido.getResponsavel().getIdCliente() != null) {
            where.add("(p.cliente.idCliente = :cliente and p.clienteResponsavel.idCliente = :responsavel)");
            params.put("cliente", pedido.getCliente().getIdCliente());
            params.put("responsavel", pedido.getResponsavel().getIdCliente());
        }

        if (pedido.getRepres().getIdRepres() != null) {
            where.add("p.repres.idRepres = :repres");
            params.put("repres", pedido.getRepres().getIdRepres());
        }
        if ((pedido.getSubGrupoProduto() != null) && (pedido.getSubGrupoProduto().getIdCodSubGrupo() != null)) {
            where.add("p.idPedido in (select i.pedido.idPedido from ItemPedido i, Produto pr where p.idPedido = i.pedido.idPedido and i.produto.idProduto = pr.idProduto and pr.subGrupoProduto.idCodSubGrupo = :subgrupo)");
            params.put("subgrupo", pedido.getSubGrupoProduto().getIdCodSubGrupo());
        } else if (pedido.getGrupoProduto().getIdCodGrupo() != null) {
            where.add("p.idPedido in (select i.pedido.idPedido from ItemPedido i, Produto pr, GrupoProduto g where p.idPedido = i.pedido.idPedido and i.produto.idProduto = pr.idProduto and g.idCodGrupo = pr.grupoProduto.idCodGrupo and g.idCodGrupo = :grupo)");
            params.put("grupo", pedido.getGrupoProduto().getIdCodGrupo());
        }
        if (pedido.getDtNotaIni() != null) {
            where.add("a.dtNota between :dtNota1 and :dtNota2");
            params.put("dtNota1", pedido.getDtNotaIni());
            params.put("dtNota2", pedido.getDtNotaEnd());
        }
        if (pedido.getDtPgtoNotaIni() != null) {
            //where.add("p.idPedido in (select a.atendimentoPedidoPK.idPedido from AtendimentoPedido a where a.atendimentoPedidoPK.idPedido = p.idPedido and a.dtPgtoComissao between :dtPgto1 and :dtPgto2)");
            where.add("a.dtPgtoComissao between :dtPgto1 and :dtPgto2");
            params.put("dtPgto1", pedido.getDtPgtoNotaIni());
            params.put("dtPgto2", pedido.getDtPgtoNotaEnd());
        }
        
        if ("P".equals(pedido.getSituacao())) {
            where.add("a.dtPgtoComissao is not null");
        } else if ("S".equals(pedido.getSituacao())) {
            where.add("(a.dtPgtoComissao is null or (p.situacao = :situacaoPedido))");
            params.put("situacaoPedido", "N");
        } else if ("N".equals(pedido.getSituacao())) {
            where.add("p.situacao = :situacaoPedido");
            params.put("situacaoPedido", "N");
        }

        for (String item : where) {
            sb.append(" and ");
            sb.append(item);
        }
        
        sb.append(" order by ");
        
        switch (pedido.getOrdem()) {
            case 1: //embarque
                sb.append(" p.repres.razao, p.dtEntrega");
                break;
            case 2: //pedido
                sb.append(" p.repres.razao, p.idPedido, a.atendimentoPedidoPK.nf");
                break;
            case 3: //dtnota
                sb.append(" p.repres.razao, a.dtNota, a.atendimentoPedidoPK.nf");
                break;
            case 4: //dtpedido
                sb.append(" p.repres.razao, p.dtPedido, p.idPedido");
                break;
            case 5: //nf
                sb.append(" p.repres.razao, a.atendimentoPedidoPK.nf");
                break;
            default: //cliente
                sb.append(" p.repres.razao, p.cliente.razao, p.idPedido");
                break;
        }
        //getLogger().info(sb.toString());
        Query q = ManagerFactory.getEntityManager().createQuery(sb.toString());

        Iterator iterator = params.keySet().iterator();
        while (iterator.hasNext()) {
            String s = (String) iterator.next();
            q.setParameter(s, params.get(s));

        }
        
        return q.getResultList();
    }

    public List<MapaComissao> findMapaComissao(MapaFilterBean cliente) {
        Date ini, end;
        int ano = cliente.getAno();
        try {
            ini = DateUtils.parse("01/01/" + ano);
            end = DateUtils.parse("31/12/" + ano);
        } catch (Exception e) {
            return null;
        }

        getLogger().info("findMapaComissao ini");
        StringBuilder sb = new StringBuilder();
        if (!cliente.getVendas()) {
            sb.append("select razao, extract(month from dtpedido), sum(comissaoreal) from (").append("\n");
            sb.append("   select razao, dtpedido, valorvenda, situacao, comissaovenda,").append("\n");
            sb.append("      case when (situacao = 'A' or atend = 'P') then comissaonf else comissaovenda end comissaoreal").append("\n");
            sb.append("   from (").append("\n");
            sb.append("      select p.dtpedido, r.idrepres, r.razao, p.situacao, p.atend, p.valor valorvenda,").append("\n");
            sb.append("                 p.valorcomissao + (case when p.valorop is null then 0 else p.valorop end) comissaovenda,").append("\n");
            sb.append("                (select sum(a.valorcom) from tbatendimentopedido a where p.idpedido = a.idpedido) comissaonf").append("\n");
            sb.append("             from tbpedido p, tbrepres r").append("\n");
            sb.append("     where p.idRepres = r.idRepres and (p.dtPedido between :ini and :end)").append("\n");
            sb.append("   )").append("\n");
            sb.append(") group by 1,2").append("\n");
        } else {
            sb.append("select r.razao, extract(month from p.dtpedido), sum(p.valorcomissao + (case when p.valorop is null then 0 else p.valorop end)) comissaovenda ");
            sb.append(" from tbpedido p, tbrepres r ");
            sb.append(" where p.idrepres = r.idrepres and (p.dtPedido between :ini and :end)");
            sb.append(" group by 1,2").append("\n");
        }
        EntityManager em = ManagerFactory.getEntityManager();
        String s = sb.toString();
        Query q = em.createNativeQuery(s);
        q.setParameter("ini", ini);
        q.setParameter("end", end);
        List<Object[]> lista = q.getResultList();

        Map<String, MapaComissao> mapa = new HashMap<String, MapaComissao>();
        MapaComissao o;
        for (Object[] item : lista) {
            o = mapa.get((String) item[0]);
            if (o == null) {
                o = new MapaComissao();
                o.setRazao((String) item[0]);
                mapa.put((String) item[0], o);
            }
            switch ((Short) item[1]) {
                case 1:
                    o.setMes1((BigDecimal) item[2]);
                    break;
                case 2:
                    o.setMes2((BigDecimal) item[2]);
                    break;
                case 3:
                    o.setMes3((BigDecimal) item[2]);
                    break;
                case 4:
                    o.setMes4((BigDecimal) item[2]);
                    break;
                case 5:
                    o.setMes5((BigDecimal) item[2]);
                    break;
                case 6:
                    o.setMes6((BigDecimal) item[2]);
                    break;
                case 7:
                    o.setMes7((BigDecimal) item[2]);
                    break;
                case 8:
                    o.setMes8((BigDecimal) item[2]);
                    break;
                case 9:
                    o.setMes9((BigDecimal) item[2]);
                    break;
                case 10:
                    o.setMes10((BigDecimal) item[2]);
                    break;
                case 11:
                    o.setMes11((BigDecimal) item[2]);
                    break;
                case 12:
                    o.setMes12((BigDecimal) item[2]);
                    break;
            }
        }
        List<MapaComissao> result = new ArrayList<MapaComissao>();
        Set<Map.Entry<String, MapaComissao>> contactValues = mapa.entrySet();
        Iterator<Map.Entry<String, MapaComissao>> contactIterator = contactValues.iterator();
        while (contactIterator.hasNext()) {
            Map.Entry<String, MapaComissao> anEntry = contactIterator.next();
            result.add(anEntry.getValue());
        }
        Collections.sort(result, new MapaComparator());
        return result;
    }

    public List[] findComissaoResumo(PedidoFilter pedido) {
        getLogger().info("findComissaoResumo ini");
        StringBuilder sb = new StringBuilder();
        HashMap<String, Object> params = new HashMap<String, Object>();
        
        sb.append("select r.razao representada, v.nome vendedor,  sum(z.valorvenda) as valorVenda, sum(z.comissaovenda) as comissaovenda,").append("\n");
        sb.append("   sum(z.comvendedor) as comissaoVendedor, sum(z.comempresa) as comissaoEmpresa, sum(z.comissaoreal) as comissaoTotal, z.idcliente").append("\n");
        sb.append(" from tbrepres r, tbvendedor v, (").append("\n");
        sb.append("    select pedido.*,").append("\n");
        sb.append("       case when pedido.recebe = '1' then pedido.comissaoreal * pedido.comissven / 100 else 0 end comvendedor,").append("\n");
        sb.append("       case pedido.recebe when '1' then pedido.comissaoreal - (pedido.comissaoreal * pedido.comissven / 100) else pedido.comissaoreal end comempresa").append("\n");
        sb.append("    from (").append("\n");
        sb.append("        select idpedido, idcliente, idrepres, idvendedor, valorvenda, comissao, comissven, recebe, situacao, comissaovenda,").append("\n");
        if ("S".equals(pedido.getSituacao())) {
            sb.append("           case when (situacao = 'A' or atend = 'P') then saldo else comissaovenda end comissaoreal").append("\n");
        } else {
            sb.append("           case when (situacao = 'A' or atend = 'P') then comissaonf else comissaovenda end comissaoreal").append("\n");
        }
        sb.append("        from (").append("\n");
        sb.append("            select p.idpedido, p.idcliente, p.idRepres, p.idVendedor, p.valor valorvenda, p.comissao, p.comissven, p.situacao, p.atend, v.comissao recebe,").append("\n");
        sb.append("                         p.valorcomissao + (case when p.valorop is null then 0 else p.valorop end) comissaovenda,").append("\n");
        sb.append("                        (select sum(a.valorcom) from tbatendimentopedido a where p.idpedido = a.idpedido").append("\n");
        if ("P".equals(pedido.getSituacao())) {
            sb.append("                           and a.dtCom is not null").append("\n");
        }
        sb.append("                        ) comissaonf,").append("\n");
        sb.append("                        (select sum(a.valorcom) from tbatendimentopedido a where p.idpedido = a.idpedido and a.dtCom is null) saldo").append("\n");
        sb.append("                     from tbpedido p, tbvendedor v").append("\n");
        sb.append("                     where p.idvendedor = v.idvendedor").append("\n");

        if (pedido.getDtEmissaoIni() != null) {
            sb.append("             and (p.dtPedido between :dtemissao1 and :dtemissao2)").append("\n");
            params.put("dtemissao1", pedido.getDtEmissaoIni());
            params.put("dtemissao2", pedido.getDtEmissaoEnd());
        }
        if (pedido.getVendedor() != null && pedido.getVendedor().getIdVendedor() != null) {
            sb.append("             and p.idVendedor = :vendedor").append("\n");
            params.put("vendedor", pedido.getVendedor().getIdVendedor());
        }
        if (pedido.getRepres().getIdRepres() != null) {
            sb.append("             and p.idRepres = :repres").append("\n");
            params.put("repres", pedido.getRepres().getIdRepres());
        }
        if (pedido.getCliente().getIdCliente() != null) {
            sb.append("             and p.idCliente = :cliente").append("\n");
            params.put("cliente", pedido.getCliente().getIdCliente());
        }
        if (pedido.getDtNotaIni() != null) {
            sb.append("             and p.idPedido in (select a.idPedido from tbAtendimentoPedido a where p.idPedido = a.idPedido and a.dtNota between :dtNota1 and :dtNota2)").append("\n");
            params.put("dtNota1", pedido.getDtNotaIni());
            params.put("dtNota2", pedido.getDtNotaEnd());
        }
        if ((pedido.getSubGrupoProduto() != null) && (pedido.getSubGrupoProduto().getIdCodSubGrupo() != null)) {
            sb.append("             and p.idpedido in (select i.idPedido from tbitempedido i, tbproduto pr where i.idpedido = p.idpedido and i.idproduto = pr.idproduto and pr.idcodsubgrupo = :subgrupo)").append("\n");
            params.put("subgrupo", pedido.getSubGrupoProduto().getIdCodSubGrupo());
        } else if (pedido.getGrupoProduto().getIdCodGrupo() != null) {
            sb.append("             and p.idpedido in (select i.idPedido from tbitempedido i, tbproduto pr where i.idpedido = p.idpedido and i.idproduto = pr.idproduto and pr.idcodgrupo = :grupo)").append("\n");
            params.put("grupo", pedido.getGrupoProduto().getIdCodGrupo());
        }
        if (pedido.getDtPgtoNotaIni() != null) {
            sb.append("             and p.idPedido in (select a.idPedido from tbAtendimentoPedido a where p.idPedido = a.idPedido and a.dtCom between :dtPgto1 and :dtPgto2)").append("\n");
            params.put("dtPgto1", pedido.getDtPgtoNotaIni());
            params.put("dtPgto2", pedido.getDtPgtoNotaEnd());
        }
        if ("P".equals(pedido.getSituacao())) {
            sb.append("             and p.idPedido in (select a.idPedido from tbAtendimentoPedido a where p.idPedido = a.idPedido and a.dtCom is not null)").append("\n");
        } else if ("S".equals(pedido.getSituacao())) {
            sb.append("             and (p.idPedido in (select a.idPedido from tbAtendimentoPedido a where p.idPedido = a.idPedido and a.dtCom is null)").append("\n");
            sb.append("                 or (p.situacao = :situacaoPedido))").append("\n");
            params.put("situacaoPedido", "N");
        } else if ("N".equals(pedido.getSituacao())) {
            sb.append("             and p.situacao = :situacaoPedido");
            params.put("situacaoPedido", "N");
        }
        sb.append("         )").append("\n");
        sb.append("     ) pedido").append("\n");
        sb.append(" ) z").append("\n");
        sb.append(" where z.idrepres = r.idrepres").append("\n");
        sb.append(" and z.idvendedor = v.IDVENDEDOR").append("\n");
        sb.append(" group by r.razao, z.idcliente, v.nome").append("\n");
        
        EntityManager em = ManagerFactory.getEntityManager();
        String s = sb.toString();
        if (pedido.getQuebrarSegmento()) {
            sb = new StringBuilder();
            sb.append("select rc.representada, rc.vendedor, rc.valorVenda, rc.comissaovenda, rc.comissaoVendedor, rc.comissaoEmpresa,  ");
            sb.append("       rc.comissaoTotal, seg.nome, rc.idcliente");
            sb.append(" from (").append(s).append(") as rc, tbsegmercado as seg, tbsegmentocliente sc");
            sb.append(" where sc.idsegmento = seg.idsegmento");
            sb.append("       and rc.idcliente = sc.idcliente");
            sb.append(" order by seg.nome, rc.vendedor");
        }
        s = sb.toString();
        
        Query q = em.createNativeQuery(s);

        Iterator iterator = params.keySet().iterator();
        while (iterator.hasNext()) {
            String st = (String) iterator.next();
            q.setParameter(st, params.get(st));

        }

        //getLogger().info(s);
        
        List<Object[]> lista = q.getResultList();
        List<ResumoComissao> resumo = new ArrayList<ResumoComissao>();
        for (Object[] item : lista) {
            ResumoComissao comissao = new ResumoComissao();
            comissao.setRepresentada((String) item[0]);
            comissao.setIdVendedor(0);
            comissao.setVendedor((String) item[1]);
            comissao.setValorVenda((BigDecimal) item[2]);
            comissao.setComissaoVenda((BigDecimal) item[3]);
            if (item[4] != null) {
                comissao.setComissaoVendedor((BigDecimal) item[4]);
            } else {
                comissao.setComissaoVendedor(new BigDecimal(0));
            }
            if (item[5] != null) {
                comissao.setComissaoEmpresa((BigDecimal) item[5]);
            } else {
                comissao.setComissaoEmpresa(new BigDecimal(0));
            }
            if (item[6] != null) {
                comissao.setComissaoTotal((BigDecimal) item[6]);
            } else {
                comissao.setComissaoTotal(new BigDecimal(0));
            }
            if (pedido.getQuebrarSegmento()) {
                comissao.setSegmento((String)item[7]);
            }
            resumo.add(comissao);
        }

        List<String> vendedores = new ArrayList<String>();
        q = em.createNativeQuery("select vendedor, sum(comissaoVendedor) from ( " + s + ") v group by vendedor order by 2 desc");
        iterator = params.keySet().iterator();
        while (iterator.hasNext()) {
            String st = (String) iterator.next();
            q.setParameter(st, params.get(st));
        }
        lista = q.getResultList();
        for (Object[] item : lista) {
            vendedores.add((String) item[0]);
        }
        List[] result = {resumo, vendedores};
        return result;
    }
    
    public List findTotaisDesconto(MovimentoFilter filter) {
        StringBuilder sb = new StringBuilder();
        List<Object[]>  lista = null;
        

        sb.append("select ap.operador, sum(pc.valor / 100 * ap.PERCDESCONTO / 30 * datediff(day from ap.DTDESCONTO to pc.venc)) from tbatendimentopedido ap");
        sb.append(" inner join TBPGTOCLIENTE pc on pc.idpedido = ap.idpedido and pc.NF = ap.nf");
        sb.append(" where (pc.tppgto = 'CH') and pc.idpedido in (select idpedido from tbpgtocliente where (tppgto = 'DS') and (venc between :d1 and :d2))");
        sb.append(" and ap.operador is not null and ap.operador <> ''");
        sb.append(" group by ap.operador");
          
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNativeQuery(sb.toString());
            
            
            q.setParameter("d1", DateUtils.parse(DateUtils.format(filter.getDtInicio())));
            q.setParameter("d2", DateUtils.parse(DateUtils.format(filter.getDtTermino())));
            lista = q.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        UnidadeItem obj;
        List<UnidadeItem> result = new ArrayList<>();
        for (Object[] item : lista) {
            obj = new UnidadeItem(((Character)item[0]).toString(), (BigDecimal)item[1]);
            result.add(obj);
        }
        return result;
    }
    
    public List findClienteSemPedidoPorPrazo(Integer idVendedor, Integer numPrazo, Date dtAtual) {
        StringBuilder sb = new StringBuilder();
        List lista = null;

        sb.append("select p.idpedido from tbPedido p");  
        sb.append(" where p.emailVendEnviado is null ");  
        sb.append(" and p.dtpedido >= :dtinicio and datediff(day, p.Entrega, current_date) >= 30");   
        
        EntityManager em = getEntityManager();
        try {

            Query q = em.createNativeQuery(sb.toString());
            q.setParameter("dtinicio", DateUtils.parse("01/01/2015"));
            lista = q.getResultList();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        
        Session session = ManagerFactory.getSession();
        Criteria criteria = session.createCriteria(Pedido.class);
        
        criteria.add(Restrictions.in("idPedido", lista));
        
        return criteria.list();
    }

    public UltimoPedido findUltPedidoCliente(Integer cliente) {
        //getLogger().info("findUltPedidoCliente ini");
        Session session = ManagerFactory.getSession();
        org.hibernate.Query q = session.getNamedQuery("Pedido.findUltPedidoCliente").setResultTransformer(Transformers.aliasToBean(UltimoPedido.class));
        q.setParameter("cliente", cliente);
        List lista = q.list();
        if ((lista == null) || (lista.isEmpty())) {
            return new UltimoPedido();
        } else {
            return (UltimoPedido) lista.get(0);
        }
    }

    public UltimoPedido findUltPedidoClienteRepres(Integer cliente, Integer repres) {
        //getLogger().info("findUltPedidoClienteRepres ini");
        Session session = ManagerFactory.getSession();
        org.hibernate.Query q = session.getNamedQuery("Pedido.findUltPedidoClienteRepres").setResultTransformer(Transformers.aliasToBean(UltimoPedido.class));
        q.setParameter("cliente", cliente);
        q.setParameter("repres", repres);
        //getLogger().info("cliente: " + cliente + " repres: " + repres);
        List lista = q.list();
        if ((lista == null) || (lista.isEmpty())) {
            return new UltimoPedido();
        } else {
            return (UltimoPedido) lista.get(0);
        }
    }

    public List findPgtosPedido(Integer pedido) {
        getLogger().info("findPgtosPedido ini");
        Query q = ManagerFactory.getEntityManager().createQuery("select o from PgtoCliente where atendimentoPedido.pedido.idPedido = :pedido");
        q.setParameter("pedido", pedido);
        return q.getResultList();
    }

    public List findUltimosPedidos(Integer cliente) {
        getLogger().info("findUltimosPedidos ini");
        StringBuilder sb = new StringBuilder();
        sb.append(" select p.idPedido, p.situacao, p.dtPedido, r.razao, v.nome, p.valor");
        sb.append(" from Pedido p, Repres r, Vendedor v");
        sb.append(" where p.repres.idRepres = r.idRepres");
        sb.append(" and p.vendedor.idVendedor = v.idVendedor");
        sb.append(" and p.cliente.idCliente = :cliente");
        sb.append(" order by p.dtPedido desc");
        Query q = ManagerFactory.getEntityManager().createQuery(sb.toString());
        q.setParameter("cliente", cliente);
        q.setMaxResults(10);
        List lista = q.getResultList();
        return lista;
    }

    public void deletePedido(Integer pedido) throws DAOException {
        getLogger().info("deletePedido ini");
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Query q = em.createQuery("delete from PgtoCliente where atendimentoPedido.atendimentoPedidoPK.idPedido = :pedido");
            q.setParameter("pedido", pedido);
            q.executeUpdate();
            q = em.createQuery("delete from ItemPedidoAtend where itemPedidoAtendPK.idPedido = :pedido");
            q.setParameter("pedido", pedido);
            q.executeUpdate();
            q = em.createQuery("delete from AtendimentoPedido where idPedido = :pedido");
            q.setParameter("pedido", pedido);
            q.executeUpdate();
            q = em.createQuery("delete from ItemPedido where idPedido = :pedido");
            q.setParameter("pedido", pedido);
            q.executeUpdate();
            q = em.createQuery("delete from ArquivoPedido where idPedido = :pedido");
            q.setParameter("pedido", pedido);
            q.executeUpdate();
            q = em.createQuery("delete from PedidoEmbarque where idPedido = :pedido");
            q.setParameter("pedido", pedido);
            q.executeUpdate();
            q = em.createQuery("delete from Pedido where idPedido = :pedido");
            q.setParameter("pedido", pedido);
            q.executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new DAOException(e);
        } finally {
            em.close();
        }
    }

    public void deleteItens(Integer pedido) {
        getLogger().info("deleteItens ini");
        Query q = ManagerFactory.getEntityManager().createQuery("delete from ItemPedido where idPedido = :pedido");
        q.setParameter("pedido", pedido);
        int n = q.executeUpdate();
    }

    public void updateAtendimento(AtendimentoPedido nota) throws DAOException {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Query q = em.createQuery("delete from ItemPedidoAtend where itemPedidoAtendPK.idPedido = :pedido and itemPedidoAtendPK.nf = :nota");
            q.setParameter("pedido", nota.getPedido().getIdPedido());
            q.setParameter("nota", nota.getAtendimentoPedidoPK().getNf());
            q.executeUpdate();
            nota = em.merge(nota);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new DAOException(e);
        } finally {
            em.close();
        }
    }

    public void updateAtendimento(AtendimentoPedido nota, Repres repres) throws DAOException {
        EntityManager em = getEntityManager();
        ManagerFactory.getSession();
        try {
            em.getTransaction().begin();
            Query q; // = em.createQuery("update AtendimentoPedido p set p.total = :vlr where p.idRepres = :idRepres");
            nota = em.merge(nota);
            q = em.createQuery("update Repres p set p.total = :vlr where p.idRepres = :idRepres");
            q.setParameter("vlr", repres.getTotal());
            q.setParameter("idRepres", repres.getIdRepres());
            q.executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new DAOException(e);
        } finally {
            em.close();
        }
    }

    public void updateAtendimentos(List<AtendimentoPedido> atendimentos, Repres repres) throws DAOException {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Query q = em.createQuery("update AtendimentoPedido p set dtPgtoComissao = :dt, recibo = :recibo where atendimentoPedidoPK.idPedido = :pedido and atendimentoPedidoPK.nf = :nota");
            for (AtendimentoPedido nota : atendimentos) {
                if (nota.getDtPgtoComissao() != null) {
                    getLogger().info("atualizando " + nota);
                    q.setParameter("dt", nota.getDtPgtoComissao());
                    q.setParameter("recibo", "1");
                    q.setParameter("pedido", nota.getAtendimentoPedidoPK().getIdPedido());
                    q.setParameter("nota", nota.getAtendimentoPedidoPK().getNf());
                    q.executeUpdate();
                }
            }
            q = em.createQuery("update Repres p set p.total = :vlr where p.idRepres = :idRepres");
            q.setParameter("vlr", repres.getTotal());
            q.setParameter("idRepres", repres.getIdRepres());
            q.executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new DAOException(e);
        } finally {
            em.close();
        }
    }

    public void updateEmbarquePedido(Pedido pedido) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        PedidoEmbarque embarque = new PedidoEmbarque();
        embarque.setPedido(pedido);
        embarque.setDtEmbarque(pedido.getDtEntrega());

        Query q;
        EntityTransaction t = em.getTransaction();
        try {
            insertRecord(embarque, em);
            q = em.createQuery("update Pedido set dtEntrega = :novadata where idPedido = :pedido");
            q.setParameter("pedido", pedido.getIdPedido());
            q.setParameter("novadata", pedido.getDtEntrega());
            q.executeUpdate();
            t.commit();
        } catch (Exception e) {
            t.rollback();
        } finally {
            em.close();
        }
    }
    
    public void updatePedido(Object object) throws DAOException {
        Pedido pedido = (Pedido) object;
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Query q = em.createQuery("delete from ItemPedido where pedido.idPedido = :pedido");
            q.setParameter("pedido", pedido.getIdPedido());
            int n = q.executeUpdate();
            //getLogger().info(n);
            object = em.merge(object);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new DAOException(e);
        } finally {
            em.close();
        }
    }

    public List findUnidadesByPedido(Integer pedido) {
        Query q = ManagerFactory.getEntityManager().createQuery("select new vendas.beans.UnidadeItem(p.undCumulativa.idUnidade, sum(i.qtd * p.fatorConversao)) from ItemPedido i, Produto p where i.pedido.idPedido = :pedido and i.produto.idProduto = p.idProduto group by p.undCumulativa.idUnidade");
        q.setParameter("pedido", pedido);
        //q.setMaxResults(batchSize);
        //q.setFirstResult(firstItem);
        List lista = q.getResultList();
        return lista;
    }

    public List findAllUnidadesByPedido(Integer pedido) {
        StringBuilder sb = new StringBuilder(); 
        sb.append("select 1 as tipo, p.idundcumul, sum(p.FATORCNV * ip.QTD) as quant from TBUNIDADEPRODUTO u");
        sb.append("  inner join tbproduto p on p.idunidade = u.idunidade");
        sb.append("  inner join tbitempedido ip on ip.idproduto = p.idproduto and ip.idpedido = :pedido");
        sb.append("  group by 1,2");
        sb.append("  union");
        sb.append("  select 2 as tipo, p.idundcumul, sum(p.FATORCNV * ip.QTD) as quant from TBUNIDADEPRODUTO u");
        sb.append("  inner join tbproduto p on p.idunidade = u.idunidade");
        sb.append("  inner join tbitempedidoatend ip on ip.idproduto = p.idproduto and ip.idpedido = :pedido");
        sb.append("  group by 1,2");
        sb.append("  order by 1,2");
        
        Query q = ManagerFactory.getEntityManager().createNativeQuery(sb.toString());
        q.setParameter("pedido", pedido);
        List<UnidadeItem> unidades = new ArrayList<>();
        List<Object[]> lista = q.getResultList();
        UnidadeItem resumo;
        
        for (Object[] item : lista) {
            resumo = new UnidadeItem((String)item[1], (BigDecimal)item[2]);
            resumo.setTipo((Integer)item[0]);
            unidades.add(resumo);
        }
        
        return unidades;
    }
    
    public List<UnidadeItem> findUndSaldoByPedido(Integer pedido) {
        StringBuilder sb = new StringBuilder();   
        
        sb.append("select p.idundcumul, p.quant, a.quantatend");
        sb.append(" from (");
        sb.append("  select 1 as tipo, p.idundcumul, sum(p.FATORCNV * ip.QTD) as quant from TBUNIDADEPRODUTO u");
        sb.append("  inner join tbproduto p on p.idunidade = u.idunidade");
        sb.append("  inner join tbitempedido ip on ip.idproduto = p.idproduto and ip.idpedido = :pedido");
        sb.append("  group by 1,2");
        sb.append(" ) as p");
        sb.append(" left outer join (");
        sb.append("  select 2 as tipo, p.idundcumul, sum(p.FATORCNV * ip.QTD) as quantatend from TBUNIDADEPRODUTO u");
        sb.append("  inner join tbproduto p on p.idunidade = u.idunidade");
        sb.append("  inner join tbitempedidoatend ip on ip.idproduto = p.idproduto and ip.idpedido = :pedido");
        sb.append("  group by 1,2");
        sb.append(" ) as a on a.idundcumul = p.idundcumul");
  
        Query q = ManagerFactory.getEntityManager().createNativeQuery(sb.toString());
        q.setParameter("pedido", pedido);
        List<UnidadeItem> unidades = new ArrayList<>();
        List<Object[]> lista = q.getResultList();
        UnidadeItem resumo;
        BigDecimal itempedido;
        BigDecimal itematend;
        
        for (Object[] item : lista) {
            itempedido = (BigDecimal)item[1];
            itematend = (BigDecimal)item[2];
            if (itematend == null) itematend = BigDecimal.ZERO;
            resumo = new UnidadeItem((String)item[0], itempedido.subtract(itematend));
            unidades.add(resumo);
        }
        
        return unidades;
    }

    public List findItensPedido(Integer pedido) {
        getLogger().info("findItensPedido ini");
        Query q = ManagerFactory.getEntityManager().createQuery("select o from ItemPedido as o where o.pedido.idPedido = :pedido order by o.produto.descricao ASC");
        q.setParameter("pedido", pedido);
        List lista = q.getResultList();
        return lista;
    }

    public List getUnidadesAtendidas(Integer pedido, String nf) {
        StringBuilder sb = new StringBuilder();
 
        sb.append("select pr.IDUNDCUMUL, sum(i.qtd * pr.FATORCNV)");
        sb.append(" from tbproduto pr");
        sb.append(" inner join tbitempedidoatend i on i.idproduto = pr.idproduto");
        sb.append(" where i.idpedido = :pedido and i.NF = :nf");
        sb.append(" group by pr.IDUNDCUMUL");
        
        Query q = ManagerFactory.getEntityManager().createNativeQuery(sb.toString());
        q.setParameter("pedido", pedido);
        q.setParameter("nf", nf);
        List<Object[]> lista = q.getResultList();
        List<UnidadeItem> result = new ArrayList<>();
        UnidadeItem resumo;
        
        for (Object[] item : lista) {
            resumo = new UnidadeItem((String)item[0], (BigDecimal)item[1]);
            result.add(resumo);
        }
        return result;
    }
    
    public Pedido findPedidoRepres(Integer pedido) {
        getLogger().info("findPedidoRepres ini");
        Query q = ManagerFactory.getEntityManager().createQuery("select o from Pedido as o where o.idPedidoRepres = :pedido");
        q.setParameter("pedido", pedido);
        q.setMaxResults(1);
        //q.setFirstResult(firstItem);
        return (Pedido) q.getSingleResult();
    }

    public List findPgtosResumo(CobrancaFilter filter) {
        getLogger().info("findPgtosResumo ini");
        Session session = ManagerFactory.getSession();
        HashMap<String, Object> params = new HashMap<String, Object>();

        StringBuilder sb = new StringBuilder();
        sb.append("select r.razao, c.razao, sum(g.valor)");
        sb.append("   from PgtoCliente g, Pedido p, Repres r, Cliente c");
        sb.append("   where g.atendimentoPedido.pedido.idPedido = p.idPedido and p.repres.idRepres = r.idRepres and p.cliente.idCliente = c.idCliente");

        if ("P".equals(filter.getSituacao())) {
            sb.append("   and g.dtPgto != null");
        } else if ("N".equals(filter.getSituacao())) {
            sb.append("   and g.dtPgto is null");
        }
        if (filter.getDtEmissaoIni() != null) {
            sb.append("   and p.dtPedido between :d1 and :d2");
            params.put("d1", filter.getDtEmissaoIni());
            params.put("d2", filter.getDtEmissaoEnd());
        }
        if (filter.getDtVencimentoIni() != null) {
            sb.append("   and g.dtVencimento between :v1 and :v2");
            params.put("v1", filter.getDtVencimentoIni());
            params.put("v2", filter.getDtVencimentoEnd());
        }
        if (filter.getDtPgtoIni() != null) {
            sb.append("   and g.dtPgto between :g1 and :g2");
            params.put("g1", filter.getDtPgtoIni());
            params.put("g2", filter.getDtPgtoEnd());
        }
        if (filter.getDtPrevPgtoIni() != null) {
            sb.append("   and g.dtPrevisaoPgto between :p1 and :p2");
            params.put("p1", filter.getDtPrevPgtoIni());
            params.put("p2", filter.getDtPrevPgtoEnd());
        }
        if ((filter.getTipoPgto() != null) && (filter.getTipoPgto().length() > 0)) {
            sb.append("   and g.tipoPgto in (:tipo)");
            params.put("tipo", filter.getTipoPgto());
        }
        if ((filter.getVendedor() != null) && (filter.getVendedor().getIdVendedor() != null)) {
            sb.append("   and p.vendedor.idVendedor = :vendedor");
            params.put("vendedor", filter.getVendedor().getIdVendedor());
        }
        if ((filter.getCliente() != null) && (filter.getCliente().getIdCliente() != null)) {
            sb.append("   and p.cliente.idCliente = :cliente");
            params.put("cliente", filter.getCliente().getIdCliente());
        }
        if ((filter.getRepres() != null) && (filter.getRepres().getIdRepres() != null)) {
            sb.append("   and p.repres.idRepres = :repres");
            params.put("repres", filter.getRepres().getIdRepres());
        }
        sb.append("   group by r.razao, c.razao");

        org.hibernate.Query q = session.createQuery(sb.toString());
        Iterator iterator = params.keySet().iterator();
        while (iterator.hasNext()) {
            String s = (String) iterator.next();
            q.setParameter(s, params.get(s));

        }
        List<ResumoVendasUnidade> result = new ArrayList<>();
        List<Object[]> lista = q.list();
        ResumoVendasUnidade resumo;
        for (Object[] item : lista) {
            resumo = new ResumoVendasUnidade();
            resumo.setRepresentada((String) item[0]);
            resumo.setCliente((String) item[1]);
            resumo.setValor((BigDecimal) item[2]);
            result.add(resumo);
        }
        return result;

    }

    public List getPgtosPendentes(ClientesFilter filter) {
        getLogger().info("getPgtosPendentes ini");
        StringBuilder sb = new StringBuilder();
        sb.append("select p.vendedor.nome, p.cliente.razao, sum(o.valor) from PgtoCliente o, Pedido p where o.dtPgto is null and o.atendimentoPedido.atendimentoPedidoPK.idPedido = p.idPedido");
        if (filter.getVendedor().getIdVendedor() != null) {
            sb.append(" and p.vendedor.idVendedor = " + filter.getVendedor().getIdVendedor());
        }
        sb.append("  group by p.vendedor.nome, p.cliente.razao");
        switch (filter.getOrder()) {
            case 1:
                sb.append(" order by p.cliente.razao");
                break;
            case 2:
                sb.append(" order by 3");
                if (filter.getDecrescente()) {
                    sb.append(" desc");
                }
                break;
            default:
                sb.append(" order by p.vendedor.nome, p.cliente.razao");
                break;
        }
        EntityManager em = getEntityManager();
        List<Object[]> lista = em.createQuery(sb.toString()).getResultList();
        List<VendasCliente> result = new ArrayList<VendasCliente>();
        VendasCliente resumo;
        for (Object[] item : lista) {
            resumo = new VendasCliente();
            resumo.setVendedor((String) item[0]);
            resumo.setRAZAO((String) item[1]);
            resumo.setVALORVENDA((BigDecimal) item[2]);
            result.add(resumo);
        }
        return result;
    }
    
    public BigDecimal getTotalItemAtendido(ItemPedidoAtend item) {
        getLogger().info("getTotalItemAtendido ini");
        return getSaldoItem(item.getItemPedidoAtendPK().getIdPedido(), item.getItemPedidoAtendPK().getIdProduto());
    }

    public BigDecimal getSaldoItem(Integer pedido, Integer produto) {
        getLogger().info("getSaldoItem ini");
        StringBuilder sb = new StringBuilder();
        BigDecimal result;
  
        sb.append("select qtd - case when a is null then 0 else a end as b from ("); 
        sb.append("select i.qtd, (select sum(a.qtd) as atendido from tbitempedidoatend a where a.idpedido = i.idpedido and a.idproduto = i.idproduto) as a"); 
        sb.append(" from tbitempedido i"); 
        sb.append(" where i.idpedido = :pedido and i.idproduto = :produto)");
        EntityManager em = getEntityManager();
        
        try {
            Query q = em.createNativeQuery(sb.toString());
            q.setParameter("pedido", pedido);
            q.setParameter("produto", produto);
            result = (BigDecimal) q.getSingleResult();

        } catch (Exception e) {
            e.printStackTrace();
            result = null;
        } finally {
            em.close();
        }
        return result;
    }
        
    public BigDecimal getPgtosClientePendende(Integer idCliente) {
        getLogger().info("getPgtosClientePendende ini");
        StringBuilder sb = new StringBuilder();
        sb.append("select sum(o.valor) from PgtoCliente o, Pedido p where o.dtPgto is null and o.atendimentoPedido.atendimentoPedidoPK.idPedido = p.idPedido and p.cliente.idCliente = ").append(idCliente);
        EntityManager em = getEntityManager();
        return (BigDecimal) em.createQuery(sb.toString()).getSingleResult();
    }

    public List findPgtosCliente(CobrancaFilter filter) {
        getLogger().info("findPgtosCliente ini");
        Session session = ManagerFactory.getSession();

        Criteria criteria = session.createCriteria(PgtoCliente.class);
        Criteria atendCriteria = criteria.createCriteria("atendimentoPedido");
        Criteria pedidoCriteria = atendCriteria.createCriteria("pedido");
        Criteria clienteCriteria = null;

        if (filter.getOperador() != null && filter.getOperador().length() > 0) {
            atendCriteria.add(Restrictions.eq("operador", filter.getOperador()));
        }

        if ("P".equals(filter.getSituacao())) {
            criteria.add(Restrictions.isNotNull("dtPgto"));
        } else if ("N".equals(filter.getSituacao())) {
            criteria.add(Restrictions.isNull("dtPgto"));
        } else if ("E".equals(filter.getSituacao())) {
            criteria.add(Restrictions.isNull("dtPgto"));
            //criteria.add(Restrictions.le("dtVencimento", DateUtils.getNextDate(new Date(), 7)));
            criteria.add(Restrictions.le("dtVencimento", DateUtils.getDate()));
        }
        if (filter.getDtEmissaoIni() != null) {
            pedidoCriteria.add(Restrictions.between("dtPedido", filter.getDtEmissaoIni(), filter.getDtEmissaoEnd()));
        }
        if (filter.getDtEmissaoIni() != null) {
            pedidoCriteria.add(Restrictions.between("dtPedido", filter.getDtEmissaoIni(), filter.getDtEmissaoEnd()));
        }
        if (filter.getDtDescontoIni() != null) {
            atendCriteria.add(Restrictions.between("dtDesconto", filter.getDtDescontoIni(), filter.getDtDescontoEnd()));
        }
        if (filter.getDtVencimentoIni() != null && filter.getDtVencimentoEnd() != null) {
            criteria.add(Restrictions.between("dtVencimento", filter.getDtVencimentoIni(), filter.getDtVencimentoEnd()));
        } else if (filter.getDtVencimentoEnd() != null) {
            criteria.add(Restrictions.le("dtVencimento", filter.getDtVencimentoEnd()));
        }
        if (filter.getDtPgtoIni() != null) {
            criteria.add(Restrictions.between("dtPgto", filter.getDtPgtoIni(), filter.getDtPgtoEnd()));
        }
        if (filter.getDtPrevPgtoIni() != null) {
            criteria.add(Restrictions.between("dtPrevisaoPgto", filter.getDtPrevPgtoIni(), filter.getDtPrevPgtoEnd()));
        }
        if ((filter.getTipoPgto() != null) && (filter.getTipoPgto().length() > 0)) {
            String[] args = filter.getTipoPgto().split(",");
            
            criteria.add(Restrictions.in("tipoPgto", args));
        }
        if ((filter.getVendedor() != null) && (filter.getVendedor().getIdVendedor() != null)) {
            pedidoCriteria.createCriteria("vendedor").add(Restrictions.eq("idVendedor", filter.getVendedor().getIdVendedor()));
        }
        boolean isCliente = ((filter.getCliente() != null) && (filter.getCliente().getIdCliente() != null));
        boolean isResponsavel = ((filter.getResponsavel() != null) && (filter.getResponsavel().getIdCliente() != null));

        if (!isCliente && isResponsavel) {
            pedidoCriteria.add(Restrictions.eq("clienteResponsavel.idCliente", filter.getResponsavel().getIdCliente()));
        } else if (isCliente  && !isResponsavel) {
            pedidoCriteria.add(Restrictions.or(Restrictions.eq("cliente.idCliente", filter.getCliente().getIdCliente()), Restrictions.eq("clienteResponsavel.idCliente", filter.getCliente().getIdCliente())));

        } else if (isCliente && isResponsavel) {
            pedidoCriteria.add(Restrictions.eq("cliente.idCliente", filter.getCliente().getIdCliente()));            
            pedidoCriteria.add(Restrictions.eq("clienteResponsavel.idCliente", filter.getResponsavel().getIdCliente()));            
        }

        Criteria represCriteria = pedidoCriteria.createCriteria("repres");
        if ((filter.getRepres() != null) && (filter.getRepres().getIdRepres() != null)) {
            represCriteria.add(Restrictions.eq("idRepres", filter.getRepres().getIdRepres()));
        }
        if (filter.getGrupo().getIdGrupoCliente() != null) {
            if (clienteCriteria == null) {
                clienteCriteria = pedidoCriteria.createCriteria("cliente");
            }
            Criteria grpcli = clienteCriteria.createCriteria("gruposCliente");
            grpcli.add(Restrictions.eq("idGrupoCliente", filter.getGrupo().getIdGrupoCliente()));
            //Restrictions.sqlRestriction(sql)
        }
        if (filter.getOrdem() == 0) {
            criteria.addOrder(Order.asc("dtVencimento"));
        } else if (filter.getOrdem() == 1) {
            criteria.addOrder(Order.asc("dtPgto"));
        } else if (filter.getOrdem() == 2) {
            if (clienteCriteria == null) {
                clienteCriteria = pedidoCriteria.createCriteria("cliente");
            }
            clienteCriteria.addOrder(Order.asc("razao"));
        }
        //atendCriteria.addOrder(Order.asc("atendimentoPedidoPK.nf"));
        return criteria.list();
    }

    public List findCompradosNota(PedidoFilter filter) {
        getLogger().info("findCompradosNota ini");
        Session session = ManagerFactory.getSession();
        StringBuilder sb = new StringBuilder();

        sb.append(" select c.razao as cliente, r.razao as fornecedor, pr.descricao, p.dtpedido, p.situacao, p.atend, pr.fatorcnv,");
        sb.append(" ip.idpedido, ip.idproduto, ip.valor, ip.dtentrega, ip.valorcliente, ip.qtd, ip.qtd * pr.fatorcnv as undentregue, ipa.qtdentregue,");
        sb.append(" p.idpedidorepres");
        sb.append(" from tbitempedido ip");
        
        if (filter.getDtNotaIni() != null) {
            sb.append(" inner join (");
        } else {
            sb.append(" left outer join (");
        }
        
        sb.append("     select ipa.idpedido, ipa.idproduto, sum(ipa.qtd) as qtdentregue ");
        sb.append("     from tbitempedidoatend ipa");
        sb.append("     inner join tbatendimentopedido pa on pa.idpedido = ipa.idpedido and pa.nf = ipa.nf");
        
        if (filter.getDtNotaIni() != null) {
            sb.append("     where pa.dtnota between :dtnotaini and :dtnotaend");
        }
        
        sb.append("     group by ipa.idpedido, ipa.idproduto");
        sb.append(" ) ipa on ipa.idpedido = ip.idpedido and ipa.idproduto = ip.idproduto");
        sb.append(" inner join tbpedido p on p.idpedido = ip.idpedido ");
        sb.append(" inner join tbcliente c on c.idcliente = p.idcliente");
        sb.append(" inner join tbrepres r on r.idrepres = p.idrepres ");
        sb.append(" inner join tbproduto pr on pr.idproduto = ip.idproduto");
        sb.append(" inner join tbsitcliente sc on sc.idsitcliente = c.idsitcliente");  
        sb.append(" where 1 = 1");
        
        if (filter.getGrupo().getIdGrupoCliente() != null) {
            sb.append(" and p.idCliente in (select cl.idCliente from tbcliente cl inner join tbClientegrupo s on s.idcliente = cl.idcliente where s.idgrpcliente = :grupoCliente)");
        }
        if (filter.getDtEmissaoIni() != null) {
            sb.append(" and p.dtPedido between :dtPedido1 and :dtPedido2");
        }
        if (filter.getDtEntregaIni() != null) {
            sb.append(" and ip.dtEntrega between :dtEntrega1 and :dtEntrega2");
        }
        if ((filter.getProduto() != null) && (filter.getProduto().getIdProduto() != null)) {
            sb.append(" and ip.idProduto = :produto");
        } else if ((filter.getSubGrupoProduto() != null) && (filter.getSubGrupoProduto().getIdCodSubGrupo() != null)) {
            sb.append(" and pr.idCodSubGrupo = :subgrupo");
        } else if (filter.getGrupoProduto().getIdCodGrupo() != null) {
            sb.append(" and pr.idCodGrupo = :grupo");
        }
        if (filter.getVendedor().getIdVendedor() != null) {
            sb.append(" and p.idVendedor = :vendedor");
        }
        
        if (filter.getSubGrupos() != null) {
            sb.append(" and pr.idCodSubGrupo in (").append(StringUtils.listAsString(filter.getSubGrupos())).append(")");
        }

        boolean isCliente = filter.getCliente().getIdCliente() != null;
        boolean isResponsavel = filter.getResponsavel().getIdCliente() != null;

        if (isCliente || isResponsavel) {
            if (isCliente) {
                sb.append(" and p.idCliente = :cliente");
            } else {
                sb.append(" and p.idClienteRes = :responsavel");
            }
        } else {
            sb.append(" and sc.pedido = '1'");
        }

        if (filter.getRepres().getIdRepres() != null) {
            sb.append(" and p.idRepres = :repres");
        } else {
            sb.append(" and r.inativo = '0'");
        }
        if (filter.getSituacao() != null) 
            switch (filter.getSituacao()) {
            case "A":
                sb.append(" and ip.situacao in ('A', 'P')");
                break;
            case "N":
                sb.append(" and ip.situacao in ( 'N', 'P')");
                break;
        }
        
        if (filter.getOrdem() == 0) {
            sb.append(" order by c.razao, pr.descricao, ip.dtEntrega");
        } else if (filter.getOrdem() == 1) {
            sb.append(" order by c.razao, ip.dtEntrega, pr.descricao");
        } else if (filter.getOrdem() == 2) {
            sb.append(" order by c.razao, p.idPedido, pr.descricao");
        }
        String sql = sb.toString();
        getLogger().info(sql);
        SQLQuery q = (SQLQuery) session.createSQLQuery(sql);

        if (filter.getGrupo().getIdGrupoCliente() != null) {
            q.setParameter("grupoCliente", filter.getGrupo().getIdGrupoCliente());
        }
        if (filter.getDtEmissaoIni() != null) {
            q.setParameter("dtPedido1", filter.getDtEmissaoIni());
            q.setParameter("dtPedido2", filter.getDtEmissaoEnd());
        }
        if (filter.getDtEntregaIni() != null) {
            q.setParameter("dtEntrega1", filter.getDtEntregaIni());
            q.setParameter("dtEntrega2", filter.getDtEntregaEnd());
        }

        if ((filter.getProduto() != null) && (filter.getProduto().getIdProduto() != null)) {
            q.setParameter("produto", filter.getProduto().getIdProduto());
        } else if ((filter.getSubGrupoProduto() != null) && (filter.getSubGrupoProduto().getIdCodSubGrupo() != null)) {
            q.setParameter("subgrupo", filter.getSubGrupoProduto().getIdCodSubGrupo());
        } else if (filter.getGrupoProduto().getIdCodGrupo() != null) {
            q.setParameter("grupo", filter.getGrupoProduto().getIdCodGrupo());
        }
        if (filter.getVendedor().getIdVendedor() != null) {
            q.setParameter("vendedor", filter.getVendedor().getIdVendedor());
        }
        if (filter.getCliente().getIdCliente() != null) {
            q.setParameter("cliente", filter.getCliente().getIdCliente());
        }
        if (filter.getResponsavel().getIdCliente() != null) {
            q.setParameter("responsavel", filter.getResponsavel().getIdCliente());
        }
        if (filter.getRepres().getIdRepres() != null) {
            q.setParameter("repres", filter.getRepres().getIdRepres());
        }
        
        if (filter.getDtNotaIni() != null) {
            q.setParameter("dtnotaini", filter.getDtNotaIni());
            q.setParameter("dtnotaend", filter.getDtNotaEnd());
        }

        List<Object[]> lista = q.list();
        List<ProdutosComprados> comprados = new ArrayList<>();
        
        for (Object[] item : lista) {
            ProdutosComprados pc = new ProdutosComprados();
            pc.setCliente((String) item[0]);
            pc.setRepres((String) item[1]);
            pc.setDescricao((String) item[2]);
            pc.setDtPedido((Date) item[3]);
            pc.setSituacao((String) item[4]);
            pc.setAtendimento((String) item[5]);
            pc.setFatorConversao((BigDecimal) item[6]);
            pc.setPedido((Integer) item[7]);
            pc.setProduto(item[8].toString());
            pc.setValor((BigDecimal) item[9]);
            pc.setDtEntrega((Date) item[10]);
            pc.setValorCliente((BigDecimal) item[11]);
            pc.setQtd((BigDecimal) item[12]);
            pc.setQtdEntrega((BigDecimal) item[14]);
            pc.setPedidoRepres((Integer) item[15]);
            
            if (pc.getQtdEntrega() == null) {
                pc.setQtdEntrega(new BigDecimal(0));
            }
            
            pc.setQtdSaldo(pc.getQtd().subtract(pc.getQtdEntrega()));
            
            comprados.add(pc);
        }
        return comprados;
    }

    public List findClientesGrupo(PedidoFilter filter) {
        getLogger().info("findClientesGrupo ini");
        Session session = ManagerFactory.getSession();
        StringBuilder sb = new StringBuilder();
        sb.append("select grupos.representada, grupos.cliente, grupos.nomegrupo, grupos.nome, p.idpedido, grupos.dtpedido, grupos.idCliente");
        sb.append(" from tbpedido p, (");
        sb.append(" select pedido.idRepres, pedido.idCliente, pedido.idVendedor, repres.razao as representada, grupo.nomeGrupo, cliente.razao as cliente, vendedor.nome, max(pedido.dtpedido) as dtpedido");
        sb.append("  from tbpedido pedido, tbvendedor vendedor, tbcliente cliente, tbsitcliente sitcliente, tbitempedido item, tbgrupoproduto grupo, tbproduto produto, tbrepres repres");
        sb.append("  where pedido.idcliente = cliente.idcliente");
        sb.append("  and pedido.idrepres = repres.idrepres");
        sb.append("  and pedido.idvendedor = vendedor.idvendedor");
        sb.append("  and cliente.idsitcliente = sitcliente.idsitcliente");
        sb.append("  and sitcliente.pedido = '1'");
        sb.append("  and pedido.idpedido = item.idpedido");
        sb.append("  and item.idproduto = produto.idproduto");
        sb.append("  and produto.idcodgrupo = grupo.idcodgrupo");
        if (filter.getDtEmissaoIni() != null) {
            sb.append("  and pedido.dtPedido between :dtPedido1 and :dtPedido2");
        }
        if (filter.getGrupoProduto().getIdCodGrupo() != null) {
            sb.append("  and grupo.idCodGrupo = :grupo");
        }
        if ((filter.getSubGrupoProduto() != null) && (filter.getSubGrupoProduto().getIdCodSubGrupo() != null)) {
            sb.append("  and produto.idCodSubGrupo = :subgrupo");
        }
        if (filter.getRepres().getIdRepres() != null) {
            sb.append(" and repres.idRepres = :repres");
        } else {
            sb.append(" and repres.inativo = '0'");
        }
        sb.append("  group by pedido.idRepres, pedido.idCliente, pedido.idVendedor, repres.razao, grupo.nomeGrupo, cliente.razao, vendedor.nome");
        sb.append(" ) grupos");
        sb.append(" where");
        sb.append("     p.idRepres = grupos.idRepres");
        sb.append("     and p.idCliente = grupos.idCliente");
        sb.append("     and p.idVendedor = grupos.idVendedor");
        sb.append("     and p.dtPedido = grupos.dtPedido");
        sb.append(" order by grupos.representada, grupos.nomegrupo, grupos.cliente, grupos.nome");

        SQLQuery q = (SQLQuery) session.createSQLQuery(sb.toString());

        if (filter.getDtEmissaoIni() != null) {
            q.setParameter("dtPedido1", filter.getDtEmissaoIni());
            q.setParameter("dtPedido2", filter.getDtEmissaoEnd());
        }
        if (filter.getGrupoProduto().getIdCodGrupo() != null) {
            q.setParameter("grupo", filter.getGrupoProduto().getIdCodGrupo());
        }
        if ((filter.getSubGrupoProduto() != null) && (filter.getSubGrupoProduto().getIdCodSubGrupo() != null)) {
            q.setParameter("subgrupo", filter.getSubGrupoProduto().getIdCodSubGrupo());
        }
        if (filter.getRepres().getIdRepres() != null) {
            q.setParameter("repres", filter.getRepres().getIdRepres());
        }
        List<Object[]> lista = q.list();
        List<ClientesGrupo> clienteGrupo = new ArrayList<ClientesGrupo>();
        for (Object[] item : lista) {
            ClientesGrupo pc = new ClientesGrupo();
            pc.setRepresentada((String) item[0]);
            pc.setCliente((String) item[1]);
            pc.setNomeGrupo((String) item[2]);
            pc.setVendedor((String) item[3]);
            pc.setPedido((Integer) item[4]);
            pc.setDtPedido((Date) item[5]);
            pc.setIdCliente((Integer)item[6]);
            clienteGrupo.add(pc);
        }
        return clienteGrupo;
    }

    public List findComprados(PedidoFilter filter) {
        getLogger().info("findComprados ini");
        if (filter.getDtNotaIni() != null) {
            return findCompradosNota(filter);
        }
        Session session = ManagerFactory.getSession();
        Criteria criteria = session.createCriteria(ItemPedido.class);
        Criteria pedidoCriteria = criteria.createCriteria("pedido");
        if (filter.getDtEmissaoIni() != null) {
            pedidoCriteria.add(Restrictions.between("dtPedido", filter.getDtEmissaoIni(), filter.getDtEmissaoEnd()));
        }
        if (filter.getDtEntregaIni() != null) {
            criteria.add(Restrictions.between("dtEntrega", filter.getDtEntregaIni(), filter.getDtEntregaEnd()));
        }
        Criteria atendCriteria;
        if (filter.getDtNotaIni() != null) {
            atendCriteria = pedidoCriteria.createCriteria("atendimentos");
            atendCriteria.add(Restrictions.between("dtNota", filter.getDtNotaIni(), filter.getDtNotaEnd()));
        }
        Criteria produtoCriteria = criteria.createCriteria("produto");
        if ((filter.getProduto() != null) && (filter.getProduto().getIdProduto() != null)) {
            produtoCriteria.add(Restrictions.eq("idProduto", filter.getProduto().getIdProduto()));
        } else if (filter.getGrupoProduto().getIdCodGrupo() != null) {
            produtoCriteria.createCriteria("grupoProduto").add(Restrictions.eq("idCodGrupo", filter.getGrupoProduto().getIdCodGrupo()));
        }
        //if (!filter.getProdutosInativos()) {
        //    criteria.createCriteria("produtos").add(Restrictions.eq("ativo", Short.decode("1")));
        //}
        if (filter.getVendedor().getIdVendedor() != null) {
            pedidoCriteria.createCriteria("vendedor").add(Restrictions.eq("idVendedor", filter.getVendedor().getIdVendedor()));
        }
        Criteria clienteCriteria = pedidoCriteria.createCriteria("cliente").addOrder(Order.asc("razao"));
        if (filter.getGrupo().getIdGrupoCliente() != null) {
            clienteCriteria.createCriteria("gruposCliente").add(Restrictions.eq("idGrupoProduto", filter.getGrupo().getIdGrupoCliente()));
        }
        if (filter.getCliente().getIdCliente() != null) {
            clienteCriteria.add(Restrictions.eq("idCliente", filter.getCliente().getIdCliente()));
        } else {
            clienteCriteria.createCriteria("situacaoCliente").add(Restrictions.eq("pedido", "1"));
        }
        Criteria represCriteria = pedidoCriteria.createCriteria("repres");
        if (filter.getRepres().getIdRepres() != null) {
            represCriteria.add(Restrictions.eq("idRepres", filter.getRepres().getIdRepres()));
        } else {
            represCriteria.add(Restrictions.eq("inativo", '0'));
        }
        if (filter.getRoteiro().getIdRoteiro() != null) {
            Criteria roteiroCriteria = clienteCriteria.createCriteria("roteiros").add(Restrictions.eq("idRoteiro", filter.getRoteiro().getIdRoteiro()));
        }
        if ("A".equals(filter.getSituacao())) {
            criteria.add(Restrictions.in("situacao", new String[]{"A", "P"}));
        } else if ("N".equals(filter.getSituacao())) {
            criteria.add(Restrictions.eq("situacao", "N"));
            pedidoCriteria.add(Restrictions.eq("situacao", 'N'));
        }
        if (filter.getOrdem() == 0) {
            produtoCriteria.addOrder(Order.asc("descricao"));
            criteria.addOrder(Order.asc("dtEntrega"));
        } else if (filter.getOrdem() == 1) {
            criteria.addOrder(Order.asc("dtEntrega"));
            produtoCriteria.addOrder(Order.asc("descricao"));
        } else if (filter.getOrdem() == 2) {
            pedidoCriteria.addOrder(Order.asc("idPedido"));
            produtoCriteria.addOrder(Order.asc("descricao"));
        }
        return criteria.list();
    }

    public List findClientesProduto(PedidoFilter filter) {
        getLogger().info("findClientesProduto ini");
        Session session = ManagerFactory.getSession();
        StringBuilder sb = new StringBuilder();
        sb.append("select repres.razao representada, cliente.razao cliente,");
        sb.append(" produto.descricao produto, produto.idundcumul unidade, sum(item.qtd * produto.fatorcnv) valor");
        sb.append(" from tbitempedido item, tbproduto produto, tbpedido pedido, tbrepres repres, tbcliente cliente, tbgrupoproduto grupo, tbsitcliente sitcliente");
        sb.append(" where pedido.idpedido = item.idpedido ");
        sb.append("  and item.idproduto = produto.idproduto");
        sb.append("  and pedido.idcliente = cliente.idcliente");
        sb.append("  and cliente.idsitcliente = sitcliente.idsitcliente");
        sb.append("  and pedido.idrepres = repres.idrepres");
        sb.append("  and produto.idcodgrupo = grupo.idcodgrupo");
        if (filter.getDtEmissaoIni() != null) {
            sb.append("  and pedido.dtPedido between :dtPedido1 and :dtPedido2");
        }
        if (filter.getGrupoProduto().getIdCodGrupo() != null) {
            sb.append("  and grupo.idCodGrupo = :grupo");
        }
        if ((filter.getSubGrupoProduto() != null) && (filter.getSubGrupoProduto().getIdCodSubGrupo() != null)) {
            sb.append("  and produto.idCodSubGrupo = :subgrupo");
        }
        if (filter.getRepres().getIdRepres() != null) {
            sb.append(" and repres.idRepres = :repres");
        } else {
            sb.append(" and repres.inativo = '0'");
        }
        if (filter.getCliente().getIdCliente() != null) {
            sb.append(" and cliente.idCliente = :cliente");
        } else {
            sb.append("  and sitcliente.pedido = '1'");
        }
        if (filter.getOrdem() == 0) {
            sb.append(" group by repres.razao, produto.descricao, cliente.razao, produto.idundcumul ");
        } else {
            sb.append(" group by repres.razao, cliente.razao, produto.descricao, produto.idundcumul ");
        }

        SQLQuery q = (SQLQuery) session.createSQLQuery(sb.toString());

        if (filter.getDtEmissaoIni() != null) {
            q.setParameter("dtPedido1", filter.getDtEmissaoIni());
            q.setParameter("dtPedido2", filter.getDtEmissaoEnd());
        }
        if (filter.getGrupoProduto().getIdCodGrupo() != null) {
            q.setParameter("grupo", filter.getGrupoProduto().getIdCodGrupo());
        }
        if ((filter.getSubGrupoProduto() != null) && (filter.getSubGrupoProduto().getIdCodSubGrupo() != null)) {
            q.setParameter("subgrupo", filter.getSubGrupoProduto().getIdCodSubGrupo());
        }
        if (filter.getRepres().getIdRepres() != null) {
            q.setParameter("repres", filter.getRepres().getIdRepres());
        }
        if (filter.getCliente().getIdCliente() != null) {
            q.setParameter("cliente", filter.getCliente().getIdCliente());
        }
        List<Object[]> lista = q.list();
        List<ResumoVendasUnidade> clienteGrupo = new ArrayList<ResumoVendasUnidade>();
        for (Object[] item : lista) {
            ResumoVendasUnidade pc = new ResumoVendasUnidade();
            pc.setRepresentada((String) item[0]);
            pc.setCliente((String) item[1]);
            pc.setProduto((String) item[2]);
            pc.setUnidade((String) item[3]);
            pc.setValor((BigDecimal) item[4]);
            clienteGrupo.add(pc);
        }
        return clienteGrupo;
    }

    public List findVendasUnidade(VendasUnidadeFilter filter) {
        List lista = null;
        switch (filter.getAgrupamento()) {
            case 0:
                lista = vendasUnidadeGrupo(filter);
                break;
            case 1:
                lista = vendasUnidadeCliente(filter);
                break;
            case 2:
                lista = vendasUnidadeResumo(filter);
                break;
        }
        return lista;
    }

    private List vendasUnidadeGrupo(VendasUnidadeFilter filter) {
        getLogger().info("vendasUnidadeGrupo ini");
        Session session = ManagerFactory.getSession();
        StringBuilder sql = new StringBuilder();
        sql.append("select r.razao as representada, g.nomeGrupo, pr.undCumulativa.idUnidade as unidade, sum(it.qtd * pr.fatorConversao) as valor");
        sql.append(" from Repres as r, Pedido as p, ItemPedido as it, Produto as pr, GrupoProduto as g");
        sql.append(" where it.pedido = p and it.produto = pr");
        sql.append(" and p.repres = r and pr.grupoProduto = g");
        
        if (filter.getDtEmissaoIni() != null) {
            sql.append(" and p.dtPedido between :emissao1 and :emissao2");
        }
        if (filter.getDtEntregaIni() != null) {
            sql.append(" and p.dtEntrega between :entrega1 and :entrega2");
        }
        if (filter.getDtNotaIni() != null) {
            sql.append(" and p.idPedido in (select a.pedido.idPedido from AtendimentoPedido as a where a.pedido = p");
            sql.append(" and a.dtNota between :nota1 and :nota2)");
        }
        if (filter.getDtPgtoNotaIni() != null) {
            sql.append(" and p.idPedido in (select a.pedido.idPedido from AtendimentoPedido as a where a.pedido = p");
            sql.append(" and a.dtPgtoComissao between :pgtonota1 and :pgtonota2)");
        }        
        if (filter.getFornecedor() != null && filter.getFornecedor().getIdRepres() != null) {
            sql.append(" and r.idRepres = ").append(filter.getFornecedor().getIdRepres().toString());
        }
        if (filter.getCliente() != null && filter.getCliente().getIdCliente() != null) {
            sql.append(" and p.cliente.idCliente = ").append(filter.getCliente().getIdCliente().toString());
        }
        if (filter.getClienteResponsavel() != null && filter.getClienteResponsavel().getIdCliente() != null) {
            sql.append(" and p.clienteResponsavel.idCliente = ").append(filter.getClienteResponsavel().getIdCliente().toString());
        }
        if (filter.getVendedor() != null && filter.getVendedor().getIdVendedor() != null) {
            sql.append(" and p.vendedor.idVendedor = ").append(filter.getVendedor().getIdVendedor().toString());
        }
        if (filter.isPendentes()) {
            sql.append(" and p.situacao = 'N'");
        }
        
        if (("A".equals(filter.getSituacao())) || ("N".equals(filter.getSituacao()))) {
            sql.append( " and p.situacao = '").append(filter.getSituacao()).append("'");
        }
        if (!"T".equals(filter.getAtendimento())) {
            sql.append( " and p.atendimento = '").append(filter.getAtendimento()).append("'");
        }
        
        GrupoCliente grupo = filter.getGrupoCliente();
        if (grupo != null && grupo.getIdGrupoCliente() != null) {
            sql.append(" and p.cliente.idCliente in (select clientes.idCliente from GrupoCliente where idGrupoCliente = :grupo)");
        }
        
        sql.append(" group by r.razao, g.nomeGrupo, pr.undCumulativa.idUnidade");

        org.hibernate.Query q = session.createQuery(sql.toString());
        
        if (grupo != null && grupo.getIdGrupoCliente() != null) {
            q.setParameter("grupo", grupo.getIdGrupoCliente());
        }
        
        if (filter.getDtEmissaoIni() != null) {
            q.setParameter("emissao1", filter.getDtEmissaoIni());
            q.setParameter("emissao2", filter.getDtEmissaoEnd());
        }
        if (filter.getDtEntregaIni() != null) {
            q.setParameter("entrega1", filter.getDtEntregaIni());
            q.setParameter("entrega2", filter.getDtEntregaEnd());
        }
        if (filter.getDtNotaIni() != null) {
            q.setParameter("nota1", filter.getDtNotaIni());
            q.setParameter("nota2", filter.getDtNotaEnd());
        }
        if (filter.getDtPgtoNotaIni() != null) {
            q.setParameter("pgtonota1", filter.getDtPgtoNotaIni());
            q.setParameter("pgtonota2", filter.getDtPgtoNotaEnd());
        }        
        List<Object[]> lista = q.list();
        List<ResumoVendasUnidade> unidades = new ArrayList<ResumoVendasUnidade>();
        for (Object[] und : lista) {
            ResumoVendasUnidade resumo = new ResumoVendasUnidade();
            resumo.setRepresentada((String) und[0]);
            resumo.setNomeGrupo((String) und[1]);
            resumo.setUnidade((String) und[2]);
            resumo.setValor((BigDecimal) und[3]);
            unidades.add(resumo);
        }
        return unidades;
    }

    private List vendasUnidadeCliente(VendasUnidadeFilter filter) {
        getLogger().info("vendasUnidadeCliente ini");
        Session session = ManagerFactory.getSession();
        Criteria criteria = session.createCriteria(Pedido.class).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        if (filter.getDtEmissaoIni() != null) {
            criteria.add(Restrictions.between("dtPedido", filter.getDtEmissaoIni(), filter.getDtEmissaoEnd()));
        }
        if (filter.getDtEntregaIni() != null) {
            criteria.add(Restrictions.between("dtEntrega", filter.getDtEntregaIni(), filter.getDtEntregaEnd()));
        }
        if (filter.isPendentes()) {
            criteria.add(Restrictions.eq("situacao", "N"));
        }
        if (filter.getDtNotaIni() != null) {
            criteria.createCriteria("atendimentos").add(Restrictions.between("dtNota", filter.getDtNotaIni(), filter.getDtNotaEnd()));
        }
        if (filter.getDtPgtoNotaIni() != null) {
            criteria.createCriteria("atendimentos2").add(Restrictions.between("dtPgtoComissao", filter.getDtPgtoNotaIni(), filter.getDtPgtoNotaEnd()));
        }     
        Criteria represCriteria = criteria.createCriteria("repres");
        if (filter.getFornecedor()!= null && filter.getFornecedor().getIdRepres() != null) {
            represCriteria.add(Restrictions.eq("idRepres", filter.getFornecedor().getIdRepres()));
        }
        represCriteria.addOrder(Order.asc("razao"));
                
        Criteria clienteCriteria = criteria.createCriteria("cliente");
        if (filter.getCliente() != null && filter.getCliente().getIdCliente() != null) {
            clienteCriteria.add(Restrictions.eq("idCliente", filter.getCliente().getIdCliente()));
        }
        if (filter.getClienteResponsavel()!= null && filter.getClienteResponsavel().getIdCliente() != null) {
            Criteria clienteRes = criteria.createCriteria("clienteResponsavel");
            clienteRes.add(Restrictions.eq("idCliente", filter.getClienteResponsavel().getIdCliente()));
        }
        
        GrupoCliente grupo = filter.getGrupoCliente();
        if (grupo != null && grupo.getIdGrupoCliente() != null) {
            Criteria criteriaGrupo = clienteCriteria.createCriteria("gruposCliente");
            criteriaGrupo.add(Restrictions.eq("idGrupoCliente", grupo.getIdGrupoCliente()));
        }
        
        clienteCriteria.addOrder(Order.asc("razao"));
        
        if (filter.getVendedor() != null && filter.getVendedor().getIdVendedor() != null) {
            Criteria vendedorCriteria = criteria.createCriteria("vendedor");
            vendedorCriteria.add(Restrictions.eq("idVendedor", filter.getVendedor().getIdVendedor()));
        }
        return criteria.list();
    }

    public List vendasUnidadeResumo(VendasUnidadeFilter filter) {
        getLogger().info("vendasUnidadeResumo ini");
        Session session = ManagerFactory.getSession();
        StringBuilder sql = new StringBuilder();
        
        sql.append("select r.razao as representada, pr.IDUNDCUMUL as unidade, sum(it.qtd * pr.FATORCNV) as valor");
        sql.append(" from tbrepres r");
        sql.append(" inner join tbpedido p on p.idrepres = r.idrepres");
        if (filter.getDtNotaIni() != null) {
            sql.append(" inner join TBATENDIMENTOPEDIDO atp on atp.idpedido = p.idpedido");
            sql.append(" inner join tbitempedidoatend it on it.idpedido = p.idpedido and it.nf = atp.nf");
        } else {
            sql.append(" inner join tbitempedido it on it.idpedido = p.idpedido");
        }
        sql.append(" inner join tbproduto pr on pr.idproduto = it.idproduto where 1 = 1");
        
        if (filter.getDtEmissaoIni() != null) {
            sql.append(" and p.dtPedido between :emissao1 and :emissao2");
        }
        if (filter.getDtEntregaIni() != null) {
            sql.append(" and (p.entrega between :entrega1 and :entrega2)");
        }
        if (filter.getDtNotaIni() != null) {
            sql.append(" and (atp.dtNota between :nota1 and :nota2)");
        }
        if (filter.getDtPgtoNotaIni() != null) {
            sql.append(" and (atp.dtcom between :pgtonota1 and :pgtonota2)");
        }        
        if (filter.getFornecedor() != null && filter.getFornecedor().getIdRepres() != null) {
            sql.append(" and r.idRepres = ").append(filter.getFornecedor().getIdRepres().toString());
        }
        if (filter.getCliente() != null && filter.getCliente().getIdCliente() != null) {
            sql.append(" and p.idCliente = ").append(filter.getCliente().getIdCliente().toString());
        }
        if (filter.getClienteResponsavel() != null && filter.getClienteResponsavel().getIdCliente() != null) {
            sql.append(" and p.idClienteRes = ").append(filter.getClienteResponsavel().getIdCliente().toString());
        }
        if (filter.getVendedor() != null && filter.getVendedor().getIdVendedor() != null) {
            sql.append(" and p.idVendedor = ").append(filter.getVendedor().getIdVendedor().toString());
        }
        if (("A".equals(filter.getSituacao())) || ("N".equals(filter.getSituacao()))) {
            sql.append( " and p.situacao = '").append(filter.getSituacao()).append("'");
        }
        if (!"T".equals(filter.getAtendimento())) {
            sql.append( " and p.atend = '").append(filter.getAtendimento()).append("'");
        }
        
        GrupoCliente grupo = filter.getGrupoCliente();
        if (grupo != null && grupo.getIdGrupoCliente() != null) {
            sql.append(" and p.idCliente in (select idCliente from tbClienteGrupo where IDGRPCLIENTE = :grupo)");
        }
        
        sql.append(" group by r.razao, pr.idundcumul");
        getLogger().info(sql.toString());
        org.hibernate.Query q = session.createSQLQuery(sql.toString());
        
        if (grupo != null && grupo.getIdGrupoCliente() != null) {
            q.setParameter("grupo", grupo.getIdGrupoCliente());
        }
        
        if (filter.getDtEmissaoIni() != null) {
            q.setParameter("emissao1", filter.getDtEmissaoIni());
            q.setParameter("emissao2", filter.getDtEmissaoEnd());
        }
        if (filter.getDtEntregaIni() != null) {
            q.setParameter("entrega1", filter.getDtEntregaIni());
            q.setParameter("entrega2", filter.getDtEntregaEnd());
        }
        if (filter.getDtNotaIni() != null) {
            q.setParameter("nota1", filter.getDtNotaIni());
            q.setParameter("nota2", filter.getDtNotaEnd());
        }
        if (filter.getDtPgtoNotaIni() != null) {
            q.setParameter("pgtonota1", filter.getDtPgtoNotaIni());
            q.setParameter("pgtonota2", filter.getDtPgtoNotaEnd());
        }
        
        List<Object[]> lista = q.list();
        
        List<ResumoVendasUnidade> resumoList = new ArrayList<>();
        ResumoVendasUnidade resumo;
        
        for (Object[] item : lista) {
            resumo = new ResumoVendasUnidade();
            resumo.setRepresentada((String)item[0]);
            resumo.setUnidade((String)item[1]);
            resumo.setValor((BigDecimal)item[2]);
            resumoList.add(resumo);
        }
        return resumoList;
    }
    
    public List vendasUndAtendimentoResumo(VendasUnidadeFilter filter) {
        getLogger().info("vendasUndAtendimentoResumo ini");
        Session session = ManagerFactory.getSession();
        StringBuilder sql = new StringBuilder();
        
        sql.append("select r.razao as representada, pr.IDUNDCUMUL as unidade, sum(it.qtd * pr.FATORCNV) as valor");
        sql.append(" from tbrepres r");
        sql.append(" inner join tbpedido p on p.idrepres = r.idrepres");
        if (filter.getDtNotaIni() != null) {
            sql.append(" inner join TBATENDIMENTOPEDIDO atp on atp.idpedido = p.idpedido");
            sql.append(" inner join tbitempedidoatend it on it.idpedido = p.idpedido and it.nf = atp.nf");
        } else {
            sql.append(" inner join tbitempedido it on it.idpedido = p.idpedido");
        }
        
        sql.append(" inner join tbproduto pr on pr.idproduto = it.idproduto where 1 = 1");
        
        if (filter.getDtEmissaoIni() != null) {
            sql.append(" and p.dtPedido between :emissao1 and :emissao2");
        }
        if (filter.getDtEntregaIni() != null) {
            sql.append(" and (p.dtEntrega between :entrega1 and :entrega2)");
        }
        if (filter.getDtNotaIni() != null) {
            sql.append(" and (atp.dtNota between :nota1 and :nota2)");
        }
        if (filter.getDtPgtoNotaIni() != null) {
            sql.append(" and (a.dtcom between :pgtonota1 and :pgtonota2)");
        }        
        if (filter.getFornecedor() != null && filter.getFornecedor().getIdRepres() != null) {
            sql.append(" and r.idRepres = ").append(filter.getFornecedor().getIdRepres().toString());
        }
        if (filter.getCliente() != null && filter.getCliente().getIdCliente() != null) {
            sql.append(" and p.idCliente = ").append(filter.getCliente().getIdCliente().toString());
        }
        if (filter.getClienteResponsavel() != null && filter.getClienteResponsavel().getIdCliente() != null) {
            sql.append(" and p.idClienteRes = ").append(filter.getClienteResponsavel().getIdCliente().toString());
        }     
        if (filter.getVendedor() != null && filter.getVendedor().getIdVendedor() != null) {
            sql.append(" and p.idVendedor = ").append(filter.getVendedor().getIdVendedor().toString());
        }
        
        if (("A".equals(filter.getSituacao())) || ("N".equals(filter.getSituacao()))) {
            sql.append( " and p.situacao = '").append(filter.getSituacao()).append("'");
        }
        if (!"T".equals(filter.getAtendimento())) {
            sql.append( " and p.atend = '").append(filter.getAtendimento()).append("'");
        }
        
        GrupoCliente grupo = filter.getGrupoCliente();
        if (grupo != null && grupo.getIdGrupoCliente() != null) {
            sql.append(" and p.idCliente in (select idCliente from tbClienteGrupo where IDGRPCLIENTE = :grupo)");
        }
        
        sql.append(" group by r.razao, pr.idundcumul");
        getLogger().info(sql.toString());
        org.hibernate.Query q = session.createSQLQuery(sql.toString());
        
        if (grupo != null && grupo.getIdGrupoCliente() != null) {
            q.setParameter("grupo", grupo.getIdGrupoCliente());
        }
        
        if (filter.getDtEmissaoIni() != null) {
            q.setParameter("emissao1", filter.getDtEmissaoIni());
            q.setParameter("emissao2", filter.getDtEmissaoEnd());
        }
        if (filter.getDtEntregaIni() != null) {
            q.setParameter("entrega1", filter.getDtEntregaIni());
            q.setParameter("entrega2", filter.getDtEntregaEnd());
        }
        if (filter.getDtNotaIni() != null) {
            q.setParameter("nota1", filter.getDtNotaIni());
            q.setParameter("nota2", filter.getDtNotaEnd());
        }
        if (filter.getDtPgtoNotaIni() != null) {
            q.setParameter("pgtonota1", filter.getDtPgtoNotaIni());
            q.setParameter("pgtonota2", filter.getDtPgtoNotaEnd());
        }
        
        List<Object[]> lista = q.list();
        
        List<ResumoVendasUnidade> resumoList = new ArrayList<>();
        ResumoVendasUnidade resumo;
        
        for (Object[] item : lista) {
            resumo = new ResumoVendasUnidade();
            resumo.setRepresentada((String)item[0]);
            resumo.setUnidade((String)item[1]);
            resumo.setValor((BigDecimal)item[2]);
            resumoList.add(resumo);
        }
        return resumoList;
    }

    private List getSqlVendasCliente(PedidoFilter pedido) {
        Session session = ManagerFactory.getSession();
        StringBuilder sb = new StringBuilder();
        HashMap<String, Object> params = new HashMap<String, Object>();

        sb.append("select c.razao, sum(p.valor + (case when (p.valorop is null) then 0 else p.valorop end)) as valorvenda, sum(");
        sb.append(" case p.situacao");
        sb.append(" when 'A' then (select sum(a.valorcom) from tbatendimentopedido as a where p.idPedido = a.idPedido)");
        sb.append(" else p.valorComissao + (case when (p.valorop is null) then 0 else p.valorop end) end");
        sb.append(" ) as valorcomissao");
        sb.append(" from tbPedido as p, tbCliente as c");
        sb.append(" where p.idClienteRes = c.idCliente");
        sb.append(" and p.dtPedido between :emissao1 and :emissao2");
        params.put("emissao1", pedido.getDtEmissaoIni());
        params.put("emissao2", pedido.getDtEmissaoEnd());
        if (pedido.getVendedor().getIdVendedor() != null) {
            sb.append(" and p.idVendedor = :vendedor");
            params.put("vendedor", pedido.getVendedor().getIdVendedor());
        }
        if (pedido.getRepres().getIdRepres() != null) {
            sb.append(" and p.idRepres = :repres");
            params.put("repres", pedido.getRepres().getIdRepres());
        }
        if (pedido.getCliente().getIdCliente() == null && pedido.getResponsavel().getIdCliente() != null) {
            sb.append(" and p.idClienteRes = :responsavel");
            params.put("responsavel", pedido.getResponsavel().getIdCliente());
        } else if (pedido.getCliente().getIdCliente() != null && pedido.getResponsavel().getIdCliente() == null) {
            sb.append(" and (p.idCliente = :cliente or p.idClienteRes = :responsavel)");
            params.put("cliente", pedido.getCliente().getIdCliente());
            params.put("responsavel", pedido.getCliente().getIdCliente());
        } else if (pedido.getCliente().getIdCliente() != null && pedido.getResponsavel().getIdCliente() != null) {
            sb.append(" and (p.idCliente = :cliente and p.idClienteRes = :responsavel)");
            params.put("cliente", pedido.getCliente().getIdCliente());
            params.put("responsavel", pedido.getResponsavel().getIdCliente());
        }

        if (pedido.getGrupo().getIdGrupoCliente() != null) {
            sb.append(" and p.idClienteRes in (select cl.idCliente from tbcliente cl inner join tbClientegrupo s on s.idcliente = cl.idcliente where s.idgrpcliente = :grupoCliente)");
            params.put("grupoCliente", pedido.getGrupo().getIdGrupoCliente());
        }
        if (pedido.getSegmento().getIdSegmento() != null) {
            sb.append(" and p.idClienteRes in (select c.idCliente from TBSEGMENTOCLIENTE c where c.IDSEGMENTO = :segmento)");
            params.put("segmento", pedido.getSegmento().getIdSegmento());
        }
        sb.append(" group by c.razao");
        sb.append(" order by valorcomissao desc");

        String sqlQuery = sb.toString();
        SQLQuery q = (SQLQuery) session.createSQLQuery(sqlQuery).setResultTransformer(Transformers.aliasToBean(VendasCliente.class));
        Iterator iterator = params.keySet().iterator();
        while (iterator.hasNext()) {
            String s = (String) iterator.next();
            q.setParameter(s, params.get(s));

        }
        //getLogger().info(sqlQuery);
        return q.list();
    }

    private List getSqlVendasGrupoCliente(PedidoFilter pedido) {
        Session session = ManagerFactory.getSession();
        StringBuilder sb = new StringBuilder();
        HashMap<String, Object> params = new HashMap<>();
        sb.append("select g.nomegrupo as razao, sum(pc.valorVenda) as valorVenda, sum(pc.valorComissao) as valorComissao from tbgrupocliente g");
        sb.append(" inner join tbclientegrupo cg on g.idgrpcliente = cg.idgrpcliente");
        sb.append(" inner join (");
        sb.append(" select c.idCliente, sum(p.valor + (case when (p.valorop is null) then 0 else p.valorop end)) as valorvenda, sum(");
        sb.append(" case p.situacao");
        sb.append(" when 'A' then (select sum(a.valorcom) from tbatendimentopedido as a where p.idPedido = a.idPedido)");
        sb.append(" else p.valorComissao + (case when (p.valorop is null) then 0 else p.valorop end) end");
        sb.append(" ) as valorcomissao");
        sb.append(" from tbPedido as p, tbCliente as c");
        sb.append(" where p.idClienteRes = c.idCliente");
        sb.append(" and p.dtPedido between :emissao1 and :emissao2");
        params.put("emissao1", pedido.getDtEmissaoIni());
        params.put("emissao2", pedido.getDtEmissaoEnd());
        if (pedido.getVendedor().getIdVendedor() != null) {
            sb.append(" and p.idVendedor = :vendedor");
            params.put("vendedor", pedido.getVendedor().getIdVendedor());
        }
        if (pedido.getRepres().getIdRepres() != null) {
            sb.append(" and p.idRepres = :repres");
            params.put("repres", pedido.getRepres().getIdRepres());
        }
        if (pedido.getCliente().getIdCliente() == null && pedido.getResponsavel().getIdCliente() != null) {
            sb.append(" and p.idClienteRes = :responsavel");
            params.put("responsavel", pedido.getResponsavel().getIdCliente());
        } else if (pedido.getCliente().getIdCliente() != null && pedido.getResponsavel().getIdCliente() == null) {
            sb.append(" and (p.idCliente = :cliente or p.idClienteRes = :responsavel)");
            params.put("cliente", pedido.getCliente().getIdCliente());
            params.put("responsavel", pedido.getCliente().getIdCliente());
        } else if (pedido.getCliente().getIdCliente() != null && pedido.getResponsavel().getIdCliente() != null) {
            sb.append(" and (p.idCliente = :cliente and p.idClienteRes = :responsavel)");
            params.put("cliente", pedido.getCliente().getIdCliente());
            params.put("responsavel", pedido.getResponsavel().getIdCliente());
        }

        if (pedido.getGrupo().getIdGrupoCliente() != null) {
            sb.append(" and p.idClienteRes in (select cl.idCliente from tbcliente cl inner join tbClientegrupo s on s.idcliente = cl.idcliente where s.idgrpcliente = :grupoCliente)");
            params.put("grupoCliente", pedido.getGrupo().getIdGrupoCliente());
        }
        if (pedido.getSegmento().getIdSegmento() != null) {
            sb.append(" and p.idClienteRes in (select c.idCliente from TBSEGMENTOCLIENTE c where c.IDSEGMENTO = :segmento)");
            params.put("segmento", pedido.getSegmento().getIdSegmento());
        }
        sb.append(" group by c.idCliente");
        sb.append(" ) pc on cg.idCliente = pc.idCliente");
        sb.append(" group by g.nomeGrupo");
        sb.append(" order by valorcomissao desc");

        String sqlQuery = sb.toString();
        SQLQuery q = (SQLQuery) session.createSQLQuery(sqlQuery).setResultTransformer(Transformers.aliasToBean(VendasCliente.class));
        Iterator iterator = params.keySet().iterator();
        while (iterator.hasNext()) {
            String s = (String) iterator.next();
            q.setParameter(s, params.get(s));

        }
        //getLogger().info(sqlQuery);
        return q.list();
    }

    public List findVendasCliente(PedidoFilter pedido) {
        getLogger().info("findVendasCliente ini");
        if (pedido.getTotalGrupoCliente())
            return getSqlVendasGrupoCliente(pedido);
        else
            return getSqlVendasCliente(pedido);
    }

    public void incluirPagamento(PgtoCliente pgto) throws DAOException {
        getLogger().info("incluirPagamento ini");
        Session session = ManagerFactory.getSession();
        Transaction tx = null;
        AtendimentoPedido atendimento = pgto.getAtendimentoPedido();
        if ((pgto.getDtPgto() != null) && (pgto.getTipoPgto().indexOf("P") >= 0) && (pgto.getAtendimentoPedido().getDtPgtoComissao() == null)) {

            try {
                tx = session.beginTransaction();
                if (pgto.getValorPgto() != null) {
                    atendimento.setValorComissao(pgto.getValorPgto());
                } else {
                    atendimento.setValorComissao(pgto.getValor());
                }
                if (pgto.getDtPgto() != null) {
                    atendimento.setRecibo("1");

                    atendimento.setDtPgtoComissao(pgto.getDtPgto());

                }
                super.updateRow(atendimento);
                insertRecord(pgto);
                if (pgto.getDtPgto() != null) {
                    EntradaRepres entrada = new EntradaRepres();
                    entrada.setDtEntrada(pgto.getDtPgto());
                    entrada.setHistorico("Pgto. cliente");
                    entrada.setPedido(pgto.getAtendimentoPedido().getPedido().toString());
                    entrada.setRepres(pgto.getAtendimentoPedido().getPedido().getRepres());
                    entrada.setTipo("P");
                    entrada.setValor(pgto.getValorPgto());
                    insertRecord(entrada);
                }
                tx.commit();
            } catch (Exception e) {
                if (tx != null) {
                    tx.rollback();
                }
                throw new DAOException(e);
            }
        } else {
            insertRecord(pgto);
        }
        Pedido pedido = atendimento.getPedido();
        pedido.setPreCobranca(Boolean.TRUE);
        super.updateRow(pedido);
    }

    public void atualizarPagamento(PgtoCliente pgto) throws DAOException {

        EntityManager em = getEntityManager();

        try {
            em.getTransaction().begin();
            //if (pgto.getDtPgto() != null) {
            if (pgto.getTipoPgto().indexOf("P") >= 0) {
                AtendimentoPedido atendimento = pgto.getAtendimentoPedido();
                if (pgto.getValorPgto() != null) {
                    atendimento.setValorComissao(pgto.getValorPgto());
                } else {
                    atendimento.setValorComissao(pgto.getValor());
                }
                if (pgto.getDtPgto() != null) {
                    atendimento.setRecibo("1");
                    atendimento.setDtPgtoComissao(pgto.getDtPgto());

                }
                updateNota(atendimento, em);

                if (pgto.getDtPgto() != null) {
                    EntradaRepres entrada = new EntradaRepres();
                    entrada.setDtEntrada(pgto.getDtPgto());
                    entrada.setHistorico("Pgto. cliente");
                    entrada.setPedido(atendimento.getPedido().toString());
                    entrada.setRepres(atendimento.getPedido().getRepres());
                    entrada.setTipo("P");
                    entrada.setValor(pgto.getValorPgto());
                    insertRecord(entrada, em);
                }
            }
            updatePgtoCliente(pgto, em);
            //} else {
            //    updatePgtoCliente(pgto, em);
            //}
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new DAOException(e);
        }
    }

    public void setEmitido(Pedido pedido) {
        Session session = ManagerFactory.getSession();
        org.hibernate.Query q = session.createQuery("update Pedido set emitido = :emitido, emitidoCliente = :emitidoCliente, emitidocobranca = :emitidoCobranca where idPedido = :pedido");
        q.setParameter("emitido", pedido.getEmitido());
        q.setParameter("emitidoCliente", pedido.getEmitidoCliente());
        q.setParameter("emitidoCobranca", pedido.getEmitidoCobranca());
        q.setParameter("pedido", pedido.getIdPedido());
        q.executeUpdate();
    }

    public void setEmitidoCobranca(Pedido pedido) {
        Session session = ManagerFactory.getSession();
        org.hibernate.Query q = session.createQuery("update Pedido set emitidoCobranca = :emitido where idPedido = :pedido");
        q.setParameter("emitido", pedido.getEmitidoCobranca());
        q.setParameter("pedido", pedido.getIdPedido());
        q.executeUpdate();
    }

    public void updateSituacaoPedido(Pedido pedido) {
        Session session = ManagerFactory.getSession();
        org.hibernate.Query q = session.createQuery("update Pedido set situacao = :situacao, atendimento = :atendimento where idPedido = :pedido");
        q.setParameter("situacao", pedido.getSituacao());
        q.setParameter("atendimento", pedido.getAtendimento());
        q.setParameter("pedido", pedido.getIdPedido());
        q.executeUpdate();

    }

    /*
     *
    
    soma de qtd
    if qryPosicaoItensQTD.AsFloat - qryPosicaoItensATENDIDO.AsFloat = 0 then
    UpdAtendimentoItem('A', pedido, qryPosicaoItensPRODUTO.AsInteger)
    else
    if qryPosicaoItensQTD.AsFloat - qryPosicaoItensATENDIDO.AsFloat = qryPosicaoItensQTD.AsFloat then
    UpdAtendimentoItem('N', pedido, qryPosicaoItensPRODUTO.AsInteger)
    else
    UpdAtendimentoItem('P', pedido, qryPosicaoItensPRODUTO.AsInteger);
     *
     *
    
     *
     */
    private ItemPedido getItemFromAtendido(ItemPedidoAtend atendido, List<ItemPedido> itens) {
        for (ItemPedido item : itens) {
            if (atendido.getProduto().getIdProduto().compareTo(item.getProduto().getIdProduto()) == 0) {
                if (atendido.getItemPedidoAtendPK().getIdPedido() == item.getPedido().getIdPedido().intValue()) {
                    return item;
                }
            }
        }
        return null;

    }

    public boolean updSituacaoItem(Collection<ItemPedido> itens) {
        Session session = ManagerFactory.getSession();
        boolean atendido = true;
        StringBuilder sb = new StringBuilder();
        sb.append("select i.qtd - a.quant from tbitempedido i, (");
        sb.append("  select idproduto, sum(qtd) as quant from tbitempedidoatend where idpedido = :pedido1 and idproduto = :produto group by idproduto");
        sb.append("  ) as a");
        sb.append("  where i.idpedido = :pedido2 and i.idproduto = a.idproduto");
        SQLQuery q = (SQLQuery) session.createSQLQuery(sb.toString());
        for (ItemPedido item : itens) {
            q.setParameter("pedido1", item.getPedido().getIdPedido());
            q.setParameter("pedido2", item.getPedido().getIdPedido());
            q.setParameter("produto", item.getProduto().getIdProduto());
            BigDecimal o = (BigDecimal) q.uniqueResult();
            if (o == null) {
                item.setSituacao("N");
                atendido = false;
            } else if (o.compareTo(BigDecimal.ZERO) <= 0) {
                item.setSituacao("A");
            } else if (item.getQtd().subtract(o).compareTo(BigDecimal.ZERO) <= 0) {
                item.setSituacao("N");
                atendido = false;
            } else {
                item.setSituacao("P");
                atendido = false;
            }

            //getLogger().info(item.getProduto().getIdProduto() + " " + item.getSituacao());
            org.hibernate.Query q1 = session.createQuery("update ItemPedido i set i.situacao = :situacao where i.pedido.idPedido = :pedido and i.produto.idProduto = :produto");
            q1.setParameter("situacao", item.getSituacao());
            q1.setParameter("pedido", item.getPedido().getIdPedido());
            q1.setParameter("produto", item.getProduto().getIdProduto());
            q1.executeUpdate();
        }
        // org.hibernate.Query q = session.createQuery("update ItemPedido i set i.situacao = :situacao where i.pedido.idPedido = :pedido and i.produto.idProduto = :produto");

        //q.setParameter("situacao", pedido.getSituacao());
        //q.setParameter("pedido", pedido.getIdPedido());
        return atendido;
    }

    public void deleteAtendimento(AtendimentoPedido atend) {
        Session session = ManagerFactory.getSession();
        org.hibernate.Query q = session.createQuery("delete from ItemPedidoAtend where idPedido = :pedido and itemPedidoAtendPK.nf = :nf");
        try {
            q.setParameter("pedido", atend.getPedido().getIdPedido());
            q.setParameter("nf", atend.getAtendimentoPedidoPK().getNf());
            q.executeUpdate();

            q = session.createQuery("delete from PgtoCliente where atendimentoPedido.atendimentoPedidoPK.idPedido = :pedido and atendimentoPedido.atendimentoPedidoPK.nf = :nf");
            q.setParameter("pedido", atend.getPedido().getIdPedido());
            q.setParameter("nf", atend.getAtendimentoPedidoPK().getNf());
            q.executeUpdate();

            q = session.createQuery("delete from AtendimentoPedido where idPedido = :pedido and atendimentoPedidoPK.nf = :nf");
            q.setParameter("pedido", atend.getPedido().getIdPedido());
            q.setParameter("nf", atend.getAtendimentoPedidoPK().getNf());
            q.executeUpdate();
        } catch (Exception e) {
            getLogger().error(e.getLocalizedMessage(), e);
        }
    }

    public void updateReciboEmitido(List<AtendimentoPedido> lista, Vendedor vendedor, byte[] arq) throws DAOException {
        EntityManager em = getEntityManager();
        
        try {
            em.getTransaction().begin();
            Date dtRecibo = new Date();
            Query q = em.createQuery("update AtendimentoPedido set recibo = :recibo, dtReciboComissao = :dtRecibo where atendimentoPedidoPK.idPedido = :pedido and atendimentoPedidoPK.nf = :nota");
            for (AtendimentoPedido nota : lista) {
                q.setParameter("recibo", "0");
                q.setParameter("pedido", nota.getAtendimentoPedidoPK().getIdPedido());
                q.setParameter("nota", nota.getAtendimentoPedidoPK().getNf());
                q.setParameter("dtRecibo", dtRecibo);
                q.executeUpdate();
            }
            
            ReciboComissao recibo = new ReciboComissao();
            recibo.setDtRecibo(dtRecibo);
            recibo.setVendedor(vendedor);
            recibo.setRecibo(arq);
            
            em.persist(recibo);

            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new DAOException(e);
        } finally {
            em.close();
        }
    }

    private void updateNota(AtendimentoPedido pgto, EntityManager em) throws DAOException {

        try {

            StringBuilder sb = new StringBuilder();
            sb.append("update AtendimentoPedido a set a.dtPgtoComissao = :dtPgtoComissao, a.recibo = :recibo, a.valorComissao = :valorComissao where a.atendimentoPedidoPK.idPedido = :idPedido and a.atendimentoPedidoPK.nf = :nf");
            Query q = em.createQuery(sb.toString());

            q.setParameter("dtPgtoComissao", pgto.getDtPgtoComissao());
            //q.setParameter("dtcomprev", pgto.getDtcomprev());
            //q.setParameter("emitido", pgto.getEmitido());
            //q.setParameter("pago", pgto.getPago());
            q.setParameter("recibo", pgto.getRecibo());
            //q.setParameter("valor", pgto.getValor());
            q.setParameter("valorComissao", pgto.getValorComissao());
            q.setParameter("idPedido", pgto.getPedido().getIdPedido());
            q.setParameter("nf", pgto.getAtendimentoPedidoPK().getNf());



            q.executeUpdate();

        } catch (Exception e) {
            throw new DAOException(e);
        }
    }

    private void updatePgtoCliente(PgtoCliente pgto, EntityManager em) throws DAOException {

        try {

            StringBuilder sb = new StringBuilder();
            sb.append("update PgtoCliente p set p.complemento = :complemento, p.contaRepres = :contaRepres,");
            sb.append("   p.dtPgto = :dtPgto, p.dtPrevisaoPgto = :dtPrevisaoPgto, p.dtVencimento = :dtVencimento, p.tipoPgto = :tipoPgto, p.valor = :valor, p.valorPgto = :valorPgto, ");
            sb.append("   p.observacao = :obs");
            sb.append("   where p.idPgtoCliente = :id");
            Query q = em.createQuery(sb.toString());

            q.setParameter("complemento", pgto.getComplemento());
            q.setParameter("contaRepres", pgto.getContaRepres());
            q.setParameter("dtPgto", pgto.getDtPgto());
            q.setParameter("dtPrevisaoPgto", pgto.getDtPrevisaoPgto());
            q.setParameter("dtVencimento", pgto.getDtVencimento());
            q.setParameter("tipoPgto", pgto.getTipoPgto());
            q.setParameter("valor", pgto.getValor());
            q.setParameter("valorPgto", pgto.getValorPgto());
            q.setParameter("obs", pgto.getObservacao());
//            q.setParameter("valorcobranca", pgto.getValorCobranca());
//            q.setParameter("cobranca", pgto.getCobranca());
            q.setParameter("id", pgto.getIdPgtoCliente());

            q.executeUpdate();

        } catch (Exception e) {
            throw new DAOException(e);
        }
    }

    public List<ItemPedidoAtend> findItensPedidoPendentes(Integer idPedido) throws Exception {
        EntityManager em = ManagerFactory.getEntityManager();
        StringBuilder sb = new StringBuilder();
        sb.append("select i.idproduto, i.qtd - t.quant as r, i.valorCliente as valor, i.qtd, i.comissao, i.embalagem from tbitempedido i left outer join (");
        sb.append("  select idproduto, sum(qtd) as quant from tbitempedidoatend where idpedido = :p1 group by idproduto");
        sb.append("  ) as t");
        sb.append("  on i.idproduto = t.idproduto and (i.qtd - t.quant) > 0");
        sb.append("  where i.idpedido = :p2 and i.situacao <> :atendimento");

        Query q = em.createNativeQuery(sb.toString());
        q.setParameter("p1", idPedido);
        q.setParameter("p2", idPedido);
        q.setParameter("atendimento", "A");
        List<Object[]> lista = q.getResultList();
        List<ItemPedidoAtend> result = new ArrayList<ItemPedidoAtend>();
        for (Object[] item : lista) {
            ItemPedidoAtend atend = new ItemPedidoAtend();
            atend.getItemPedidoAtendPK().setIdPedido(idPedido);
            atend.getItemPedidoAtendPK().setIdProduto((Integer) item[0]);
            BigDecimal valor = (BigDecimal) item[1];
            if (valor == null || valor.intValue() <= 0) {
                valor = (BigDecimal) item[3];
            }
            atend.setQtd(valor);
            atend.setValor((BigDecimal) item[2]);
            atend.setPercComissao((BigDecimal) item[4]);
            atend.setEmbalagem((BigDecimal) item[5]);
            result.add(atend);
            atend.setProduto((Produto) findById(Produto.class, (Integer) item[0]));
        }
        return result;
    }

    public void setNextValue(Integer idPedido) {
        //String s = "select max(idpedido) from tbpedido";
        Session session = ManagerFactory.getSession();
        //SQLQuery q = (SQLQuery) session.createSQLQuery(s);
        //Integer bi = (Integer) q.uniqueResult();
        String s = "set generator TBPEDIDO_IDPEDIDO_SEQ to " + idPedido.toString();
        SQLQuery q = (SQLQuery) session.createSQLQuery(s);
        //q.setParameter("num", bi.intValue());
        q.executeUpdate();
    }

    public BigDecimal getVendasClientePendende(Integer idCliente) {
        getLogger().info("getVendasClientePendende ini");
        StringBuilder sb = new StringBuilder();
        sb.append("select sum(p.valor + p.valorOp) from Pedido p where p.situacao = :situacao and p.cliente.idCliente = :cliente");
        EntityManager em = getEntityManager();

        Query q = em.createQuery(sb.toString());
        q.setParameter("situacao", "N");
        q.setParameter("cliente", idCliente);

        return (BigDecimal) q.getSingleResult();
    }

    public void baixarCobranca(List<PgtoCliente> lista) throws DAOException {
        getLogger().info("baixarCobranca ini");
        for (PgtoCliente pgto : lista) {
            if ((pgto.getDtPgto() == null) && (pgto.getValor() != null)) {
                pgto.setDtPgto(DateUtils.getCurrentDate());
                pgto.setValorPgto(pgto.getValor());
                atualizarPagamento(pgto);
            }
        }
        ClienteDao clidao = new ClienteDao();
        for (PgtoCliente pgto : lista) {
            clidao.atualizaAtrasoCliente(pgto.getAtendimentoPedido().getPedido().getCliente().getIdCliente());
        }
    }

    public List<Meta> findMetas() {
        getLogger().info("findMetas ini");
        Query q = ManagerFactory.getEntityManager().createQuery("select o from Meta o order by anoMes desc");
        return q.getResultList();
    }

    public BigDecimal getMeta(String anoMes) {
        getLogger().info("getMeta ini");
        Query q = ManagerFactory.getEntityManager().createQuery("select o.valorMeta from Meta o where o.anoMes = :ano");
        q.setParameter("ano", anoMes);
        List lista = q.getResultList();
        if (lista == null || lista.size() == 0) {
            return null;
        } else {
            return (BigDecimal) lista.get(0);
        }
    }

    public BigDecimal getComissoes(Integer vendedor, Date inicio, Date termino) {
        getLogger().info("getComissoes ini");
        Query q = ManagerFactory.getEntityManager().createQuery("select sum((o.valorOp + o.valorComissao) * o.comissaoVendedor / 100) from Pedido o where o.dtPedido between :dt1 and :dt2 and o.vendedor.idVendedor = :vendedor");
        q.setParameter("dt1", inicio);
        q.setParameter("dt2", termino);
        q.setParameter("vendedor", vendedor);
        BigDecimal result = (BigDecimal) q.getSingleResult();
        
        return result;
    }

    public List<ArquivoPedido> getArquivos(Integer idPedido) {
        List<ArquivoPedido> lista;
        Query q = ManagerFactory.getEntityManager().createQuery("select t from ArquivoPedido t where t.pedido.idPedido = :pedido");
        q.setParameter("pedido", idPedido);
        return q.getResultList();
    }

    public List findProdutosMaisVendidos(PedidoFilter pedidoFilter) {
        if ((pedidoFilter.getRepres().getIdRepres() != null) || (pedidoFilter.getQuebrarRepres()))
            return findProdutosMaisVendidosRepres(pedidoFilter);
        else
            return findProdutosMaisVendidosGeral(pedidoFilter);
    }
    public List findProdutosMaisVendidosRepres(PedidoFilter pedidoFilter) {
        StringBuilder sb = new StringBuilder();

        sb.append("select repres.razao, produto.descricao, sum(item.valor * item.qtd) as valor, sum(item.valor * item.qtd * item.comissao / 100) as comissao, sum(item.qtd * produto.fatorcnv) as unidades");
        sb.append(" from tbproduto produto, tbitempedido item, tbpedido pedido, tbrepres repres, tbvendedor vendedor");
        sb.append(" where produto.idproduto = item.idproduto");
        sb.append(" and item.idpedido = pedido.idpedido");
        sb.append(" and pedido.idrepres = repres.idrepres");
        sb.append(" and pedido.idvendedor = vendedor.idvendedor");
        sb.append(" and pedido.dtpedido between :dtini and :dtend");

        if (pedidoFilter.getRepres().getIdRepres() != null)
            sb.append(" and repres.idrepres = :repres");
        if (pedidoFilter.getVendedor().getIdVendedor() != null)
            sb.append(" and vendedor.idvendedor = :vendedor");
        if (pedidoFilter.getCliente().getIdCliente() != null)
            sb.append(" and pedido.idcliente = :cliente");

        if (pedidoFilter.getProduto() != null) {
            GrupoProduto grupo = pedidoFilter.getProduto().getGrupoProduto();
            if ( grupo != null  && grupo.getIdCodGrupo() != null) {
                sb.append(" and produto.idcodgrupo = :grupo");
            }
            SubGrupoProduto subgrupo = pedidoFilter.getProduto().getSubGrupoProduto();
            if ( subgrupo != null  && subgrupo.getIdCodSubGrupo() != null) {
                sb.append(" and produto.idcodsubgrupo = :subgrupo");
            }
        }

        sb.append(" group by repres.razao, produto.descricao");
        sb.append(" order by 1, 4 desc");

        Query q = ManagerFactory.getEntityManager().createNativeQuery(sb.toString());
        q.setParameter("dtini", pedidoFilter.getDtEmissaoIni());
        q.setParameter("dtend", pedidoFilter.getDtEmissaoEnd());

        if (pedidoFilter.getRepres().getIdRepres() != null)
            q.setParameter("repres", pedidoFilter.getRepres().getIdRepres());
        if (pedidoFilter.getVendedor().getIdVendedor() != null)
            q.setParameter("vendedor", pedidoFilter.getVendedor().getIdVendedor());
        if (pedidoFilter.getCliente().getIdCliente() != null)
            q.setParameter("cliente", pedidoFilter.getCliente().getIdCliente());
        if (pedidoFilter.getProduto() != null) {
            GrupoProduto grupo = pedidoFilter.getProduto().getGrupoProduto();
            if ( grupo != null  && grupo.getIdCodGrupo() != null) {
                q.setParameter("grupo", grupo.getIdCodGrupo());
            }
            SubGrupoProduto subgrupo = pedidoFilter.getProduto().getSubGrupoProduto();
            if ( subgrupo != null  && subgrupo.getIdCodSubGrupo() != null) {
                 q.setParameter("subgrupo", subgrupo.getIdCodSubGrupo());
            }
        }

        List<Object[]> lista = q.getResultList();
        List<ProdutosComprados> result = new ArrayList();
        for (Object[] item : lista) {
            ProdutosComprados pc = new ProdutosComprados();
            pc.setRepres((String)item[0]);
            pc.setDescricao((String)item[1]);
            pc.setValor((BigDecimal)item[2]);
            pc.setComissao((BigDecimal)item[3]);
            pc.setUnidades((BigDecimal)item[4]);
            result.add(pc);
        }
        return result;
    }

    public List findProdutosMaisVendidosGeral(PedidoFilter pedidoFilter) {
        StringBuilder sb = new StringBuilder();

        sb.append("select produto.descricao, sum(item.valor * item.qtd) as valor, sum(item.valor * item.qtd * item.comissao / 100) as comissao, sum(item.qtd * produto.fatorcnv) as unidades");
        sb.append(" from tbproduto produto, tbitempedido item, tbpedido pedido, tbvendedor vendedor");
        sb.append(" where produto.idproduto = item.idproduto");
        sb.append(" and item.idpedido = pedido.idpedido");
        sb.append(" and pedido.idvendedor = vendedor.idvendedor");
        sb.append(" and pedido.dtpedido between :dtini and :dtend");

        if (pedidoFilter.getVendedor().getIdVendedor() != null)
            sb.append(" and vendedor.idvendedor = :vendedor");
        if (pedidoFilter.getCliente().getIdCliente() != null)
            sb.append(" and pedido.idcliente = :cliente");

        if (pedidoFilter.getProduto() != null) {
            GrupoProduto grupo = pedidoFilter.getProduto().getGrupoProduto();
            if ( grupo != null && grupo.getIdCodGrupo() != null) {
                sb.append(" and produto.idcodgrupo = :grupo");
            }
            SubGrupoProduto subgrupo = pedidoFilter.getProduto().getSubGrupoProduto();
            if ( subgrupo != null && subgrupo.getIdCodSubGrupo() != null) {
                sb.append(" and produto.idcodsubgrupo = :subgrupo");
            }
        }

        sb.append(" group by produto.descricao");
        sb.append(" order by 3 desc");

        Query q = ManagerFactory.getEntityManager().createNativeQuery(sb.toString());
        q.setParameter("dtini", pedidoFilter.getDtEmissaoIni());
        q.setParameter("dtend", pedidoFilter.getDtEmissaoEnd());
        if (pedidoFilter.getVendedor().getIdVendedor() != null)
            q.setParameter("vendedor", pedidoFilter.getVendedor().getIdVendedor());
        if (pedidoFilter.getCliente().getIdCliente() != null)
            q.setParameter("cliente", pedidoFilter.getCliente().getIdCliente());

        if (pedidoFilter.getProduto() != null) {
            GrupoProduto grupo = pedidoFilter.getProduto().getGrupoProduto();
            if ( grupo != null  && grupo.getIdCodGrupo() != null) {
                q.setParameter("grupo", grupo.getIdCodGrupo());
            }
            SubGrupoProduto subgrupo = pedidoFilter.getProduto().getSubGrupoProduto();
            if ( subgrupo != null  && subgrupo.getIdCodSubGrupo() != null) {
                 q.setParameter("subgrupo", subgrupo.getIdCodSubGrupo());
            }
        }

        List<Object[]> lista = q.getResultList();
        List<ProdutosComprados> result = new ArrayList();
        for (Object[] item : lista) {
            ProdutosComprados pc = new ProdutosComprados();
            pc.setDescricao((String)item[0]);
            pc.setValor((BigDecimal)item[1]);
            pc.setComissao((BigDecimal)item[2]);
            pc.setUnidades((BigDecimal)item[3]);
            result.add(pc);
        }
        return result;
    }

    public void updateComissao(ItemPedido item, BigDecimal calculaComissao) throws Exception {
        EntityManager em = getEntityManager();
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            updateRow(item, em);
            Query q = em.createQuery("update Pedido set valorComissao = :val where idPedido = :pedido");
            q.setParameter("val", calculaComissao);
            q.setParameter("pedido", item.getPedido().getIdPedido());
            q.executeUpdate();
            et.commit();
        } catch (Exception e) {
            et.rollback();
            throw e;
        }
    }

    public List findTotaisOP(PedidoFilter pedidoFilter) {
        StringBuilder sb = new StringBuilder();

        sb.append("select r.razao, sum(p.valorOp) from Pedido p join p.repres r");
        sb.append(" where p.valorOp is not null and p.valorOp > 0");
        sb.append("    and p.dtPedido between :dtini and :dtend");
        if (pedidoFilter.getVendedor().getIdVendedor() != null)
            sb.append(" and p.vendedor.idVendedor = :vendedor");
        sb.append(" group by r.razao");


        Query q = ManagerFactory.getEntityManager().createQuery(sb.toString());
        q.setParameter("dtini", pedidoFilter.getDtEmissaoIni());
        q.setParameter("dtend", pedidoFilter.getDtEmissaoEnd());
        if (pedidoFilter.getVendedor().getIdVendedor() != null)
            q.setParameter("vendedor", pedidoFilter.getVendedor().getIdVendedor());
        List<Object[]> lista = q.getResultList();
        List<ResumoVendasUnidade> result = new ArrayList<ResumoVendasUnidade>();
        for (Object[] item : lista) {
            ResumoVendasUnidade cg = new ResumoVendasUnidade();
            cg.setRepresentada((String)item[0]);
            cg.setValor((BigDecimal)item[1]);
            result.add(cg);
        }
        return result;
    }

    public List findClienteSemVenda(PedidoFilter pedidoFilter) {
        StringBuilder sb = new StringBuilder();

        sb.append("select v.nome, c.razao from Cliente c join c.vendedores v");
        sb.append(" where c.situacaoCliente.pedido = :pedido");
        sb.append(" and c.idCliente not in (select o.cliente.idCliente from Pedido o where o.dtPedido between :dtini and :dtend)");
        if (pedidoFilter.getVendedor().getIdVendedor() != null)
            sb.append(" and v.idVendedor = :vendedor");
        sb.append(" order by v.nome, c.razao");


        Query q = ManagerFactory.getEntityManager().createQuery(sb.toString());
        q.setParameter("pedido", "1");
        q.setParameter("dtini", pedidoFilter.getDtEmissaoIni());
        q.setParameter("dtend", pedidoFilter.getDtEmissaoEnd());
        if (pedidoFilter.getVendedor().getIdVendedor() != null)
            q.setParameter("vendedor", pedidoFilter.getVendedor().getIdVendedor());
        List<Object[]> lista = q.getResultList();
        List<ClientesGrupo> result = new ArrayList<ClientesGrupo>();
        for (Object[] item : lista) {
            ClientesGrupo cg = new ClientesGrupo();
            cg.setCliente((String)item[1]);
            cg.setVendedor((String)item[0]);
            result.add(cg);
        }
        return result;
    }

    public List<ResumoVendasUnidade> findVendasSegmento(PedidoFilter pedidoFilter) {
        StringBuilder sb = new StringBuilder();

        sb.append("select nome, sum(case when (situacao = 'A' or atend = 'P') then comissaonf else comissaovenda end)");
        sb.append(" from (");
        sb.append(" select s.nome, p.idpedido, p.situacao, p.atend,");
        sb.append("   p.valorcomissao + (case when p.valorop is null then 0 else p.valorop end) as comissaovenda, ");
        sb.append("   (select sum(ap.VALORCOM) from TBATENDIMENTOPEDIDO ap where p.idpedido = ap.idpedido) as comissaonf");
        sb.append(" from tbpedido p");
        sb.append(" inner join tbcliente c on p.idcliente = c.idcliente");
        sb.append(" inner join TBSEGMENTOCLIENTE sc on c.idcliente = sc.IDCLIENTE");
        sb.append(" inner join TBSEGMERCADO s on sc.IDSEGMENTO = s.IDSEGMENTO");
        sb.append(" where p.dtpedido between :dtini and :dtend");
        if (pedidoFilter.getVendedor().getIdVendedor() != null)
            sb.append("  and p.idvendedor = :vendedor");
        sb.append(" )");
        sb.append(" group by nome");
        sb.append(" order by 2 desc");

        Query q = ManagerFactory.getEntityManager().createNativeQuery(sb.toString());
        q.setParameter("dtini", pedidoFilter.getDtEmissaoIni());
        q.setParameter("dtend", pedidoFilter.getDtEmissaoEnd());
        if (pedidoFilter.getVendedor().getIdVendedor() != null)
            q.setParameter("vendedor", pedidoFilter.getVendedor().getIdVendedor());
        List<Object[]> lista = q.getResultList();
        List<ResumoVendasUnidade> result = new ArrayList<ResumoVendasUnidade>();
        for (Object[] item : lista) {
            ResumoVendasUnidade pc = new ResumoVendasUnidade();
            pc.setSegmento((String)item[0]);
            pc.setComissao((BigDecimal)item[1]);
            result.add(pc);
        }
        return result;
    }

    public List<ResumoVendasUnidade> findVendasGrupoProduto(PedidoFilter pedidoFilter) {
        StringBuilder sb = new StringBuilder();

        sb.append("select nomegrupo, sum(case when (situacao = 'A' or atend = 'P') then comissaonf else comissaovenda end)");
        sb.append(" from (");
        sb.append(" select t.nomegrupo, p.situacao, p.atend, p.valorcomissao + (case when p.valorop is null then 0 else p.valorop end) as comissaovenda, ");
        sb.append("   (select sum(ap.VALORCOM) from TBATENDIMENTOPEDIDO ap where p.idpedido = ap.idpedido) as comissaonf");
        sb.append(" from tbpedido p");
        sb.append(" inner join (");
        sb.append(" select distinct pe.idpedido, g.NOMEGRUPO");
        sb.append(" from tbpedido pe");
        sb.append(" inner join tbitempedido ip on pe.IDPEDIDO = ip.IDPEDIDO");
        sb.append(" inner join tbproduto pr on ip.idproduto = pr.idproduto");
        sb.append(" inner join TBGRUPOPRODUTO g on pr.IDCODGRUPO = g.IDCODGRUPO");
        sb.append(" where pe.dtpedido between :dtini and :dtend");
        if (pedidoFilter.getVendedor().getIdVendedor() != null)
            sb.append("  and pe.idvendedor = :vendedor");
        sb.append(" ) t on t.idpedido = p.idpedido");
        sb.append(" )");
        sb.append(" group by nomegrupo");
        sb.append(" order by 2 desc");

        Query q = ManagerFactory.getEntityManager().createNativeQuery(sb.toString());
        q.setParameter("dtini", pedidoFilter.getDtEmissaoIni());
        q.setParameter("dtend", pedidoFilter.getDtEmissaoEnd());
        if (pedidoFilter.getVendedor().getIdVendedor() != null)
            q.setParameter("vendedor", pedidoFilter.getVendedor().getIdVendedor());
        List<Object[]> lista = q.getResultList();
        List<ResumoVendasUnidade> result = new ArrayList<ResumoVendasUnidade>();
        for (Object[] item : lista) {
            ResumoVendasUnidade pc = new ResumoVendasUnidade();
            pc.setNomeGrupo((String)item[0]);
            pc.setComissao((BigDecimal)item[1]);
            result.add(pc);
        }
        return result;
    }

    public void exportarPgtoCliente(List<PgtoCliente> lista, int qtd, int numDias) throws Exception {
        for (PgtoCliente pgc : lista) {
            Date dtPgto = pgc.getDtPgto();
            Date dtPrevPgto = pgc.getDtPrevisaoPgto();
            Date dtVencimento = pgc.getDtVencimento();
            for (int i = 0; i < qtd; i++) {
                if (dtPgto != null)
                    dtPgto = DateUtils.getNextDate(dtPgto, numDias);
                if (dtPrevPgto != null)
                    dtPrevPgto = DateUtils.getNextDate(dtPrevPgto, numDias);
                if (dtVencimento != null)
                    dtVencimento = DateUtils.getNextDate(dtVencimento, numDias);
                PgtoCliente value = new PgtoCliente();
                value.setAtendimentoPedido(pgc.getAtendimentoPedido());
                value.setCobranca(pgc.getCobranca());
                value.setComplemento(pgc.getComplemento());
                value.setContaRepres(pgc.getContaRepres());
                value.setDtPgto(dtPgto);
                value.setDtPrevisaoPgto(dtPrevPgto);
                value.setDtVencimento(dtVencimento);
                value.setObservacao(pgc.getObservacao());
                value.setTipoPgto(pgc.getTipoPgto());
                value.setValor(pgc.getValor());
                insertRecord(value);
            }
        }
    }

    public void baixaPgtoCliente(String bo) {
        StringBuilder sb = new StringBuilder();

        sb.append("update tbpgtocliente pc set pc.dtpgto = :dtatual, pc.vlrPgto = pc.valor where pc.idpgtocliente in (");
        sb.append("	select pgc.idpgtocliente from tbpedido p");
        sb.append("	inner join tbcliente c on c.idcliente = p.idcliente");
        sb.append("	inner join tbsitcliente sc on sc.idsitcliente = c.idsitcliente");
        sb.append("	inner join tbpgtocliente pgc on pgc.idpedido = p.idpedido");
        sb.append("	where sc.nome <> :descricao and dateadd(1 day to pgc.venc) = :dtatual and pgc.tppgto = :bo and pgc.dtpgto is null");
        sb.append(")");
        
        EntityManager em = getEntityManager();
        EntityTransaction t = em.getTransaction();
        try {
            t.begin();
            Query q = em.createNativeQuery(sb.toString());
            q.setParameter("bo", bo);
            q.setParameter("dtatual", DateUtils.getCurrentDate());
            q.setParameter("descricao", "FINANCEIRO RUIM");
            getLogger().info("Baixa automatica: " + q.executeUpdate());
            t.commit();
        } catch (Exception e) {
            t.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }

    }

}

class MapaComparator implements Comparator {

    @Override
    public int compare(Object obj1, Object obj2) {
        MapaComissao grupo1 = (MapaComissao) obj1;
        MapaComissao grupo2 = (MapaComissao) obj2;

        return grupo2.getTotalMeses().compareTo(grupo1.getTotalMeses());

    }
}
