package com.lyft.data.gateway.ha.router.strategy;

import com.lyft.data.proxyserver.ProxyServerConfiguration;
import lombok.Data;

import java.util.List;

/**
 * @author tangyun@bigo.sg
 * @date 11/1/19 11:14 AM
 */
@Data
public class ClusterInfo {
  private ClusterState clusterState;
  private List<QueryInfo> queryInfos;
  private ProxyServerConfiguration proxyServerConfiguration;
}
