package io.github.aleksandarharalanov.chatguard.core.security.penalty;

import me.zavdav.zcore.api.Punishments;
import me.zavdav.zcore.data.Mute;
import me.zavdav.zcore.util.PlayerUtils;

import java.util.UUID;

public class ZCoreMuteHandler implements MuteHandler {

    @Override
    public boolean isUserMuted(String username) {
        return Punishments.isPlayerMuted(PlayerUtils.getUUIDFromUsername(username));
    }

    @Override
    public long getUserMuteTimeout(String username) {
        UUID uuid = PlayerUtils.getUUIDFromUsername(username);
        if (!Punishments.isPlayerMuted(uuid)) {
            return System.currentTimeMillis();
        }

        Mute mute = Punishments.getMute(uuid);

        // It is possible that getDuration() returns null, which specifies a permanent mute.
        // In this case, return 100 years as the timeout.
        if (mute.getDuration() == null) {
            return 31_536_000L * 1000 * 100;
        } else {
            return mute.getDuration() * 1000;
        }
    }

    @Override
    public void setUserMuteTimeout(String username, long timestamp) {
        Punishments.mutePlayer(PlayerUtils.getUUIDFromUsername(username),
                null,
                (timestamp - System.currentTimeMillis()) / 1000,
                "Bad words in chat message");
    }
}
