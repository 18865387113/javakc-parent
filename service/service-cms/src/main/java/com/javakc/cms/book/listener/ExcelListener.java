package com.javakc.cms.book.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.javakc.cms.book.entity.Book;
import com.javakc.cms.book.service.BookService;
import com.javakc.cms.book.vo.BookData;
import org.springframework.beans.BeanUtils;

public class ExcelListener extends AnalysisEventListener<BookData> {

    private BookService bookService;

    public ExcelListener() {

    }

    public ExcelListener(BookService bookService) {
        this.bookService = bookService;
    }

    @Override
    public void invoke(BookData data, AnalysisContext context) {
        // ## 创建Book实体
        Book book = new Book();
        BeanUtils.copyProperties(data, book);
        // ## 保存
        bookService.saveOrUpdate(book);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

    }
}
