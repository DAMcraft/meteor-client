/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package meteordevelopment.meteorclient.pathing;

import baritone.api.BaritoneAPI;
import baritone.api.utils.SettingsUtil;
import meteordevelopment.meteorclient.MeteorClient;
import net.minecraft.client.MinecraftClient;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaritoneUtils {
    public static boolean IS_AVAILABLE = false;
    private static final Pattern pattern = Pattern.compile("^(?<setting>[^ ]+) +(?<value>.+)");

    public static String getPrefix() {
        if (IS_AVAILABLE) {
            return BaritoneAPI.getSettings().prefix.value;
        }

        return "";
    }

    public static void updateSettings(File file) {
        // Copy settings file to the baritone folder
        baritone.api.Settings settings = BaritoneAPI.getSettings();
        if (settings != null) {
            try {
                LineIterator it = FileUtils.lineIterator(file);
                while (it.hasNext()) {
                    String line = it.nextLine();
                    Matcher matcher = pattern.matcher(line);
                    if (matcher.matches()) {
                        String setting = matcher.group("setting");
                        String value = matcher.group("value");
                        SettingsUtil.parseAndApply(settings, setting, value);
                    }
                }
            } catch (IOException e) {
                MeteorClient.LOG.error("Failed to find the Baritone settings file.");
            }
        }
    }

    public static void copySettings(File file) {
        // Copies the baritone settings to another file
        File settingsFile = new File(
            MinecraftClient.getInstance().runDirectory.toPath().resolve("baritone").toFile(),
            SettingsUtil.SETTINGS_DEFAULT_NAME
        );
        if (settingsFile.exists()) {
            try {
                FileUtils.copyFile(settingsFile, file);
            } catch (IOException e) {
                MeteorClient.LOG.info("Failed to find the Baritone settings file.");
            }
        }
    }
}
