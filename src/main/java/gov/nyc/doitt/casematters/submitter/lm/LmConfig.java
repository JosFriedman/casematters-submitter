package gov.nyc.doitt.casematters.submitter.lm;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "lmEntityManagerFactory", transactionManagerRef = "lmTransactionManager", basePackages = {
		"gov.nyc.doitt.casematters.submitter.lm" })

public class LmConfig {

	@Bean(name = "lmDataSource")
	@ConfigurationProperties(prefix = "lm.datasource")
	public DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "lmEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean barEntityManagerFactory(EntityManagerFactoryBuilder builder,
			@Qualifier("lmDataSource") DataSource dataSource) {
		return builder.dataSource(dataSource).packages("gov.nyc.doitt.casematters.submitter.lm")
				.persistenceUnit("CASEMATTERS_INTERNET_INTAKE").build();
	}

	@Bean(name = "lmTransactionManager")
	public PlatformTransactionManager lmTransactionManager(
			@Qualifier("lmEntityManagerFactory") EntityManagerFactory lmEntityManagerFactory) {
		return new JpaTransactionManager(lmEntityManagerFactory);
	}
}
