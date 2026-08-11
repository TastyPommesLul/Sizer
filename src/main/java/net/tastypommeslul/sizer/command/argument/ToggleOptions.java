package net.tastypommeslul.sizer.command.argument;

import net.tastypommeslul.sizer.SizerClient;

public enum ToggleOptions {
    ENABLED,
    USE_DIFFERENT_VALUES,
    APRIL_FOOLS,
    EVERYONE;

    public void toggleConfigValue() {
        switch (this) {
            case ENABLED -> SizerClient.config.enabled = !SizerClient.config.enabled;
            case USE_DIFFERENT_VALUES -> SizerClient.config.useDifferentValues = !SizerClient.config.useDifferentValues;
            case EVERYONE -> SizerClient.config.everyone = !SizerClient.config.everyone;
            case APRIL_FOOLS -> SizerClient.config.aprilFools = !SizerClient.config.aprilFools;
        }
    }

    public boolean getConfigValue() {
        return switch (this) {
            case ENABLED -> SizerClient.config.enabled;
            case USE_DIFFERENT_VALUES -> SizerClient.config.useDifferentValues;
            case EVERYONE -> SizerClient.config.everyone;
            case APRIL_FOOLS -> SizerClient.config.aprilFools;
        };
    }

    public String getReadable(boolean bool) {
        return bool ? "On" : "Off";
    }
    @Override
    public String toString() {
        return name().toLowerCase();
    }
}