package com.crazyjava.data.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.AutoMappingBehavior;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class DataConfiguration {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/** DataSource Main 생성 */
	@Primary
    @Bean(name="dataSource1")
    @ConfigurationProperties(prefix = "spring.datasource1")
    public DataSource mysql1DataSource() {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .build();
    }
 
    /** DataSource Sub 생성 */
    @Bean(name="dataSource2")
    @ConfigurationProperties(prefix = "spring.datasource2")
    public DataSource mysql2DataSource() {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .build();
    }
    
    /** sqlSessionFactory Main 생성 */
    @Primary
    @Bean(name="sqlSessionFactory1")
    public SqlSessionFactory sqlSessionFactoryMain(@Autowired @Qualifier("dataSource1") DataSource dataSource) throws Exception {
        logger.info("SqlSessionFactory Main Start");
        org.apache.ibatis.session.Configuration configuration = this.getMybatisConfig();
 
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        //war사용시 factoryBean.setVfs(SpringBootVFS.class);
        factoryBean.setConfiguration(configuration);
        //factoryBean.setTypeHandlersPackage("com.commax.tool.framework.mybatis.typehandler");
 
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resource = resolver.getResources("mybatis/*.xml");
        factoryBean.setMapperLocations(resource);
 
        return factoryBean.getObject();
    }
 
    /** sqlSessionFactory Sub 생성 */
    @Bean(name="sqlSessionFactory2")
    public SqlSessionFactory sqlSessionFactory(@Autowired @Qualifier("dataSource2") DataSource dataSource) throws Exception {
        logger.info("SqlSessionFactory SUB Start");
        org.apache.ibatis.session.Configuration configuration = this.getMybatisConfig();
 
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        //war사용시 factoryBean.setVfs(SpringBootVFS.class);
        factoryBean.setConfiguration(configuration);
        //factoryBean.setTypeHandlersPackage("com.commax.tool.framework.mybatis.typehandler");
 
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resource = resolver.getResources("mybatis/*.xml");
        factoryBean.setMapperLocations(resource);
 
        return factoryBean.getObject();
    }
    
    /** sqlSession Main 생성 */
    @Bean
    @Primary
    public SqlSession sqlSessionMain(@Autowired @Qualifier("sqlSessionFactory1") SqlSessionFactory factory) {
        return new SqlSessionTemplate(factory);
    }
 
    /** sqlSession Sub 생성 */
    @Bean
    public SqlSession sqlSessionSub(@Autowired @Qualifier("sqlSessionFactory2") SqlSessionFactory factory) {
        return new SqlSessionTemplate(factory);
    }
 
    /** MybatisConfig 설정정보 */
    private org.apache.ibatis.session.Configuration getMybatisConfig() {
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setCacheEnabled(true);
        configuration.setLazyLoadingEnabled(false);
        configuration.setAggressiveLazyLoading(false);
        configuration.setMultipleResultSetsEnabled(true);
        configuration.setUseColumnLabel(true);
        configuration.setAutoMappingBehavior(AutoMappingBehavior.PARTIAL);
        configuration.setDefaultExecutorType(ExecutorType.SIMPLE);
        configuration.setDefaultStatementTimeout(25000);
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setJdbcTypeForNull(JdbcType.NVARCHAR);
        //configuration.setLazyLoadTriggerMethods(new HashSet<>(Arrays.asList("equals", "clone", "hashCode", "toString")));
        configuration.setLogPrefix("[SQL]");
 
        return configuration;
    }
}
