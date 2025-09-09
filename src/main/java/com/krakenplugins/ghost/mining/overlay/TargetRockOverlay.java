package com.krakenplugins.ghost.mining.overlay;

import com.krakenplugins.ghost.mining.MiningConfig;
import com.krakenplugins.ghost.mining.script.MiningScript;
import net.runelite.api.Client;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;

import javax.inject.Inject;
import java.awt.*;

public class TargetRockOverlay extends Overlay {
    private final Client client;
    private final MiningScript miningScript;
    private final ModelOutlineRenderer modelOutlineRenderer;
    private final MiningConfig config;

    @Inject
    public TargetRockOverlay(Client client, MiningScript miningScript, ModelOutlineRenderer modelOutlineRenderer, MiningConfig config) {
        this.client = client;
        this.miningScript = miningScript;
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

        if(config.highlightTargetRock()) {
            renderTargetRock();
        }
        return null;
    }

    private void renderTargetRock() {
        if(miningScript.getScriptContext().getTargetRock() != null) {
            modelOutlineRenderer.drawOutline(miningScript.getScriptContext().getTargetRock(), 2, Color.GREEN, 2);
        }
    }
}
