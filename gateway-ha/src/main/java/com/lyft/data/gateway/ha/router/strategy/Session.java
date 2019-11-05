package com.lyft.data.gateway.ha.router.strategy;

import lombok.Data;

import java.util.List;

/**
 * @author tangyun@bigo.sg
 * @date 10/31/19 8:35 PM
 */
@Data
public class Session {
  private String queryId;
  private String user;
  private String source;
  private String catalog;
  private String schema;
  private String remoteUserAddress;
  private String userAgent;
  private List<String> clientTags;
  private long startTime;
}
