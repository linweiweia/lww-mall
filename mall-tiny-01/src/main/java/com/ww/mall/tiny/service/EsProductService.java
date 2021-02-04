package com.ww.mall.tiny.service;

import com.ww.mall.tiny.nosql.es.EsProduct;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author linweiwei
 * @version 1.0
 * @date 2021-01-25 10:19
 * @describe:
 */


public interface EsProductService {

    /**
     * 从数据库中导入所有商品到ES
     */
    boolean importAll();

    /**
     * 根据id删除商品
     */
    void delete(String id);

    /**
     * 根据id创建商品
     */
    boolean create(String index, String id, Object data);

    /**
     * 批量删除商品
     */
    void delete(List<String> ids);

    /**
     * 根据关键字搜索名称或者副标题
     */
    Page<EsProduct> search(String keyword, Integer pageNum, Integer pageSize);

}
