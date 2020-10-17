package com.github.eug.msv.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;
import java.time.Instant;

@Getter
@Entity
public class ClusterCounter {

    @Id
    private Long id;

    @Version
    private Long version;

    @Setter
    private Long nodeCounter;
}