package com.ghost.plugins.woodcutting.types;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ChoppingMode {
    DYNAMIC("Dynamic"),
    CHOP_AND_WAIT("Chop & Wait"),
    CLUSTER("Cluster (3 Trees)");

    private final String name;

    @Override
    public String toString() {
        return name;
    }
}