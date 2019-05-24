package excel.write.plugin.cellstyle;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

@FunctionalInterface
public interface FnCellStyle {

  CellStyle createCellStyle(Workbook workbook);

}