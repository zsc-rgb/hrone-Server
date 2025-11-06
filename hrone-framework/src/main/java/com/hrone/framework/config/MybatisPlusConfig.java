package com.hrone.framework.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis-Plus配置类
 * 
 * 功能说明：
 * 1. 配置MyBatis-Plus拦截器
 * 2. 配置分页插件
 * 3. 配置Mapper扫描路径
 * 
 * 技术要点：
 * - @MapperScan：自动扫描Mapper接口
 * - MybatisPlusInterceptor：MyBatis-Plus拦截器
 * - PaginationInnerInterceptor：分页插件
 * 
 * 使用场景：
 * - 实现分页查询
 * - 自动注入Mapper
 * - SQL性能优化
 * 
 * @author hrone
 */
@Configuration
@MapperScan("com.hrone.**.mapper")
public class MybatisPlusConfig {
    
    /**
     * MyBatis-Plus拦截器
     * 
     * 作用：
     * - 添加分页插件
     * - 添加其他插件（如乐观锁、防全表更新删除等）
     * 
     * @return MybatisPlusInterceptor
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        
        // 添加分页插件
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        
        // 设置数据库类型为MySQL
        paginationInnerInterceptor.setDbType(DbType.MYSQL);
        
        // 设置最大单页限制数量，默认500条，-1不受限制
        paginationInnerInterceptor.setMaxLimit(500L);
        
        // 溢出总页数后是否进行处理
        // true：回到首页
        // false：继续请求（默认）
        paginationInnerInterceptor.setOverflow(false);
        
        // 单页分页条数限制（默认无限制）
        // paginationInnerInterceptor.setMaxLimit(1000L);
        
        interceptor.addInnerInterceptor(paginationInnerInterceptor);
        
        return interceptor;
    }
}

