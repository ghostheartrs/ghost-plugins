package com.ghost.plugins.gemcrab.script;

import com.google.inject.Inject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;

@Slf4j
public abstract class BaseScriptNode {

    @Getter
    protected final Client client;

    @Getter
    protected final ScriptContext context;

    @Inject
    public BaseScriptNode(Client client, ScriptContext context) {
        this.client = client;
        this.context = context;
    }
}