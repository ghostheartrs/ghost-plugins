
package com.krakenplugins.example.mining;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup("autominer")
public interface MiningConfig extends Config {

	@ConfigSection(
		name = "General",
		description = "",
		position = 1
	)
	String general = "General";

	@ConfigItem(
		keyName = "highlightTargetRock",
		name = "Highlight Target Rock",
		description = "Highlights the selected rock to mine.",
		position = 1,
		section = general
	)
	default boolean highlightTargetRock() {
		return false;
	}
}
