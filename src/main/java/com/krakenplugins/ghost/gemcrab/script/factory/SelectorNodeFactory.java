package com.krakenplugins.ghost.gemcrab.script.factory;

import com.kraken.api.core.script.BehaviorNode;
import com.kraken.api.core.script.node.SelectorNode;

import java.util.List;

public interface SelectorNodeFactory {
    SelectorNode create(List<BehaviorNode> children);
}
