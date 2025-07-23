import cn.hutool.json.JSONUtil;
import com.itsoku.lesson007.dto.UserExportRequest;
import com.itsoku.lesson007.excel.ExcelExportField;
import com.itsoku.lesson007.excel.ExcelExportResponse;
import com.itsoku.lesson007.excel.ExcelExportUtils;
import com.itsoku.lesson007.service.UserService;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/1 15:20 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public class UserServiceTest {
    public static void main(String[] args) throws Exception {
        UserService userService = new UserService();
        UserExportRequest request = new UserExportRequest();
        request.setExcelName("用户列表");
        request.setSheetName("用户列表");
        List<ExcelExportField> fieldList = new ArrayList<>();
        fieldList.add(new ExcelExportField("userName", "用户名"));
        fieldList.add(new ExcelExportField("age", "年龄"));
        fieldList.add(new ExcelExportField("address", "地址"));
        request.setFieldList(fieldList);
        ExcelExportResponse excelExportResponse = userService.userExport(request);
        System.out.println(JSONUtil.toJsonPrettyStr(excelExportResponse));
        writeExcelToFile(excelExportResponse);
    }

    public static void writeExcelToFile(ExcelExportResponse result) throws Exception {
        String fileName = "E:\\tmp\\" + System.currentTimeMillis() + ".xlsx";
        File file = new File(fileName);
        if (!file.exists()) {
            file.createNewFile();
        }
        try (FileOutputStream fileOutputStream = new FileOutputStream(file);) {
            ExcelExportUtils.write(result, fileOutputStream);
        }
    }

}
