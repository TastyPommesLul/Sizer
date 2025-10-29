package net.tastypommeslul.sizer.compat;

import com.moulberry.lattice.annotation.LatticeCategory;
import com.moulberry.lattice.annotation.LatticeOption;
import com.moulberry.lattice.annotation.constraint.LatticeFloatRange;
import com.moulberry.lattice.annotation.constraint.LatticeShowIf;
import com.moulberry.lattice.annotation.widget.LatticeWidgetButton;
import com.moulberry.lattice.annotation.widget.LatticeWidgetSlider;

@SuppressWarnings("unused")
public class Config {
    public Config() {}
    @LatticeCategory(name = "Sizer")
    public SizerConfig sizer = new SizerConfig();
    
    public static class SizerConfig {
        @LatticeOption(title = "Enable Sizer?", description = "Enables/disables the mod (visual height)")
        @LatticeWidgetButton
        public boolean enabled = true;

        public boolean isEnabled() {
            return enabled;
        }

        @LatticeOption(title = "Use different values?", description = "Use different x, y, z values (requires reloading the config menu)")
        @LatticeShowIf(function = "isEnabled")
        @LatticeWidgetButton
        public boolean useDifferentValues = false;
        public boolean isUseDifferentValues() {
            return useDifferentValues;
        }
        public boolean isNotUseDifferentValues() {
            return !useDifferentValues;
        }

        @LatticeOption(title = "Scale X")
        @LatticeShowIf(function = "isUseDifferentValues")
        @LatticeFloatRange(min = 0.25f, max = 2f)
        @LatticeWidgetSlider
        public float shrinkAmountX = 0.5f;

        @LatticeOption(title = "Scale Y")
        @LatticeShowIf(function = "isUseDifferentValues")
        @LatticeFloatRange(min = 0.25f, max = 2f)
        @LatticeWidgetSlider
        public float shrinkAmountY = 0.5f;

        @LatticeOption(title = "Scale Z")
        @LatticeShowIf(function = "isUseDifferentValues")
        @LatticeFloatRange(min = 0.25f, max = 2f)
        @LatticeWidgetSlider
        public float shrinkAmountZ = 0.5f;

        @LatticeOption(title = "Scale")
        @LatticeShowIf(function = "isNotUseDifferentValues")
        @LatticeFloatRange(min = 0.25f, max = 2f)
        @LatticeWidgetSlider
        public float shrinkAmount = 0.5f;
    }
}
