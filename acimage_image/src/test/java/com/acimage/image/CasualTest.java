package com.acimage.image;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.acimage.common.utils.common.ListUtils;
import com.acimage.image.utils.BitUtils;
import com.acimage.image.utils.DhashUtils;
import net.coobird.thumbnailator.Thumbnails;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;

@SpringBootTest
public class CasualTest {

    @Test
    public void testGetSnowflakeId(){
        long id=IdUtil.getSnowflake().nextId();
        System.out.println(id);
        System.out.println(Long.toString(id).length());

        System.out.println();
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
    public void testTh(){

        BufferedImage inputStream= null;
        try {
            inputStream = ImageIO.read(new FileInputStream("F:\\MyImage\\素材\\bg2.png"));
//            inputStream = new FileInputStream("F:\\MyImage\\素材\\1.jpeg");
            List<BufferedImage> inputStreams=new ArrayList<>(List.of(inputStream));
            Thumbnails.Builder<BufferedImage> inputStreamBuilder=Thumbnails.fromImages(inputStreams);
            inputStreamBuilder.forceSize(9,8);
//            inputStreamBuilder.asBufferedImage().toFile(new File("F:\\MyImage\\素材\\0001.jpeg"));
            ImageIO.write(inputStreamBuilder.asBufferedImage(),"jpeg",new File("F:\\MyImage\\素材\\0bg2.png"));
//            inputStreamBuilder.asFiles(new ArrayList<>(List.of(new File("F:\\MyImage\\素材\\0001.jpeg"))));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGetImageDhashFrom(){

        InputStream inputStream1= null;
        InputStream inputStream2= null;
        try {
            inputStream1 = new FileInputStream("F:\\MyImage\\素材\\1.jpeg");
            inputStream2 = new FileInputStream("F:\\MyImage\\素材\\0001.jpeg");
            System.out.println(DhashUtils.distanceBetween(inputStream1,inputStream2));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testImageScale(){
        Image img=Toolkit.getDefaultToolkit().getImage(System.getProperty("user.dir"));

        InputStream inputStream1= null;
        InputStream inputStream2= null;
        try {
            inputStream1 = new FileInputStream("F:\\MyImage\\素材\\0001.jpeg");
            inputStream2 = new FileInputStream("F:\\MyImage\\素材\\1.jpeg");
            System.out.println(DhashUtils.distanceBetween(inputStream1,inputStream2));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testBitUtils(){
        long a=18588L;
        System.out.println(BitUtils.longToStr64(a));
        System.out.println(BitUtils.sumOfBits(a));
        System.out.println(BitUtils.str64ToLong(BitUtils.longToStr64(a)));
        new ArrayList<>(Arrays.asList()).subList(0,10);
    }

    @Test
    public void test89(){
        List<Long> list1=new ArrayList<>(Arrays.asList(1L,2L,5L,7L,10089L,999999L));
        List<Long> list2=new ArrayList<>(Arrays.asList(2L,4L,6L,7L,10086L));
        System.out.println(ListUtils.differenceSetOf(list1,list2));
    }








}
