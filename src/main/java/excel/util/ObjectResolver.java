package excel.util;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public enum ObjectResolver {
  INSTANCE;

  private final ConcurrentMap<Class<?>, List<Property>> types = new ConcurrentHashMap<>();

  public List<Property> getProperties(final Class<?> type) {
    Objects.requireNonNull(type);
    if (types.containsKey(type)) {
      return types.get(type);
    } else {
      return types.computeIfAbsent(type, k -> {
        List<Property> list = resolve(k);
        return Collections.unmodifiableList(list);
      });
    }
  }

  private List<Property> resolve(final Class<?> clazz) {
    try {
      Field[] fields = clazz.getDeclaredFields();
      List<Property> properties = new ArrayList<>(fields.length);
      PropertyDescriptor[] propertyDescriptors = Introspector.getBeanInfo(clazz)
              .getPropertyDescriptors();
      for (Field field : fields) {
        for (PropertyDescriptor descriptor : propertyDescriptors) {
          if (field.getName().equalsIgnoreCase(descriptor.getName())) {
            Property property = new Property(field, descriptor);
            properties.add(property);
          }
        }
      }
      return properties;
    } catch (IntrospectionException e) {
      throw new IllegalArgumentException(e);
    }
  }


}
