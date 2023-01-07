package com.acimage.admin.config.mybatis;


import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "com.acimage.admin.dao.image", sqlSessionFactoryRef = "imageSqlSessionFactory",sqlSessionTemplateRef ="imageSqlSessionTemplate")
public class ImageDataSourceConfig {


    @Bean(name = "imageDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.image")
    public DataSource imageDataSource() {
        return DataSourceBuilder.create().build();
    }

//    @Bean
//    @ConfigurationProperties(prefix = "mybatis-plus.global-config")
//    GlobalConfig globalConfig(){
//        return new GlobalConfig();
//    }




    @Bean(name = "imageSqlSessionFactory")
    public SqlSessionFactory imageSqlSessionFactory(@Qualifier("imageDataSource") DataSource dataSource, @Autowired GlobalConfig globalConfig) throws Exception {
        MybatisSqlSessionFactoryBean sqlSessionFactoryBean=new MybatisSqlSessionFactoryBean();

        sqlSessionFactoryBean.setDataSource(dataSource);

        sqlSessionFactoryBean.setGlobalConfig(globalConfig);

        sqlSessionFactoryBean.setTypeAliasesPackage("com.acimage.common.model.domain");
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/image/*.xml"));
        sqlSessionFactoryBean.setTypeHandlersPackage("com.acimage.admin.config.mybatis.typehandler");
        return sqlSessionFactoryBean.getObject();

    }


    @Bean(name = "imageTransactionManager")
    public DataSourceTransactionManager imageTransactionManager(@Qualifier("imageDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }


    @Bean(name = "imageSqlSessionTemplate")
    public SqlSessionTemplate memberSqlSessionTemplate(
            @Qualifier("imageSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
