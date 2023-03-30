/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas.swing.app.pedido;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;
import javax.mail.util.ByteArrayDataSource;
import javax.swing.event.InternalFrameEvent;
import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import ritual.util.DateUtils;
import ritual.util.JExtenso;
import vendas.dao.EmpresaDao;
import vendas.dao.PedidoDao;
import ritual.swing.TApplication;
import vendas.dao.VendedorDao;
import vendas.entity.AtendimentoPedido;
import vendas.entity.Params;
import vendas.entity.Vendedor;
import vendas.swing.app.ComboSelectPanel;
import vendas.swing.core.DesktopHelper;
import vendas.util.Messages;
import ritual.util.NumberUtils;
import vendas.util.Constants;
import vendas.util.ReportViewFrame;
import vendas.util.Reports;

/**
 *
 * @author Sam
 */
public class PedidosReports extends DesktopHelper {

    PedidoDao pedidoDao;
    TApplication app = TApplication.getInstance();

    public PedidosReports() {
        super();

        pedidoDao = (PedidoDao) app.lookupService("pedidoDao");
    }

    public void emitirRecibo(final List<AtendimentoPedido> lista, Vendedor vendedor, VendedorSelectPanel panel) {
        double valor = 0;
        for (AtendimentoPedido nota : lista) {
            valor += nota.getComissaoVendedor().doubleValue();
        }
        JExtenso extenso = new JExtenso(valor);
        URL url;
        url = getClass().getResource(Constants.JRRECIBOCOMISSAO);
        Map model = app.getDefaultMap(app.getResourceString("reciboComissaoTitle"));
        EmpresaDao empresaDao = (EmpresaDao) app.lookupService("empresaDao");
        Params params = null;
        try {
            params = (Params) empresaDao.findById(Params.class, new Integer(-1));
        } catch (Exception e) {
            getLogger().error(e);
        }
        model.put("valorPgto", NumberUtils.format(valor));
        model.put("dtPgto", DateUtils.format(DateUtils.getDate()));
        model.put("empresa", params.getRazao());
        model.put("valorExtenso", extenso.toString());
        try {
            ReportViewFrame repview = Reports.showReport(url, model, lista);
            repview.addInternalFrameListener(new javax.swing.event.InternalFrameListener() {

                @Override
                public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
                    onClose(evt);
                }

                @Override
                public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {

                }

                @Override
                public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
                }

                @Override
                public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
                }

                @Override
                public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
                }

                @Override
                public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
                }

                private void onClose(InternalFrameEvent evt) {
                    if (Messages.confirmQuestion("Recibo impresso corretamente?")) {
                        try {
                            JasperPrint jasperPrint = repview.getJasperPrint();
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            JRAbstractExporter exporter = new JRPdfExporter();
                            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, stream);
                            exporter.exportReport();
                            //source = new ByteArrayDataSource(stream.toByteArray(), "application/pdf");
                            pedidoDao.updateReciboEmitido(lista, vendedor, stream.toByteArray());
                            panel.refreshModel(vendedor);
                        } catch (Exception e) {
                            getLogger().error(app.getResourceString("reportError"), e);
                        }
                    }
                }

                @Override
                public void internalFrameOpened(InternalFrameEvent e) {
                }
            });
        } catch (Exception e) {
            getLogger().error(e.getMessage(), e);
            Messages.errorMessage(app.getResourceString("reportError"));
        }

    }
}
