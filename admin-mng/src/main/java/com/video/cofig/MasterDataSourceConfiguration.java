package com.video.cofig;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import tk.mybatis.spring.annotation.MapperScan;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "com.video.mapper", sqlSessionTemplateRef = "masterSqlSessionTemplate")
public class MasterDataSourceConfiguration {

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Bean(name = "masterDataSource")
    @Primary
    public DataSource dataSource() {
        HikariDataSource dataSource = (HikariDataSource) DataSourceBuilder.create().build();
        dataSource.setConnectionInitSql("set names utf8mb4");
        dataSource.setDriverClassName(this.driverClassName);
        dataSource.setJdbcUrl(this.url);
        dataSource.setUsername(this.username);
        dataSource.setPassword(this.password);

        //        DruidDataSource dataSource = new DruidDataSource();
        //        dataSource.setDriverClassName(this.driverClassName);
        //        dataSource.setUrl(this.url);
        //        dataSource.setUsername(this.username);
        //        dataSource.setPassword(this.password);
        //        dataSource.setConnectionInitSqls(Collections.singleton("set names utf8mb4;"));
        return dataSource;
    }

    @Bean(name = "masterSqlSessionFactory")
    @Primary
    public SqlSessionFactory sqlSessionFactory(@Qualifier("masterDataSource") DataSource dataSource)
            throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources("classpath*:/mapper/**/*Mapper.xml"));
        bean.setTypeAliasesPackage("com.video.mapper");
        return bean.getObject();
    }

    @Bean(name = "masterTransactionManager")
    @Primary
    public DataSourceTransactionManager
            transactionManager(@Qualifier("masterDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "masterSqlSessionTemplate")
    @Primary
    public SqlSessionTemplate sqlSessionTemplate(
            @Qualifier("masterSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}