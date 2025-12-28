package com.easyjob.easyjobapi.utils.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;

@Slf4j
public abstract class AbstractCVGenerationStrategy implements CVGenerationStrategy {
    protected static final BaseColor DARK_BLUE = new BaseColor(44, 62, 80);
    protected static final BaseColor MEDIUM_BLUE = new BaseColor(52, 152, 219);
    protected static final BaseColor LIGHT_BLUE = new BaseColor(174, 214, 241);
    protected static final BaseColor DARK_GRAY = new BaseColor(52, 73, 94);
    protected static final BaseColor MEDIUM_GRAY = new BaseColor(127, 140, 141);
    protected static final BaseColor LIGHT_GRAY = new BaseColor(236, 240, 241);
    protected static final BaseColor BLACK = BaseColor.BLACK;
    protected static final BaseColor WHITE = BaseColor.WHITE;
    protected static final BaseColor ORANGE = new BaseColor(230, 126, 34);
    protected static final BaseColor GREEN = new BaseColor(46, 204, 113);
    protected static final BaseColor PURPLE = new BaseColor(155, 89, 182);

    protected Document createDocument() {
        return new Document(PageSize.A4, 72, 72, 72, 72); // 1 inch margins
    }

    protected Document createDocument(float marginLeft, float marginRight,
                                      float marginTop, float marginBottom) {
        return new Document(PageSize.A4, marginLeft, marginRight, marginTop, marginBottom);
    }

    protected byte[] documentToBytes(Document document, ByteArrayOutputStream baos) {
        document.close();
        byte[] pdfBytes = baos.toByteArray();
        log.info("PDF generated successfully. Size: {} bytes", pdfBytes.length);
        return pdfBytes;
    }

    protected LineSeparator createSectionLine(BaseColor color, float lineWidth) {
        return new LineSeparator(lineWidth, 100, color, Element.ALIGN_LEFT, -2);
    }

    protected void addSpacing(Document document, float spacing) throws DocumentException {
        Paragraph spacer = new Paragraph(" ");
        spacer.setSpacingBefore(spacing);
        document.add(spacer);
    }

    protected com.itextpdf.text.List createBulletList(String symbol, float indentation) {
        com.itextpdf.text.List list = new com.itextpdf.text.List(com.itextpdf.text.List.UNORDERED);
        list.setListSymbol(symbol);
        list.setIndentationLeft(indentation);
        return list;
    }

    protected PdfPTable createBorderlessTable(int columns) {
        PdfPTable table = new PdfPTable(columns);
        table.setWidthPercentage(100);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        return table;
    }

    protected PdfPCell createBorderlessCell() {
        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }

    protected PdfPCell createColoredCell(BaseColor backgroundColor) {
        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setBackgroundColor(backgroundColor);
        cell.setPadding(10);
        return cell;
    }

    protected String safeString(String value, String defaultValue) {
        return value != null && !value.isEmpty() ? value : defaultValue;
    }

    protected String safeString(String value) {
        return safeString(value, "");
    }
}