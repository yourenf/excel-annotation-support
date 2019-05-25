### Excel 注解支持

#### EXCEL生成

###### 1. 定义一个模型

``` java
import excel.write.annotation.CellStyle;
import excel.write.annotation.Column;
//修改默认header的背景色
@CellStyle(rgb = 0xAAFFCCdd)
public class ExportModel {
  //设置header文字，自动合并单元格
  @Column(value = {"这是一个Header", "这是一个Header", "a"}, order = 1)
  private String name;
  @Column(value = {"这是一个Header", "这是一个Header", "b"}, order = 2)
  private String name2;

  public ExportModel() {
  }

  public HM(String name) {
    this.name = name;
    this.name2 = name;
  }

  public String getName() {
    return name;
  }

  public String getName2() {
    return name2;
  }
}
```
###### 2. 实现
``` java
import excel.write.annotation.CellStyle;
import excel.write.annotation.Column;
import org.junit.Test;

import java.io.File;
import java.util.List;

public class ExcelWriterImplTest {

  @Test
  public void write() {
    SheetConfig config = SheetConfig.builder()
            //自定义导出文件名
            .fileName("aa")
            //每个sheet可写入多少行 受限于不同Excel版本允许写入最大行数
            .limit(500)
            //一个WorkBook创建几个Sheet
            .writeType(WriteType.SINGLE_SHEET)
            //创建excel 2007 还是excel 2003
            .excelType(ExcelType.XLSX)
            .build();
    ExcelWriter writer = ExcelWriter.create(config);
    //编辑Header
    writer.initHeader(fnSheet -> {
      //header 按添加顺序生成
      fnSheet.addHeader(ExportModel.class);
      fnSheet.addHeader(ExportModel.class);
      //将header写出到Excel
      fnSheet.writeHeader();
    });

    for (int i = 0; i < 1000; i++) {
      ExportModel h = new ExportModel(i + "a");
      ExportModel h2 = new ExportModel(i + "b");
      //写数据 详细使用方法查看方法文档
      int write = writer.write(h, h2);
    }
    //获取生成所有文件
    List<File> files = writer.getFiles();
    for (File file : files) {
      System.out.println(file);
    }
  }
}
```

