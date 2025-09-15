package com.ghost.plugins.agility;

import com.ghost.plugins.agility.types.AgilityCourse;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("semiautoagi")
public interface AgilityConfig extends Config {

    @ConfigItem(
            keyName = "course",
            name = "Agility Course",
            description = "The agility course to run.",
            position = 0
    )
    default AgilityCourse course() {
        return AgilityCourse.GNOME_STRONGHOLD;
    }
}
