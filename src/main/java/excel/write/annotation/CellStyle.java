package excel.write.annotation;

import excel.write.plugin.cellstyle.FnCellStyle;
import excel.write.plugin.cellstyle.HeaderFnCellStyle;

import java.lang.annotation.*;

/**
 * 作用于Header的CellStyle
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface CellStyle {

  Class<? extends FnCellStyle> value() default HeaderFnCellStyle.class;

  /**
   * @return rgb
   * @see java.awt.Color(0xFEE699)
   */
  int rgb() default 0xFFFFFF;

}
