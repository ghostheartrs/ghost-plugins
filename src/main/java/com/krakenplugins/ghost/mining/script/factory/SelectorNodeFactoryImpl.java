package com.krakenplugins.example.mining.script.factory;

import com.google.inject.Inject;
import com.kraken.api.core.script.BehaviorNode;
import com.kraken.api.core.script.node.SelectorNode;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(onConstructor_ =  {@Inject})
public class SelectorNodeFactoryImpl implements SelectorNodeFactory {

    @Override
    public SelectorNode create(List<BehaviorNode> children) {
        SelectorNode selector = new SelectorNode();
        children.forEach(selector::addChild);
        return selector;
    }
}