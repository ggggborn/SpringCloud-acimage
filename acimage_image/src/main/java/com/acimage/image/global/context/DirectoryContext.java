package com.acimage.image.global.context;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


public class DirectoryContext {
    @Value("${my-config.images-directory}")
    public String imagesDirectory;
    @Value("${my-config.photos-directory}")
    public String photosDirectory;

    public static String IMAGES_DIRECTORY;

    public static String PHOTOS_DIRECTORY;

    @PostConstruct
    void init(){
        IMAGES_DIRECTORY= imagesDirectory;
        PHOTOS_DIRECTORY= photosDirectory;
    }
}
