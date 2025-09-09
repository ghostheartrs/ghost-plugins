package com.krakenplugins.ghostloader;

import javax.swing.JPanel;

/**
 * This interface defines the contract for a plugin that can be managed by the Ghost Loader.
 * Any plugin that implements this interface will be discovered by the loader and displayed in the sidebar.
 */
public interface IManagedPlugin {
    /**
     * Gets the name of the plugin.
     * @return The plugin's name.
     */
    String getName();

    /**
     * Gets a short description of what the plugin does.
     * @return The plugin's description.
     */
    String getDescription();

    /**
     * This method is called when the user enables the plugin from the Ghost Loader panel.
     */
    void onEnable();

    /**
     * This method is called when the user disables the plugin from the Ghost Loader panel.
     */
    void onDisable();

    /**
     * Gets the configuration panel for this plugin.
     * This allows the plugin to provide its own settings UI.
     * @return The plugin's configuration panel, or null if it doesn't have one.
     */
    JPanel getConfigurationPanel();
}
