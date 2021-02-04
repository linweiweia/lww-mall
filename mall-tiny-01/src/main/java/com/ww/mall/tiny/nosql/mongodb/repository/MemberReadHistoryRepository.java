package com.ww.mall.tiny.nosql.mongodb.repository;

import com.ww.mall.tiny.nosql.mongodb.document.MemberReadHistory;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @author linweiwei
 * @version 1.0
 * @date 2021-01-27 09:41
 * @describe:   操作mongodb
 */
public interface MemberReadHistoryRepository extends MongoRepository<MemberReadHistory,String> {

    /**
     * 无需自己实现接口，只要继承MongoRepository，并且按命名规范（会有提示）即可
     * @param memberId  会员Id
     * @return 游览记录
     */
    List<MemberReadHistory> findByMemberIdOrderByCreateTimeDesc(Long memberId);

}
