package violet.dainty.features.gravestone.corelib.math;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class Rotation {

    private List<AxisRotation> rotations;

    public Rotation(float angle, Axis axis) {
        this();
        add(angle, axis);
    }

    public Rotation() {
        rotations = new ArrayList<>();
    }

    public Rotation add(float angle, Axis axis) {
        rotations.add(new AxisRotation(angle, axis));
        return this;
    }

    public Rotation add(@Nullable Rotation rotation) {
        if (rotation != null) {
            rotations.addAll(rotation.rotations);
        }
        return this;
    }

    private static class AxisRotation {
        private float angle;
        private Axis axis;

        public AxisRotation(float angle, Axis axis) {
            this.angle = angle;
            this.axis = axis;
        }
    }

    public void applyRotation(PoseStack matrixStack) {
        for (AxisRotation rotation : rotations) {
            matrixStack.mulPose(rotation.axis.rotationDegrees(rotation.angle));
        }
    }

}
