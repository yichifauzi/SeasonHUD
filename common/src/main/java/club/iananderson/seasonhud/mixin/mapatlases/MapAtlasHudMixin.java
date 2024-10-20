package club.iananderson.seasonhud.mixin.mapatlases;

import club.iananderson.seasonhud.client.overlays.MapAtlasesCommon;
import club.iananderson.seasonhud.impl.minimaps.CurrentMinimap;
import club.iananderson.seasonhud.impl.minimaps.CurrentMinimap.Minimap;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import pepjebs.mapatlases.client.Anchoring;
import pepjebs.mapatlases.client.ui.MapAtlasesHUD;
import pepjebs.mapatlases.config.MapAtlasesClientConfig;
import pepjebs.mapatlases.utils.MapDataHolder;

@Mixin(MapAtlasesHUD.class)
public class MapAtlasHudMixin {
  @Shadow
  protected final int BG_SIZE = 64;

  @Shadow
  private float globalScale;

  @Inject(method = "render", at = @At(value = "INVOKE", target = "Lpepjebs/mapatlases/client/ui/MapAtlasesHUD;"
      + "drawMapComponentBiome(Lnet/minecraft/client/gui/GuiGraphics;" + "Lnet/minecraft/client/gui/Font;"
      + "IIIFLnet/minecraft/core/BlockPos;"
      + "Lnet/minecraft/world/level/Level;)V", shift = At.Shift.BY, by = 2), locals = LocalCapture.CAPTURE_FAILSOFT)
  private void render(GuiGraphics graphics, float partialTick, int screenWidth, int screenHeight, CallbackInfo ci,
      ItemStack atlas, MapDataHolder activeMap, ClientLevel level, LocalPlayer player, PoseStack poseStack,
      int mapWidgetSize, Anchoring anchorLocation, int off, int x, int y, float yRot, int light, int borderSize,
      float textScaling, int textHeightOffset, int actualBgSize, Font font) {

    if (CurrentMinimap.mapAtlasesLoaded() && CurrentMinimap.shouldDrawMinimapHud(Minimap.MAP_ATLASES)) {
      if (MapAtlasesClientConfig.drawMinimapBiome.get()) {
        textHeightOffset += (int) (10.0F * textScaling);
      }
      MapAtlasesCommon.drawMapComponentSeason(graphics, font, x, (int) (y + BG_SIZE + (textHeightOffset / globalScale)),
                                              actualBgSize, textScaling, globalScale);
    }
  }
}
