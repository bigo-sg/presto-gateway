package com.lyft.data.gateway.ha.router.strategy;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lyft.data.gateway.ha.config.ProxyBackendConfiguration;
import com.lyft.data.gateway.ha.utils.HttpClientCreator;
import com.lyft.data.proxyserver.ProxyServerConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author tangyun@bigo.sg
 * @date 10/31/19 3:13 PM
 */
@Slf4j
public class ClusterUtils {

  public static String getPrestoData(String url) {
    HttpClient httpClient = HttpClientCreator.getHttpClient();
    HttpGet httpGet = new HttpGet(url);
    httpGet.setHeader("Accept", "application/json");
    try {
      HttpResponse httpResponse = httpClient.execute(httpGet);
      String response = EntityUtils.toString(httpResponse.getEntity());
      return response;
    } catch (IOException e) {
      log.error("failed to request url {}", url, e);
    }
    return null;
  }

  public static ClusterState getClusterState(ProxyServerConfiguration proxyServerConfiguration) {

    String url = proxyServerConfiguration.getProxyTo() + "/v1/cluster";
    String data = getPrestoData(url);
    ObjectMapper mapper = new ObjectMapper();
    ClusterState clusterState = null;
    if (data == null) {
      return clusterState;
    }
    try {
      clusterState = mapper.readValue(data, ClusterState.class);
    } catch (IOException e) {
      log.error("parser error ", e);
    }
    return clusterState;
  }

  public static List<QueryInfo> getQueryInfo(ProxyServerConfiguration proxyServerConfiguration,
                                             List<QueryState> queryStates) {
    if (queryStates.size() == 0) {
      return new ArrayList<>();
    }
    List<QueryInfo> queryInfos = getQueryInfo(proxyServerConfiguration, queryStates.get(0));
    for (int i = 1; i < queryStates.size(); ++i) {
      List<QueryInfo> subQueryInfos = getQueryInfo(proxyServerConfiguration, queryStates.get(i));
      if (subQueryInfos != null) {
        queryInfos.addAll(subQueryInfos);
      } else {
        return null;
      }
    }
    return queryInfos;
  }

  private static List<QueryInfo> getQueryInfo(ProxyServerConfiguration proxyServerConfiguration,
                                              QueryState queryState) {
    String url = proxyServerConfiguration.getProxyTo() + "/v1/query?state=" + queryState;
    String data = getPrestoData(url);
    ObjectMapper mapper = new ObjectMapper();
    JsonNode jsonNode = null;
    List<QueryInfo> queryInfos = new ArrayList<>();
    if (data == null) {
      return null;
    }
    try {
      jsonNode = mapper.readTree(data);
    } catch (IOException e) {
      e.printStackTrace();
    }
    jsonNode.forEach(new Consumer<JsonNode>() {
      @Override
      public void accept(JsonNode jsonNode) {
        QueryInfo queryInfo = new QueryInfo();
        QueryState queryState1 = QueryState.valueOf(jsonNode.get("state").asText());
        Session session = new Session();
        JsonNode sessionNode = jsonNode.get("session");
        session.setCatalog(sessionNode.get("catalog").asText());
        session.setQueryId(sessionNode.get("queryId").asText());
        session.setSource(sessionNode.get("source").asText());
        session.setUser(sessionNode.get("user").asText());
        session.setUserAgent(sessionNode.get("userAgent").asText());
        queryInfo.setSession(session);
        queryInfo.setState(queryState1);
        queryInfos.add(queryInfo);
      }
    });
    return queryInfos;
  }

  public static ClusterInfo getClusterInfoBeforeFinished(ProxyServerConfiguration proxyServerConfiguration) {
    List<QueryState> queryStates = new ArrayList<>();
    queryStates.add(QueryState.QUEUED);
    queryStates.add(QueryState.WAITING_FOR_RESOURCES);
    queryStates.add(QueryState.DISPATCHING);
    queryStates.add(QueryState.PLANNING);
    queryStates.add(QueryState.STARTING);
    queryStates.add(QueryState.RUNNING);
    queryStates.add(QueryState.FINISHING);

    List<QueryInfo> queryInfos = getQueryInfo(proxyServerConfiguration, queryStates);
    ClusterState clusterState = getClusterState(proxyServerConfiguration);
    ClusterInfo clusterInfo = new ClusterInfo();
    if (clusterState == null) {
      clusterInfo.setAlive(false);
    } else {
      clusterInfo.setAlive(true);
    }
    clusterInfo.setClusterState(clusterState);
    clusterInfo.setProxyServerConfiguration(proxyServerConfiguration);
    clusterInfo.setQueryInfos(queryInfos);
    return clusterInfo;
  }

  public static List<ClusterInfo> getClusterInfosBeforeFinished(List<ProxyBackendConfiguration> proxyServerConfigurations) {
    List<ClusterInfo> clusterInfos = new ArrayList<>();
    for (ProxyServerConfiguration proxyServerConfiguration : proxyServerConfigurations) {
      ClusterInfo clusterInfo = getClusterInfoBeforeFinished(proxyServerConfiguration);
      // maybe cluster is not alive
      if (clusterInfo.isAlive()) {
        clusterInfos.add(clusterInfo);
      }
    }
    return clusterInfos;
  }

}
