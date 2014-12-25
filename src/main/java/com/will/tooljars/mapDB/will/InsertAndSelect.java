package com.will.tooljars.mapDB.will;

import java.util.Map;

/**
 * 应用场景：
 *      开发了master-module模式的软件，各个模块是以进程形式存在的。所以各个模块之间的通信不是很方便，开始使用一些restful api，
 *      随着需要沟通的东西越来越多，感觉通过restful server来沟通越来越困难。
 *      嵌入式DB应时应景的解决这个问题，考虑逐步向这方面迁移；
 *      
 *      但是当discover将发现的数据放进db文件。在app模块读取相应信息的时候，会出现奇怪的问题，就是在多次查询中，只有一次拿到正确结果。
 *      肯定是这些数据多数时间并没有准备好被其他进程读取。想到到可能有：没有commit, 没有close.测试后，发现这两个步骤都需要。并不像
 *      mysql之类的DB，只要commit了，就会被永久到保存并提供查询。
 * 
 * <br/><br/>
 * @author will
 *
 * @2014-12-25
 */
public class InsertAndSelect {
    public static void main(String[] args) {

        Map<String, String> hashMap = MapDBUtil.getHashMap("test-will");
        MapDBUtil.getAll();
        System.out.println("\n\nkey \t value");
        // hashMap.put("name", "will");
        // MapDBUtil.commit();
        // MapDBUtil.close();
        hashMap = MapDBUtil.getHashMap("test-will");
        for (String key : hashMap.keySet()) {
            System.out.println(key + " \t" + hashMap.get(key));
        }
    }
}
