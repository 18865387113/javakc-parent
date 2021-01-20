package com.javakc.cms.book.service;


import com.javakc.cms.book.dao.BookDao;
import com.javakc.cms.book.entity.Book;
import com.javakc.cms.book.vo.BookQuery;
import com.javakc.commonutils.jpa.base.service.BaseService;
import com.javakc.commonutils.jpa.dynamic.SimpleSpecificationBuilder;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BookService extends BaseService<BookDao,Book> {

    @Autowired
    private BookDao bookDao;

    public List<Book> findAll() {
        return bookDao.findAll();
    }

    public Page<Book> pageBook(BookQuery bookQuery,Integer pageNo,Integer pageSize){
        /**
         * 可用操作符
         * = 等值、!= 不等值 (字符串、数字)
         * >=、<=、>、< (数字)
         * ge，le，gt，lt(字符串)
         * :表示like %v%
         * l:表示 v%
         * :l表示 %v
         * null表示 is null
         * !null表示 is not null
         */
        SimpleSpecificationBuilder simpleSpecificationBuilder = new SimpleSpecificationBuilder();
        if (!StringUtils.isEmpty(bookQuery.getBookName())) {
            simpleSpecificationBuilder.and("bookName", ":", bookQuery.getBookName().trim());
        }
        if (!StringUtils.isEmpty(bookQuery.getBeginDate())) {
            simpleSpecificationBuilder.and("grantStartTime", "ge", bookQuery.getBeginDate());
        }
        if (!StringUtils.isEmpty(bookQuery.getEndDate())) {
            simpleSpecificationBuilder.and("grantStartTime", "lt", bookQuery.getEndDate());
        }
        Specification<Book> specification = simpleSpecificationBuilder.getSpecification();

        return dao.findAll(specification, PageRequest.of(pageNo - 1, pageSize));
    }

    /**
     * 导出Excel
     * @param response
     */
    public void exportExcel(HttpServletResponse response) {
        try {
            // ## 设置表头值
            String[] titles = {"书名", "作者", "一级分类", "二级分类", "是否连载", "是否上线或未上线状态", "是否付费", "授权开始时间", "授权结束时间", "是否原创"};

            // ## 1.创建workbook,对应一个Excel文件
            HSSFWorkbook workbook = new HSSFWorkbook();
            // ## 2.在workbook中创建一个sheet
            HSSFSheet sheet = workbook.createSheet("第一个sheet");
            // ## 3.在sheet中添加表头,第0行
            HSSFRow row = sheet.createRow(0);
            // ## 4.在第0行设置表头
            for (int i = 0; i < titles.length; i++) {
                row.createCell(i).setCellValue(titles[i]);
            }
            // ## 5.查询并写入数据
            List<Book> bookList = this.findAll();
            if (null != bookList) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                for (int i = 0; i < bookList.size(); i++) {
                    Book book = bookList.get(i);
                    // ## 创建行
                    HSSFRow hssfRow = sheet.createRow(i + 1);
                    hssfRow.createCell(0).setCellValue(book.getBookName());
                    hssfRow.createCell(1).setCellValue(book.getAuthor());
                    if(book.getLevel1Id()!=null)hssfRow.createCell(2).setCellValue(book.getLevel1Id());
                    if(book.getLevel2Id()!=null)hssfRow.createCell(3).setCellValue(book.getLevel2Id());
                    if(book.getIsSerialize()!=null)hssfRow.createCell(4).setCellValue(book.getIsSerialize());
                    hssfRow.createCell(5).setCellValue(book.getIsOnline());
                    if(book.getIsCharge()!=null)hssfRow.createCell(6).setCellValue(book.getIsCharge());
                    hssfRow.createCell(7).setCellValue(sdf.format(book.getGrantStartTime()));
                    hssfRow.createCell(8).setCellValue(sdf.format(book.getGrantEndTime()));
                    if(book.getIsOriginal()!=null)hssfRow.createCell(9).setCellValue(book.getIsOriginal());
                }
            }
            String fileName = new String(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            // ## 6.将Excel文件输出到客户端浏览器
            response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xls");
            OutputStream os = response.getOutputStream();
            workbook.write(os);
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 导入Excel
     * @param file
     */
    @Transactional(readOnly = false)
    public void importExcel(MultipartFile file) {
        try {
            // ## 获取文件流
            InputStream inputStream = file.getInputStream();
            // ## 声明一个Excel接口
            Workbook workbook = null;
            // ## 根据文件后缀赋予不同POIExcel对象
            if (file.getOriginalFilename().endsWith(".xlsx")) {
                // ##处理2007版及以上Excel
                workbook = new XSSFWorkbook(inputStream);
            } else {
                // ##仅处理2003版Excel
                workbook = new HSSFWorkbook(inputStream);
            }
            // ## 得到当前excel中的sheet总数
            int numberOfSheets = workbook.getNumberOfSheets();
            // ## 循环所有sheet并得到其中内容
            for (int i = 0; i < numberOfSheets; i++) {
                // ## 每循环一次,通过当前下标依次获取sheet
                Sheet sheet = workbook.getSheetAt(i);
                // ## 通过sheet得到当前共多少行
                int rows = sheet.getPhysicalNumberOfRows();

                List<Book> list = new ArrayList<>();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                // ## 循环所有的行,j从1开始,因为0为第一行,第一行为表头
                for (int j = 1; j < rows; j++) {
                    // ## 每循环一次,通过当前下标一行一行的获取
                    Row row = sheet.getRow(j);
                    Book book = new Book();
                    book.setBookName(row.getCell(0).getStringCellValue());
                    book.setAuthor(row.getCell(1).getStringCellValue());
                    if(row.getCell(2)!=null)book.setLevel1Id((int)row.getCell(2).getNumericCellValue());
                    if(row.getCell(3)!=null)book.setLevel2Id((int)row.getCell(3).getNumericCellValue());
                    if(row.getCell(4)!=null)book.setIsSerialize((byte) row.getCell(4).getNumericCellValue());
                    book.setIsOnline((byte) row.getCell(5).getNumericCellValue());
                    if(row.getCell(6)!=null)book.setIsCharge((byte) row.getCell(6).getNumericCellValue());
                    book.setGrantStartTime(sdf.parse(row.getCell(7).getStringCellValue()));
                    book.setGrantEndTime(sdf.parse(row.getCell(8).getStringCellValue()));
                    if(row.getCell(9)!=null)book.setIsOriginal((byte) row.getCell(9).getNumericCellValue());
                    list.add(book);
                }
                // ## 批量添加
                dao.saveAll(list);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
