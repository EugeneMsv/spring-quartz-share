package com.github.eug.msv.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * InternalJob.
 *
 * @author Eugene_Moiseev
 */
@Slf4j
public class InternalJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.warn("InternalJob executed!!!!!!!!!!");
    }
}