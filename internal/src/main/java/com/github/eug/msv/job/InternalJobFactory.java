package com.github.eug.msv.job;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

/**
 * InternalJobFactory.
 *
 * @author Eugene_Moiseev
 */
public class InternalJobFactory extends SpringBeanJobFactory {

    protected InternalJob createJobInstance(TriggerFiredBundle bundle) throws Exception {
        return new InternalJob();
    }

}