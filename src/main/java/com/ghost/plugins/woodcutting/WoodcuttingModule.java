package com.ghost.plugins.woodcutting;

import com.google.inject.AbstractModule;
import com.ghost.plugins.woodcutting.factory.SelectorNodeFactory;
import com.ghost.plugins.woodcutting.factory.SelectorNodeFactoryImpl;
import com.ghost.plugins.woodcutting.factory.SequenceNodeFactory;
import com.ghost.plugins.woodcutting.factory.SequenceNodeFactoryImpl;
import com.ghost.plugins.woodcutting.script.ScriptContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WoodcuttingModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(SequenceNodeFactory.class).to(SequenceNodeFactoryImpl.class);
        bind(SelectorNodeFactory.class).to(SelectorNodeFactoryImpl.class);
        bind(ScriptContext.class);
    }
}
