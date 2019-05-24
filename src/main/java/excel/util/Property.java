package excel.util;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;

public class Property {
  private final Field field;
  private final Class<?> type;
  private final PropertyDescriptor propertyDescriptor;

  public Property(Field field, PropertyDescriptor propertyDescriptor) {
    this.field = Objects.requireNonNull(field);
    this.type = field.getType();
    this.propertyDescriptor = Objects.requireNonNull(propertyDescriptor);
  }

  public Class<?> getType() {
    return type;
  }

  public PropertyDescriptor getPropertyDescriptor() {
    return propertyDescriptor;
  }

  public String getName() {
    return field.getName();
  }

  public Field getField() {
    return field;
  }

  public <T extends Annotation> T getAnnotation(final Class<T> annotationCls) {
    return field.getAnnotation(annotationCls);
  }

  public Method getReadMethod() {
    return propertyDescriptor.getReadMethod();
  }

  public Method getWriteMethod() {
    return propertyDescriptor.getWriteMethod();
  }

  @Override
  public String toString() {
    return "Property{" +
            "field=" + field.getName() +
            ", type=" + type.toString() +
            '}';
  }
}
