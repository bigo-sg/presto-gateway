package com.lyft.data.gateway.ha.router.strategy;

import lombok.Data;

/**
 * @author tangyun@bigo.sg
 * @date 11/1/19 11:49 AM
 */
@Data
public class QueryHeader {
    private String user;
    private String source;
    private String routingGroup;
}
