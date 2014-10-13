package com.will.str;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.will.utils.ExecShell;

public class PHPModuleChecker {
    public static void main(String[] args) throws Exception {
//        tryExec();
        String filePath = "/usr/local/will/test/smartAgent/smartAgentStandard/app/php/lib/code/Install/php/conf/smart_agent.ini";
//        List<String> readLines = FileUtils.readLines(new File(filePath));
//        StringBuilder sb = new StringBuilder();
//        for (String line : readLines) {
//            
//        }
        String outputDir = "\"/var/neeke\"";
        String blackList = "\"a|b\"";
        String whiteList = "\"curl_init|curl_setopt|file_get_contents|file_put_contents|mysql_connect|mysql_query|PDO::__construct|PDO::query|PDO::prepare|Memcache::connect|Redis::connect\"";
        String content = FileUtils.readFileToString(new File(filePath));
        content = content.replace("{outputDir}", outputDir);
        content = content.replace("{blackList}", blackList);
        content = content.replace("{whiteList}", whiteList);
        System.out.println(content);
        
    }

    /**
     * 业务场景： 
     *  php的code agent需要确定smart_agent.so已经被正确到加载进入php了。
     *  应该执行到命令是：php -m | grep SmartAgent, 如果有结果，那么就是安装了。
     * 
     * <br/><br/>
     * @author will
     * @2014-9-30
     */
    private static void tryExec() throws IOException {
        String execCmd = ExecShell.getExecCmdString("php -m | grep SmartAgent");
        if (execCmd.contains("SmartAgent")) {
            System.out.println("loaded");
        } else {
            System.out.println("not loaded");
        }
        System.out.println(execCmd);
        
        System.out.println(ExecShell.getExecCmdString(FileUtils.readFileToString(new File("/usr/local/will/test/smartAgent/smartAgentStandard/app/php/lib/code/Install/test.sh"))));
    }
}
