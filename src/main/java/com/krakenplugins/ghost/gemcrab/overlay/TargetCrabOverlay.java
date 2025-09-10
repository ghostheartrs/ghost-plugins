package com.krakenplugins.ghost.gemcrab.overlay;

import com.krakenplugins.ghost.gemcrab.GemCrabConfig;
import com.krakenplugins.ghost.gemcrab.script.GemCrabScript;
import net.runelite.api.Client;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;

import javax.inject.Inject;
import java.awt.*;

public class TargetCrabOverlay extends Overlay {
    private final Client client;
    private final GemCrabScript gemCrabScript;
    private final ModelOutlineRenderer modelOutlineRenderer;
    private final GemCrabConfig config;

    @Inject
    public TargetCrabOverlay(Client client, GemCrabScript gemCrabScript, ModelOutlineRenderer modelOutlineRenderer, GemCrabConfig config) {
        this.client = client;
        this.gemCrabScript = gemCrabScript;
        this.modelOutlineRenderer = modelOutlineRenderer;
        this.config = config;
        this.setPosition(OverlayPosition.DYNAMIC);
        this.setLayer(OverlayLayer.ABOVE_WIDGETS);
        this.setPriority(OverlayPriority.HIGH);
    }

    public Dimension render(Graphics2D graphics) {
        if (this.client.getCanvas() == null) {
            return null;
        }

        if(config.highlightTargetCrab()) {
            renderTargetCrab();
        }
        return null;
    }

    private void renderTargetCrab() {
        if(gemCrabScript.getScriptContext().getTargetCrab() != null) {
            modelOutlineRenderer.drawOutline(gemCrabScript.getScriptContext().getTargetCrab(), 2, Color.GREEN, 2);
        }
    }
}
