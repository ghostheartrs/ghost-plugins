package com.ghost.plugins.tutorialisland.script;

import com.google.inject.Singleton;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Singleton
public class ScriptContext {
    private String status = "Starting...";
}