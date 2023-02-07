package com.acimage.community;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.acimage.common.model.domain.image.Image;
import com.acimage.common.utils.common.ListUtils;
import com.acimage.community.global.enums.TopicAttribute;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import java.util.regex.Pattern;

@SpringBootTest
public class CasualTest {


    @Test
    public void testGetSnowflakeId(){
        long id=IdUtil.getSnowflake().nextId();
        System.out.println(id);
        System.out.println(Long.toString(id).length());
        System.out.println(System.currentTimeMillis());
        TopicAttribute topicAttribute = TopicAttribute.ACTIVITY_TIME;


    }


    @Test
    public void testMd5(){
        String txt="b3cfd02862e5565ebecfa608035911d612346546";
        System.out.println(DigestUtil.md5Hex(txt).length());
    }

    @Test
    public void testUUID(){
        String uuid=IdUtil.simpleUUID();
        System.out.println(uuid.length());
    }

    @Test
    public void testPatternMatch(){
        final String PASSWORD_PATTERN="^(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$";
        String password="5555555----";
        System.out.println(Pattern.matches(PASSWORD_PATTERN,password));
    }

    @Test
    public void testDateNow(){
        Date beginOfDay = DateUtil.beginOfDay(new Date());
        //获取昨天开始时间
        Date startDate=DateUtil.offsetDay(beginOfDay,-1);
        String startTime=DateUtil.format(startDate,"yyyy-MM-dd HH:mm:ss");
        System.out.println(startTime);
    }

    @Test
    public void testSubAfter(){
        String str="558.968.jpeg";
        System.out.println(StrUtil.subAfter(str,'.',true));
        List<String> s= Arrays.asList("jpg","png");
        System.out.println(s.contains("jpg"));
    }

    @Test
    public void testListUtils(){
        List<Image> images=new ArrayList<>();
        images.add(new Image(1L,null,null,null));
        images.add(new Image(2L,null,null,null));
        images.add(new Image(3L,null,null,null));
        System.out.println(ListUtils.extract(Image::getId,images));
    }



}
