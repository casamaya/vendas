/*
 * Reports.java
 * 
 * Created on 24/06/2007, 13:03:01
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package vendas.util;

import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterJob;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JRViewer;
import org.icepdf.core.views.DocumentViewController;
import org.icepdf.ri.common.PrintHelper;
import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.SwingViewBuilder;
import org.icepdf.ri.common.views.DocumentViewControllerImpl;
import ritual.swing.MDIDesktopPane;
import ritual.swing.TApplication;

/**
 *
 * @author Sam
 */
public class Reports {

    public Reports() {
    }

    public static ReportViewFrame showReport(URL reportFile, Map model, List lista) throws Exception {
        JasperPrint jasperPrint = JasperFillManager.fillReport(reportFile.openStream(), model, new JRBeanCollectionDataSource(lista));
        return showReport(jasperPrint, (String) model.get("ReportTitle"));
    }

    public static ReportViewFrame showReport(URL reportFile, Map model, JRDataSource ds) throws Exception {
        JasperPrint jasperPrint = JasperFillManager.fillReport(reportFile.openStream(), model, ds);
        return showReport(jasperPrint, (String) model.get("ReportTitle"));
    }

    public static ReportViewFrame showReport(InputStream stream, String reportTitle) throws Exception {
        MDIDesktopPane panel = TApplication.getInstance().getDesktopPane();
        ReportViewFrame jif = new ReportViewFrame(reportTitle);
        jif.setSize(500, 300);

        MySwingController controller = new MySwingController();
        controller.setPageViewMode(DocumentViewControllerImpl.ONE_PAGE_VIEW, true);
        controller.setPageFitMode(DocumentViewControllerImpl.PAGE_FIT_ACTUAL_SIZE, true);
        controller.openDocument(stream, reportTitle, null);
        DocumentViewControllerImpl vc = (DocumentViewControllerImpl) controller.getDocumentViewController();
        vc.setDocument(controller.getDocument());
        vc.setFitMode(DocumentViewControllerImpl.PAGE_FIT_ACTUAL_SIZE);
        vc.setZoom(100.0f);
        SwingViewBuilder factory = new SwingViewBuilder(controller);

        jif.setContentPane(factory.buildViewerPanel());
        panel.add(jif);
        jif.setLocation(0, 0);
        jif.setVisible(true);
        jif.pack();
        return jif;
    }
    public static ReportViewFrame showReportStream(byte[] stream, String reportTitle) throws Exception {
        MDIDesktopPane panel = TApplication.getInstance().getDesktopPane();
        ReportViewFrame jif = new ReportViewFrame(reportTitle);
        jif.setBytes(stream);
        jif.setSize(500, 300);

        MySwingController controller = new MySwingController();
        controller.setPageViewMode(DocumentViewControllerImpl.ONE_PAGE_VIEW, true);
        controller.setPageFitMode(DocumentViewControllerImpl.PAGE_FIT_ACTUAL_SIZE, true);
        controller.openDocument(new ByteArrayInputStream(stream), reportTitle, null);
        DocumentViewControllerImpl vc = (DocumentViewControllerImpl) controller.getDocumentViewController();
        vc.setDocument(controller.getDocument());
        vc.setFitMode(DocumentViewControllerImpl.PAGE_FIT_ACTUAL_SIZE);
        vc.setZoom(100.0f);
        SwingViewBuilder factory = new SwingViewBuilder(controller);

        jif.setContentPane(factory.buildViewerPanel());
        panel.add(jif);
        jif.setVisible(true);
        jif.pack();
        return jif;
    }
    public static ReportViewFrame showReportStream(InputStream stream, String reportTitle) throws Exception {
        MDIDesktopPane panel = TApplication.getInstance().getDesktopPane();
        ReportViewFrame jif = new ReportViewFrame(reportTitle);
        jif.setCustomStream(stream);
        jif.setSize(500, 300);

        MySwingController controller = new MySwingController();
        controller.setPageViewMode(DocumentViewControllerImpl.ONE_PAGE_VIEW, true);
        controller.setPageFitMode(DocumentViewControllerImpl.PAGE_FIT_ACTUAL_SIZE, true);
        controller.openDocument(stream, reportTitle, null);
        
        SwingViewBuilder factory = new SwingViewBuilder(controller);
        
        controller.getDocumentViewController().setAnnotationCallback(new org.icepdf.ri.common.MyAnnotationCallback(
            controller.getDocumentViewController()));
        
        DocumentViewControllerImpl vc = (DocumentViewControllerImpl) controller.getDocumentViewController();
        vc.setDocument(controller.getDocument());
        vc.setFitMode(DocumentViewControllerImpl.PAGE_FIT_ACTUAL_SIZE);
        vc.setZoom(100.0f);
        
        JPanel viewerComponentPanel = factory.buildViewerPanel();
        
        jif.setContentPane(viewerComponentPanel);
        
        panel.add(jif);
        jif.setVisible(true);
        jif.pack();
        return jif;
    }

    static class MySwingController extends SwingController {

        public MySwingController() {
            super();
        }

        @Override
        public void print(boolean arg0) {
            System.out.println("print");
            PrinterJob pJob = PrinterJob.getPrinterJob();
            //pJob.setPrintService();
            pJob.setJobName("Certificate");
            pJob.setCopies(1);
            DocumentViewController vc = new DocumentViewControllerImpl(this);

            vc.setDocument(getDocument());
            PrintHelper printHelper = new PrintHelper(vc, getDocument().getPageTree());
            printHelper.setupPrintService(0, 10, 1, true, false);
            PageFormat pf = PrinterJob.getPrinterJob().defaultPage();
            Book book = new Book();
            book.append(printHelper, pf, getDocument().getNumberOfPages());
            pJob.setPageable(book);

            // Remove margins
            Paper paper = new Paper();
            paper.setImageableArea(0, 0, paper.getWidth(), paper.getHeight());
            // Set A4 size
            paper.setSize(601.92, 852.48);
            // Set the margins.
            paper.setImageableArea(0, 0, 601.92, 852.48);
            pf.setPaper(paper);
            try {
                if (getDocument().getNumberOfPages() > 0) {
                    printHelper.setupPrintService(0, getDocument().getNumberOfPages(), 1, true, false);
                    pJob.print();
                }

                //printHelper.print();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static ReportViewFrame showReport(JasperPrint jasperPrint, String reportTitle) throws Exception {

        JRViewer jr = new JRViewer(jasperPrint);
        MDIDesktopPane panel = TApplication.getInstance().getDesktopPane();
        ReportViewFrame jif = new ReportViewFrame(reportTitle, jasperPrint);
        jif.setContentPane(jr);
        panel.add(jif);
        //jif.setLocation(0, 0);
        jif.setSelected(true);
        jif.setVisible(true);
        jif.setMaximizable(true); 
        jif.setMaximum(true); 
        return jif;
    }

    public static void showReport(String title, String subTitle, URL reportFile, JRDataSource ds) throws Exception {
        Map model = TApplication.getInstance().getDefaultMap(title, subTitle);
        showReport(reportFile, model, ds);
    }

    public static void showReport(String title, String subTitle, URL reportFile, List lista) throws Exception {
        Map model = TApplication.getInstance().getDefaultMap(title, subTitle);
        showReport(reportFile, model, lista);
    }
}
