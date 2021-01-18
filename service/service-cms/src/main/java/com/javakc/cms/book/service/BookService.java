package com.javakc.cms.book.service;


import com.javakc.cms.book.dao.BookDao;
import com.javakc.cms.book.entity.Book;
import com.javakc.cms.book.vo.BookQuery;
import com.javakc.commonutils.jpa.base.service.BaseService;
import com.javakc.commonutils.jpa.dynamic.SimpleSpecificationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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

}
