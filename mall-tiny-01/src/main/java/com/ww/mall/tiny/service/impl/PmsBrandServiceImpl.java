package com.ww.mall.tiny.service.impl;

import com.github.pagehelper.PageHelper;
import com.ww.mall.tiny.mbg.mapper.PmsBrandMapper;
import com.ww.mall.tiny.mbg.mode.PmsBrand;
import com.ww.mall.tiny.mbg.mode.PmsBrandExample;
import com.ww.mall.tiny.service.PmsBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author linweiwei
 * @version 1.0
 * @date 2020-11-26 16:29
 * @describe:   品牌
 */

@Service
public class PmsBrandServiceImpl implements PmsBrandService {

    @Autowired
    private PmsBrandMapper brandMapper;

    @Override
    public int createBrand(PmsBrand brand) {
        //insertSelective 看sql源码就是多增加了判断
        return brandMapper.insertSelective(brand);
    }

    @Override
    public int deleteBrand(Long id) {
        return brandMapper.deleteByPrimaryKey(id);
    }

    @Override
    public PmsBrand getBrand(Long id) {
        return brandMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateBrand(Long id, PmsBrand brand) {
        brand.setId(id);
        return brandMapper.updateByPrimaryKeyWithBLOBs(brand);
    }

    /**
     * generator自动生成代码，如果数据库字段是text、blob大数据，generator在做查询修改等操作会把这些字段单独出来，应该是为了性能。
     * 查找全出有两个方法
     * 1.selectByExample：不包括text、blob字段操作
     * 2.selectByExampleWithBLOBs：包括text、blob字段操作
     * @return
     */
    @Override
    public List<PmsBrand> listAllBrand() {
        return brandMapper.selectByExampleWithBLOBs(new PmsBrandExample());
    }

    /**
     * 使用PageHelp进行分页处理
     *      PageHelper.startPage(pageNum, pageSize);只是使用PageHelp的一种使用方式，后面要紧跟select的查询
     */
    @Override
    public List<PmsBrand> listBrand(int pageNum, int pageSize) {
        //使用pageHelp
        PageHelper.startPage(pageNum, pageSize);//会对下面的查询进行物理分页（注意两句要连着）
        return brandMapper.selectByExampleWithBLOBs(new PmsBrandExample());//做select查询和generator无关，只要是select查询就行。
    }
}
