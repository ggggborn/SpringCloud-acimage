package com.acimage.common.utils;

import com.acimage.common.utils.common.ListUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlUtils {

    private static final Pattern imageSrcPattern;

    static {
        String regex = "<img\\b[^>]*\\bsrc\\b\\s*=\\s*('|\")?(/[^'\"\n\r\f>]+(\\.jpg|\\.png\\.jpe|\\.jpeg|\\.webp))\\b[^>]*>";
        imageSrcPattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
    }

    public static String html2Text(String strHtml) {
        String content = strHtml.replaceAll("</?[^>]+>", ""); //剔出<html>的标签
        content = content.replaceAll("(\\s|\t|\r|\n)+", " ");//去除字符串中的空格,回车,换行符,制表符
        content = content.replaceAll("(&[a-z]{2,6}+;)+", " ");//替换掉如&nbsp;之类的符号
        return content;
    }

    /**
     * 获取html中相对路径开头的图片，并对结果去重
     */
    public static List<String> getInnerImageUrls(String html) {
        List<String> imageSrcList = new ArrayList<>();

        Matcher m = imageSrcPattern.matcher(html);
        String src;
        while (m.find()) {
            String quote = m.group(1);
            src = (quote == null || quote.trim().length() == 0) ? m.group(2).split("\\s+")[0] : m.group(2);
            imageSrcList.add(src);
        }
        return ListUtils.removeRepeat(imageSrcList);
    }
}
