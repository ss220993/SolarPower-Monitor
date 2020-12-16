package com.redisproject.solarpower.app.script;

public enum ScriptOperation {
    GREATERTHAN(">"),
    LESSTHAN("<");

    private final String symbol;

    ScriptOperation(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}
