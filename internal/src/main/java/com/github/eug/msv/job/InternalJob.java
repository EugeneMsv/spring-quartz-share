package com.github.eug.msv.job;

import com.github.eug.msv.entity.ClusterCounter;
import com.github.eug.msv.repository.ClusterCounterRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.atomic.AtomicLong;

/**
 * InternalJob.
 *
 * @author Eugene_Moiseev
 */
@RequiredArgsConstructor
@Slf4j
public class InternalJob implements Job {

    private static AtomicLong perNodeCounter = new AtomicLong(0);

    @Autowired
    private ClusterCounterRepository clusterCounterRepository;

    @Value("${service.job.internal.time}")
    private int jobEmulationTime;

    @Override
    @Transactional
    @SneakyThrows
    public void execute(JobExecutionContext context) throws JobExecutionException {
        long nodeCounterResult = perNodeCounter.incrementAndGet();
        ClusterCounter clusterCounter = clusterCounterRepository.findOne(1L);

        log.warn("InternalJob starting perNodeCounter={}, clusterCounter={}",
                nodeCounterResult, clusterCounter.getVersion());

        Thread.sleep(jobEmulationTime);

        log.warn("InternalJob finished perNodeCounter={}, clusterCounter={}",
                nodeCounterResult, clusterCounter.getVersion());

        clusterCounter.setNodeCounter(nodeCounterResult);
        clusterCounterRepository.save(clusterCounter);
    }
}