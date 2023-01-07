package com.acimage.common.global.enums;

import java.util.Arrays;
import java.util.List;

public enum ImageType {
    HOME_CAROUSEL;

    public static List<ImageType> enumAll(){
        return Arrays.asList(HOME_CAROUSEL);
    }

    public static ImageType getImageType(String imageTypeString){
        List<ImageType> allTypes=enumAll();
        for(ImageType imageType:allTypes){
            if(imageType.toString().equals(imageTypeString)){
                return imageType;
            }
        }
        return null;
    }

}
