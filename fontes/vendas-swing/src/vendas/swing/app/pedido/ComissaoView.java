/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas.swing.app.pedido;
//itextpdf
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.BorderLayout;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import vendas.beans.PedidoFilter;
import vendas.dao.PedidoDao;
import ritual.swing.TApplication;
import ritual.swing.ViewFrame;
import ritual.util.DateUtils;
import ritual.util.NumberUtils;
import vendas.beans.ResumoComissao;
import vendas.dao.EmpresaDao;
import vendas.entity.Params;
import vendas.swing.core.FilterFrame;
import vendas.util.Constants;
import vendas.util.Messages;
import vendas.util.Reports;

/**
 *
 * @author Sam
 */
public class ComissaoView extends FilterFrame {

    TApplication app = TApplication.getInstance();

    public ComissaoView() {
        super();
        setTitle(app.getResourceString("ctrlComissaoTitle"));
        ComissaoFilterPanel editPanel = new ComissaoFilterPanel();
        editPanel.init();
        setEditPanel(editPanel);
        PedidoFilter pedidoFilter = new PedidoFilter();
        //pedidoFilter.setSituacao("S");
        setFilterObject(pedidoFilter);
    }

    @Override
    public void execute() {
        PedidoFilter pedidoFilter = (PedidoFilter) getFilterObject();
        PedidoDao pedidoDao = (PedidoDao) app.lookupService("pedidoDao");
        try {
            if (pedidoFilter.getTipoRelatorio() == 0) {
                List lista;
                if (pedidoFilter.getFiltrarPgtos()) {
                    lista = pedidoDao.findComissaoPgto(pedidoFilter);
                    ComissaoPgtoDataSource ds = new ComissaoPgtoDataSource(lista);
                    ds.setSituacao(pedidoFilter.getSituacao());
                    Reports.showReport(app.getResourceString("ctrlComissoes"), pedidoFilter.getTitle(), getClass().getResource(Constants.JRCOMISSAOPGTO), ds);
                } else {
                    lista = pedidoDao.findComissao(pedidoFilter);
                    PedidoDataSource ds = new PedidoDataSource(lista);
                    ds.setSituacao(pedidoFilter.getSituacao());
                    Reports.showReport(app.getResourceString("ctrlComissoes"), pedidoFilter.getTitle(), getClass().getResource(Constants.JRCONTROLECOMISSAO), ds);
                }
            } else {
                List[] demonstrativo = pedidoDao.findComissaoResumo(pedidoFilter);
                //Reports.showReport(app.getResourceString("resumoComissoes"), pedidoFilter.getTitle(), getClass().getResource(Constants.JRRESUMOCOMISSAO), resumo);
                montaGrid(app.getResourceString("resumoComissoes"), pedidoFilter, demonstrativo[0], demonstrativo[1]);
            }
        } catch (Exception e) {
            getLogger().error(e.getMessage(), e);
            Messages.errorMessage(app.getResourceString("reportError"));
        }
    }

    private void montaGrid(String title, PedidoFilter filter, List<ResumoComissao> lista, List<String> vendedorList) {
        List<String> represList = new ArrayList<String>();
        String s;
        for (ResumoComissao resumo : lista) {
            if (filter.getQuebrarSegmento())
                s = resumo.getSegmento();
            else
                s = resumo.getRepresentada();
            if (!represList.contains(s)) {
                represList.add(s);
            }
        }
        int colcnt = vendedorList.size() + 7;
        int rowcnt = represList.size() + 2;
        Object[][] grid = new Object[rowcnt][colcnt];
        int cnt = 1;
        for (String item : represList) {
            grid[cnt++][1] = item;
        }
        cnt = 4;
        for (String item : vendedorList) {
            grid[0][cnt++] = item;
        }
        for (int i = 1; i < rowcnt; i++) {
            for (int j = 2; j < colcnt; j++) {
                grid[i][j] = new BigDecimal(0);
            }
        }
        grid[0][0] = "#";
        if (filter.getQuebrarSegmento())
            grid[0][1] = "SEGMENTO";
        else
            grid[0][1] = "FORNECEDOR";
        grid[0][2] = "VENDAS";
        grid[0][3] = "COMISSÃO";
        grid[0][colcnt - 3] = "EMPRESA";
        grid[0][colcnt - 2] = "TOTAL";
        grid[0][colcnt - 1] = "%";
        //grid[rowcnt - 1][0] = "TOTAIS (" + (rowcnt - 2) + ")";
        grid[rowcnt - 1][0] = "";
        grid[rowcnt - 1][1] = "TOTAIS";
        grid[rowcnt - 1][colcnt - 1] = new BigDecimal(100);
        int idxvendedor;
        int idxrepres;
        for (ResumoComissao resumo : lista) {
            idxvendedor = vendedorList.indexOf(resumo.getVendedor());
            if (filter.getQuebrarSegmento())
                s = resumo.getSegmento();
            else
                s = resumo.getRepresentada();
            idxrepres = represList.indexOf(s);
            // venda
            grid[idxrepres + 1][2] = ((BigDecimal) grid[idxrepres + 1][2]).add(resumo.getValorVenda());
            // comissao venda
            grid[idxrepres + 1][3] = ((BigDecimal) grid[idxrepres + 1][3]).add(resumo.getComissaoVenda());
            // vendedor
            grid[idxrepres + 1][idxvendedor + 4] = ((BigDecimal) grid[idxrepres + 1][idxvendedor + 4]).add(resumo.getComissaoVendedor());
            // empresa
            grid[idxrepres + 1][colcnt - 3] = ((BigDecimal) grid[idxrepres + 1][colcnt - 3]).add(resumo.getComissaoEmpresa());
            // total
            grid[idxrepres + 1][colcnt - 2] = ((BigDecimal) grid[idxrepres + 1][colcnt - 2]).add(resumo.getComissaoTotal());
        }

        for (int i = 1; i < rowcnt - 1; i++) {
            for (int j = 2; j < colcnt; j++) {
                grid[rowcnt - 1][j] = ((BigDecimal) grid[rowcnt - 1][j]).add((BigDecimal) grid[i][j]);
            }
        }
        BigDecimal vt = (BigDecimal) grid[rowcnt -1][colcnt - 2];
        BigDecimal v100 = new BigDecimal(100l);

        for (int i = 1; i < rowcnt - 1; i++) {
            BigDecimal tmp = (BigDecimal) grid[i][colcnt - 2];
            tmp = tmp.multiply(v100);
            tmp = tmp.divide(vt, 2, BigDecimal.ROUND_HALF_UP);
            grid[i][colcnt - 1] = tmp;
        }

        Arrays.sort(grid, new LastFirstComparator());

        for (int i = 1; i < rowcnt - 1; i++) {
            grid[i][0] = "" + i;
        }
        
        InputStream inputStream;

        try {
            Document document = new Document(PageSize.A4.rotate(), 20, 20, 20, 20);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = PdfWriter.getInstance(document, baos);
            writer.setViewerPreferences(PdfWriter.PageLayoutOneColumn | PdfWriter.FitWindow);
            document.open();
            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            
            EmpresaDao empresaDao = (EmpresaDao) TApplication.getInstance().lookupService("empresaDao");
            Params value = (Params) empresaDao.findById(Params.class, new Integer(-1));
            Image img = Image.getInstance(value.getArquivo());
            //img.setWidthPercentage(25);
            PdfPCell cell = new PdfPCell(img, false);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(title + "\r" + filter.getTitle()));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(DateUtils.format(new Date())));
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            document.add(table);

            //float[] widths = {0.8f, 0.1f, 0.1f};
            float[] cols = new float[colcnt];

            for (int i = 0; i < colcnt; i++) {
                if (i == 0)
                    cols[i] = 15;
                else if (i == 1) {
                    cols[i] = 106;
                } else if (i == colcnt - 1) {
                    cols[i] = 21;
                } else {
                    cols[i] = 38;
                }
            }
            table = new PdfPTable(cols); //colcnt
            table.setWidthPercentage(100);
            Font f = new Font(Font.FontFamily.TIMES_ROMAN);
            f.setSize(8);

            //table.addCell("1.1");
            for (int i = 0; i < rowcnt; i++) {
                for (int j = 0; j < colcnt; j++) {
                    if ((i > 0) && (j > 1)) {
                        BigDecimal number = (BigDecimal) grid[i][j];
                        String st;
                        if (number.doubleValue() == 0) {
                            st = "";
                        } else {
                            st = NumberUtils.format(number.doubleValue());
                        }
                        cell = new PdfPCell();
                        Paragraph p = new Paragraph(st, f);
                        p.setAlignment(Element.ALIGN_RIGHT);
                        cell.addElement(p);
                        cell.setPaddingRight(6.0f);
                        table.addCell(cell);
                    } else {
                        if (grid[i][j] == null)
                            grid[i][j] = "*";
                        s = (grid[i][j]).toString();
                        if ((j == 1) && (i == rowcnt - 1)) {
                            s = "TOTAIS (" + (rowcnt - 2) + ")";
                        }
                        cell = new PdfPCell(new Phrase(s,
                                FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.BOLD)));
                        if (i == 0) {
                            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        }

                        cell.setPaddingRight(6.0f);
                        table.addCell(cell);
                    }
                }
            }
            document.add(table);
            document.close();
            inputStream = new ByteArrayInputStream(baos.toByteArray());
            //Reports.showReportStream(inputStream, title);
            Reports.showReportStream(baos.toByteArray(), title);
        } catch (Exception e2) {
            e2.printStackTrace();
        }



        /*
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append("<body>");
        sb.append("<div align='center'><h1>" + title + "</h1>");
        sb.append("<h2>" + subTitle + "</h2></div>");
        sb.append("<table>");
        sb.append("<tr>");
        for (int i = 0; i < colcnt; i++) {
        sb.append("<th>");
        sb.append(grid[0][i]);
        sb.append("</th>");
        }
        sb.append("</tr>");
        for (int i = 1; i < rowcnt; i++) {
        sb.append("<tr>");
        for (int j = 0; j < colcnt; j++) {
        if ((i > 0) && (j > 0)) {
        sb.append("<td align='right'  WIDTH='9%'>");
        BigDecimal number = (BigDecimal) grid[i][j];
        String st = NumberUtils.format(number.doubleValue());
        sb.append(st);
        } else {
        if (i == rowcnt - 1) {
        sb.append("<th>");
        } else {
        sb.append("<td>");
        }
        sb.append(grid[i][j]);
        }
        if (i == rowcnt - 1) {
        sb.append("</th>");
        } else {
        sb.append("</td>");
        }
        }
        sb.append("</tr>");
        }
        sb.append("</table></body></html>");
        showGrid(title, subTitle, sb.toString());
         */
    }

    private void showGrid(String title, String subTitle, String toString) {
        //getLogger().info(toString);
        HtmlView hv = new HtmlView(title + " - " + subTitle, toString);
        TApplication.getInstance().getDesktopPane().add(hv);
        hv.setVisible(true);
    }
}

class HtmlView extends ViewFrame {

    private JEditorPane jep;

    public HtmlView(String title, String toString) {
        super(title, true, true, true, true);
        jep = new JEditorPane();
        jep.setEditable(false);
        jep.setContentType("text/html");
        HTMLEditorKit kit = new HTMLEditorKit();
        jep.setEditorKit(kit);
        StyleSheet styleSheet = kit.getStyleSheet();
        styleSheet.addRule("TR { font-size: 10pt; border-style: solid; border-width:1px; }");
        styleSheet.addRule("TD { font-size: 10pt; border-style: solid; border-width:1px; }");
        styleSheet.addRule("TH { font-size: 10pt; background-color:#cccccc; font-weight: bold; border-style: solid; border-width:1px; }");
        styleSheet.addRule("H1 { font-size: 14pt; font-weight: bold;}");
        styleSheet.addRule("H2 { font-size: 12pt; font-weight: bold;}");
        javax.swing.text.Document doc = kit.createDefaultDocument();
        jep.setDocument(doc);
        jep.setText(toString);
        JScrollPane scrollPane = new JScrollPane(jep);
        BorderLayout layout = new BorderLayout();
        getContentPane().setLayout(layout);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
    }

    @Override
    public void report() {
        try {
            jep.print();
        } catch (Exception e) {
            getLogger().error(e.getMessage(), e);
            Messages.errorMessage(TApplication.getInstance().getResourceString("reportError"));
        }
    }
}

class LastFirstComparator implements Comparator {

    private boolean isTotalizador(String s1) {
        if ("TOTAL".equals(s1)) {
            return true;
        }
        if ("TOTAIS".equals(s1)) {
            return true;
        }
        if ("%".equals(s1)) {
            return true;
        }
        return false;
    }
    
    @Override
    public int compare(Object obj1, Object obj2) {
        int result = 0;
        Object[] grid1 = (Object[]) obj1;
        Object[] grid2 = (Object[]) obj2;
        int len = grid1.length - 1;
        if (grid1[len - 1] instanceof String) {
            if (isTotalizador((String) grid1[len - 1])) {
                return 0;
            }
        }
        if (grid2[len - 1] instanceof String) {
            if (isTotalizador((String) grid2[len - 1])) {
                return 0;
            }
        }
        if (isTotalizador((String) grid1[1])) {
            return 0;
        }
        if (isTotalizador((String) grid2[1])) {
            return 0;
        }

        try {
            BigDecimal b1 = (BigDecimal) grid1[len - 1];
            BigDecimal b2 = (BigDecimal) grid2[len - 1];
            if ((result = b2.compareTo(b1)) == 0) {
                String s1 = (String) grid1[0];
                String s2 = (String) grid2[0];
                result = s1.compareTo(s2);
            }
        } catch (Exception e) {
            result = 0;
        }

        return result;
    }
}
