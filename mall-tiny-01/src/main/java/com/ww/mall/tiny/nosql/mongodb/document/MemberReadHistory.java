package com.ww.mall.tiny.nosql.mongodb.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @author linweiwei
 * @version 1.0
 * @date 2021-01-26 18:06
 * @describe:   用户游览上商品记录存在mongodb中 mysql没有对应的表
 */

@Document   //文档（一行记录）
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberReadHistory {

    @Id //主键
    private String id;
    @Indexed   //索引
    private Long memberId;
    private String memberNickname;
    private String memberIcon;
    @Indexed   //索引
    private Long productId;
    private String productName;
    private String productPic;
    private String productSubTitle;
    private String productPrice;
    private Date createTime;

}
