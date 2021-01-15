package com.javakc.cms.book.dao;

import com.javakc.cms.book.entity.Book;
import com.javakc.commonutils.jpa.base.dao.BaseDao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookDao extends BaseDao<Book, Integer> {

}
