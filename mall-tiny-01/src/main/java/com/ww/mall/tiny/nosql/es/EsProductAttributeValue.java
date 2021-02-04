package com.ww.mall.tiny.nosql.es;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author linweiwei
 * @version 1.0
 * @date 2021-01-25 11:19
 * @describe:   产品与产品属性中间表 做\\
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EsProductAttributeValue {

    private Long id;
    private Long productAttributeId;
    //属性值
    private String value;
    //属性参数：0->规格；1->参数   属性表中才有
    private Integer type;
    //属性名称                   属性表中才有
    private String name;

}
