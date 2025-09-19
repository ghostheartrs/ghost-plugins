package com.ghost.plugins.woodcutting.types;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public enum Tree {
    NORMAL("Tree", List.of(1276, 1278)),
    OAK("Oak", List.of(10820, 42395, 51772)),
    WILLOW("Willow", List.of(10819, 10829, 10831, 10833)),
    YEW("Yew", List.of(10822, 36683, 40756, 42391));

    private final String name;
    private final List<Integer> treeIds;

    @Override
    public String toString() {
        return name;
    }
}
