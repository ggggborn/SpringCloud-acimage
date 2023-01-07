package com.acimage.image.utils;

public class ImageUtils {
    public static int rgb2Gray(int r, int g, int b) {
        return (int) Math.round(r * 0.299 + g * 0.581 + b * 0.114);
    }
}

