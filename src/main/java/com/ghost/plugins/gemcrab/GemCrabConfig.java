package com.ghost.plugins.gemcrab;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup("autogemcrab")
public interface GemCrabConfig extends Config {

	@ConfigSection(
		name = "General",
		description = "",
		position = 1
	)
	String general = "General";

	@ConfigItem(
		keyName = "highlightTargetCrab",
		name = "Highlight Target Crab",
		description = "Highlights the selected crab to attack.",
		position = 1,
		section = general
	)
	default boolean highlightTargetCrab() {
		return true;
	}
}
