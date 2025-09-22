package com.ghost.plugins.woodcutting.types;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.runelite.api.ItemID;

import java.util.List;

@Getter
@RequiredArgsConstructor
public enum Tree {
    NORMAL("Tree", ItemID.LOGS, List.of(1276, 1278)),
    OAK("Oak", ItemID.OAK_LOGS, List.of(10820, 42395, 51772)),
    WILLOW("Willow", ItemID.WILLOW_LOGS, List.of(10819, 10829, 10831, 10833)),
    YEW("Yew", ItemID.YEW_LOGS, List.of(10822, 36683, 40756, 42391));

    private final String name;
    private final int logId;
    private final List<Integer> treeIds;

    @Override
    public String toString() {
        return name;
    }
}