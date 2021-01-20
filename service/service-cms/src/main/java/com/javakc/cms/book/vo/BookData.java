package com.javakc.cms.book.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class BookData {

    @ExcelProperty(value = "书名", index = 0)
    private String bookName;

    @ExcelProperty(value = "作者", index = 1)
    private String author;

    @ExcelProperty(value = "一级分类", index = 2)
    private Integer level1Id;

    @ExcelProperty(value = "二级分类", index = 3)
    private Integer level2Id;

    @ExcelProperty(value = "是否连载", index = 4)
    private Byte isSerialize;

    @ExcelProperty(value = "是否上线或未上线状态", index = 5)
    private byte isOnline;

    @ExcelProperty(value = "是否付费", index = 6)
    private Byte isCharge;

    @ExcelProperty(value = "授权开始时间", index = 7)
    private Date grantStartTime;

    @ExcelProperty(value = "授权结束时间", index = 8)
    private Date grantEndTime;

    @ExcelProperty(value = "是否原创", index = 9)
    private Byte isOriginal;

}
