package io.github.aleksandarharalanov.chatguard.core.security.penalty;

public interface MuteHandler {

    boolean isUserMuted(String username);

    long getUserMuteTimeout(String username);

    void setUserMuteTimeout(String username, long timestamp);
}
