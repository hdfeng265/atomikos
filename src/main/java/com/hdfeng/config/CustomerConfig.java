package com.hdfeng.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.atomikos.jdbc.AtomikosDataSourceBean;
//import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.mysql.jdbc.jdbc2.optional.MysqlXADataSource;

@Configuration
@MapperScan(basePackages = "com.hdfeng.dao.customer", sqlSessionTemplateRef = "customerSqlSessionTemplate")
public class CustomerConfig {

	@Autowired
	private CustomerDatasourceProperties customerDatasourceProperties = new CustomerDatasourceProperties();

	@Primary
	@Bean(initMethod = "init", destroyMethod = "close")
	public DataSource customerDataSource() {
		MysqlXADataSource mysqlXaDataSource = new MysqlXADataSource();
		mysqlXaDataSource.setUrl(customerDatasourceProperties.getUrl());
		mysqlXaDataSource.setPinGlobalTxToPhysicalConnection(true);
		mysqlXaDataSource.setPassword(customerDatasourceProperties.getPassword());
		mysqlXaDataSource.setUser(customerDatasourceProperties.getUsername());
		mysqlXaDataSource.setPinGlobalTxToPhysicalConnection(true);

		AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
		xaDataSource.setXaDataSource(mysqlXaDataSource);
		xaDataSource.setUniqueResourceName("xads1");
		return xaDataSource;

	}
	
	@Bean
	SqlSessionFactoryBean customerSqlSessionFactory(@Qualifier("customerDataSource")DataSource customerDataSource) {

		Properties sqlSessionFactoryProperties = new Properties();
		sqlSessionFactoryProperties.put("cacheEnabled", true);
		// 查询时，关闭关联对象即时加载以提高性能
		sqlSessionFactoryProperties.put("lazyLoadingEnabled", false);
		// 设置关联对象加载的形态，此处为按需加载字段(加载字段由SQL指定)，不会加载关联表的所有字段，以提高性能
		sqlSessionFactoryProperties.put("aggressiveLazyLoading", true);
		// 对于未知的SQL查询，允许返回不同的结果集以达到通用的效果
		sqlSessionFactoryProperties.put("multipleResultSetsEnabled", true);
		// 允许使用列标签代替列名
		sqlSessionFactoryProperties.put("useColumnLabel", true);
		// 允许使用自定义的主键值(比如由程序生成的UUID 32位编码作为键值)，数据表的PK生成策略将被覆盖
		sqlSessionFactoryProperties.put("useGeneratedKeys", true);
		// 给予被嵌套的resultMap以字段-属性的映射支持
		sqlSessionFactoryProperties.put("autoMappingBehavior", "FULL");
		// 对于批量更新操作缓存SQL以提高性能
		sqlSessionFactoryProperties.put("defaultExecutorType", "BATCH");
		// 数据库超过25000秒仍未响应则超时
		sqlSessionFactoryProperties.put("defaultStatementTimeout", 25000);

		SqlSessionFactoryBean sqlSession = new SqlSessionFactoryBean();
		sqlSession.setDataSource(customerDataSource);
		sqlSession.setConfigurationProperties(sqlSessionFactoryProperties);
		
		return sqlSession;
	}
	
	
	@Bean(name = "customerSqlSessionTemplate")
	public SqlSessionTemplate customerSqlSessionTemplate(
			@Qualifier("customerSqlSessionFactory") SqlSessionFactory customerSqlSessionFactory) throws Exception {
		return new SqlSessionTemplate(customerSqlSessionFactory);
	}

}
