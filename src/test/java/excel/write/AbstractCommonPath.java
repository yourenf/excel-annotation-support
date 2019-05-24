package excel.write;

import org.junit.Before;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

public abstract class AbstractCommonPath {
  private String basePath;

  @Before
  public void initDate() throws IOException, URISyntaxException {
    ClassLoader classLoader = getClass().getClassLoader();
    URI fn = classLoader.getResource("fn").toURI();
    Path resource = Paths.get(fn).getParent();
    System.out.println(resource);
    String dir = Paths.get(resource.toString(), "test-file").toString();
    if (System.getProperty("os.name").startsWith("Windows")) {
      dir = dir.substring(1);
    }
    Random random = new Random();
    Path path = Paths.get(dir, random.nextInt(1000000) + "");
    Files.createDirectories(path);
    basePath = path.toString();
  }

  public String getBasePath() {
    return basePath;
  }

}
