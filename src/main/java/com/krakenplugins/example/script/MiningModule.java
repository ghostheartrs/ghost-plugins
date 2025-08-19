package com.krakenplugins.example.script;

import com.google.inject.AbstractModule;
import com.krakenplugins.example.script.factory.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MiningModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(SequenceNodeFactory.class).to(SequenceNodeFactoryImpl.class);
        bind(RepeatNodeFactory.class).to(RepeatNodeFactoryImpl.class);
        bind(SelectorNodeFactory.class).to(SelectorNodeFactoryImpl.class);
    }
}