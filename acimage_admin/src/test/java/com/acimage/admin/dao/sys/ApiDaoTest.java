package com.acimage.admin.dao.sys;


import com.acimage.common.global.enums.MatchRule;
import com.acimage.common.model.domain.sys.Api;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;

@SpringBootTest
public class ApiDaoTest {

    @Autowired
    ApiDao apiDao;

    @Test
    void testInsert(){
        Api it=new Api();
        it.setPath("/dasjdhaks/asdjhaskd");
        it.setMethod(HttpMethod.GET);
        it.setMatchRule(MatchRule.PREFIX);
        it.setPermissionId(55);
        apiDao.insert(it);
    }

    @Test
    void testSelect(){
        System.out.println(apiDao.selectList(null));
    }
}
