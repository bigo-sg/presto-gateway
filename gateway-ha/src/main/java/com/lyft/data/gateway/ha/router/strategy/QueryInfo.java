package com.lyft.data.gateway.ha.router.strategy;

import lombok.Data;

/**
 * @author tangyun@bigo.sg
 * @date 10/31/19 8:30 PM
 */
@Data
public class QueryInfo {

  private Session session;
  private QueryState state;
}
