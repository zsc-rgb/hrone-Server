package com.hrone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 启动类
 * 
 * @author hrone
 */
@SpringBootApplication
public class HROneApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(HROneApplication.class, args);
        System.out.println("\n" +
            "  _   _ ____   ___             \n" +
            " | | | |  _ \\ / _ \\ _ __   ___ \n" +
            " | |_| | |_) | | | | '_ \\ / _ \\\n" +
            " |  _  |  _ <| |_| | | | |  __/\n" +
            " |_| |_|_| \\_\\\\___/|_| |_|\\___|\n" +
            "\n" +
            "========================================\n" +
            "   HROne 后端系统启动成功！\n" +
            "   访问地址：http://localhost:8080\n" +
            "   测试接口：http://localhost:8080/test/hello\n" +
            "========================================\n");
    }
}

