package com.ghost.plugins.woodcutting.overlay;

import com.ghost.plugins.woodcutting.WoodcuttingConfig;
import com.ghost.plugins.woodcutting.script.ScriptContext;
import net.runelite.api.Client;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;

import javax.inject.Inject;
import java.awt.*;

public class TargetTreeOverlay extends Overlay {
    private final Client client;
    private final ScriptContext scriptContext;
    private final ModelOutlineRenderer modelOutlineRenderer;
    private final WoodcuttingConfig config;

    @Inject
    public TargetTreeOverlay(Client client, ScriptContext scriptContext, ModelOutlineRenderer modelOutlineRenderer, WoodcuttingConfig config) {
        this.client = client;
        this.scriptContext = scriptContext;
        this.modelOutlineRenderer = modelOutlineRenderer;
        this.config = config;
        this.setPosition(OverlayPosition.DYNAMIC);
        this.setLayer(OverlayLayer.ABOVE_SCENE);
        this.setPriority(OverlayPriority.HIGH);
    }

    public Dimension render(Graphics2D graphics) {
        if (this.client.getCanvas() == null) {
            return null;
        }

        if(config.highlightTargetTree() && scriptContext.getTargetTree() != null) {
            modelOutlineRenderer.drawOutline(scriptContext.getTargetTree(), 2, Color.GREEN, 2);
        }
        return null;
    }
}