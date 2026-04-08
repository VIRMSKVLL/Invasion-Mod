package com.invasion.client.render.entity;

import com.invasion.InvasionMod;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.WolfEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.WolfEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.util.Identifier;

public class IMWolfEntityRenderer extends WolfEntityRenderer {

	public IMWolfEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.addFeature(new WolfNexusFeatureRenderer(this));
    }

    @Override
    protected void scale(WolfEntity entity, MatrixStack matrices, float amount) {
        float f = 1.4F;
        matrices.scale(f, (2 + f) / 3F, f);
    }

	@Override
    public Identifier getTexture(WolfEntity wolfEntity) {
        return wolfEntity.getTextureId();
    }
}


@Environment(EnvType.CLIENT)
class WolfNexusFeatureRenderer extends FeatureRenderer<WolfEntity, WolfEntityModel<WolfEntity>> {
    private static final Identifier SKIN = InvasionMod.id("textures/entity/wolf/tame_nexus.png");

    public WolfNexusFeatureRenderer(FeatureRendererContext<WolfEntity, WolfEntityModel<WolfEntity>> featureRendererContext) {
        super(featureRendererContext);
    }

    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, WolfEntity wolfEntity, float f, float g, float h, float j, float k, float l) {
        if (wolfEntity.isTamed() && !wolfEntity.isInvisible()) {
            int m = wolfEntity.getCollarColor().getEntityColor();
            VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutoutNoCull(SKIN));
            ((WolfEntityModel)this.getContextModel()).render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, m);
        }
    }
}
