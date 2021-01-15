package com.javakc.cms.book.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name="cms_content_book")
@EntityListeners(AuditingEntityListener.class)
public class Book {
    /** 主键 */
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "书籍主键，自动生成id")
    private Integer id ;
    /** 书名 */
    @Column(name="book_name")
    @ApiModelProperty(value = "书籍名")
    private String bookName ;
    /** 作者 */
    @Column(name="author")
    @ApiModelProperty(value = "作者")
    private String author ;
    /** 二级分类 */
    @Column(name="level2_id")
    @ApiModelProperty(value = "二级分类")
    private String level2Id ;
    /** 版本 */
    @Column(name="copyright_id")
    @ApiModelProperty(value = "版权ID")
    private Integer copyrightId ;
    /** 是否连载 */
    @Column(name="is_serialize")
    @ApiModelProperty(value = "是否连载")
    private Byte isSerialize ;
    /** 是否原创 */
    @Column(name="is_original")
    @ApiModelProperty(value = "是否原创")
    private Byte isOriginal ;
    /** 授权开始时间 */
    @Column(name="grant_start_time")
    @ApiModelProperty(value = "授权开始时间")
    private Date grantStartTime ;
    /** 授权结束时间 */
    @Column(name="grant_end_time")
    @ApiModelProperty(value = "授权结束时间")
    private Date grantEndTime ;
    /** 简介 */
    @Column(name="introduction")
    @ApiModelProperty(value = "简介")
    private String introduction ;
    /** 书封 */
    @Column(name="book_cover")
    @ApiModelProperty(value = "书封")
    private String bookCover ;
    /** 字数 */
    @Column(name="word_number")
    @ApiModelProperty(value = "字数")
    private Integer wordNumber ;
    /** 状态 */
    @Column(name="is_online")
    @ApiModelProperty(value = "是否上线")
    private Byte isOnline ;
    /** 全本收费 */
    @Column(name="is_charge")
    @ApiModelProperty(value = "是否收费")
    private Byte isCharge ;
    /** 乐观锁 */
    @Column(name="revision")
    @ApiModelProperty(value = "乐观锁")
    private Integer revision ;
    /** 创建人 */
    @Column(name="created_by")
    @ApiModelProperty(value = "创建人", example = "root")
    private String createdBy ;
    /** 创建时间 */
    @CreatedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss", timezone = "GMT+8")
    @Column(name="gmt_created",nullable = false,updatable = false)
    @ApiModelProperty(value = "创建时间", example = "2020-12-12 9:00:00")
    private Date gmtCreated ;
    /** 更新人 */
    @Column(name="updated_by")
    @ApiModelProperty(value = "更新人", example = "root")
    private String updatedBy ;
    /** 更新时间 */
    @LastModifiedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss", timezone = "GMT+8")
    @ApiModelProperty(value = "更新时间", example = "2020-12-12 9:00:00")
    @Column(name="gmt_modified",nullable = false,insertable = false)
    private Date gmtModified ;
}
