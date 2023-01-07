package com.acimage.admin.config.mybatis;


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
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "com.acimage.admin.dao.community", sqlSessionFactoryRef = "communitySqlSessionFactory")
public class CommunityDataSourceConfig {

    @Bean(name = "communityDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.community")
    public DataSource communityDataSource() {
        return DataSourceBuilder.create().build();
    }


    @Bean(name = "communitySqlSessionFactory")
    public SqlSessionFactory orderSqlSessionFactory(@Qualifier("communityDataSource")DataSource dataSource,@Autowired GlobalConfig globalConfig) throws Exception {
        MybatisSqlSessionFactoryBean sqlSessionFactoryBean=new MybatisSqlSessionFactoryBean();

        sqlSessionFactoryBean.setDataSource(dataSource);

        sqlSessionFactoryBean.setGlobalConfig(globalConfig);

        sqlSessionFactoryBean.setTypeAliasesPackage("com.acimage.common.model.domain");
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/community/*.xml"));
        sqlSessionFactoryBean.setTypeHandlersPackage("com.acimage.admin.config.mybatis.typehandler");
        return sqlSessionFactoryBean.getObject();
    }


    @Bean(name = "communityTransactionManager")
    public DataSourceTransactionManager orderTransactionManager(@Qualifier("communityDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }


    @Bean(name = "communitySqlSessionTemplate")
    public SqlSessionTemplate memberSqlSessionTemplate(
            @Qualifier("communitySqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
