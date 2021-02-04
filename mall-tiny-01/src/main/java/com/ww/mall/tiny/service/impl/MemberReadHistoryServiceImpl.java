package com.ww.mall.tiny.service.impl;

import com.ww.mall.tiny.nosql.mongodb.document.MemberReadHistory;
import com.ww.mall.tiny.nosql.mongodb.repository.MemberReadHistoryRepository;
import com.ww.mall.tiny.service.MemberReadHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author linweiwei
 * @version 1.0
 * @date 2021-01-27 09:59
 * @describe: 用户游览记录实现类
 */

@Service
public class MemberReadHistoryServiceImpl implements MemberReadHistoryService {

    @Autowired
    private MemberReadHistoryRepository memberReadHistoryRepository;

    //创建游览记录
    @Override
    public int create(MemberReadHistory memberReadHistory) {
        memberReadHistory.setId(null);
        memberReadHistory.setCreateTime(new Date());
        MemberReadHistory memberReadHistory1 = memberReadHistoryRepository.save(memberReadHistory);
        return 1;
    }

    //批量删除游览记录 ids游览记录的id
    @Override
    public int delete(List<String> ids) {
        ArrayList<MemberReadHistory> deleteList = new ArrayList<>();
        for (String id: ids) {
            MemberReadHistory memberReadHistory = new MemberReadHistory();
            memberReadHistory.setId(id);
            deleteList.add(memberReadHistory);
        }
        memberReadHistoryRepository.deleteAll(deleteList);
        return ids.size();
    }

    //获取用户游览记录
    @Override
    public List<MemberReadHistory> list(Long memberId) {
        return memberReadHistoryRepository.findByMemberIdOrderByCreateTimeDesc(memberId);
    }

}
