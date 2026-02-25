package net.tastypommeslul.sizer.command.argument;

import net.tastypommeslul.sizer.SizerClient;

public enum ToggleOptions {
    ENABLED,
    USE_DIFFERENT_VALUES,
    APRIL_FOOLS,
    EVERYONE;

    public void toggleConfigValue() {
        switch (this) {
            case ENABLED -> SizerClient.config.sizer.enabled = !SizerClient.config.sizer.enabled;
            case USE_DIFFERENT_VALUES -> SizerClient.config.sizer.useDifferentValues = !SizerClient.config.sizer.useDifferentValues;
            case EVERYONE -> SizerClient.config.sizer.everyone = !SizerClient.config.sizer.everyone;
            case APRIL_FOOLS -> SizerClient.config.sizer.aprilFools = !SizerClient.config.sizer.aprilFools;
        }
    }

    public boolean getConfigValue() {
        return switch (this) {
            case ENABLED -> SizerClient.config.sizer.enabled;
            case USE_DIFFERENT_VALUES -> SizerClient.config.sizer.useDifferentValues;
            case EVERYONE -> SizerClient.config.sizer.everyone;
            case APRIL_FOOLS -> SizerClient.config.sizer.aprilFools;
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
