package club.iananderson.seasonhud.fabric.platform;

import club.iananderson.seasonhud.Common;
import club.iananderson.seasonhud.impl.minimaps.CurrentMinimap;
import club.iananderson.seasonhud.platform.services.IMinimapHelper;
import io.github.lucaargolo.seasons.FabricSeasons;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import pepjebs.mapatlases.MapAtlasesMod;
import pepjebs.mapatlases.client.MapAtlasesClient;
import pepjebs.mapatlases.config.MapAtlasesClientConfig;
import sereneseasons.init.ModConfig;

public class FabricMinimapHelper implements IMinimapHelper {

  @Override
  public boolean hideHudInCurrentDimension() {
    ResourceKey<Level> currentDim = Objects.requireNonNull(Minecraft.getInstance().level).dimension();
    if (Common.fabricSeasonsLoaded()) {
      return !FabricSeasons.CONFIG.isValidInDimension(currentDim);
    }
    if (Common.sereneSeasonsLoaded()) {
      return !ModConfig.seasons.isDimensionWhitelisted(currentDim);
    } else {
      return false;
    }
  }

  // Needed for older versions. Makes it easier to port.
  @Override
  public boolean hideMapAtlases() {
    if (CurrentMinimap.mapAtlasesLoaded()) {
      Minecraft mc = Minecraft.getInstance();

      if (mc.level == null || mc.player == null || mc.options.renderDebug) {
        return true;
      }

      Item atlasItem = MapAtlasesMod.MAP_ATLAS.get();

      boolean drawMinimapHud = MapAtlasesClientConfig.drawMiniMapHUD.get();
      boolean emptyAtlas = MapAtlasesClient.getCurrentActiveAtlas().isEmpty();
      boolean hideInHand = MapAtlasesClientConfig.hideWhenInHand.get();
      boolean hasAtlas = (mc.player.getMainHandItem().is(atlasItem) || mc.player.getOffhandItem().is(atlasItem));

      return !drawMinimapHud || emptyAtlas || (hideInHand && hasAtlas);
    } else {
      return false;
    }
  }
}