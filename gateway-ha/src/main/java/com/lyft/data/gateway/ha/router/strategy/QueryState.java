package com.lyft.data.gateway.ha.router.strategy;

import lombok.AllArgsConstructor;

/**
 * @author tangyun@bigo.sg
 * @date 10/31/19 8:41 PM
 */
@AllArgsConstructor
public enum QueryState {
    /**
     * Query has been accepted and is awaiting execution.
     */
    QUEUED(false),
    /**
     * Query is waiting for the required resources (beta).
     */
    WAITING_FOR_RESOURCES(false),
    /**
     * Query is being dispatched to a coordinator.
     */
    DISPATCHING(false),
    /**
     * Query is being planned.
     */
    PLANNING(false),
    /**
     * Query execution is being started.
     */
    STARTING(false),
    /**
     * Query has at least one running task.
     */
    RUNNING(false),
    /**
     * Query is finishing (e.g. commit for autocommit queries)
     */
    FINISHING(false),
    /**
     * Query has finished executing and all output has been consumed.
     */
    FINISHED(true),
    /**
     * Query execution failed.
     */
    FAILED(true);

    private final boolean doneState;

    /**
     * Is this a terminal state.
     */
    public boolean isDone()
    {
        return doneState;
    }

}
