package com.github.eug.msv.job;

import com.github.eug.msv.entity.ClusterCounter;
import com.github.eug.msv.repository.ClusterCounterRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.time.Instant;
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

    @Override
    @Transactional
    public void execute(JobExecutionContext context) throws JobExecutionException {
        ClusterCounter clusterCounter = clusterCounterRepository.findOne(1L);
        long nodeCounterResult = perNodeCounter.getAndIncrement();
        log.warn("InternalJob executed!!!!!!!!!! perNodeCounter={}, clusterCounter={}",
                nodeCounterResult,
                clusterCounter.getVersion());

        clusterCounter.setNodeCounter(nodeCounterResult);
        clusterCounterRepository.save(clusterCounter);
    }
}