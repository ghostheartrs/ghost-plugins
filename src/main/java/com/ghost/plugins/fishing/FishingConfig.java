package com.ghost.plugins.fishing;

import com.ghost.plugins.fishing.script.types.FishingLocation;
import com.ghost.plugins.fishing.script.types.Fish;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup("autofisher")
public interface FishingConfig extends Config {

    @ConfigSection(
            name = "General",
            description = "General settings for the Fishing plugin.",
            position = 0
    )
    String general = "General";

    @ConfigItem(
            keyName = "fishType",
            name = "Fish",
            description = "The type of fish to catch.",
            position = 0,
            section = general
    )
    default Fish fishType() {
        return Fish.SHRIMP;
    }

    @ConfigItem(
            keyName = "fishingLocation",
            name = "Location",
            description = "The location to fish at.",
            position = 1,
            section = general
    )
    default FishingLocation fishingLocation() {
        return FishingLocation.DRAYNOR_VILLAGE;
    }

    @ConfigItem(
            keyName = "bankingEnabled",
            name = "Banking Enabled",
            description = "Do you want to bank the fish?",
            position = 2,
            section = general
    )
    default boolean bankingEnabled() {
        return true;
    }
}
