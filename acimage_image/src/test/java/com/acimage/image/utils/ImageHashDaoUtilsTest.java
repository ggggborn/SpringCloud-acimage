package com.acimage.image.utils;


import net.coobird.thumbnailator.Thumbnails;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class ImageHashDaoUtilsTest {

    @Test
    public void scaleImageTest(){

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
    public void getImageDhashFromTest(){

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
    public void imageDistanceTest(){
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
    public void bitUtilsTest(){
        long a=185L;
        System.out.println(BitUtils.longToStr64(a));
        System.out.println(BitUtils.str64ToLong(BitUtils.longToStr64(a)));
    }





}
