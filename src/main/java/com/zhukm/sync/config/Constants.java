package com.zhukm.sync.config;

public class Constants {
    public static final String TASK_QUEUE = "task-queue";
    public static final String LEADER_ELECTION_MAP = "leader-election";
    public static final String LEADER_ELECTION_KEY = "leader";
    public static final long LEASE_TIME_SECONDS = 15; // 租约时间 15 秒
    public static final int MIN_CLUSTER_SIZE = 2; // 集群中最小节点数

    private Constants() {
        // empty private
    }
}
