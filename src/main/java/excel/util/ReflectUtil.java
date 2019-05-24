package excel.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

public class ReflectUtil {
  private ReflectUtil() {
  }

  public static Object getValue(Method getter, Object arg) {
    try {
      return getter.invoke(arg);
    } catch (RuntimeException | IllegalAccessException | InvocationTargetException e) {
      return null;
    }
  }

  public static <T> T create(Class<T> type, Object... args) {
    try {
      Constructor<T> constructor;
      if (args == null) {
        constructor = type.getConstructor();
      } else {
        Class<?>[] parameterTypes = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
          Object arg = args[i];
          Objects.requireNonNull(arg);
          parameterTypes[i] = arg.getClass();
        }
        constructor = type.getConstructor(parameterTypes);
      }
      return constructor.newInstance(args);
    } catch (NoSuchMethodException e) {
      throw new IllegalArgumentException(e);
    } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
      return null;
    }
  }
}
