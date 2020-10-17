package com.github.eug.msv.external.job;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.concurrent.atomic.AtomicLong;

/**
 * ExternalJob.
 *
 * @author Eugene_Moiseev
 */
@Slf4j
public class ExternalJob implements Job {

    private static AtomicLong perNodeCounter = new AtomicLong(0);

    @Override
    @SneakyThrows
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.warn("ExternalJob executed!!!!!!!!!! perNodeCounter={}", perNodeCounter.getAndIncrement());
    }
}