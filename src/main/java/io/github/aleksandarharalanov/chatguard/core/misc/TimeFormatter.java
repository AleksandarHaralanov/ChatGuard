package io.github.aleksandarharalanov.chatguard.core.misc;

import com.earth2me.essentials.User;
import io.github.aleksandarharalanov.chatguard.util.misc.ColorUtil;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public final class TimeFormatter {

    private TimeFormatter() {}

    public static void printFormattedMuteDuration(User user) {
        long remainingMillis = user.getMuteTimeout() - System.currentTimeMillis();

        Map<String, Integer> timeUnits = new LinkedHashMap<>();
        timeUnits.put("d.", (int) (remainingMillis / (1000 * 60 * 60 * 24)));
        timeUnits.put("h.", (int) ((remainingMillis / (1000 * 60 * 60)) % 24));
        timeUnits.put("m.", (int) ((remainingMillis / (1000 * 60)) % 60));
        timeUnits.put("s.", (int) ((remainingMillis / 1000) % 60));

        String formattedTime = timeUnits.entrySet().stream()
                .filter(entry -> entry.getValue() > 0)
                .map(entry -> entry.getValue() + entry.getKey())
                .collect(Collectors.joining(" ", "", ""));

        user.sendMessage(ColorUtil.translateColorCodes(String.format(
                "&7Expires in %s", formattedTime
        )));
    }
}
