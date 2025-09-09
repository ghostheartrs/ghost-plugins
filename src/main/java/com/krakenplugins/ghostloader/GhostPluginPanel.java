package com.krakenplugins.ghostloader;

import net.runelite.client.ui.PluginPanel;
import net.runelite.client.util.ImageUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * This class represents the panel that is displayed in the RuneLite sidebar.
 * It shows a list of all manageable "Ghost" plugins and allows the user to interact with them.
 */
class GhostPluginPanel extends PluginPanel {
    
    private static final ImageIcon GHOST_ICON;
    
    static {
        final BufferedImage ghostIcon = ImageUtil.loadImageResource(GhostPluginPanel.class, "/kraken.png");
        GHOST_ICON = new ImageIcon(ghostIcon);
    }

    /**
     * Constructs the plugin panel.
     * @param plugins A list of all manageable plugins to be displayed.
     */
    GhostPluginPanel(final List<IManagedPlugin> plugins) {
        super();
        setLayout(new BorderLayout());

        // --- Title Bar ---
        final JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BorderLayout());
        titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        final JLabel iconLabel = new JLabel(new ImageIcon(GHOST_ICON.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH)));
        titlePanel.add(iconLabel, BorderLayout.WEST);

        // Panel for the title and subtitle text
        final JPanel titleTexts = new JPanel();
        titleTexts.setLayout(new GridLayout(2, 1));
        titleTexts.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        final JLabel title = new JLabel("Ghost's Plugins");
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        titleTexts.add(title);

        final JLabel subtitle = new JLabel("based on Kraken");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
        titleTexts.add(subtitle);

        titlePanel.add(titleTexts, BorderLayout.CENTER);

        add(titlePanel, BorderLayout.NORTH);

        // --- Plugin List ---
        final JPanel pluginListPanel = new JPanel();
        pluginListPanel.setLayout(new GridLayout(0, 1, 0, 5));
        
        if (plugins.isEmpty()) {
            pluginListPanel.add(new JLabel("No Ghost plugins found."));
        } else {
            for (final IManagedPlugin plugin : plugins) {
                pluginListPanel.add(createPluginEntry(plugin));
            }
        }

        // Add the plugin list to a scroll pane to handle cases where the list is too long.
        final JScrollPane scrollPane = new JScrollPane(pluginListPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Creates a UI component for a single plugin entry in the list.
     * This includes the plugin name and a toggle button to enable/disable it.
     * @param plugin The manageable plugin to create an entry for.
     * @return A JPanel representing the plugin entry.
     */
    private JPanel createPluginEntry(final IManagedPlugin plugin) {
        final JPanel entryPanel = new JPanel();
        entryPanel.setLayout(new BorderLayout());
        entryPanel.setBorder(BorderFactory.createEtchedBorder());

        final JLabel nameLabel = new JLabel(plugin.getName());
        nameLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        entryPanel.add(nameLabel, BorderLayout.CENTER);
        
        final JToggleButton toggleButton = new JToggleButton("Off");
        
        toggleButton.setSelected(false);
        
        toggleButton.addActionListener(e -> {
            if (toggleButton.isSelected()) {
                plugin.onEnable();
                toggleButton.setText("On");
            } else {
                plugin.onDisable();
                toggleButton.setText("Off");
            }
        });
        
        entryPanel.add(toggleButton, BorderLayout.EAST);

        return entryPanel;
    }
}
