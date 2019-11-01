package com.lyft.data.gateway.ha.router.strategy;

import lombok.Data;

/**
 * @author tangyun@bigo.sg
 * @date 10/31/19 3:08 PM
 */
@Data
public class ClusterState {
  private long runningQueries;
  private long blockedQueries;
  private long queuedQueries;
  private long activeCoordinators;
  private long activeWorkers;
  private long runningDrivers;
  private long totalAvailableProcessors;
  private long reservedMemory;
  private long totalInputRows;
  private long totalInputBytes;
  private long totalCpuTimeSecs;
}
