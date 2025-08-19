
package com.krakenplugins.example;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup("exampleplugin")
public interface MiningConfig extends Config {

	@ConfigSection(
		name = "General",
		description = "",
		position = 1
	)
	String general = "General";

	@ConfigItem(
		keyName = "key",
		name = "Configures Something",
		description = "Configures something in the plugin.",
		position = 1,
		section = general
	)
	default boolean doSomething() {
		return false;
	}
}
