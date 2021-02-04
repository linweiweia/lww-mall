package com.ww.mall.tiny.dao;

import com.ww.mall.tiny.nosql.es.EsProduct;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author linweiwei
 * @version 1.0
 * @date 2021-01-25 10:29
 * @describe:   产品Dao
 */

public interface EsProductDao {
    /**
     * 获取所有产品信息
     */
    List<EsProduct> getAllProductList(@Param("id") Long id);
}
