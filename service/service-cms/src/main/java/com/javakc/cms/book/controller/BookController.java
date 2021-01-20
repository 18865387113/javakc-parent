package com.javakc.cms.book.controller;

import com.alibaba.excel.EasyExcel;
import com.javakc.cms.book.entity.Book;
import com.javakc.cms.book.listener.ExcelListener;
import com.javakc.cms.book.service.BookService;
import com.javakc.cms.book.vo.BookData;
import com.javakc.cms.book.vo.BookQuery;
import com.javakc.commonutils.api.APICODE;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Api(tags = "书籍管理")
@RestController
@RequestMapping("/cms/book")
@CrossOrigin
public class BookController {

    @Autowired
    private BookService bookService;

    @ApiOperation(value = "查询所有书籍数据")
    @GetMapping
    public APICODE findAll() {
        List<Book> list=bookService.findAll();
        return APICODE.OK().data("items",list);
    }

    @ApiOperation("根据条件进行分页查询 - 书籍管理")
    @PostMapping("{pageNo}/{pageSize}")
    public APICODE pageBook(@RequestBody(required = false) BookQuery bookQuery, @PathVariable Integer pageNo,@PathVariable Integer pageSize){
        Page<Book> page = bookService.pageBook(bookQuery, pageNo, pageSize);
        long totalElements = page.getTotalElements();
        List<Book> list = page.getContent();
        return APICODE.OK().data("total",totalElements).data("items",list);
    }

    @ApiOperation(value = "设置书籍上下架")
    @PutMapping("{id}/{isOnline}")
    public APICODE upOrDownBook(@PathVariable Integer id, @PathVariable Byte isOnline) {
        // ## 根据id查询书籍数据
        Book book = bookService.getById(id);
        book.setId(id);
        book.setIsOnline(isOnline);
        // ## 修改数据
        bookService.saveOrUpdate(book);
        return APICODE.OK();
    }

    @ApiOperation(value = "新增书籍")
    @PostMapping("saveBook")
    public APICODE saveBook(@RequestBody Book book) {
        bookService.saveOrUpdate(book);
        return APICODE.OK();
    }

    @ApiOperation(value = "根据ID获取书籍")
    @GetMapping("getBookById/{id}")
    public APICODE getBookById(@PathVariable Integer id) {
        Book book = bookService.getById(id);
        return APICODE.OK().data("book", book);
    }

    @ApiOperation(value = "修改书籍")
    @PostMapping("updateBook")
    public APICODE updateBook(@RequestBody Book book) {
        bookService.saveOrUpdate(book);
        return APICODE.OK();
    }

    @ApiOperation(value = "根据ID删除书籍")
    @DeleteMapping("deleteById/{id}")
    public APICODE deleteById(@PathVariable Integer id) {
        bookService.removeById(id);
        return APICODE.OK();
    }

    @ApiOperation(value = "列表导出", notes = "使用阿里EasyExcel导出Excel格式的用户列表数据")
    @GetMapping("exportEasyExcel")
    public void exportEasyExcel(HttpServletResponse response) {
        try {
            // ## 查询所有书籍
            List<Book> bookList = bookService.findAll();
            // ## 定义导出列表集合
            List<BookData> bookDataList = new ArrayList<>();

            for (Book book : bookList) {
                BookData bookData = new BookData();
                BeanUtils.copyProperties(book, bookData);
                bookDataList.add(bookData);
            }

            String fileName = "booklist";

            // ## 设置响应信息
            response.reset();
            response.setContentType("application/vnd.ms-excel; charset=utf-8");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8") + ".xlsx");
            EasyExcel.write(response.getOutputStream(), BookData.class).sheet("书籍列表").doWrite(bookDataList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ApiOperation(value="Excel导入",notes = "使用阿里 EasyExcel 技术实现的导入功能")
    @PostMapping
    public void importEasyExcel(MultipartFile file){
        try{
            EasyExcel.read(file.getInputStream(),BookData.class,new ExcelListener(bookService)).sheet().doRead();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
