/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import vendas.beans.AniversarioFilter;
import vendas.beans.ClientesFilter;
import vendas.beans.RepresFilter;
import vendas.beans.ResumoVendasUnidade;
import vendas.entity.Cliente;
import vendas.entity.ClienteRepres;
import vendas.entity.ClienteRepresPK;
import vendas.entity.ContaRepres;
import vendas.entity.EntradaRepres;
import vendas.entity.Repres;
import vendas.entity.VendedorRepres;
import vendas.entity.manager.ManagerFactory;

/**
 *
 * @author Sam
 */
public class RepresDao extends BaseDao {

    public List<VendedorRepres> findAniversariantes(AniversarioFilter filtro) {
        Session session = ManagerFactory.getSession();
        Criteria criteria = session.createCriteria(VendedorRepres.class);
        if (filtro.getNome() != null && filtro.getNome().length() > 0)
            criteria.add(Restrictions.like("contato", filtro.getNome(), MatchMode.ANYWHERE));
        else {
            criteria.add(Restrictions.ge("dtaniver", filtro.getMesInicial()));
            criteria.add(Restrictions.le("dtaniver", filtro.getMesFinal()));
        }
        return criteria.list();
    }

    @Override
    public List findAll() {
        getLogger().info("findAll");
        Repres repres = new Repres();
        repres.setInativo('A');
        RepresFilter filtro = new RepresFilter();
        filtro.setRepres(repres);
        return findByExample(filtro);
    }

    @Override
    public List findByExample(Object obj) {
        getLogger().info("findByExample");
        RepresFilter filter = (RepresFilter) obj;
        Session session = ManagerFactory.getSession();
        Criteria criteria = session.createCriteria(Repres.class).addOrder(Order.asc("razao"));
        Repres repres = filter.getRepres();

        if ((repres.getRazao() != null) && (repres.getRazao().length() > 0)) {
            criteria.add(Restrictions.like("razao", "%" + repres.getRazao() + "%"));
        }
        if ((repres.getCidade() != null) && (repres.getCidade().length() > 0)) {
            criteria.add(Restrictions.eq("cidade", repres.getCidade()));
        }
        if ((repres.getCnpj() != null) && (repres.getCnpj().length() > 0)) {
            criteria.add(Restrictions.eq("cnpj", repres.getCnpj()));
        }
        if ((repres.getBairro() != null) && (repres.getBairro().length() > 0)) {
            criteria.add(Restrictions.like("bairro", "%" + repres.getBairro() + "%"));
        }
        if (repres.getInativo() == 'A') {
            criteria.add(Restrictions.eq("inativo", '0'));
        } else if (repres.getInativo() == 'I') {
            criteria.add(Restrictions.eq("inativo", '1'));
        }
        criteria.add(Restrictions.gt("idRepres", -1));
        return criteria.list();
    }

    public List findRepresPgto() {
        getLogger().info("findRepresPgto");
        return ManagerFactory.getEntityManager().createQuery("select object(r) from Repres as r where r.idRepres in (select p.repres.idRepres from Pedido as p, AtendimentoPedido as a where p = a.pedido and p.repres = r and a.dtPgtoComissao is null) and r.total > 0 order by r.razao").getResultList();
    }

    public String findCodigo(Cliente cliente, Repres repres) {
        getLogger().info("findCodigo");
        ClienteRepresPK pk = new ClienteRepresPK();
        pk.setIdCliente(cliente.getIdCliente());
        pk.setIdRepres(repres.getIdRepres());
        ClienteRepres clirepres;
        try {
            clirepres = (ClienteRepres) findById(ClienteRepres.class, pk);
        } catch (Exception e) {
            clirepres = null;
        }
        if (clirepres == null) {
            return "sem código";
        } else {
            StringBuilder sb = new StringBuilder();
            String codigo = clirepres.getCodRepres();
            if ((codigo != null) && (codigo.length() > 0)) {
                sb.append(codigo);
            }
            String ident = clirepres.getCodIdentificador();
            if ((ident != null) && (ident.length() > 0)) {
                sb.append(" - ");
                sb.append(ident);
            }
            return sb.toString();
        }
    }

    public List<EntradaRepres> findRecebimentosByRepres(RepresFilter filter) {

        getLogger().info("findRecebimentos");
        Session session = ManagerFactory.getSession();
        StringBuilder sb = new StringBuilder();
        sb.append("select o from EntradaRepres o");
        sb.append(" where o.repres.idRepres = :repres");
        sb.append(" and o.dtEntrada between :dt1 and :dt2");
        sb.append(" order by o.dtEntrada desc");
        org.hibernate.Query q = session.createQuery(sb.toString());
        q.setParameter("repres", filter.getRepres().getIdRepres());
        q.setParameter("dt1", filter.getDtRecebimentoIni());
        q.setParameter("dt2", filter.getDtRecebimentoEnd());

        return q.list();

    }
    public List findRecebimentos(RepresFilter filter) {
        getLogger().info("findRecebimentos");
        Session session = ManagerFactory.getSession();
        StringBuilder sb = new StringBuilder();
        sb.append("select r.razao as representada, sum(e.valor) as valor from EntradaRepres as e, Repres as r");
        sb.append(" where e.repres.idRepres = r.idRepres");
        //sb.append(" and e.dtEntrada between :dt1 and :dt2 and r.inativo = '0'");
        sb.append(" and e.dtEntrada between :dt1 and :dt2");
        if (filter.getRepres().getIdRepres() != null) {
            sb.append(" and r.idRepres = :repres");
        }
        sb.append(" group by r.razao");
        if (filter.getOrdem() == filter.RAZAO)
            sb.append(" order by r.razao");
        else
            sb.append(" order by 2");
        if (filter.getDesc())
            sb.append(" desc");
        org.hibernate.Query q = session.createQuery(sb.toString()).setResultTransformer(Transformers.aliasToBean(ResumoVendasUnidade.class));
        q.setParameter("dt1", filter.getDtRecebimentoIni());
        q.setParameter("dt2", filter.getDtRecebimentoEnd());
        if (filter.getRepres().getIdRepres() != null) {
            q.setParameter("repres", filter.getRepres().getIdRepres());
        }

        return q.list();
    }

    public List getContas(RepresFilter filter) {
        Session session = ManagerFactory.getSession();

        Criteria criteria = session.createCriteria(ContaRepres.class);
        Criteria represCriteria = criteria.createCriteria("repres").addOrder(Order.asc("razao"));
        Repres repres = filter.getRepres();

        if ((repres.getRazao() != null) && (repres.getRazao().length() > 0)) {
            represCriteria.add(Restrictions.like("razao", "%" + repres.getRazao() + "%"));
        }
        if ((repres.getCidade() != null) && (repres.getCidade().length() > 0)) {
            represCriteria.add(Restrictions.eq("cidade", repres.getCidade()));
        }
        if ((repres.getCnpj() != null) && (repres.getCnpj().length() > 0)) {
            represCriteria.add(Restrictions.eq("cnpj", repres.getCnpj()));
        }
        if ((repres.getBairro() != null) && (repres.getBairro().length() > 0)) {
            represCriteria.add(Restrictions.like("bairro", "%" + repres.getBairro() + "%"));
        }
        if (repres.getInativo() == 'A') {
            represCriteria.add(Restrictions.eq("inativo", '0'));
        } else if (repres.getInativo() == 'I') {
            represCriteria.add(Restrictions.eq("inativo", '1'));
        }
        represCriteria.add(Restrictions.gt("idRepres", -1));
        return criteria.list();
    }
    public List getContasById(Integer id) {
        Session session = ManagerFactory.getSession();

        Criteria criteria = session.createCriteria(ContaRepres.class);
        Criteria represCriteria = criteria.createCriteria("repres").addOrder(Order.asc("razao"));
        represCriteria.add(Restrictions.eq("idRepres", id));
        return criteria.list();
    }

    public List findClientes(ClientesFilter filter) {
        getLogger().info("findClientes");
        Session session = ManagerFactory.getSession();

        StringBuilder sb = new StringBuilder();
        sb.append("select o from ClienteRepres o, Cliente c, SituacaoCliente s, Repres r");
        sb.append(" where c.situacaoCliente.idSitCliente = s.idSitCliente");
        sb.append(" and c.idCliente = o.clienteRepresPK.idCliente");
        sb.append(" and o.clienteRepresPK.idRepres = r.idRepres");




        sb.append(" and r.inativo = '0'");
        if (filter.getDtPedidoIni() != null) {
            sb.append(" and c.idCliente in (select p.cliente.idCliente from Pedido p where p.cliente.idCliente = c.idCliente and p.dtPedido between :d1 and :d2)");
        }
        if (filter.getDtInclusaoIni() != null) {
            sb.append(" c.dtInclusao between :dti1 and :dti2");
        }
        if (!filter.getPedidoDesabilitado()) {
            sb.append(" and s.pedido = '1'");
        }
        if (filter.getRepres().getIdRepres() != null) {
            sb.append(" and r.idRepres = :rep");
        }
        if (filter.getVendedor().getIdVendedor() != null) {
            sb.append(" and c.idCliente in (select cl.idCliente from Cliente cl join cl.vendedores s where exists (select t from Vendedor t where s = t and t.idVendedor = :vendedor))");
        }
        if (filter.getSegmento().getIdSegmento()!= null) {
            sb.append(" and c.idCliente in (select cl.idCliente from Cliente cl join cl.segmentos s where exists (select t from SegMercado t where s = t and t.idSegmento = :segmento))");
        }
        if ((filter.getCliente().getCidade() != null) && (filter.getCliente().getCidade().length() > 0)) {
            sb.append(" and c.cidade like :cid");
        }
        sb.append(" order by r.razao");
        if (filter.getOrder() == 0) {
            sb.append(", o.codRepres");
        } else {
            sb.append(", c.razao");
        }

        org.hibernate.Query q = session.createQuery(sb.toString());

        if (filter.getDtPedidoIni() != null) {
            q.setParameter("d1", filter.getDtPedidoIni());
            q.setParameter("d2", filter.getDtPedidoEnd());
        }
        if (filter.getDtInclusaoIni() != null) {
            q.setParameter("dti1", filter.getDtInclusaoIni());
            q.setParameter("dti2", filter.getDtInclusaoEnd());
        }
        if (!filter.getPedidoDesabilitado()) {
            sb.append(" and s.pedido = '1'");
        }
        if (filter.getRepres().getIdRepres() != null) {
            q.setParameter("rep", filter.getRepres().getIdRepres());
        }
        if (filter.getVendedor().getIdVendedor() != null) {
            q.setParameter("vendedor", filter.getVendedor().getIdVendedor());
        }
        if (filter.getSegmento().getIdSegmento()!= null) {
            q.setParameter("segmento", filter.getSegmento().getIdSegmento());
        }
        if ((filter.getCliente().getCidade() != null) && (filter.getCliente().getCidade().length() > 0)) {
            q.setParameter("cid", "%" + filter.getCliente().getCidade() + "%");
        }
        
        return q.list();
    }

    public void addCliente(Repres repres, Cliente cliente) {
        getLogger().info("addCliente");
        ClienteRepresPK pk = new ClienteRepresPK(repres.getIdRepres(), cliente.getIdCliente());
        ClienteRepres clirepr;
        try {
            clirepr = (ClienteRepres) findById(ClienteRepres.class, pk);
        } catch (Exception e) {
            clirepr = null;
        }
        if (clirepr == null) {
            getLogger().info("clirepr nullo");
            clirepr = new ClienteRepres();
            clirepr.setClienteRepresPK(pk);
            clirepr.setRepres(repres);
            clirepr.setCliente(cliente);
            try {
                insertRecord(clirepr);
            } catch (Exception e) {
                getLogger().error(e.getMessage(), e);
            }
        } else {
            getLogger().info("clirepr not null");
        }

    }

    public void atualizaTabela(Date date, Integer id) throws Exception {
        getLogger().info("atualizaTabela");
        EntityManager em = getEntityManager();
        Query q = em.createQuery("update Repres set dataTabela = :tabela where idRepres = :repres");
        q.setParameter("tabela", date);
        q.setParameter("repres", id);
        try {
            em.getTransaction().begin();
            q.executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new Exception(e);
        } finally {
            em.close();
        }
    }

    public void alteraIPI(Repres repres, BigDecimal value) throws Exception {
                getLogger().info("atualizaIPI");
        EntityManager em = getEntityManager();
        Query q = em.createQuery("update RepresProduto set ipi = :valorIPI where represProdutoPK.idRepres = :repres");
        q.setParameter("valorIPI", value);
        q.setParameter("repres", repres.getIdRepres());
        try {
            em.getTransaction().begin();
            q.executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new Exception(e);
        } finally {
            em.close();
        }
    }
}
