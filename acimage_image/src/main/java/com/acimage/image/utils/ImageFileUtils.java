package com.acimage.image.utils;

import com.acimage.image.global.consts.MyFileConstants;
import com.acimage.image.global.context.DirectoryContext;

import javax.validation.constraints.NotNull;

public class ImageFileUtils {
    public static String imageIdToImagePath(@NotNull Long imageId){
        return DirectoryContext.IMAGES_DIRECTORY+"/"+imageId+"."+ MyFileConstants.IMAGE_FORMAT;
    }
}
