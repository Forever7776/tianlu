package common.service;

import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.render.RenderException;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by liuyj on 2015/4/20.
 */
public class MysqlService {
    private Log logger = Log.getLog(this.getClass());
    public void membersExport(String templatePath,String schema, List listName, HttpServletResponse response) {
        HSSFWorkbook workbook = null;
        String outFileName = "";
        try {
            workbook = new HSSFWorkbook(new FileInputStream(new File(templatePath + "/mysql.xls")));
            workbook = createWorkbookByMembers(workbook,schema,listName);
            outFileName = schema+"_" + new Date().getTime() + ".xls";
            //获取response对象
            response.reset();
            response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(outFileName, "UTF-8"));
            response.setContentType("application/msexcel;charset=UTF-8");
            OutputStream os = null;
            try {
                os = response.getOutputStream();
                workbook.write(os);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RenderException(e);
            } finally {
                try {
                    if (os != null) {
                        os.flush();
                        os.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            logger.error("memberTempateExport 导出出错", e);
        }

    }

    public HSSFWorkbook createWorkbookByMembers(HSSFWorkbook wb, String schema,List dataList) {
        //Create a blank sheet
        HSSFSheet sheet = wb.getSheetAt(0);
        HSSFRow nRow = null;
        HSSFCell nCell = null;

        // 创建单元格样式对象
        HSSFCellStyle cellStyle = null;
        CellRangeAddress cellRangeAddress = null;

        //需要填写内容的第 0 行
        //创建行


        HSSFFont font = wb.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 100);// 设置字体大小



        for(int i=0;i<dataList.size();i++){
            int rowNo = 1;//行号


            cellStyle = (HSSFCellStyle) wb.createCellStyle();
            cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
            cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
            cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
            cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
            cellStyle.setWrapText(true);

            //获取table详情
            List<Record> table=  Db.find("select column_name,column_type,column_comment from INFORMATION_SCHEMA.Columns where table_name=? and table_schema=? ",dataList.get(i),schema);
            System.out.println(dataList.get(i));
            if(i==0){

            }else{
                sheet=wb.createSheet();
            }

            wb.setSheetName(i,i+1+"表"+dataList.get(i));
            //横行竖列
            CellRangeAddress cra=new CellRangeAddress(0, 0, 0, 3);
            //在sheet里增加合并单元格
            sheet.addMergedRegion(cra);
            nRow = sheet.createRow(0);
            nCell = nRow.createCell(0);
            nCell.setCellValue(dataList.get(i)+"数据表详细");
            int serial = 1;
            for(int j=0;j<table.size();j++){
                int colNo = 0;//列号
                //创建行
                nRow = sheet.createRow(rowNo++);
                //生成序号
                nCell = nRow.createCell(colNo++);
                nCell.setCellValue(serial++);
                nCell.setCellStyle(cellStyle);

                //字段名称
                nCell = nRow.createCell(colNo++);
                nCell.setCellValue(table.get(j).getColumns().get("column_name")+"");
                nCell.setCellStyle(cellStyle);

                //类型
                nCell = nRow.createCell(colNo++);
                nCell.setCellValue(table.get(j).getColumns().get("column_type")+"");
                nCell.setCellStyle(cellStyle);

                //注释
                nCell = nRow.createCell(colNo++);
                nCell.setCellValue(table.get(j).getColumns().get("column_comment")+"");
                nCell.setCellStyle(cellStyle);
            }
        }

        return wb;
    }


}
