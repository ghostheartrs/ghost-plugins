package com.krakenplugins.example.script.factory;

import com.kraken.api.core.script.BehaviorNode;
import com.kraken.api.core.script.node.SequenceNode;

import java.util.List;

public class SequenceNodeFactoryImpl implements SequenceNodeFactory {
    @Override
    public SequenceNode create(List<BehaviorNode> children) {
        SequenceNode seq = new SequenceNode();
        children.forEach(seq::addChild);
        return seq;
    }
}
