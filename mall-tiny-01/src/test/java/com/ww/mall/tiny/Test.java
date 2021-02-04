package com.ww.mall.tiny;

import com.ww.mall.tiny.comom.utlis.ElasticsearchUtils;
import com.ww.mall.tiny.dao.EsProductDao;
import com.ww.mall.tiny.mbg.mode.PmsBrand;
import com.ww.mall.tiny.comom.utlis.RedisUtils;
import com.ww.mall.tiny.nosql.es.EsProduct;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author linweiwei
 * @version 1.0
 * @date 2020-12-08 17:06
 * @describe:
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class Test {

    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private EsProductDao esProductDao;
    @Autowired
    private ElasticsearchUtils elasticsearchUtils;

    @org.junit.Test
    public void redisTest() {
        PmsBrand pmsBrand = new PmsBrand();
        pmsBrand.setName("中文");
        pmsBrand.setBigPic("efiohe");
        pmsBrand.setBrandStory("品牌故事");
        pmsBrand.setFactoryStatus(1);
        Map<String, Object> map = new HashMap<>();
        map.put("valueKey", pmsBrand);
        redisUtils.hmSet("brand", map, 60);
    }

    @org.junit.Test
    public void esProductDaoTest() {
        List<EsProduct> allProductList = esProductDao.getAllProductList(null);
        System.out.println(allProductList.size());
    }


    @org.junit.Test
    public void elasticsearchUtilsTest() throws IOException {
        boolean pms_product = elasticsearchUtils.createIndex("pms_product");
        System.out.println("创建结果：" + pms_product);
        boolean existIndex = elasticsearchUtils.existsIndex("pms_product");
        System.out.println("索引是否存在：" + pms_product);
    }


}
