package com.weberpyth.util;

import java.util.ArrayList;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

/**
 * Legacy text helpers.
 *
 * <p>Configuration files in this plugin use either section signs ({@code §}) or ampersands
 * ({@code &}) for colours. Both are accepted; components are produced through Adventure so the
 * server never sees a deprecated {@code String} overload.
 */
public final class WeberText {

    private static final LegacyComponentSerializer SECTION = LegacyComponentSerializer.legacySection();
    private static final LegacyComponentSerializer AMPERSAND = LegacyComponentSerializer.legacyAmpersand();

    private WeberText() {
    }

    public static Component deserialize(String text) {
        if (text == null) {
            return Component.empty();
        }
        return (text.indexOf('\u00a7') >= 0 ? SECTION : AMPERSAND).deserialize(text);
    }

    public static List<Component> deserialize(List<String> lines) {
        List<Component> components = new ArrayList<>(lines.size());
        for (String line : lines) {
            components.add(deserialize(line));
        }
        return components;
    }

    public static String serialize(Component component) {
        return component == null ? "" : SECTION.serialize(component);
    }

    /** Converts any accepted colour syntax to the section sign form used by {@code ItemMeta#getLore()}. */
    public static String toSection(String text) {
        if (text == null) {
            return "";
        }
        return SECTION.serialize(deserialize(text));
    }
}
