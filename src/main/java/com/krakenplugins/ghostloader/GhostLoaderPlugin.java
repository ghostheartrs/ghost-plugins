package com.krakenplugins.ghostloader;

import com.google.inject.Inject;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginManager;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ImageUtil;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This plugin is responsible for loading and managing other "Ghost" plugins.
 * It adds a side panel to the RuneLite client that lists all installed Ghost plugins
 * and allows the user to enable or disable them.
 */
@PluginDescriptor(
        name = "Ghost Loader",
        description = "Loads and manages Ghost plugins from the sidebar.",
        tags = {"ghost", "loader", "kraken"}
)
public class GhostLoaderPlugin extends Plugin {
    @Inject
    private ClientToolbar clientToolbar;
    @Inject
    private PluginManager pluginManager;
    private NavigationButton navButton;
    private GhostPluginPanel panel;
    private List<IManagedPlugin> managedPlugins;

    /**
     * This method is called when the plugin is started.
     * It finds all manageable plugins, creates the UI, and adds the navigation button.
     * @throws Exception if any error occurs during startup.
     */
    @Override
    protected void startUp() throws Exception {
        this.managedPlugins = pluginManager.getPlugins().stream()
                .filter(p -> p instanceof IManagedPlugin)
                .map(p -> (IManagedPlugin) p)
                .collect(Collectors.toList());

        panel = new GhostPluginPanel(managedPlugins);

        final BufferedImage icon = ImageUtil.loadImageResource(getClass(), "/kraken.png");

        navButton = NavigationButton.builder()
                .tooltip("Ghost Loader")
                .icon(icon)
                .priority(1)
                .panel(panel)
                .build();

        clientToolbar.addNavigation(navButton);
    }

    /**
     * This method is called when the plugin is stopped.
     * It removes the navigation button and cleans up resources.
     * @throws Exception if any error occurs during shutdown.
     */
    @Override
    protected void shutDown() throws Exception {
        clientToolbar.removeNavigation(navButton);
        this.managedPlugins = null;
        this.panel = null;
    }
}
