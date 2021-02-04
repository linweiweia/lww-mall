package com.ww.mall.tiny.service.impl;

import com.ww.mall.tiny.comom.utlis.ElasticsearchUtils;
import com.ww.mall.tiny.dao.EsProductDao;
import com.ww.mall.tiny.nosql.es.EsProduct;
import com.ww.mall.tiny.service.EsProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * @author linweiwei
 * @version 1.0
 * @date 2021-01-25 14:27
 * @describe:  商品搜索service
 */


@Service
public class EsProductServiceImpl implements EsProductService {

    @Autowired
    private ElasticsearchUtils elasticsearchUtils;
    @Autowired
    private EsProductDao esProductDao;


    //将数据库数据导入es中
    @Override
    public boolean importAll() {
        List<EsProduct> productList = esProductDao.getAllProductList(null);
        return elasticsearchUtils.bulkCreateDocument("pms_product", productList);
    }

    @Override
    public void delete(String id) {

    }

    @Override
    public boolean create(String index, String id, Object data) {
        return elasticsearchUtils.createDocument(index, id, data);
    }

    @Override
    public void delete(List<String> ids) {

    }

    @Override
    public Page<EsProduct> search(String keyword, Integer pageNum, Integer pageSize) {
        return null;
    }
}
