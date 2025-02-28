package io.github.aleksandarharalanov.chatguard.core.security.penalty;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;

public class EssentialsMuteHandler implements MuteHandler {

    private final Essentials essentials;

    public EssentialsMuteHandler(Essentials essentials) {
        this.essentials = essentials;
    }

    @Override
    public boolean isUserMuted(String username) {
        User user = essentials.getUser(username);
        return user.isMuted();
    }

    @Override
    public long getUserMuteTimeout(String username) {
        User user = essentials.getUser(username);
        return user.getMuteTimeout();
    }

    @Override
    public void setUserMuteTimeout(String username, long timestamp) {
        User user = essentials.getUser(username);
        user.setMuteTimeout(timestamp);
        user.setMuted(true);
    }
}
