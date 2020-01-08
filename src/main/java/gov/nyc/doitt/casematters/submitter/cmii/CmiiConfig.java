package gov.nyc.doitt.casematters.submitter.cmii;


import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "cmiiEntityManagerFactory",
        transactionManagerRef = "cmiiTransactionManager",
        basePackages = {"gov.nyc.doitt.casematters.submitter.cmii"}
)
public class CmiiConfig {

    @Primary
    @Bean(name = "cmiiDataSource")
    @ConfigurationProperties(prefix = "cmii.datasource")
    public DataSource cmiiDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "cmiiEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean
    entityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("cmiiDataSource") DataSource dataSource
    ) {
        return builder
                .dataSource(dataSource)
                .packages("gov.nyc.doitt.casematters.submitter.cmii")
                .persistenceUnit("josfriedman")
                .build();
    }

    @Primary
    @Bean(name = "cmiiTransactionManager")
    public PlatformTransactionManager cmiiTransactionManager(
            @Qualifier("cmiiEntityManagerFactory") EntityManagerFactory
                    cmiiEntityManagerFactory
    ) {
        return new JpaTransactionManager(cmiiEntityManagerFactory);
    }
}
