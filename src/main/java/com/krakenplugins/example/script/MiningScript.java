package com.krakenplugins.example.script;

import com.google.inject.Inject;
import com.kraken.api.Context;
import com.kraken.api.core.Script;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MiningScript extends Script {

    @Inject
    public MiningScript(Context context) {
        super(context);
    }

    @Override
    public void onStart() {
        log.info("Mining script started");
    }

    @Override
    public long loop() {
        return 0;
    }

    @Override
    public void onEnd() {
        log.info("Mining script ended");
    }
}
