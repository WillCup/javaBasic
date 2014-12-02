package com.will.str;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternMatcher {
    public static final Pattern INCLUDE_PATTERN = Pattern.compile("^\\sInclude.*",
            Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
    public static void main(String[] args) {
        
        String include = " <IfModule mod_include.c>";
        String include2 = " include.c>";
        Matcher matcher = INCLUDE_PATTERN.matcher(include2);
        if (matcher.find()) {
            System.out.println(matcher.group());
        }
        System.exit(0);
//        startup();
//        htmlPattern();
//        htmlReplace();
    }

    private static void htmlReplace() {
        String pageText = "<html><head></head><body>Hello there?</body></html>";

        // use regex to find the links
//        Pattern pattern = Pattern.compile("test.*");
        // 这样会把每个字符都匹配到。
        Pattern pattern = Pattern.compile("");
        Matcher matcher = pattern.matcher(pageText);
        int offset = 0;
        if (matcher.find(offset)) {
            System.out.println("after replace : " + matcher.replaceAll("<body id='dfd'>I am Will. "));
            System.out.println("original : " + pageText);
        }
    }

    public static void htmlPattern() {
        String pageText = "<html><head></head><body>Hello there?</body></html>";

        // use regex to find the links
//        Pattern pattern = Pattern.compile("test.*");
        Pattern pattern = Pattern.compile("<body>");
        Matcher matcher = pattern.matcher(pageText);
        int offset = 0;
        if (matcher.find(offset)) {
            System.out.println("after replace : " + matcher.replaceAll("<body id='dfd'>I am Will. "));
            System.out.println("original : " + pageText);
        }
    }

    public static void startup() {
        String pageText = "will test ]sdfd[ds";

        // use regex to find the links
//        Pattern pattern = Pattern.compile("test.*");
        Pattern pattern = Pattern.compile("test*");
        Matcher matcher = pattern.matcher(pageText);
        int offset = 0;
        String newText = "";
        if (matcher.find(offset)) {
            
            System.out.println("matcher.group() - " + matcher.group());
            System.out.println("m.start() - " + matcher.start());
            System.out.println("m.end() - " + matcher.end());
            // update the text
            newText += pageText.substring(offset, matcher.start());
            System.out.println("this should be will: " + newText);
            // update the offset
            offset = matcher.end();
            System.out.println("offset should be 9: " + offset);
            // get the matching string
            String match = pageText.substring(matcher.start(), matcher.end());
            // replace the body tag
            System.out.println("match should be test :" + match);
            newText += "";
            // add the final text
            newText += pageText.substring(offset, pageText.length());
            System.out.println("after replace : " + matcher.replaceAll("d"));
            System.out.println("newText should has no test:" + newText);
        }
    }
}
