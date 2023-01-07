package com.acimage.common.utils.common;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

public class ArrayUtils {

    public static Integer firstIndexOf(String[] strs, String targetStr){
        for(int i=0;i<strs.length;i++){
            if(strs[i].equals(targetStr)){
                return i;
            }
        }
        return null;
    }


}
