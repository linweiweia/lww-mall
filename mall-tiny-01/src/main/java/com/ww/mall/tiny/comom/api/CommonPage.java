package com.ww.mall.tiny.comom.api;

import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author linweiwei
 * @version 1.0
 * @date 2020-11-27 17:39
 * @describe:   包含分页信息和数据的封装类
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonPage<T> {

    //当前页
    private Integer pageNum;
    //页面大小
    private Integer pageSize;
    //页面数量
    private Integer totalPage;
    //总记录数
    private Long total;
    //查询的数据
    private List<T> list;

    //填充分页信息
    public static <T> CommonPage<T> fullPage(List<T> list) {
        CommonPage<T> result = new CommonPage<>();
        PageInfo<T> pageInfo = new PageInfo<>(list);
        result.setPageNum(pageInfo.getPageNum());
        result.setPageSize(pageInfo.getSize());
        result.setTotalPage(pageInfo.getPages());
        result.setTotal(pageInfo.getTotal());
        result.setList(pageInfo.getList());
        return result;
    }

}
