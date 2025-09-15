package com.ghost.plugins.woodcutting.types;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public enum Tree {
    NORMAL("Tree", List.of(1276, 1278)),
    OAK("Oak", List.of(1751)),
    WILLOW("Willow", List.of(1750, 1756, 1758, 1760)),
    YEW("Yew", List.of(1753));

    private final String name;
    private final List<Integer> treeIds;

    @Override
    public String toString() {
        return name;
    }
}
