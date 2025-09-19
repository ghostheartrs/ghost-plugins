package com.ghost.plugins.woodcutting;

import com.ghost.plugins.woodcutting.types.Tree;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import com.ghost.plugins.woodcutting.types.BankLocation;

@ConfigGroup("autowoodcutting")
public interface WoodcuttingConfig extends Config {

    @ConfigSection(
            name = "General",
            description = "General settings",
            position = 0
    )
    String general = "General";

    @ConfigItem(
            keyName = "treeType",
            name = "Tree",
            description = "The tree to chop.",
            position = 0,
            section = general
    )
    default Tree treeType() {
        return Tree.NORMAL;
    }

    @ConfigItem(
            keyName = "highlightTargetTree",
            name = "Highlight Target Tree",
            description = "Highlights the selected tree to chop.",
            position = 1,
            section = general
    )
    default boolean highlightTargetTree() {
        return true;
    }

    @ConfigItem(
            keyName = "showDebugInfo",
            name = "Show Debug Info",
            description = "Displays debugging information on the overlay.",
            position = 2,
            section = general
    )
    default boolean showDebugInfo() {
        return false;
    }

    @ConfigSection(
            name = "Banking",
            description = "Configure banking settings",
            position = 1
    )
    String banking = "Banking";

    @ConfigItem(
            keyName = "bankingEnabled",
            name = "Enable Banking",
            description = "Enable to bank logs, disable to drop them.",
            position = 0,
            section = banking
    )
    default boolean bankingEnabled() {
        return false;
    }
    @ConfigItem(
            keyName = "bankLocation",
            name = "Bank Location",
            description = "The bank to use when banking is enabled.",
            position = 1,
            section = banking
    )
    default BankLocation bankLocation() {
        return BankLocation.DRAYNOR_VILLAGE;
    }
    @ConfigSection(
            name = "Area",
            description = "Configure area settings",
            position = 2
    )
    String area = "Area";

    @ConfigItem(
            keyName = "wanderDistance",
            name = "Wander Distance",
            description = "Max distance (tiles) to wander from start point. 0 to disable.",
            position = 0,
            section = area
    )
    default int wanderDistance() {
        return 15;
    }
}