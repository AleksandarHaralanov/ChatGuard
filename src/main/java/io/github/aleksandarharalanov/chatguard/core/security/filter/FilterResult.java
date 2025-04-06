package io.github.aleksandarharalanov.chatguard.core.security.filter;

public final class FilterResult {

    private final String trigger;
    private final String match;

    public FilterResult(String trigger, String match) {
        this.trigger = trigger;
        this.match = match;
    }

    public String getTrigger() {
        return trigger;
    }

    public String getMatch() {
        return match;
    }
}