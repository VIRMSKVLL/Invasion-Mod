package com.invasion.client.render.entity.model;

import com.invasion.entity.ImpEntity;

import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.util.math.MathHelper;

public class ImpEntityModel extends SinglePartEntityModel<ImpEntity> {
    private final ModelPart root;

    private final ModelPart head;

    private final ModelPart rightArm;
    private final ModelPart leftArm;

    private final ModelPart rightLeg;
    private final ModelPart leftLeg;

    private final ModelPart rightShin;
    private final ModelPart leftShin;

    private final ModelPart rightFoot;
    private final ModelPart leftFoot;

    public ImpEntityModel(ModelPart root) {
        this.root = root;
        head = root.getChild(EntityModelPartNames.HEAD);
        rightArm = root.getChild(EntityModelPartNames.RIGHT_ARM);
        leftArm = root.getChild(EntityModelPartNames.LEFT_ARM);
        rightLeg = root.getChild(EntityModelPartNames.RIGHT_LEG);
        leftLeg = root.getChild(EntityModelPartNames.LEFT_LEG);
        rightShin = root.getChild("right_shin");
        leftShin = root.getChild("left_shin");
        rightFoot = root.getChild("right_foot");
        leftFoot = root.getChild("left_foot");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot();
        ModelPartData head = root.addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create().uv(44, 0).cuboid(-2.733333F, -3, -2, 5, 3, 4), ModelTransform.of(-0.4F, 9.8F, -3.3F, 0.15807F, 0, 0));
        head.addChild("right_horn", ModelPartBuilder.create().uv(0, 1).cuboid(1.272F, -5, -1F, 1, 2, 1), ModelTransform.pivot(0, 0, 0));
        head.addChild("left_horn", ModelPartBuilder.create().uv(0, 1).cuboid(-2.733333F, -5, -1F, 1, 2, 1), ModelTransform.pivot(0, 0, 0));
        root.addChild(EntityModelPartNames.BODY, ModelPartBuilder.create().uv(23, 1).cuboid(-4, 0, -4, 6.99F, 4, 3), ModelTransform.of(0, 9.1F, -0.8666667F, 0.64346F, 0, 0));
        root.addChild(EntityModelPartNames.RIGHT_ARM, ModelPartBuilder.create().uv(26, 9).cuboid(-2, -0.7333333F, -1.133333F, 2, 7, 2), ModelTransform.pivot(-4, 10.8F, -2.066667F));
        root.addChild(EntityModelPartNames.LEFT_ARM, ModelPartBuilder.create().uv(18, 9).cuboid(0, -0.8666667F, -1, 2, 7, 2), ModelTransform.pivot(3, 10.8F, -2.1F));
        root.addChild(EntityModelPartNames.RIGHT_LEG, ModelPartBuilder.create().uv(0, 17).cuboid(-2, 0, -2, 2, 4, 3), ModelTransform.of(-2, 16.9F, -1, -0.15807F, 0, 0));
        root.addChild(EntityModelPartNames.LEFT_LEG, ModelPartBuilder.create().uv(0, 24).cuboid(-0, 0, -2, 2, 4, 3), ModelTransform.of(1, 17, -1, -0.15919F, 0, 0));
        root.addChild("right_shin", ModelPartBuilder.create().uv(10, 17).cuboid(-3, 0.6F, -4.4F, 2, 3, 2), ModelTransform.of(-1, 16.9F, -1, 0.82623F, 0, 0));
        root.addChild("right_foot", ModelPartBuilder.create().uv(18, 18).cuboid(-2.99F, 4.2F, -1, 1.989F, 3, 2), ModelTransform.of(-1, 17, -1, -0.01214F, 0, 0));
        root.addChild("left_shin", ModelPartBuilder.create().uv(10, 22).cuboid(0.01F, 0.6F, -4.433333F, 1.99F, 3, 2), ModelTransform.of(1, 16.9F, -1, 0.82623F, 0, 0));
        root.addChild("left_foot", ModelPartBuilder.create().uv(10, 27).cuboid(-0, 4.2F, -1, 1.99F, 3, 2), ModelTransform.of(1, 17, -1, -0.01214F, 0, 0));
        root.addChild("stomach", ModelPartBuilder.create().uv(1, 1).cuboid(-0.01F, 0, 0, 7.02F, 5, 3), ModelTransform.of(-4, 12.46667F, -2.266667F, -0.15807F, 0, 0));
        root.addChild("neck", ModelPartBuilder.create().uv(44, 7).cuboid(0, 0, 0, 3, 2, 2), ModelTransform.of(-2, 9.6F, -4.033333F, 0.27662F, 0, 0));
        root.addChild("chest", ModelPartBuilder.create().uv(0, 9).cuboid(0.01F, -0.9F, 0.01F, 6.99F, 6, 2), ModelTransform.of(-4, 12.36667F, -3.8F, 0.31614F, 0, 0));
        root.addChild("tail", ModelPartBuilder.create().uv(18, 23).cuboid(0, 0, 0, 1, 8, 1), ModelTransform.of(-1, 15, -0.6666667F, 0.47304F, 0, 0));
        root.addChild("tail_tip", ModelPartBuilder.create().uv(22, 23).cuboid(0, 0, 0, 1, 4, 1), ModelTransform.of(-1, 22.1F, 2.9F, 1.38309F, 0, 0));
        return TexturedModelData.of(data, 64, 32);
    }

    @Override
    public ModelPart getPart() {
        return root;
    }

    @Override
    public void setAngles(ImpEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        head.yaw = headYaw / 57.29578F;
        head.pitch = headPitch / 57.29578F;

        float armPitch = MathHelper.sin(animationProgress * 0.067F) * 0.05F;
        float armRoll = MathHelper.cos(animationProgress * 0.09F) * 0.05F + 0.05F;

        float cosA = MathHelper.cos(limbAngle * 0.6662F);
        float cosB = MathHelper.cos(limbAngle * 0.6662F + MathHelper.PI);

        rightArm.setAngles((cosB * limbDistance) + armPitch, 0, armRoll);
        leftArm.setAngles((cosA * limbDistance) - armPitch, 0, -armRoll);

        rightLeg.pitch = cosA * 1.4F * limbDistance - 0.158F;
        leftLeg.pitch = cosB * 1.4F * limbDistance - 0.15919F;

        rightShin.pitch = cosA * 1.4F * limbDistance + 0.82623F;
        leftShin.pitch = cosB * 1.4F * limbDistance + 0.82461F;
        rightFoot.pitch = cosA * 1.4F * limbDistance - 0.01403F;
        leftFoot.pitch = cosB * 1.4F * limbDistance - 0.01214F;
    }
}