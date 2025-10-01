package com.ghost.plugins.fishing.script;

import com.ghost.plugins.fishing.factory.SelectorNodeFactory;
import com.ghost.plugins.fishing.factory.SelectorNodeFactoryImpl;
import com.ghost.plugins.fishing.factory.SequenceNodeFactory;
import com.ghost.plugins.fishing.factory.SequenceNodeFactoryImpl;
import com.ghost.plugins.fishing.script.ScriptContext;
import com.google.inject.AbstractModule;

public class FishingModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(SequenceNodeFactory.class).to(SequenceNodeFactoryImpl.class);
        bind(SelectorNodeFactory.class).to(SelectorNodeFactoryImpl.class);
        bind(ScriptContext.class);
    }
}