package com.github.eug.msv;

import com.github.eug.msv.external.job.ExternalJob;
import com.github.eug.msv.job.AutowiringSpringBeanJobFactory;
import com.github.eug.msv.job.InternalJob;
import com.github.eug.msv.job.InternalJobFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
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
 * InternalQuartzConfig.
 *
 * @author Eugene_Moiseev
 */
@RequiredArgsConstructor
@Configuration
@Slf4j
public class InternalQuartzConfig {
    private final DataSource dataSource;
    private final PlatformTransactionManager platformTransactionManager;
    private final ApplicationContext applicationContext;

    @PostConstruct
    public void init(){
        log.warn("Applied InternalQuartzConfig");
    }

    @Bean
    public JobDetailFactoryBean internalJobDetail() {
        JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
        jobDetailFactory.setJobClass(InternalJob.class);
        jobDetailFactory.setDurability(true);
        jobDetailFactory.setRequestsRecovery(true);
        return jobDetailFactory;
    }

    @Bean
    public SimpleTriggerFactoryBean internalSignatureJobTrigger(@Qualifier("internalJobDetail") JobDetail jobDetail) {
        SimpleTriggerFactoryBean simpleTriggerFactoryBean = new SimpleTriggerFactoryBean();
        simpleTriggerFactoryBean.setJobDetail(jobDetail);
        simpleTriggerFactoryBean.setStartDelay(0);
        simpleTriggerFactoryBean.setRepeatInterval(30000);
        return simpleTriggerFactoryBean;
    }

/*    @ConditionalOnMissingBean(SchedulerFactoryBean.class)
    @Bean
    public SchedulerFactoryBean schedulerFactoryBeanDataEvent(Trigger[] triggers, JobDetail[] jobDetails) {
        SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
        schedulerFactory.setSchedulerName("internalJobScheduler");
        schedulerFactory.setApplicationContext(applicationContext);
        schedulerFactory.setDataSource(dataSource);
        schedulerFactory.setTransactionManager(platformTransactionManager);
        schedulerFactory.setJobFactory(springBeanJobFactory());
        schedulerFactory.setTriggers(triggers);
        schedulerFactory.setJobDetails(jobDetails);
        schedulerFactory.setOverwriteExistingJobs(true);
        schedulerFactory.setQuartzProperties(quartzProperties());
        return schedulerFactory;
    }

    @ConditionalOnMissingBean(SpringBeanJobFactory.class)
    @Bean
    public SpringBeanJobFactory springBeanJobFactory() {
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }*/

    @Bean
    public Properties quartzProperties() {
        log.warn("quartzProperties");
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("internal-quartz.properties"));

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