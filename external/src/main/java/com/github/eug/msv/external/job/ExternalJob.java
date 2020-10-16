package com.github.eug.msv.external.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * ExternalJob.
 *
 * @author Eugene_Moiseev
 */
@Slf4j
public class ExternalJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.warn("ExternalJob executed!!!!!!!!!!");
    }
}