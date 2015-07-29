package com.will.poi;

import java.util.ArrayList;
import java.util.List;

/**
 * 生成插入数据list集合
 *
 * @author yaohucaizi
 */
public class ListSource {

    public List<List<String>> listSource() {
        List<List<String>> totalList = new ArrayList<List<String>>();
        for (int i = 0; i < 10; i++) {
            List<String> list = new ArrayList<String>();
            for (int j = 0; j < 5; j++) {
                String str = "";
                String source = getStr(j, str);
                list.add(source);
            }
            totalList.add(list);
        }
        return totalList;
    }

    private String getStr(int j, String str) {
        switch (j) {
            case 0:
                str = "姓名";
                break;
            case 1:
                str = "年龄";
                break;
            case 2:
                str = "地址";
                break;
            case 3:
                str = "电话";
                break;
            case 4:
                str = "爱好";
                break;
        }
        return str;
    }
}