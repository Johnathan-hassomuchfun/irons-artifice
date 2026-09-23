package io.redspace.irons_artifice.client.armor;

import com.geckolib.animatable.GeoItem;
import com.geckolib.model.DefaultedItemGeoModel;
import com.geckolib.renderer.base.GeoRenderState;
import io.redspace.irons_artifice.IronsArtifice;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import javax.annotation.Nonnull;

public class GenericArmorModel<T extends Item & GeoItem> extends DefaultedItemGeoModel<T> {

    private final ResourceLocation model;
    private final ResourceLocation texture;

    private static final ResourceLocation ANIMATION = ResourceLocation.fromNamespaceAndPath(IronsArtifice.MODID, "empty");

    public GenericArmorModel(String modid, String name) {
        this(
                ResourceLocation.fromNamespaceAndPath(modid, String.format("armor/%s", name)),
                ResourceLocation.fromNamespaceAndPath(modid, String.format("textures/models/armor/%s.png", name))
        );
    }

    public GenericArmorModel(ResourceLocation model, ResourceLocation texture) {
        super(ResourceLocation.fromNamespaceAndPath(model.getNamespace(), ""));
        this.model = model;
        this.texture = texture;
    }

    public GenericArmorModel(String name) {
        this(IronsArtifice.MODID, name);
    }

    @Override
    public @Nonnull ResourceLocation getModelResource(@Nonnull GeoRenderState renderState) {
        return model;
    }

    @Override
    public @Nonnull ResourceLocation getTextureResource(@Nonnull GeoRenderState renderState) {
        return texture;
    }

    @Override
    public @Nonnull ResourceLocation getAnimationResource(T animatable) {
        return ANIMATION;
    }
}
