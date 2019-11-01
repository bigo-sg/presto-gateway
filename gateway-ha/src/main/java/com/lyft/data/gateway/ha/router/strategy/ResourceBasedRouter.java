package com.lyft.data.gateway.ha.router.strategy;

import com.google.common.base.Strings;
import com.lyft.data.gateway.ha.config.ProxyBackendConfiguration;
import com.lyft.data.gateway.ha.router.GatewayBackendManager;
import com.lyft.data.gateway.ha.router.HaQueryHistoryManager;
import com.lyft.data.gateway.ha.router.RoutingManager;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author tangyun@bigo.sg
 * @date 10/31/19 4:21 PM
 */
@Slf4j
public class ResourceBasedRouter extends RoutingManager {
    private HaQueryHistoryManager queryHistoryManager;
    private GatewayBackendManager gatewayBackendManager;
    private static final Random RANDOM = new Random();

    public ResourceBasedRouter(GatewayBackendManager gatewayBackendManager,
                               HaQueryHistoryManager queryHistoryManager) {
        super(gatewayBackendManager);
        this.queryHistoryManager = queryHistoryManager;
        this.gatewayBackendManager = gatewayBackendManager;
    }
    @Override
    protected String findBackendForUnknownQueryId(String queryId) {
        String backend;
        backend = queryHistoryManager.getBackendForQueryId(queryId);
        if (Strings.isNullOrEmpty(backend)) {
            backend = super.findBackendForUnknownQueryId(queryId);
        }
        return backend;
    }
    /**
     * Performs routing to a given cluster group. This falls back to an adhoc backend, if no scheduled
     * backend is found.
     * @return
     */
    public String provideBackendForHeader(QueryHeader queryHeader) {
        List<ProxyBackendConfiguration> backends =
                gatewayBackendManager.getActiveBackends(queryHeader.getRoutingGroup());
        if (backends.isEmpty()) {
            return provideAdhocBackend();
        }
        List<ClusterInfo> clusterInfos = ClusterUtils.getClusterInfosBeforeFinished(backends);
        return resourceBasedSelector(clusterInfos, queryHeader);
    }

    public String resourceBasedSelector(List<ClusterInfo> clusterInfos, QueryHeader queryHeader) {
        // if source+user have a sql running on cluster A, A will comes first
        for (ClusterInfo clusterInfo: clusterInfos) {
            for (QueryInfo queryInfo: clusterInfo.getQueryInfos()) {
                if (queryInfo.getSession().getSource().equals(queryHeader.getSource())
                        && queryInfo.getSession().getUser().equals(queryHeader.getUser())) {
                    return clusterInfo.getProxyServerConfiguration().getProxyTo();
                }
            }
        }

        // less running query clusters comes first
        List<ClusterInfo> selectedClusters = new ClusterSelector() {
            @Override
            int compare(ClusterInfo clusterInfo, long baseLine) {
                if (clusterInfo.getClusterState().getRunningQueries() < baseLine) {
                    return 1;
                } else if (clusterInfo.getClusterState().getRunningQueries() == baseLine) {
                    return 0;
                }
                return -1;
            }

            @Override
            long selectedBaseLine(ClusterInfo clusterInfo) {
                return clusterInfo.getClusterState().getRunningQueries();
            }
        }.select(clusterInfos);
        if (selectedClusters.size() == 1) {
            return selectedClusters.get(0).getProxyServerConfiguration().getProxyTo();
        }

        // less runnable drivers clusters comes first
        selectedClusters = new ClusterSelector() {
            @Override
            int compare(ClusterInfo clusterInfo, long baseLine) {
                if (clusterInfo.getClusterState().getRunningDrivers() < baseLine) {
                    return 1;
                } else if (clusterInfo.getClusterState().getRunningDrivers() == baseLine) {
                    return 0;
                }
                return -1;
            }

            @Override
            long selectedBaseLine(ClusterInfo clusterInfo) {
                return clusterInfo.getClusterState().getRunningDrivers();
            }
        }.select(selectedClusters);
        if (selectedClusters.size() == 1) {
            return selectedClusters.get(0).getProxyServerConfiguration().getProxyTo();
        }

        // less queued query clusters comes first
        selectedClusters = new ClusterSelector() {
            @Override
            int compare(ClusterInfo clusterInfo, long baseLine) {
                if (clusterInfo.getClusterState().getQueuedQueries() < baseLine) {
                    return 1;
                } else if (clusterInfo.getClusterState().getQueuedQueries() == baseLine) {
                    return 0;
                }
                return -1;
            }

            @Override
            long selectedBaseLine(ClusterInfo clusterInfo) {
                return clusterInfo.getClusterState().getQueuedQueries();
            }
        }.select(selectedClusters);
        if (selectedClusters.size() == 1) {
            return selectedClusters.get(0).getProxyServerConfiguration().getProxyTo();
        }

        // less reserved memory clusters comes first
        selectedClusters = new ClusterSelector() {
            @Override
            int compare(ClusterInfo clusterInfo, long baseLine) {
                if (clusterInfo.getClusterState().getReservedMemory() < baseLine) {
                    return 1;
                } else if (clusterInfo.getClusterState().getReservedMemory() == baseLine) {
                    return 0;
                }
                return -1;
            }

            @Override
            long selectedBaseLine(ClusterInfo clusterInfo) {
                return clusterInfo.getClusterState().getReservedMemory();
            }
        }.select(selectedClusters);
        if (selectedClusters.size() == 1) {
            return selectedClusters.get(0).getProxyServerConfiguration().getProxyTo();
        } else if (selectedClusters.size() > 1) {
            int backendId = Math.abs(RANDOM.nextInt()) % selectedClusters.size();
            return selectedClusters.get(backendId).getProxyServerConfiguration().getProxyTo();
        }
        // can not find any proper cluster
        return provideAdhocBackend();
    }

    public abstract class ClusterSelector {
        public List<ClusterInfo> select(List<ClusterInfo> clusterInfos) {
            List<ClusterInfo> selectedClusters = new ArrayList<>();
            long baseLine = -1;
            for (ClusterInfo clusterInfo: clusterInfos) {
                if (compare(clusterInfo, baseLine) == 1 || baseLine == -1) {
                    selectedClusters.clear();
                    selectedClusters.add(clusterInfo);
                    baseLine = selectedBaseLine(clusterInfo);
                } else if (compare(clusterInfo, baseLine) == 0) {
                    selectedClusters.add(clusterInfo);
                }
            }
            return selectedClusters;
        }

        abstract int compare(ClusterInfo clusterInfo, long baseLine);

        abstract long selectedBaseLine(ClusterInfo clusterInfo);
    }
}
