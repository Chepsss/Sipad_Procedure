package it.almaviva.difesa.cessazione.procedure.config;

import com.zaxxer.hikari.HikariDataSource;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration("msProcDbConfig")
@EnableJpaAuditing(auditorAwareRef = "jpaAuditingConfig")
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "msprocEntityManagerFactory",
        transactionManagerRef = "msprocTransactionManager",
        basePackages = {"it.almaviva.difesa.cessazione.procedure.repository.msproc"})
public class MsProcDbConfig {

    @Value("${spring.liquibase.enabled}")
    private boolean enabled;

    @Primary
    @Bean(name = "msprocDataSourceProperties")
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties msprocDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Primary
    @Bean(name = "msprocDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    public HikariDataSource msprocDataSource(@Qualifier("msprocDataSourceProperties") DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Primary
    @Bean(name = "msprocEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean msprocEntityManagerFactory(
            EntityManagerFactoryBuilder msprocEntityManagerFactoryBuilder,
            @Qualifier("msprocDataSource") DataSource dataSource) {

        Map<String, String> msprocJpaProperties = new HashMap<>();
        msprocJpaProperties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQL91Dialect");

        return msprocEntityManagerFactoryBuilder
                .dataSource(dataSource)
                .packages("it.almaviva.difesa.cessazione.procedure.domain.msproc")
                .persistenceUnit("msprocDataSource")
                .properties(msprocJpaProperties)
                .build();
    }

    @Primary
    @Bean(name = "msprocTransactionManager")
    public PlatformTransactionManager msprocTransactionManager(
            @Qualifier("msprocEntityManagerFactory") EntityManagerFactory msprocEntityManagerFactory) {

        return new JpaTransactionManager(msprocEntityManagerFactory);
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.liquibase")
    public LiquibaseProperties msprocLiquibaseProperties() {
        return new LiquibaseProperties();
    }

    @Bean
    public SpringLiquibase msprocLiquibase(@Qualifier("msprocDataSourceProperties") DataSourceProperties msprocDataSourceProperties) {
        return springLiquibase(msprocDataSource(msprocDataSourceProperties), msprocLiquibaseProperties());
    }

    private SpringLiquibase springLiquibase(DataSource dataSource, LiquibaseProperties properties) {
        if (enabled) {
            var liquibase = new SpringLiquibase();
            liquibase.setDataSource(dataSource);
            liquibase.setChangeLog(properties.getChangeLog());
            liquibase.setContexts(properties.getContexts());
            liquibase.setDefaultSchema(properties.getDefaultSchema());
            liquibase.setLiquibaseSchema(properties.getLiquibaseSchema());
            liquibase.setLiquibaseTablespace(properties.getLiquibaseTablespace());
            liquibase.setDatabaseChangeLogLockTable(properties.getDatabaseChangeLogLockTable());
            liquibase.setDatabaseChangeLogTable(properties.getDatabaseChangeLogTable());
            liquibase.setDropFirst(properties.isDropFirst());
            liquibase.setLabels(properties.getLabels());
            liquibase.setChangeLogParameters(properties.getParameters());
            //liquibase.setRollbackFile(properties.getRollbackFile());
            //liquibase.setTestRollbackOnUpdate(properties.isTestRollbackOnUpdate());
            return liquibase;
        }
        return null;
    }
}
