package io.redspace.irons_artifice.item;

import com.geckolib.animatable.GeoItem;
import com.geckolib.animatable.client.GeoRenderProvider;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.util.GeckoLibUtil;
import com.llamalad7.mixinextras.lib.apache.commons.mutable.MutableObject;
import net.minecraft.world.item.Item;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class BaseGeoItem extends Item implements GeoItem {
    public final MutableObject<GeoRenderProvider> geoRenderProvider = new MutableObject<>();
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public BaseGeoItem(Item.Properties properties) {
        super(properties);
        GeoItem.registerSyncedAnimatable(this);
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(this.geoRenderProvider.getValue());
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    }

    @Override
    public @Nonnull AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }
}
