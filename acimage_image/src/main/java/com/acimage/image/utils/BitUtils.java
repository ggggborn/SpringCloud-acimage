package com.acimage.image.utils;

public class BitUtils {
    public static int getBit(long value, int index) {
        if (index < 0 || index > 63) {
            throw new IllegalArgumentException("index范围为0-63");
        }

        if (index == 63) {
            if (value < 0) {
                return 1;
            } else {
                return 0;
            }
        } else {
            if (value < 0) {
                //使value变为value补码去掉符号位之后的值
                value = value + (1L << 62) + (1L << 62);
            }
            return (int) ((value >> index) & 1);
        }
    }

    /**
     *
     * @param str64 64位补码
     * @return 补码对应的long值
     */
    public static long str64ToLong(String str64){
        if(str64==null||str64.length()!=64){
            throw new IllegalArgumentException("str64传参为："+str64+"；str64必须为64位长度的0-1字符串！");
        }

        int sgn=str64.charAt(0)=='1'?-1:1;
        //获取符合外之外的值
        long sum=0;
        for(int i=0;i<63;i++){
            long bit=0L;
            if(str64.charAt(63-i)=='0'){
                bit=0L;
            } else if (str64.charAt(63 - i) == '1') {
                bit =1L;
            }else{
                throw new IllegalArgumentException("str64传参为："+str64+"；str64必须为64位长度的0-1字符串！");
            }
            sum+=(bit<<i);
        }
        if(sgn<0){
            //获取原码:根据公式 补码=反码+1=（2^63-原码-1）+1=2^63-原码
            sum=(1L<<62)-sum+(1L<<62);
        }
        return sgn*sum;
    }

    public static  String longToStr64(long value){
        StringBuilder stringBuilder=new StringBuilder(64);
        for(int i=0;i<64;i++){
            stringBuilder.append(getBit(value,63-i));
        }
        return stringBuilder.toString();
    }

    public static int sumOfBits(long value){
        String str64=longToStr64(value);
        int sum=0;
        for(int i=0;i<64;i++){
            if(str64.charAt(i)=='1'){
                sum++;
            }
        }
        return sum;
    }
}
