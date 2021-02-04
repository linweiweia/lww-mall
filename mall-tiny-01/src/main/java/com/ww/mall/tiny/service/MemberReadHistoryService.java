package com.ww.mall.tiny.service;

import com.ww.mall.tiny.nosql.mongodb.document.MemberReadHistory;

import java.util.List;

/**
 * @author linweiwei
 * @version 1.0
 * @date 2021-01-27 09:54
 * @describe:   用户记录service
 */
public interface MemberReadHistoryService {

    /**
     * 生成游览记录
     */
    int create(MemberReadHistory memberReadHistory);

    /**
     * 批量删除游览记录
     */
    int delete(List<String> ids);


    /**
     * 用户游览记录
     */
    List<MemberReadHistory> list(Long memberId);

}
