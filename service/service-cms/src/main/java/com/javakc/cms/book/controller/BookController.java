package com.javakc.cms.book.controller;

import com.javakc.cms.book.entity.Book;
import com.javakc.cms.book.service.BookService;
import com.javakc.cms.book.vo.BookQuery;
import com.javakc.commonutils.api.APICODE;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation(value = "新增书籍")
    @PostMapping("saveBook")
    public APICODE saveBook(@RequestBody Book book) {
        System.out.println(book);
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



}
