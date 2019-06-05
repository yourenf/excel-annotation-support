### Excel注解支持

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
  //这个配置将会覆盖类上的配置
  @CellStyle(rgb = 0xDDFFCCdd)
  @Column(value = {"这是一个Header", "这是一个Header", "b"}, order = 2)
  private String name2;

  public ExportModel() {
  }

  public ExportModel(String name) {
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
    ExcelConfig config = ExcelConfig.builder()
            //自定义导出文件名 
            //如果生成多个文件 文件名将会是 aa01、aa02 ...
            .fileName("aa")
            //自定义Sheet名  
            .sheetName("aa")
            //每个sheet可写入多少行 受限于不同Excel版本允许写入最大行数
            //默认2003:2<<15,2007 2<<19
            .limit(500)
            //一个WorkBook创建几个Sheet 
            // 默认一个workbook一个sheet
            .writeType(WriteType.SINGLE_SHEET)
            //设置2007或2003，默认2007
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
###### 3. 生成的文件目录
![目录](https://github.com/yourenf/excel-annotation-support/blob/master/image/aa_dir.png)

###### 4. 生成的Excel文件
![Excel文件](https://github.com/yourenf/excel-annotation-support/blob/master/image/aa03.png)

### Excel读取目前还未实现