package com.ww.mall.tiny.service;

import com.ww.mall.tiny.mbg.mode.PmsBrand;

import java.util.List;

/**
 * @author linweiwei
 * @version 1.0
 * @date 2020-11-26 16:18
 * @describe:   pms商品模块 品牌管理
 */
public interface PmsBrandService {

    int createBrand(PmsBrand brand);

    int deleteBrand(Long id);

    PmsBrand getBrand(Long id);

    int updateBrand(Long id, PmsBrand brand);

    List<PmsBrand> listAllBrand();

    List<PmsBrand> listBrand(int pageNum, int pageSize);

}
