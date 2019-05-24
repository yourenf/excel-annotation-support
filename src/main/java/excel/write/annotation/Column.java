package excel.write.annotation;

import excel.write.plugin.typehandler.ToStringTypeHandler;
import excel.write.plugin.typehandler.TypeHandler;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Column {
  /**
   * 如果长度不齐，自动补齐
   *
   * @return
   */
  String[] value() default {""};

  /**
   * @return 排序字段
   */
  int order() default 0;

  /**
   * <=0 auto width
   * >0 set this width
   *
   * @return
   */
  int width() default -1;

  /**
   * 写出到excel
   *
   * @return
   */
  Class<? extends TypeHandler<?>> typeHandler() default ToStringTypeHandler.class;
}
