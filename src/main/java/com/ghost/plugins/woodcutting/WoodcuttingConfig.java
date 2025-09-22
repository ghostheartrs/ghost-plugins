package com.ghost.plugins.woodcutting;

import com.ghost.plugins.woodcutting.types.BankLocation;
import com.ghost.plugins.woodcutting.types.ChoppingMode;
import com.ghost.plugins.woodcutting.types.Tree;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

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
            keyName = "choppingMode",
            name = "Chopping Mode",
            description = "Determines how the script finds trees to chop.",
            position = 1,
            section = general
    )
    default ChoppingMode choppingMode() {
        return ChoppingMode.DYNAMIC;
    }

    @ConfigItem(
            keyName = "wanderDistance",
            name = "Dynamic Wander",
            description = "Square radius around player to chop, returns to middle if too far.",
            position = 2,
            section = general
    )
    default int wanderDistance() {
        return 15;
    }

    @ConfigItem(
            keyName = "highlightTargetTree",
            name = "Highlight Target Tree",
            description = "Highlights the selected tree to chop.",
            position = 3,
            section = general
    )
    default boolean highlightTargetTree() {
        return true;
    }

    @ConfigItem(
            keyName = "showDebugInfo",
            name = "Show Debug Info",
            description = "Displays debugging information on the overlay.",
            position = 4,
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
}