package io.github.aleksandarharalanov.chatguard.handler;

import java.util.ArrayList;
import java.util.Collections;

import static io.github.aleksandarharalanov.chatguard.ChatGuard.getConfig;

public class FilterHandler {

    private static ArrayList<String> filter = (ArrayList<String>) getConfig().getStringList(
            "chatguard.filter", Collections.singletonList("fuck")
    );

    public static ArrayList<String> getFilter() {
        return filter;
    }

    public static void resetFilter() {
        filter = (ArrayList<String>) getConfig().getStringList(
                "chatguard.filter", Collections.singletonList("fuck")
        );
    }
}
