package com.github.vector4wang.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author : wangxc
 */
public class CrawlerUtil {

    public static boolean isMatch(String regex, String url) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);
        return matcher.matches();
    }
}
