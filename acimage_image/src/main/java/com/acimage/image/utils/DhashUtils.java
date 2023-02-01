package com.acimage.image.utils;

import net.coobird.thumbnailator.Thumbnails;

import javax.validation.constraints.NotNull;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DhashUtils {
    public static long getImageDhashFrom(@NotNull InputStream inputStream) throws IOException {
        int width = 9;
        int height = 8;
        List<InputStream> inputStreams = new ArrayList<>(List.of(inputStream));
        Thumbnails.Builder<InputStream> inputStreamBuilder = Thumbnails.fromInputStreams(inputStreams).forceSize(width, height);
        BufferedImage bufferedImage = inputStreamBuilder.asBufferedImage();

        StringBuilder str64 = new StringBuilder((width - 1) * height);

        int[][] grays = new int[width][height];
        for (int hi = 0; hi < height; hi++) {
            for (int wi = 0; wi < width; wi++) {
                Color color = new Color(bufferedImage.getRGB(wi, hi));
                grays[wi][hi] = ImageUtils.rgb2Gray(color.getRed(), color.getGreen(), color.getBlue());
                if (wi > 0) {
                    int hashBit = 0;
                    if (grays[wi][hi] > grays[wi - 1][hi]) {
                        hashBit = 1;
                    }
                    str64.append(hashBit);
                }
            }
        }
        return BitUtils.str64ToLong(str64.toString());
    }

    public static int distanceBetween(@NotNull InputStream inputStream1, @NotNull InputStream inputStream2) throws IOException {
        String str1 = BitUtils.longToStr64(getImageDhashFrom(inputStream1));
        String str2 = BitUtils.longToStr64(getImageDhashFrom(inputStream2));
        return hammingDistanceBetween(str1,str2);
    }

    public static int distanceBetween(long hashValue1, long hashValue2) {
        String str1 = BitUtils.longToStr64(hashValue1);
        String str2 = BitUtils.longToStr64(hashValue2);
        return hammingDistanceBetween(str1,str2);
    }

    private static int hammingDistanceBetween(@NotNull String str1, @NotNull String str2) {
        int distance = 0;
        if (str1.length() != str2.length()) {
            throw new IllegalArgumentException("参数长度不一致");
        }
        for (int i = 0; i < str1.length(); i++) {
            if (str1.charAt(i) != str2.charAt(i)) {
                distance++;
            }
        }
        return distance;
    }
}
