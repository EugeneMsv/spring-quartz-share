package com.github.eug.msv.external;


import com.github.eug.msv.external.job.AutowiringSpringBeanJobFactory;
import com.github.eug.msv.external.job.ExternalJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

/**
 * ExternalQuartzConfig.
 *
 * @author Eugene_Moiseev
 */
@RequiredArgsConstructor
@Configuration
@Slf4j
public class ExternalQuartzConfig {
    private final DataSource dataSource;
    private final PlatformTransactionManager platformTransactionManager;
    private final ApplicationContext applicationContext;

    @PostConstruct
    public void init(){
        log.warn("Applied ExternalQuartzConfig");
    }

    @Bean
    public JobDetailFactoryBean externalJobDetail() {
        JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
        jobDetailFactory.setJobClass(ExternalJob.class);
        jobDetailFactory.setDurability(true);
        jobDetailFactory.setGroup("external-group");
        jobDetailFactory.setRequestsRecovery(true);
        return jobDetailFactory;
    }

    @Bean
    public SimpleTriggerFactoryBean externalSignatureJobTrigger(@Qualifier("externalJobDetail") JobDetail jobDetail) {
        SimpleTriggerFactoryBean simpleTriggerFactoryBean = new SimpleTriggerFactoryBean();
        simpleTriggerFactoryBean.setGroup("external-group");
        simpleTriggerFactoryBean.setJobDetail(jobDetail);
        simpleTriggerFactoryBean.setStartDelay(0);
        simpleTriggerFactoryBean.setRepeatInterval(10000);
        return simpleTriggerFactoryBean;
    }

    @ConditionalOnMissingBean
    @Bean
    public SchedulerFactoryBean externalSchedulerFactoryBeanDataEvent(Trigger[] triggers, JobDetail[] jobDetails) {
        SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
        schedulerFactory.setSchedulerName("externalJobScheduler");
        schedulerFactory.setApplicationContext(applicationContext);
        schedulerFactory.setDataSource(dataSource);
        schedulerFactory.setTransactionManager(platformTransactionManager);
        schedulerFactory.setJobFactory(externalSpringBeanJobFactory());
        schedulerFactory.setTriggers(triggers);
        schedulerFactory.setJobDetails(jobDetails);
        schedulerFactory.setOverwriteExistingJobs(true);
        schedulerFactory.setQuartzProperties(externalQuartzProperties());
        return schedulerFactory;
    }

    @ConditionalOnMissingBean
    @Bean
    public SpringBeanJobFactory externalSpringBeanJobFactory() {
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }

    @ConditionalOnMissingBean
    @Bean
    public Properties externalQuartzProperties() {
        log.warn("externalQuartzProperties");
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("external-quartz.properties"));

        try {
            propertiesFactoryBean.afterPropertiesSet();
            Properties properties = propertiesFactoryBean.getObject();
            properties.setProperty("org.quartz.scheduler.instanceName", applicationContext.getApplicationName());
            return properties;
        } catch (IOException var4) {
            throw new IllegalArgumentException("Unable to load quartz.properties", var4);
        }
    }

}