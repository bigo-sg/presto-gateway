package com.lyft.data.gateway.ha.router.strategy;

import com.lyft.data.gateway.ha.config.ProxyBackendConfiguration;
import com.lyft.data.proxyserver.ProxyServerConfiguration;
import org.junit.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tangyun@bigo.sg
 * @date 11/1/19 4:03 PM
 */
public class TestResourceBasedRouter {

  @Test
  public void testResourceBasedSelector1() {
    List<ClusterInfo> clusterInfos = new ArrayList<>();
    ClusterInfo clusterInfo = new ClusterInfo();
    ProxyServerConfiguration proxyServerConfiguration = new ProxyBackendConfiguration();
    proxyServerConfiguration.setProxyTo("ClusterA");
    clusterInfo.setProxyServerConfiguration(proxyServerConfiguration);
    ClusterState clusterState = new ClusterState();
    clusterInfo.setClusterState(clusterState);
    List<QueryInfo> queryInfos = new ArrayList<>();
    QueryInfo queryInfo = new QueryInfo();
    queryInfo.setState(QueryState.RUNNING);
    Session session = new Session();
    session.setSource("s1");
    session.setUser("u1");
    queryInfo.setSession(session);
    queryInfos.add(queryInfo);
    clusterInfo.setQueryInfos(queryInfos);
    clusterInfos.add(clusterInfo);

    clusterInfo = new ClusterInfo();
    proxyServerConfiguration = new ProxyBackendConfiguration();
    proxyServerConfiguration.setProxyTo("ClusterB");
    clusterInfo.setProxyServerConfiguration(proxyServerConfiguration);
    clusterState = new ClusterState();
    clusterInfo.setClusterState(clusterState);
    queryInfos = new ArrayList<>();
    queryInfo = new QueryInfo();
    queryInfo.setState(QueryState.RUNNING);
    session = new Session();
    session.setSource("s2");
    session.setUser("u2");
    queryInfo.setSession(session);
    queryInfos.add(queryInfo);
    clusterInfo.setQueryInfos(queryInfos);
    clusterInfos.add(clusterInfo);

    QueryHeader queryHeader = new QueryHeader();
    queryHeader.setSource("s1");
    queryHeader.setUser("u1");
    String s = new ResourceBasedRouter(null, null)
        .resourceBasedSelector(clusterInfos, queryHeader);
    Assert.assertEquals(s, "ClusterA");
  }

  @Test
  public void testResourceBasedSelector2() {
    List<ClusterInfo> clusterInfos = new ArrayList<>();
    ClusterInfo clusterInfo = new ClusterInfo();
    ProxyServerConfiguration proxyServerConfiguration = new ProxyBackendConfiguration();
    proxyServerConfiguration.setProxyTo("ClusterA");
    clusterInfo.setProxyServerConfiguration(proxyServerConfiguration);
    ClusterState clusterState = new ClusterState();
    clusterState.setRunningQueries(1);
    clusterInfo.setClusterState(clusterState);
    List<QueryInfo> queryInfos = new ArrayList<>();
    QueryInfo queryInfo = new QueryInfo();
    queryInfo.setState(QueryState.RUNNING);
    Session session = new Session();
    session.setSource("s1");
    session.setUser("u1");
    queryInfo.setSession(session);
    queryInfos.add(queryInfo);
    clusterInfo.setQueryInfos(queryInfos);
    clusterInfos.add(clusterInfo);

    clusterInfo = new ClusterInfo();
    proxyServerConfiguration = new ProxyBackendConfiguration();
    proxyServerConfiguration.setProxyTo("ClusterB");
    clusterInfo.setProxyServerConfiguration(proxyServerConfiguration);
    clusterState = new ClusterState();
    clusterInfo.setClusterState(clusterState);
    queryInfos = new ArrayList<>();
    queryInfo = new QueryInfo();
    queryInfo.setState(QueryState.RUNNING);
    session = new Session();
    session.setSource("s2");
    session.setUser("u2");
    queryInfo.setSession(session);
    queryInfos.add(queryInfo);
    clusterInfo.setQueryInfos(queryInfos);
    clusterInfos.add(clusterInfo);

    QueryHeader queryHeader = new QueryHeader();
    queryHeader.setSource("s3");
    queryHeader.setUser("u3");
    String s = new ResourceBasedRouter(null, null)
        .resourceBasedSelector(clusterInfos, queryHeader);
    Assert.assertEquals(s, "ClusterB");
  }

  @Test
  public void testResourceBasedSelector3() {
    List<ClusterInfo> clusterInfos = new ArrayList<>();
    ClusterInfo clusterInfo = new ClusterInfo();
    ProxyServerConfiguration proxyServerConfiguration = new ProxyBackendConfiguration();
    proxyServerConfiguration.setProxyTo("ClusterA");
    clusterInfo.setProxyServerConfiguration(proxyServerConfiguration);
    ClusterState clusterState = new ClusterState();
    clusterState.setRunningDrivers(1000);
    clusterInfo.setClusterState(clusterState);
    List<QueryInfo> queryInfos = new ArrayList<>();
    QueryInfo queryInfo = new QueryInfo();
    queryInfo.setState(QueryState.RUNNING);
    Session session = new Session();
    session.setSource("s1");
    session.setUser("u1");
    queryInfo.setSession(session);
    queryInfos.add(queryInfo);
    clusterInfo.setQueryInfos(queryInfos);
    clusterInfos.add(clusterInfo);

    clusterInfo = new ClusterInfo();
    proxyServerConfiguration = new ProxyBackendConfiguration();
    proxyServerConfiguration.setProxyTo("ClusterB");
    clusterInfo.setProxyServerConfiguration(proxyServerConfiguration);
    clusterState = new ClusterState();
    clusterInfo.setClusterState(clusterState);
    queryInfos = new ArrayList<>();
    queryInfo = new QueryInfo();
    queryInfo.setState(QueryState.RUNNING);
    session = new Session();
    session.setSource("s2");
    session.setUser("u2");
    queryInfo.setSession(session);
    queryInfos.add(queryInfo);
    clusterInfo.setQueryInfos(queryInfos);
    clusterInfos.add(clusterInfo);

    QueryHeader queryHeader = new QueryHeader();
    queryHeader.setSource("s3");
    queryHeader.setUser("u3");
    String s = new ResourceBasedRouter(null, null)
        .resourceBasedSelector(clusterInfos, queryHeader);
    Assert.assertEquals(s, "ClusterB");
  }

  @Test
  public void testResourceBasedSelector4() {
    List<ClusterInfo> clusterInfos = new ArrayList<>();
    ClusterInfo clusterInfo = new ClusterInfo();
    ProxyServerConfiguration proxyServerConfiguration = new ProxyBackendConfiguration();
    proxyServerConfiguration.setProxyTo("ClusterA");
    clusterInfo.setProxyServerConfiguration(proxyServerConfiguration);
    ClusterState clusterState = new ClusterState();
    clusterState.setRunningDrivers(1000);
    clusterInfo.setClusterState(clusterState);
    List<QueryInfo> queryInfos = new ArrayList<>();
    QueryInfo queryInfo = new QueryInfo();
    queryInfo.setState(QueryState.RUNNING);
    Session session = new Session();
    session.setSource("s1");
    session.setUser("u1");
    queryInfo.setSession(session);
    queryInfos.add(queryInfo);
    clusterInfo.setQueryInfos(queryInfos);
    clusterInfos.add(clusterInfo);

    clusterInfo = new ClusterInfo();
    proxyServerConfiguration = new ProxyBackendConfiguration();
    proxyServerConfiguration.setProxyTo("ClusterB");
    clusterInfo.setProxyServerConfiguration(proxyServerConfiguration);
    clusterState = new ClusterState();
    clusterState.setRunningDrivers(1001);
    clusterInfo.setClusterState(clusterState);
    queryInfos = new ArrayList<>();
    queryInfo = new QueryInfo();
    queryInfo.setState(QueryState.RUNNING);
    session = new Session();
    session.setSource("s2");
    session.setUser("u2");
    queryInfo.setSession(session);
    queryInfos.add(queryInfo);
    clusterInfo.setQueryInfos(queryInfos);
    clusterInfos.add(clusterInfo);

    QueryHeader queryHeader = new QueryHeader();
    queryHeader.setSource("s3");
    queryHeader.setUser("u3");
    String s = new ResourceBasedRouter(null, null)
        .resourceBasedSelector(clusterInfos, queryHeader);
    Assert.assertEquals(s, "ClusterA");
  }

  @Test
  public void testResourceBasedSelector5() {
    List<ClusterInfo> clusterInfos = new ArrayList<>();
    ClusterInfo clusterInfo = new ClusterInfo();
    ProxyServerConfiguration proxyServerConfiguration = new ProxyBackendConfiguration();
    proxyServerConfiguration.setProxyTo("ClusterA");
    clusterInfo.setProxyServerConfiguration(proxyServerConfiguration);
    ClusterState clusterState = new ClusterState();
    clusterState.setRunningDrivers(1000);
    clusterState.setQueuedQueries(10);
    clusterInfo.setClusterState(clusterState);
    List<QueryInfo> queryInfos = new ArrayList<>();
    QueryInfo queryInfo = new QueryInfo();
    queryInfo.setState(QueryState.RUNNING);
    Session session = new Session();
    session.setSource("s1");
    session.setUser("u1");
    queryInfo.setSession(session);
    queryInfos.add(queryInfo);
    clusterInfo.setQueryInfos(queryInfos);
    clusterInfos.add(clusterInfo);

    clusterInfo = new ClusterInfo();
    proxyServerConfiguration = new ProxyBackendConfiguration();
    proxyServerConfiguration.setProxyTo("ClusterB");
    clusterInfo.setProxyServerConfiguration(proxyServerConfiguration);
    clusterState = new ClusterState();
    clusterState.setRunningDrivers(1000);
    clusterState.setQueuedQueries(11);
    clusterInfo.setClusterState(clusterState);
    queryInfos = new ArrayList<>();
    queryInfo = new QueryInfo();
    queryInfo.setState(QueryState.RUNNING);
    session = new Session();
    session.setSource("s2");
    session.setUser("u2");
    queryInfo.setSession(session);
    queryInfos.add(queryInfo);
    clusterInfo.setQueryInfos(queryInfos);
    clusterInfos.add(clusterInfo);

    QueryHeader queryHeader = new QueryHeader();
    queryHeader.setSource("s3");
    queryHeader.setUser("u3");
    String s = new ResourceBasedRouter(null, null)
        .resourceBasedSelector(clusterInfos, queryHeader);
    Assert.assertEquals(s, "ClusterA");
  }

  @Test
  public void testResourceBasedSelector6() {
    List<ClusterInfo> clusterInfos = new ArrayList<>();
    ClusterInfo clusterInfo = new ClusterInfo();
    ProxyServerConfiguration proxyServerConfiguration = new ProxyBackendConfiguration();
    proxyServerConfiguration.setProxyTo("ClusterA");
    clusterInfo.setProxyServerConfiguration(proxyServerConfiguration);
    ClusterState clusterState = new ClusterState();
    clusterState.setRunningDrivers(1000);
    clusterState.setQueuedQueries(12);
    clusterInfo.setClusterState(clusterState);
    List<QueryInfo> queryInfos = new ArrayList<>();
    QueryInfo queryInfo = new QueryInfo();
    queryInfo.setState(QueryState.RUNNING);
    Session session = new Session();
    session.setSource("s1");
    session.setUser("u1");
    queryInfo.setSession(session);
    queryInfos.add(queryInfo);
    clusterInfo.setQueryInfos(queryInfos);
    clusterInfos.add(clusterInfo);

    clusterInfo = new ClusterInfo();
    proxyServerConfiguration = new ProxyBackendConfiguration();
    proxyServerConfiguration.setProxyTo("ClusterB");
    clusterInfo.setProxyServerConfiguration(proxyServerConfiguration);
    clusterState = new ClusterState();
    clusterState.setRunningDrivers(1000);
    clusterState.setQueuedQueries(11);
    clusterInfo.setClusterState(clusterState);
    queryInfos = new ArrayList<>();
    queryInfo = new QueryInfo();
    queryInfo.setState(QueryState.RUNNING);
    session = new Session();
    session.setSource("s2");
    session.setUser("u2");
    queryInfo.setSession(session);
    queryInfos.add(queryInfo);
    clusterInfo.setQueryInfos(queryInfos);
    clusterInfos.add(clusterInfo);

    QueryHeader queryHeader = new QueryHeader();
    queryHeader.setSource("s3");
    queryHeader.setUser("u3");
    String s = new ResourceBasedRouter(null, null)
        .resourceBasedSelector(clusterInfos, queryHeader);
    Assert.assertEquals(s, "ClusterB");
  }

  @Test
  public void testResourceBasedSelector7() {
    List<ClusterInfo> clusterInfos = new ArrayList<>();
    ClusterInfo clusterInfo = new ClusterInfo();
    ProxyServerConfiguration proxyServerConfiguration = new ProxyBackendConfiguration();
    proxyServerConfiguration.setProxyTo("ClusterA");
    clusterInfo.setProxyServerConfiguration(proxyServerConfiguration);
    ClusterState clusterState = new ClusterState();
    clusterState.setRunningDrivers(1000);
    clusterState.setQueuedQueries(11);
    clusterState.setReservedMemory(1000000);
    clusterInfo.setClusterState(clusterState);
    List<QueryInfo> queryInfos = new ArrayList<>();
    QueryInfo queryInfo = new QueryInfo();
    queryInfo.setState(QueryState.RUNNING);
    Session session = new Session();
    session.setSource("s1");
    session.setUser("u1");
    queryInfo.setSession(session);
    queryInfos.add(queryInfo);
    clusterInfo.setQueryInfos(queryInfos);
    clusterInfos.add(clusterInfo);

    clusterInfo = new ClusterInfo();
    proxyServerConfiguration = new ProxyBackendConfiguration();
    proxyServerConfiguration.setProxyTo("ClusterB");
    clusterInfo.setProxyServerConfiguration(proxyServerConfiguration);
    clusterState = new ClusterState();
    clusterState.setRunningDrivers(1000);
    clusterState.setQueuedQueries(11);
    clusterInfo.setClusterState(clusterState);
    queryInfos = new ArrayList<>();
    queryInfo = new QueryInfo();
    queryInfo.setState(QueryState.RUNNING);
    session = new Session();
    session.setSource("s2");
    session.setUser("u2");
    queryInfo.setSession(session);
    queryInfos.add(queryInfo);
    clusterInfo.setQueryInfos(queryInfos);
    clusterInfos.add(clusterInfo);

    QueryHeader queryHeader = new QueryHeader();
    queryHeader.setSource("s3");
    queryHeader.setUser("u3");
    String s = new ResourceBasedRouter(null, null)
        .resourceBasedSelector(clusterInfos, queryHeader);
    Assert.assertEquals(s, "ClusterB");
  }

  @Test
  public void testResourceBasedSelector8() {
    List<ClusterInfo> clusterInfos = new ArrayList<>();
    ClusterInfo clusterInfo = new ClusterInfo();
    ProxyServerConfiguration proxyServerConfiguration = new ProxyBackendConfiguration();
    proxyServerConfiguration.setProxyTo("ClusterA");
    clusterInfo.setProxyServerConfiguration(proxyServerConfiguration);
    ClusterState clusterState = new ClusterState();
    clusterState.setRunningDrivers(1000);
    clusterState.setRunningQueries(11);
    clusterState.setQueuedQueries(10);
    clusterState.setReservedMemory(1000000);
    clusterInfo.setClusterState(clusterState);
    List<QueryInfo> queryInfos = new ArrayList<>();
    QueryInfo queryInfo = new QueryInfo();
    queryInfo.setState(QueryState.RUNNING);
    Session session = new Session();
    session.setSource("s1");
    session.setUser("u1");
    queryInfo.setSession(session);
    queryInfos.add(queryInfo);
    clusterInfo.setQueryInfos(queryInfos);
    clusterInfos.add(clusterInfo);

    clusterInfo = new ClusterInfo();
    proxyServerConfiguration = new ProxyBackendConfiguration();
    proxyServerConfiguration.setProxyTo("ClusterB");
    clusterInfo.setProxyServerConfiguration(proxyServerConfiguration);
    clusterState = new ClusterState();
    clusterState.setRunningDrivers(1000);
    clusterState.setRunningQueries(10);
    clusterState.setQueuedQueries(14);
    clusterInfo.setClusterState(clusterState);
    queryInfos = new ArrayList<>();
    queryInfo = new QueryInfo();
    queryInfo.setState(QueryState.RUNNING);
    session = new Session();
    session.setSource("s2");
    session.setUser("u2");
    queryInfo.setSession(session);
    queryInfos.add(queryInfo);
    clusterInfo.setQueryInfos(queryInfos);
    clusterInfos.add(clusterInfo);

    QueryHeader queryHeader = new QueryHeader();
    queryHeader.setSource("s3");
    queryHeader.setUser("u3");
    String s = new ResourceBasedRouter(null, null)
        .resourceBasedSelector(clusterInfos, queryHeader);
    Assert.assertEquals(s, "ClusterA");
  }

  @Test
  public void testResourceBasedSelector9() {
    List<ClusterInfo> clusterInfos = new ArrayList<>();
    ClusterInfo clusterInfo = new ClusterInfo();
    ProxyServerConfiguration proxyServerConfiguration = new ProxyBackendConfiguration();
    proxyServerConfiguration.setProxyTo("ClusterA");
    clusterInfo.setProxyServerConfiguration(proxyServerConfiguration);
    ClusterState clusterState = new ClusterState();
    clusterState.setRunningDrivers(1000);
    clusterState.setRunningQueries(12);
    clusterState.setQueuedQueries(10);
    clusterState.setReservedMemory(1000000);
    clusterInfo.setClusterState(clusterState);
    List<QueryInfo> queryInfos = new ArrayList<>();
    QueryInfo queryInfo = new QueryInfo();
    queryInfo.setState(QueryState.RUNNING);
    Session session = new Session();
    session.setSource("s1");
    session.setUser("u1");
    queryInfo.setSession(session);
    queryInfos.add(queryInfo);
    clusterInfo.setQueryInfos(queryInfos);
    clusterInfos.add(clusterInfo);

    clusterInfo = new ClusterInfo();
    proxyServerConfiguration = new ProxyBackendConfiguration();
    proxyServerConfiguration.setProxyTo("ClusterB");
    clusterInfo.setProxyServerConfiguration(proxyServerConfiguration);
    clusterState = new ClusterState();
    clusterState.setRunningDrivers(1000);
    clusterState.setRunningQueries(10);
    clusterState.setQueuedQueries(14);
    clusterInfo.setClusterState(clusterState);
    queryInfos = new ArrayList<>();
    queryInfo = new QueryInfo();
    queryInfo.setState(QueryState.RUNNING);
    session = new Session();
    session.setSource("s2");
    session.setUser("u2");
    queryInfo.setSession(session);
    queryInfos.add(queryInfo);
    clusterInfo.setQueryInfos(queryInfos);
    clusterInfos.add(clusterInfo);

    QueryHeader queryHeader = new QueryHeader();
    queryHeader.setSource("s3");
    queryHeader.setUser("u3");
    String s = new ResourceBasedRouter(null, null)
        .resourceBasedSelector(clusterInfos, queryHeader);
    Assert.assertEquals(s, "ClusterB");
  }
}
