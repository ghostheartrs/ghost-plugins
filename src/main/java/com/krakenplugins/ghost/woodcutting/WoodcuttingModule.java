package com.krakenplugins.ghost.woodcutting;

import com.google.inject.AbstractModule;
import com.krakenplugins.ghost.woodcutting.factory.SelectorNodeFactory;
import com.krakenplugins.ghost.woodcutting.factory.SelectorNodeFactoryImpl;
import com.krakenplugins.ghost.woodcutting.factory.SequenceNodeFactory;
import com.krakenplugins.ghost.woodcutting.factory.SequenceNodeFactoryImpl;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WoodcuttingModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(SequenceNodeFactory.class).to(SequenceNodeFactoryImpl.class);
        bind(SelectorNodeFactory.class).to(SelectorNodeFactoryImpl.class);
    }
}
