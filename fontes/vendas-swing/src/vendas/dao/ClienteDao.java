/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas.dao;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import ritual.swing.TApplication;
import ritual.util.DateUtils;
import vendas.beans.AniversarioFilter;
import vendas.beans.ClientesFilter;
import vendas.beans.CobrancaFilter;
import vendas.beans.MapaComissao;
import vendas.beans.Rotatividade;
import vendas.beans.SaldoClienteTo;
import vendas.beans.VendasCliente;
import vendas.beans.VisitaFilter;
import vendas.entity.Cliente;
import vendas.entity.Comprador;
import vendas.entity.GrupoCliente;
import vendas.entity.SituacaoCliente;
import vendas.entity.Vendedor;
import vendas.entity.VisitaCliente;
import vendas.entity.manager.ManagerFactory;
import vendas.exception.DAOException;
import vendas.util.Constants;

/**
 *
 * @author Sam
 */
public class ClienteDao extends BaseDao {

    public List<Comprador> findAniversariantes(AniversarioFilter filtro) {
        Session session = ManagerFactory.getSession();
        Criteria criteria = session.createCriteria(Comprador.class);
        if (filtro.getNome() != null && filtro.getNome().length() > 0) {
            criteria.add(Restrictions.like("contato", filtro.getNome(), MatchMode.ANYWHERE));
        } else {
            criteria.add(Restrictions.ge("dtaniver", filtro.getMesInicial()));
            criteria.add(Restrictions.le("dtaniver", filtro.getMesFinal()));
        }
        return criteria.list();
    }
    
    public Boolean temClientesBloqueados() {
        getLogger().info("temClientesBloqueados ini");
        Query q = ManagerFactory.getEntityManager().createQuery("select count(*) from Cliente where bloqueado = 'S'");
        q.setMaxResults(1);
        Long result = (Long) q.getSingleResult();
        return result.intValue() > 0;
    }

    public Boolean isClientePossuiPedidoPendente(Integer id) {
        StringBuilder sb = new StringBuilder();
        sb.append("select count(*) from Pedido where cliente.idCliente = :id and situacao = :sit");
        Query q = ManagerFactory.getEntityManager().createQuery(sb.toString());
        q.setParameter("id", id);
        q.setParameter("sit", "N");
        Long result = (Long) q.getSingleResult();
        return result.intValue() > 0;
    }

    @Override
    public List<Cliente> findByExample(Object obj) {
        getLogger().info("findByExample");
        ClientesFilter clienteFilter = (ClientesFilter) obj;
        Cliente cliente = clienteFilter.getCliente();
        Session session = ManagerFactory.getSession();
        Criteria criteria = session.createCriteria(Cliente.class);
        if ((cliente.getRazao() != null) && (cliente.getRazao().length() > 0)) {
            criteria.add(Restrictions.like("razao", "%" + cliente.getRazao() + "%"));
        }
        if (clienteFilter.getTipoBloqueado() == 0) {
            if (cliente.isBloqueado()) {
                criteria.add(Restrictions.eq("bloqueado", "S"));
            }
        } else {
            if (cliente.isBloqueado()) {
                criteria.add(Restrictions.eq("bloqueado", "S"));
            } else {
                criteria.add(Restrictions.eq("bloqueado", "N"));
            }
        }
        if ((cliente.getCidade() != null) && (cliente.getCidade().length() > 0)) {
            criteria.add(Restrictions.eq("cidade", cliente.getCidade()));
        }
        if ((cliente.getUf() != null) && (cliente.getUf().length() > 0)) {
            criteria.add(Restrictions.eq("uf", cliente.getUf()));
        }
        if ((cliente.getBairro() != null) && (cliente.getBairro().length() > 0)) {
            criteria.add(Restrictions.like("bairro", "%" + cliente.getBairro() + "%"));
        }
        if ((cliente.getCnpj() != null) && (cliente.getCnpj().length() > 0)) {
            criteria.add(Restrictions.like("cnpj", cliente.getCnpj()));
        }
        if (cliente.getSituacaoCliente().getIdSitCliente() == null) {
            if (!"T".equals(cliente.getSituacaoCliente().getPedido())) {
                criteria.createCriteria("situacaoCliente").add(Restrictions.eq("pedido", cliente.getSituacaoCliente().getPedido()));
            }
        } else {
            criteria.createCriteria("situacaoCliente").add(Restrictions.eq("idSitCliente", cliente.getSituacaoCliente().getIdSitCliente()));
        }
        if (clienteFilter.getEntregarBoleto()) {
            criteria.add(Restrictions.eq("entregarBoleto", '1'));
        }
        if (clienteFilter.getRoteiro().getIdRoteiro() != null) {
            Criteria roteiro = criteria.createCriteria("roteiros");
            roteiro.add(Restrictions.eq("idRoteiro", clienteFilter.getRoteiro().getIdRoteiro()));
        }
        if (clienteFilter.getSegmento().getIdSegmento() != null) {
            Criteria segmento = criteria.createCriteria("segmentos");
            segmento.add(Restrictions.eq("idSegmento", clienteFilter.getSegmento().getIdSegmento()));
        }
        Criteria vendedorCriteria = null;
        if (clienteFilter.getVendedor().getIdVendedor() != null) {
            vendedorCriteria = criteria.createCriteria("vendedores").add(Restrictions.eq("idVendedor", clienteFilter.getVendedor().getIdVendedor()));
        } else if (TApplication.getInstance().getUser().isVendedor()) {
            vendedorCriteria = criteria.createCriteria("vendedores").add(Restrictions.eq("idVendedor", TApplication.getInstance().getUser().getIdvendedor()));
        }
        
        criteria.addOrder(Order.desc("bloqueado"));
        
        if ((clienteFilter.getGrupoCliente() != null) && (clienteFilter.getGrupoCliente().getIdGrupoCliente() != null)) {
            Criteria grupoCriteria = criteria.createCriteria("gruposCliente").add(Restrictions.eq("idGrupoCliente", clienteFilter.getGrupoCliente().getIdGrupoCliente()));
        }
        if (clienteFilter.getOrder() == 0) {
            criteria.addOrder(Order.asc("razao"));
        }
        if (clienteFilter.getOrder() == 1) {
            criteria.addOrder(Order.asc("uf"));
            criteria.addOrder(Order.asc("cidade"));
            criteria.addOrder(Order.asc("razao"));
        }
        if (clienteFilter.getOrder() == 2) {
            criteria.addOrder(Order.asc("bairro"));
            if (vendedorCriteria == null) {
                vendedorCriteria = criteria.createCriteria("vendedores");
            }
            vendedorCriteria.addOrder(Order.asc("nome"));
            criteria.addOrder(Order.asc("razao"));
        }
        if (clienteFilter.getOrder() == 3) {
            criteria.addOrder(Order.asc("bairro"));
            criteria.addOrder(Order.asc("razao"));
        }

        if (clienteFilter.getRepres() != null && clienteFilter.getRepres().getIdRepres() != null) {
            Criteria represCriteria = criteria.createCriteria("representadas");
            represCriteria.add(Restrictions.eq("clienteRepresPK.idRepres", clienteFilter.getRepres().getIdRepres()));
        }
        
        if ((clienteFilter.getClientes() != null) && !clienteFilter.getClientes().isEmpty()) {
            criteria.add(Restrictions.in("idCliente", clienteFilter.getClientes()));
        }

        List<Cliente> lista = criteria.list();

        return lista;
    }

    public List findAllAtivos() {
        getLogger().info("findAllAtivos ini");
        Cliente cliente = new Cliente();
        Vendedor vendedor = new Vendedor();
        SituacaoCliente situacao = new SituacaoCliente();
        
        situacao.setNome("TODOS");
        situacao.setPedido("1");
        cliente.setBloqueado(Boolean.FALSE);

        if (TApplication.getInstance().getUser().isVendedor()) {
            vendedor.setIdVendedor(TApplication.getInstance().getUser().getIdvendedor());
            vendedor.setNome(TApplication.getInstance().getUser().getUserName());
        } else {
            vendedor.setNome("TODOS");
        }

        cliente.setVendedor(vendedor);
        cliente.setSituacaoCliente(situacao);
        ClientesFilter clienteFilter = new ClientesFilter();
        clienteFilter.setCliente(cliente);
        clienteFilter.setTipoBloqueado(1);
        return findByExample(clienteFilter);
    }

    public List findNotInGroup(GrupoCliente grupo) {
        getLogger().info("findNotInGroup ini");
        Session session = ManagerFactory.getSession();
        Criteria criteria = session.createCriteria(Cliente.class).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria.addOrder(Order.asc("razao"));
        criteria.createCriteria("situacaoCliente").add(Restrictions.eq("pedido", "1"));
        criteria.createCriteria("grupoClientes").add(Restrictions.ne("idGrupoCliente", grupo.getIdGrupoCliente()));
        return criteria.list();
    }

    public void atualizaAtrasos() {
        getLogger().info("atualizaAtrasos ini");
        StringBuilder sb = new StringBuilder();

        sb.append("select idcliente, max(dias) as dias from ( ");
        sb.append(" select p.idcliente, max(cast('Now' as date) - cast(pg.venc as date)) as dias from tbpgtocliente pg  ");
        sb.append("	 inner join tbpedido p on p.idpedido = pg.idpedido ");
        sb.append("	 where pg.dtpgto is null and pg.venc < cast('Now' as time) ");
        sb.append("      and pg.venc > :dt");
        sb.append("	 and cast('Now' as date) - cast(pg.venc as date) > 0 ");
        sb.append("      group by p.idcliente");
        sb.append(" union ");
        sb.append(" select p.idcliente, max(cast(pg.dtpgto as date) - cast(pg.venc as date)) as dias from tbpgtocliente pg  ");
        sb.append("	 inner join tbpedido p on p.idpedido = pg.idpedido ");
        sb.append("	 where pg.dtpgto is not null ");
        sb.append("      and pg.venc > :dt");
        sb.append("	 and cast(pg.dtpgto as date) - cast(pg.venc as date) > 0 ");
        sb.append("      group by p.idcliente");
        sb.append(") group by idcliente ");

        Session session = ManagerFactory.getSession();
        SQLQuery q = (SQLQuery) session.createSQLQuery(sb.toString());

        q.setParameter("dt", DateUtils.parse("01/08/2016"));

        List<Object[]> lista = q.list();
        sb = new StringBuilder();
        sb.append("update tbcliente set numdiasatraso = :dias where idcliente = :id");

        if (lista.isEmpty()) {
            return;
        }

        EntityManager em = getEntityManager();
        EntityTransaction t = em.getTransaction();

        try {
            t.begin();
            q = (SQLQuery) session.createSQLQuery("update tbcliente set numdiasatraso = null");
            q.executeUpdate();
            q = (SQLQuery) session.createSQLQuery(sb.toString());

            for (Object[] item : lista) {
                q.setParameter("dias", item[1]);
                q.setParameter("id", item[0]);
                q.executeUpdate();
            }
        } catch (Exception e) {
            t.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }

        getLogger().info("atualizaAtrasos end");

    }

    public void atualizaAtrasoCliente(Integer id) {
        getLogger().info("atualizaAtrasos ini");
        StringBuilder sb = new StringBuilder();

        sb.append("select idcliente, max(dias) as dias from ( ");
        sb.append(" select p.idcliente, max(cast('Now' as date) - cast(pg.venc as date)) as dias from tbpgtocliente pg  ");
        sb.append("	 inner join tbpedido p on p.idpedido = pg.idpedido ");
        sb.append("	 where pg.dtpgto is null and pg.venc < cast('Now' as time) ");
        sb.append("	 and cast('Now' as date) - cast(pg.venc as date) > 0 ");
        sb.append("      and pg.venc > :dt");
        sb.append("      and p.idcliente = :cli");
        sb.append("      group by p.idcliente");
        sb.append(" union ");
        sb.append(" select p.idcliente, max(cast(pg.dtpgto as date) - cast(pg.venc as date)) as dias from tbpgtocliente pg  ");
        sb.append("	 inner join tbpedido p on p.idpedido = pg.idpedido ");
        sb.append("	 where pg.dtpgto is not null ");
        sb.append("	 and cast(pg.dtpgto as date) - cast(pg.venc as date) > 0 ");
        sb.append("      and pg.venc > :dt");
        sb.append("      and p.idcliente = :cli");
        sb.append("      group by p.idcliente");
        sb.append(") group by idcliente ");

        Session session = ManagerFactory.getSession();
        SQLQuery q = (SQLQuery) session.createSQLQuery(sb.toString());
        q.setParameter("cli", id);
        q.setParameter("dt", DateUtils.parse("01/08/2016"));

        List<Object[]> lista = q.list();
        sb = new StringBuilder();
        sb.append("update tbcliente set numdiasatraso = :dias where idcliente = :id");

        if (lista.isEmpty()) {
            return;
        }

        EntityManager em = getEntityManager();
        EntityTransaction t = em.getTransaction();

        try {
            t.begin();
            q = (SQLQuery) session.createSQLQuery("update tbcliente set numdiasatraso = null where idcliente = :cli");
            q.setParameter("cli", id);
            q.executeUpdate();

            q = (SQLQuery) session.createSQLQuery("update tbcliente set numdiasatraso = :dias where idcliente = :id");

            for (Object[] item : lista) {
                q.setParameter("dias", item[1]);
                q.setParameter("id", item[0]);
                q.executeUpdate();
            }
        } catch (Exception e) {
            t.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }

    }

    public List<VendasCliente> getClientesPgtosPendentes() {
        getLogger().info("getClientesPgtosPendentes ini");
        StringBuilder sb = new StringBuilder();

        sb.append("select idcliente, razao, sum(vencido) as vencido, sum(avencer) as avencer");
        sb.append(" from (");
        sb.append("  select c.idcliente, c.razao, ");
        sb.append("	(select pc.valor from tbpgtocliente pc where pc.idpgtocliente = o.idpgtocliente and pc.venc < 'now') as vencido,");
        sb.append("	(select pc.valor from tbpgtocliente pc where pc.idpgtocliente = o.idpgtocliente and pc.venc >= 'now') as avencer");
        sb.append("  from tbCliente c");
        sb.append("	 inner join tbpedido p on p.idcliente = c.idcliente");
        sb.append("	 inner join tbPgtoCliente o on o.idpedido = p.idpedido and o.dtpgto is null");
        sb.append("	 inner join TBSITCLIENTE s on s.IDSITCLIENTE = c.IDSITCLIENTE and s.PEDIDO = '1' and s.nome = 'FINANCEIRO RUIM'");

        if (TApplication.getInstance().getUser().isVendedor()) {
            sb.append(" inner join tbvendedorcliente v on v.idcliente = p.idcliente and v.idvendedor = ").append(TApplication.getInstance().getUser().getIdvendedor());
        }

        sb.append(" ) group by idcliente, razao order by razao    ");

        Session session = ManagerFactory.getSession();
        SQLQuery q = (SQLQuery) session.createSQLQuery(sb.toString());

        List<Object[]> lista = q.list();
        List result = new ArrayList();
        VendasCliente resumo;

        for (Object[] item : lista) {
            resumo = new VendasCliente();
            resumo.setId((Integer) item[0]);
            resumo.setRAZAO((String) item[1]);
            resumo.setValorVencido((BigDecimal) item[2]);
            resumo.setValorPgto((BigDecimal) item[3]);
            result.add(resumo);
        }

        return result;
    }

    public List<VisitaCliente> findClientesAVisitar(VisitaFilter filter) {
        getLogger().info("findClientesAVisitar ini");
        StringBuilder sb = new StringBuilder();
        sb.append("select distinct v.idvendedor, v.nome, p.dtpedido, 0 as visita, ' ' as observacao from tbpedido p, tbvendedor v where p.idvendedor = v.idvendedor and p.dtpedido between :dt1 and :dt2");
        sb.append("   and cast(p.dtpedido as varchar(30)) || v.nome not in (");
        sb.append(" select cast(vc.dtvisita as varchar(30)) || v.nome from tbvisitacliente vc, tbvendedor v where vc.idvendedor = v.idvendedor");
        sb.append(" and vc.dtvisita between :dt1 and :dt2");
        sb.append(" )");
        sb.append(" union");
        sb.append(" select v.idvendedor, v.nome, vc.dtvisita, 1 as visita, cast(obs as varchar(512)) as observacao from tbvisitacliente vc, tbvendedor v where vc.idvendedor = v.idvendedor");
        sb.append(" and vc.dtvisita between :dt1 and :dt2 and vc.idcliente is not null");
        sb.append(" union");
        sb.append(" select v.idvendedor, v.nome, vc.dtvisita, 2 as visita, cast(obs as varchar(512)) as observacao from tbvisitacliente vc, tbvendedor v where vc.idvendedor = v.idvendedor");
        sb.append(" and vc.dtvisita between :dt1 and :dt2 and vc.idcliente is null");
        sb.append(" order by 2, 3");

        Session session = ManagerFactory.getSession();
        SQLQuery q = (SQLQuery) session.createSQLQuery(sb.toString());

        q.setParameter("dt1", filter.getDtInicio());
        q.setParameter("dt2", filter.getDtFim());
        List<Object[]> visitas = q.list();
        List<VisitaCliente> lista = new ArrayList<VisitaCliente>();
        VisitaCliente o;
        PedidoDao pedidoDao = (PedidoDao) TApplication.getInstance().lookupService("pedidoDao");
        for (Object[] item : visitas) {
            o = new VisitaCliente();
            o.setDtVisita((Date) item[2]);
            o.setObs((String) item[4]);
            o.setVendedor(new Vendedor((String) item[1]));
            o.setGerouPedido(((Integer) item[3]).intValue() == 1);
            o.setSemVisita(((Integer) item[3]).intValue() == 2);
            o.setTotalComissao(pedidoDao.getComissoes((Integer) item[0], (Date) item[2], (Date) item[2]));
            lista.add(o);
        }
        for (VisitaCliente v : lista) {
            if (v.getGerouPedido()) {
                v.setCliente(findClienteByVisita(v));
            } else {
                v.setNumeroClientes(0);
            }
            if (v.getSemVisita()) {
                Cliente cli = v.getCliente();
                if (cli == null) {
                    cli = new Cliente();
                }
                String s = cli.getRazao();
                if (s == null) {
                    s = v.getObs();
                } else {
                    s += (". " + v.getObs());
                }
                cli.setRazao(s);
                v.setCliente(cli);
            }
            if (v.getTotalComissao() == null) {
                v.setTotalComissao(BigDecimal.ZERO);
            }
        }
        return lista;
    }

    public Cliente findClienteByVisita(VisitaCliente v) {
        Session session = ManagerFactory.getSession();
        StringBuilder sb = new StringBuilder();
        sb.append("select vc.cliente.fantasia from VisitaCliente vc where vc.vendedor.nome = :vendedor and vc.dtVisita = :dt order by vc.cliente.fantasia");
        Query q = ManagerFactory.getEntityManager().createQuery(sb.toString());
        q.setParameter("vendedor", v.getVendedor().getNome());
        q.setParameter("dt", v.getDtVisita());
        List<String> visitas = q.getResultList();
        StringBuilder clientes = new StringBuilder();
        int i = 0;

        for (String item : visitas) {
            if (i++ > 0) {
                clientes.append(" / ");
            }
            clientes.append(item);
        }
        v.setNumeroClientes(visitas.size());
        return new Cliente(clientes.toString());
    }

    public Integer getNumVisitas(VisitaCliente v) {
        Session session = ManagerFactory.getSession();
        StringBuilder sb = new StringBuilder();
        sb.append("select count(*) from VisitaCliente vc where vc.vendedor.nome = :vendedor and vc.dtVisita = :dt");
        Query q = ManagerFactory.getEntityManager().createQuery(sb.toString());
        q.setParameter("vendedor", v.getVendedor().getNome());
        q.setParameter("dt", v.getDtVisita());
        Long result = (Long) q.getSingleResult();
        return result.intValue();
    }

    public List<VisitaCliente> findVisitas(ClientesFilter clienteFilter) {
        getLogger().info("findVisitas ini");

        Session session = ManagerFactory.getSession();
        Criteria criteria = session.createCriteria(VisitaCliente.class);
        Criteria vendedorCriteria = criteria.createCriteria("vendedor");

        if (clienteFilter.getDtInclusaoEnd() == null) {
            criteria.add(Restrictions.ge("dtVisita", clienteFilter.getDtInclusaoIni()));
        } else {
            criteria.add(Restrictions.between("dtVisita", clienteFilter.getDtInclusaoIni(), clienteFilter.getDtInclusaoEnd()));
        }

        if (clienteFilter.getVendedor().getIdVendedor() != null) {
            vendedorCriteria.add(Restrictions.eq("idVendedor", clienteFilter.getVendedor().getIdVendedor()));
        }

        //criteria.addOrder(Order.asc("dtVisita"));
        vendedorCriteria.addOrder(Order.asc("nome"));

        List<VisitaCliente> lista = criteria.list();
        List<VisitaCliente> clientes = new ArrayList<>();
        VisitaCliente vc;

        Map<String, VisitaCliente> visitas = new HashMap<>();
        String tmp;
        VisitaCliente value;
        String razao;

        for (VisitaCliente item : lista) {
            tmp = DateUtils.format(item.getDtVisita()) + "," + item.getVendedor().getNome();

            if (visitas.containsKey(tmp)) {
                value = visitas.get(tmp);
                if (!value.getListaClientes().contains(item.getCliente())) {
                    value.getListaClientes().add(item.getCliente());
                    visitas.put(tmp, value);
                }
            } else {
                value = new VisitaCliente();
                value.setDtVisita(item.getDtVisita());
                value.setVendedor(item.getVendedor());
                value.setListaClientes(new ArrayList<>());
                value.getListaClientes().add(item.getCliente());
                value.setObs(item.getObs());
                value.setTipoVisita(item.getTipoVisita());
                visitas.put(tmp, value);
            }
        }

        Iterator iterator = visitas.keySet().iterator();

        while (iterator.hasNext()) {
            String o = (String) iterator.next();
            String[] r = o.split(",");
            value = visitas.get(o);
            clientes.add(value);
        }

        Collections.sort(clientes, new VisitaClienteComparator());

        return clientes;
    }

    public List<VisitaCliente> findSemVisitas(ClientesFilter clienteFilter) {
        getLogger().info("findSemVisitas ini");
        StringBuilder sb = new StringBuilder();
        sb.append("select v.nome, c.razao");
        sb.append(" from tbvendedor v");
        sb.append(" inner join tbvendedorcliente vc on vc.idvendedor = v.idvendedor");
        sb.append(" inner join tbcliente c on c.idcliente = vc.idcliente");
        sb.append(" inner join tbsitcliente si on si.idsitcliente = c.idsitcliente");
        sb.append(" where v.ativo = :inativo and si.pedido = :situacao and c.idcliente not in (");
        
        if (clienteFilter.getDtInclusaoEnd() == null) {
            sb.append("select vic.idcliente from tbvisitacliente vic where vic.dtvisita >= :d1 and vic.tipovisita = 'V'");
        } else {
            sb.append("select vic.idcliente from tbvisitacliente vic where vic.dtvisita between :d1 and :d2 and vic.tipovisita = 'V'");
        }

        sb.append(")");

        if (clienteFilter.getVendedor().getIdVendedor() != null) {
            sb.append(" and v.idVendedor = ").append(clienteFilter.getVendedor().getIdVendedor());
        }
        
        sb.append(" order by v.nome, c.razao");

        Session session = ManagerFactory.getSession();
        SQLQuery q = (SQLQuery) session.createSQLQuery(sb.toString());
        q.setParameter("situacao", "1");
        q.setParameter("inativo", "1");
        q.setParameter("d1", clienteFilter.getDtInclusaoIni());

        if (clienteFilter.getDtInclusaoEnd() != null) {
            q.setParameter("d2", clienteFilter.getDtInclusaoEnd());
        }
        
        List<Object[]> lista = q.list();
        List<VisitaCliente> clientes = new ArrayList<>();

        for (Object[] item : lista) {
            VisitaCliente vc = new VisitaCliente();
            vc.setTipoVisita("SV");
            vc.setCliente(new Cliente());
            vc.setVendedor(new Vendedor());
            vc.getVendedor().setNome((String) item[0]);
            vc.getCliente().setRazao((String) item[1]);
            clientes.add(vc);
        }
        
        return clientes;
    }

    public List findMapaVisitas(ClientesFilter clienteFilter) {
        getLogger().info("findMapaVisitas ini");
        Cliente cliente = clienteFilter.getCliente();
        Session session = ManagerFactory.getSession();
        StringBuilder sb = new StringBuilder();
        sb.append("select c.razao, ");
        sb.append(" (select count(*) from tbvisitacliente v where c.idcliente = v.idcliente and extract(month from v.dtvisita) = extract(month from cast('now' as timestamp)) and extract(year from v.dtvisita) = extract(year from cast('now' as timestamp))) as mes1,");
        sb.append(" (select count(*) from tbvisitacliente v, dateadd(-1, cast('now' as timestamp)) where c.idcliente = v.idcliente and extract(month from v.dtvisita) = extract(month from newdate) and extract(year from v.dtvisita) = extract(year from newdate)) as mes2,");
        sb.append(" (select count(*) from tbvisitacliente v, dateadd(-2, cast('now' as timestamp)) where c.idcliente = v.idcliente and extract(month from v.dtvisita) = extract(month from newdate) and extract(year from v.dtvisita) = extract(year from newdate)) as mes3,");
        sb.append(" (select count(*) from tbvisitacliente v, dateadd(-3, cast('now' as timestamp)) where c.idcliente = v.idcliente and extract(month from v.dtvisita) = extract(month from newdate) and extract(year from v.dtvisita) = extract(year from newdate)) as mes4,");
        sb.append(" (select count(*) from tbvisitacliente v, dateadd(-4, cast('now' as timestamp)) where c.idcliente = v.idcliente and extract(month from v.dtvisita) = extract(month from newdate) and extract(year from v.dtvisita) = extract(year from newdate)) as mes5,");
        sb.append(" (select count(*) from tbvisitacliente v, dateadd(-5, cast('now' as timestamp)) where c.idcliente = v.idcliente and extract(month from v.dtvisita) = extract(month from newdate) and extract(year from v.dtvisita) = extract(year from newdate)) as mes6,");
        sb.append(" (select count(*) from tbvisitacliente v, dateadd(-6, cast('now' as timestamp)) where c.idcliente = v.idcliente and extract(month from v.dtvisita) = extract(month from newdate) and extract(year from v.dtvisita) = extract(year from newdate)) as mes7,");
        sb.append(" (select count(*) from tbvisitacliente v, dateadd(-7, cast('now' as timestamp)) where c.idcliente = v.idcliente and extract(month from v.dtvisita) = extract(month from newdate) and extract(year from v.dtvisita) = extract(year from newdate)) as mes8,");
        sb.append(" (select count(*) from tbvisitacliente v, dateadd(-8, cast('now' as timestamp)) where c.idcliente = v.idcliente and extract(month from v.dtvisita) = extract(month from newdate) and extract(year from v.dtvisita) = extract(year from newdate)) as mes9,");
        sb.append(" (select count(*) from tbvisitacliente v, dateadd(-9, cast('now' as timestamp)) where c.idcliente = v.idcliente and extract(month from v.dtvisita) = extract(month from newdate) and extract(year from v.dtvisita) = extract(year from newdate)) as mes10,");
        sb.append(" (select count(*) from tbvisitacliente v, dateadd(-10, cast('now' as timestamp)) where c.idcliente = v.idcliente and extract(month from v.dtvisita) = extract(month from newdate) and extract(year from v.dtvisita) = extract(year from newdate)) as mes11,");
        sb.append(" (select count(*) from tbvisitacliente v, dateadd(-11, cast('now' as timestamp)) where c.idcliente = v.idcliente and extract(month from v.dtvisita) = extract(month from newdate) and extract(year from v.dtvisita) = extract(year from newdate)) as mes12");
        sb.append(" from tbcliente c, tbsitcliente s");
        sb.append(" where c.idsitcliente = s.idsitcliente and s.pedido = '1'");
        if ((cliente.getRazao() != null) && (cliente.getRazao().length() > 0)) {
            sb.append(" and c.razao like '%" + cliente.getRazao() + "%'");
        }
        if ((cliente.getCidade() != null) && (cliente.getCidade().length() > 0)) {
            sb.append(" and c.cidade like '%" + cliente.getCidade() + "%'");
        }
        if ((cliente.getCnpj() != null) && (cliente.getCnpj().length() > 0)) {
            sb.append(" and c.cgc = '" + cliente.getCnpj() + "'");
        }
        if (clienteFilter.getRoteiro().getIdRoteiro() != null) {
            sb.append(" and c.idcliente in (select r.idcliente from tbroteirocliente r where tbcliente.idcliente = r.idcliente and r.idroteiro = " + clienteFilter.getRoteiro().getIdRoteiro() + ") ");
        }
        if (clienteFilter.getSegmento().getIdSegmento() != null) {
            sb.append(" and c.idcliente in (select r.idcliente from tbsegmentocliente r where tbcliente.idcliente = r.idcliente and r.idsegmento = " + clienteFilter.getSegmento().getIdSegmento() + ") ");
        }

        if (cliente.getSituacaoCliente().getIdSitCliente() == null) {
            sb.append(" and s.pedido < 2");
        } else {
            sb.append(" and c.idsitcliente = " + cliente.getSituacaoCliente().getIdSitCliente());
        }
        if (clienteFilter.getVendedor().getIdVendedor() != null) {
            sb.append(" and c.idvendedor = " + clienteFilter.getVendedor().getIdVendedor());
        }
        if (clienteFilter.getGrupoCliente().getIdGrupoCliente() != null) {
            sb.append(" and c.idCliente in (select cl.idCliente from tbcliente cl inner join tbClientegrupo s on s.idcliente = cl.idcliente where s.idgrpcliente = " + clienteFilter.getGrupoCliente().getIdGrupoCliente() + ") ");
        }

        sb.append("order by c.razao");
        String sqlQuery = sb.toString();
        SQLQuery q = (SQLQuery) session.createSQLQuery(sqlQuery).setResultTransformer(Transformers.aliasToBean(Rotatividade.class));
        return q.list();
    }

    public List findRotatividade(ClientesFilter clienteFilter) {
        getLogger().info("findRotatividade ini");
        Cliente cliente = clienteFilter.getCliente();
        Session session = ManagerFactory.getSession();
        StringBuilder sb = new StringBuilder();
        sb.append("select tbcliente.razao, r.mes1, r.mes2, r.mes3, r.mes4, r.mes5, r.mes6, r.mes7, r.mes8, r.mes9, r.mes10, r.mes11, r.mes12 from tbcliente, (");
        sb.append(" select a.idcliente,");
        sb.append("  (select count(*) from tbpedido b where b.idcliente = a.idcliente and extract(month from b.dtpedido) = extract(month from cast('now' as timestamp)) and extract(year from b.dtpedido) = extract(year from cast('now' as timestamp))) as mes1,");
        sb.append("  (select count(*) from tbpedido b, dateadd(-1, cast('now' as timestamp))  where b.idcliente = a.idcliente and extract(month from b.dtpedido) = extract(month from newdate) and extract(year from b.dtpedido) = extract(year from newdate)) as mes2,");
        sb.append("  (select count(*) from tbpedido b, dateadd(-2, cast('now' as timestamp))  where b.idcliente = a.idcliente and extract(month from b.dtpedido) = extract(month from newdate) and extract(year from b.dtpedido) = extract(year from newdate)) as mes3,");
        sb.append("  (select count(*) from tbpedido b, dateadd(-3, cast('now' as timestamp))  where b.idcliente = a.idcliente and extract(month from b.dtpedido) = extract(month from newdate) and extract(year from b.dtpedido) = extract(year from newdate)) as mes4,");
        sb.append("  (select count(*) from tbpedido b, dateadd(-4, cast('now' as timestamp))  where b.idcliente = a.idcliente and extract(month from b.dtpedido) = extract(month from newdate) and extract(year from b.dtpedido) = extract(year from newdate)) as mes5,");
        sb.append("  (select count(*) from tbpedido b, dateadd(-5, cast('now' as timestamp))  where b.idcliente = a.idcliente and extract(month from b.dtpedido) = extract(month from newdate) and extract(year from b.dtpedido) = extract(year from newdate)) as mes6,");
        sb.append("  (select count(*) from tbpedido b, dateadd(-6, cast('now' as timestamp))  where b.idcliente = a.idcliente and extract(month from b.dtpedido) = extract(month from newdate) and extract(year from b.dtpedido) = extract(year from newdate)) as mes7,");
        sb.append("  (select count(*) from tbpedido b, dateadd(-7, cast('now' as timestamp))  where b.idcliente = a.idcliente and extract(month from b.dtpedido) = extract(month from newdate) and extract(year from b.dtpedido) = extract(year from newdate)) as mes8,");
        sb.append("  (select count(*) from tbpedido b, dateadd(-8, cast('now' as timestamp))  where b.idcliente = a.idcliente and extract(month from b.dtpedido) = extract(month from newdate) and extract(year from b.dtpedido) = extract(year from newdate)) as mes9,");
        sb.append("  (select count(*) from tbpedido b, dateadd(-9, cast('now' as timestamp))  where b.idcliente = a.idcliente and extract(month from b.dtpedido) = extract(month from newdate) and extract(year from b.dtpedido) = extract(year from newdate)) as mes10,");
        sb.append("  (select count(*) from tbpedido b, dateadd(-10, cast('now' as timestamp))  where b.idcliente = a.idcliente and extract(month from b.dtpedido) = extract(month from newdate) and extract(year from b.dtpedido) = extract(year from newdate)) as mes11,");
        sb.append("  (select count(*) from tbpedido b, dateadd(-11, cast('now' as timestamp))  where b.idcliente = a.idcliente and extract(month from b.dtpedido) = extract(month from newdate) and extract(year from b.dtpedido) = extract(year from newdate)) as mes12");
        sb.append(" from tbpedido a, tbcliente, tbsitcliente ");
        sb.append(" where a.idcliente = tbcliente.idcliente ");
        sb.append(" and tbcliente.idsitcliente = tbsitcliente.idsitcliente ");
        if (cliente.getIdCliente() != null) {
            sb.append(" and tbcliente.idCliente = " + cliente.getIdCliente());
        } else {
            if ((cliente.getRazao() != null) && (cliente.getRazao().length() > 0)) {
                sb.append(" and tbcliente.razao like '%" + cliente.getRazao() + "%'");
            }
            if ((cliente.getCidade() != null) && (cliente.getCidade().length() > 0)) {
                sb.append(" and tbcliente.cidade like '%" + cliente.getCidade() + "%'");
            }
            if ((cliente.getCnpj() != null) && (cliente.getCnpj().length() > 0)) {
                sb.append(" and tbcliente.cgc = '" + cliente.getCnpj() + "'");
            }

            if (cliente.getSituacaoCliente().getIdSitCliente() == null) {
                sb.append(" and tbsitcliente.pedido < 2");
            } else {
                sb.append(" and tbsitcliente.idsitcliente = " + cliente.getSituacaoCliente().getIdSitCliente());
            }
            if (clienteFilter.getVendedor().getIdVendedor() != null) {
                sb.append(" and tbcliente.idvendedor = " + clienteFilter.getVendedor().getIdVendedor());
            }
            if (clienteFilter.getRoteiro().getIdRoteiro() != null) {
                sb.append(" and tbcliente.idcliente in (select r.idcliente from tbroteirocliente r where tbcliente.idcliente = r.idcliente and r.idroteiro = " + clienteFilter.getRoteiro().getIdRoteiro() + ") ");
            }
            if (clienteFilter.getSegmento().getIdSegmento() != null) {
                sb.append(" and tbcliente.idcliente in (select r.idcliente from tbsegmentocliente r where tbcliente.idcliente = r.idcliente and r.idsegmento = " + clienteFilter.getSegmento().getIdSegmento() + ") ");
            }
            if (clienteFilter.getGrupoCliente().getIdGrupoCliente() != null) {
                sb.append(" and tbcliente.idCliente in (select cl.idCliente from tbcliente cl inner join tbClientegrupo s on s.idcliente = cl.idcliente where s.idgrpcliente = " + clienteFilter.getGrupoCliente().getIdGrupoCliente() + ") ");
            }
        }
        sb.append(" group by a.idcliente");
        sb.append(") as r");
        sb.append(" where tbcliente.idcliente = r.idcliente");
        sb.append(" order by tbcliente.razao");

        String sqlQuery = sb.toString();
        SQLQuery q = (SQLQuery) session.createSQLQuery(sqlQuery).setResultTransformer(Transformers.aliasToBean(Rotatividade.class));

        return q.list();

    }

    @Override
    public void deleteRow(Object object) throws DAOException {
        Cliente p = (Cliente) object;
        deleteRow(Cliente.class, p.getIdCliente());
    }

    public void removeVisitas(VisitaCliente cliente) {
        EntityManager em = getEntityManager();
        EntityTransaction t = em.getTransaction();
        Query q = em.createQuery("delete from VisitaCliente vc where vc.vendedor.idVendedor = :vendedor and vc.dtVisita = :dt");

        q.setParameter("vendedor", cliente.getVendedor().getIdVendedor());
        Date d = DateUtils.getSimpleDate(cliente.getDtVisita());
        q.setParameter("dt", d);

        try {
            t.begin();
            q.executeUpdate();
            t.commit();
        } catch (Exception e) {
            t.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void incluirVisita(VisitaCliente visita) throws DAOException {
        //removeVisitas(visita);
        VisitaCliente value;
        if (visita.getTipoVisita().equals("N")) {
            value = new VisitaCliente();
            value.setVendedor(visita.getVendedor());
            value.setDtVisita(visita.getDtVisita());
            value.setObs(visita.getObs());
            value.setTipoVisita(visita.getTipoVisita());
            insertRecord(value);
        } else {
            for (Cliente cliente : visita.getListaClientes()) {
                if (cliente != null) {
                    value = new VisitaCliente();
                    value.setVendedor(visita.getVendedor());
                    value.setCliente(cliente);
                    value.setDtVisita(visita.getDtVisita());
                    value.setObs(visita.getObs());
                    value.setTipoVisita(visita.getTipoVisita());
                    insertRecord(value);
                }
            }
        }
    }
    
    public List<SaldoClienteTo> getSaldoCliente(CobrancaFilter filter) {
        EntityManager em = ManagerFactory.getEntityManager();
        HashMap<String, Object> params = new HashMap<String, Object>();
        List<String> whereList = new ArrayList<String>();
        StringBuilder sb = new StringBuilder();
        
        sb.append("select c.razao, sum(pg.valor) ");
        sb.append("from tbpgtocliente pg ");
        sb.append("inner join tbatendimentopedido ap on ap.idpedido = pg.idpedido and ap.nf = pg.nf ");
        sb.append("inner join tbpedido p on p.idpedido = ap.idpedido ");
        sb.append("inner join tbcliente c on c.idcliente = p.idclienteres ");
        
        if (filter.getOperador() != null && filter.getOperador().length() > 0) {
            whereList.add("ap.operador = :operador");
            params.put("operador", filter.getOperador());
        }

        if ("P".equals(filter.getSituacao())) {
            whereList.add("pg.dtpgto is not null");
        } else if ("N".equals(filter.getSituacao())) {
            whereList.add("pg.dtpgto is null");
        } else if ("E".equals(filter.getSituacao())) {
            whereList.add("pg.dtpgto is null");
            whereList.add("pg.venc <= current_date");
        }
        if (filter.getDtEmissaoIni() != null) {
            whereList.add("p.dtpedido between :dtemissao1 and :dtemissao2");
            params.put("dtemissao1", filter.getDtEmissaoIni());
            params.put("dtemissao2", filter.getDtEmissaoEnd());
        }
        if (filter.getDtDescontoIni() != null) {
            whereList.add("ap.desconto between :dtdesconto1 and :dtdesconto2");
            params.put("dtdesconto1", filter.getDtDescontoIni());
            params.put("dtdesconto2", filter.getDtDescontoEnd());
        }
        if (filter.getDtVencimentoIni() != null && filter.getDtVencimentoEnd() != null) {
            whereList.add("pg.venc between :dtvenc1 and :dtvenc2");
            params.put("dtvenc1", filter.getDtVencimentoIni());
            params.put("dtvenc2", filter.getDtVencimentoEnd());
        } else if (filter.getDtVencimentoEnd() != null) {
            whereList.add("pg.venc between <= :dtvenc2");
            params.put("dtvenc2", filter.getDtVencimentoEnd());
        }
        if (filter.getDtPgtoIni() != null) {
            whereList.add("pg.dtpgto between :dtpgto1 and :dtpgto2");
            params.put("dtpgto1", filter.getDtPgtoIni());
            params.put("dtpgto2", filter.getDtPgtoEnd());
        }
        if (filter.getDtPrevPgtoIni() != null) {
            whereList.add("pg.dtprevpg between :dtprevpgto1 and :dtprevpgto2");
            params.put("dtprevpgto1", filter.getDtPrevPgtoIni());
            params.put("dtprevpgto2", filter.getDtPrevPgtoEnd());
        }
        if ((filter.getTipoPgto() != null) && (filter.getTipoPgto().length() > 0)) {
            StringBuilder tipos = new StringBuilder();
            String[] args = filter.getTipoPgto().split(",");

            for(int i = 0; i < args.length; i++) {
                if (i > 0) {
                    tipos.append(", ");
                }
               tipos.append("'").append(args[0]).append("'");
            }

            whereList.add("pg.tppgto in (" + tipos + ")");
        }
        if ((filter.getVendedor() != null) && (filter.getVendedor().getIdVendedor() != null)) {
            whereList.add("p.idvendedor = :vendedor");
            params.put("vendedor", filter.getVendedor().getIdVendedor());
        }
        boolean isCliente = ((filter.getCliente() != null) && (filter.getCliente().getIdCliente() != null));
        boolean isResponsavel = ((filter.getResponsavel() != null) && (filter.getResponsavel().getIdCliente() != null));

        if (!isCliente && isResponsavel) {
            whereList.add("p.idclienteres = :clienteres");
            params.put("clienteres", filter.getResponsavel().getIdCliente());
        } else if (isCliente  && !isResponsavel) {
            whereList.add("p.idcliente = :cliente");
            params.put("cliente", filter.getCliente().getIdCliente());
        } else if (isCliente && isResponsavel) {
            whereList.add("p.idclienteres = :clienteres");
            params.put("clienteres", filter.getResponsavel().getIdCliente());
            whereList.add("p.idcliente = :cliente");
            params.put("cliente", filter.getCliente().getIdCliente());
        }

        if ((filter.getRepres() != null) && (filter.getRepres().getIdRepres() != null)) {
            whereList.add("p.idrepres = :repres");
            params.put("repres", filter.getRepres().getIdRepres());
        }
        if (filter.getGrupo().getIdGrupoCliente() != null) {
            whereList.add("p.idclienteres in (select cg.idcliente from tbclientegrupo cg where cg.idgrpcliente = :grupo)");
            params.put("grupo", filter.getGrupo().getIdGrupoCliente());
        }  
        
        for(int i = 0; i < whereList.size(); i++) {
            if (i == 0) {
                sb.append(" where ");
            } else {
                sb.append(" and ");
            }
            sb.append(whereList.get(i));
        }
        
        sb.append(" group by c.razao order by 2 desc");
getLogger().info(sb.toString());
        
        Query q = em.createNativeQuery(sb.toString());
        
        Iterator iterator = params.keySet().iterator();
        while (iterator.hasNext()) {
            String s = (String) iterator.next();
            q.setParameter(s, params.get(s));
        }
        
        List<Object[]> lista = q.getResultList();
        List<SaldoClienteTo> result = new ArrayList<SaldoClienteTo>();
        SaldoClienteTo to;
        int i = 0;
        BigDecimal vlTotal = new BigDecimal(0);
        
        for (Object[] item : lista) {
            to = new SaldoClienteTo();
            to.setNome((String)item[0]);
            to.setValor((BigDecimal)item[1]);
            vlTotal = vlTotal.add(to.getValor());
            result.add(to);
            
            //if (++i >= 20) {
            //    break;
            //}
        }
        
        vlTotal.setScale(2, RoundingMode.CEILING);
        
        double value;
        
        for (SaldoClienteTo item : result) {
            value = item.getValor().doubleValue() * 100 / vlTotal.doubleValue();
            
            item.setPerc(new BigDecimal(value));
            item.getPerc().setScale(2, RoundingMode.CEILING);
        }
        
        return result;
    }
}

class VisitaClienteComparator implements Comparator {

    @Override
    public int compare(Object obj1, Object obj2) {
        VisitaCliente grupo1 = (VisitaCliente) obj1;
        VisitaCliente grupo2 = (VisitaCliente) obj2;

        String s1 = grupo1.getVendedor().getNome() + DateUtils.format(grupo1.getDtVisita(), "yyyy/MM/ddd");
        String s2 = grupo2.getVendedor().getNome() + DateUtils.format(grupo2.getDtVisita(), "yyyy/MM/ddd");

        return s1.compareTo(s2);

    }
}
