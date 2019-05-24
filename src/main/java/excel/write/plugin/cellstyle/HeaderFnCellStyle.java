package excel.write.plugin.cellstyle;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.IndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;

public class HeaderFnCellStyle extends BorderFnCellStyle {

  private IndexedColorMap colorMap = new DefaultIndexedColorMap();

  private final java.awt.Color color;

  public HeaderFnCellStyle() {
    color = new java.awt.Color(254, 230, 153);
  }

  public HeaderFnCellStyle(java.awt.Color color) {
    this.color = color;
  }

  @Override
  public CellStyle createCellStyle(Workbook workbook) {
    CellStyle style = super.createCellStyle(workbook);

    Font font = workbook.createFont();
    font.setFontName("宋体");
    font.setFontHeightInPoints(((short) 14));
    style.setFont(font);

    style.setAlignment(HorizontalAlignment.CENTER);
    style.setVerticalAlignment(VerticalAlignment.CENTER);
    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    style.setDataFormat((short) BuiltinFormats.getBuiltinFormat("text"));

    if (style instanceof HSSFCellStyle) {
      HSSFCellStyle cellStyle = (HSSFCellStyle) style;
      HSSFPalette palette = ((HSSFWorkbook) workbook).getCustomPalette();
      //这个是重点，具体的就是把之前的颜色 HSSFColor.LIGHT_YELLOW.index 替换成 当前 RGB
      palette.setColorAtIndex(IndexedColors.LIGHT_YELLOW.index,
              (byte) color.getRed(), (byte) color.getGreen(), (byte) color.getBlue());
      cellStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.index);
      cellStyle.setFillBackgroundColor(IndexedColors.LIGHT_YELLOW.index);
      return cellStyle;
    } else if (style instanceof XSSFCellStyle) {
      XSSFCellStyle cellStyle = (XSSFCellStyle) style;
      XSSFColor myColor = new XSSFColor(color, colorMap);
      cellStyle.setFillForegroundColor(myColor);
      cellStyle.setFillBackgroundColor(myColor);
      return cellStyle;
    } else {
      return style;
    }
  }
}