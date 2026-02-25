package net.tastypommeslul.sizer.compat;

import com.moulberry.lattice.LatticeDynamicFrequency;
import com.moulberry.lattice.annotation.LatticeOption;
import com.moulberry.lattice.annotation.constraint.LatticeFloatRange;
import com.moulberry.lattice.annotation.constraint.LatticeShowIf;
import com.moulberry.lattice.annotation.constraint.LatticeHideIf;
import com.moulberry.lattice.annotation.widget.LatticeWidgetButton;
import com.moulberry.lattice.annotation.widget.LatticeWidgetSlider;

@SuppressWarnings("unused")
public class Config {
    public Config() {}

    @LatticeOption(title = "sizer.enabled", description = "!!.description")
    @LatticeWidgetButton
    public boolean enabled = true;

    public boolean isEnabled() {
        return enabled;
    }

    @LatticeOption(title = "sizer.everyone", description = "!!.description")
    @LatticeWidgetButton
    public boolean everyone = false;

//    @LatticeOption(title = "sizer.april_fools", description = "!!.description")
//    @LatticeWidgetButton
    public boolean aprilFools = false;

    @LatticeOption(title = "sizer.use_different_values", description = "!!.description")
    @LatticeShowIf(function = "isEnabled", frequency = LatticeDynamicFrequency.EVERY_TICK)
    @LatticeWidgetButton
    public boolean useDifferentValues = false;

    public boolean isUseDifferentValues() {
        return useDifferentValues;
    }

    @LatticeOption(title = "sizer.scale_x", description = "!!.description")
    @LatticeShowIf(function = "isUseDifferentValues", frequency = LatticeDynamicFrequency.EVERY_TICK)
    @LatticeFloatRange(min = 0.25f, max = 2f)
    @LatticeWidgetSlider
    public float scaleX = 0.5f;

    @LatticeOption(title = "sizer.scale_y", description = "!!.description")
    @LatticeShowIf(function = "isUseDifferentValues", frequency = LatticeDynamicFrequency.EVERY_TICK)
    @LatticeFloatRange(min = 0.25f, max = 2f)
    @LatticeWidgetSlider
    public float scaleY = 0.5f;

    @LatticeOption(title = "sizer.scale_z", description = "!!.description")
    @LatticeShowIf(function = "isUseDifferentValues", frequency = LatticeDynamicFrequency.EVERY_TICK)
    @LatticeFloatRange(min = 0.25f, max = 2f)
    @LatticeWidgetSlider
    public float scaleZ = 0.5f;

    @LatticeOption(title = "sizer.scale", description = "!!.description")
    @LatticeHideIf(function = "isUseDifferentValues", frequency = LatticeDynamicFrequency.EVERY_TICK)
    @LatticeFloatRange(min = 0.25f, max = 2f)
    @LatticeWidgetSlider
    public float scale = 0.5f;

    @LatticeOption(title = "sizer.change_rate", description = "!!.description")
    @LatticeFloatRange(min = 0.05f, max = 0.25f)
    @LatticeWidgetSlider
    public float changeRate = 0.05f;
}