package net.jarviz.stellarmod.entity.client;

import net.jarviz.stellarmod.entity.custom.GnomEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class GnomRenderer extends GeoEntityRenderer<GnomEntity> {
    public GnomRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new GnomModel());
        this.shadowRadius = 0.5f;
    }

    @Nullable
    @Override
    public ResourceLocation getTextureLocation(GnomEntity animatable) {
        return this.model.getTextureResource(animatable);
    }
}
