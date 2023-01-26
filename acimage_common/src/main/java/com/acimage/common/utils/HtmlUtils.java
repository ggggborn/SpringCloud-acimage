package com.acimage.common.utils;

import com.acimage.common.utils.common.ListUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlUtils {

    private static final Pattern imageSrcPattern;
    static {
        imageSrcPattern = Pattern.compile("<img\\b[^>]*\\bsrc\\b\\s*=\\s*('|\")?(/[^'\"\n\r\f>]+(\\.jpg|\\.gif|\\.png|\\.svg|\\.jpe|\\.jpeg|\\.pic|\\.webp))\\b[^>]*>", Pattern.CASE_INSENSITIVE);
    }

    public static String html2Text(String strHtml) {
        String content = strHtml.replaceAll("</?[^>]+>", ""); //剔出<html>的标签
        content = content.replaceAll("(\\s|\t|\r|\n|&nbsp;)+", " ");;//去除字符串中的空格,回车,换行符,制表符
        return content;
    }

    public static List<String> getInnerImageUrlsAndRemoveRepeat(String html){
        List<String> imageSrcList = new ArrayList<>();

        Matcher m = imageSrcPattern.matcher(html);
        String src = null;
        while (m.find()) {
            String quote = m.group(1);
            // src=https://sms.reyo.cn:443/temp/screenshot/zY9Ur-KcyY6-2fVB1-1FSH4.png
            src = (quote == null || quote.trim().length() == 0) ? m.group(2).split("\\s+")[0] : m.group(2);
            imageSrcList.add(src);
        }
        return ListUtils.removeRepeat(imageSrcList);
    }
}
