package club.iananderson.seasonhud.client.overlays;

import club.iananderson.seasonhud.impl.minimaps.CurrentMinimap;
import club.iananderson.seasonhud.impl.seasons.CurrentSeason;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.MutableComponent;

public class MapAtlasesCommon {
  private MapAtlasesCommon() {
  }

  private static void drawSeasonWithLighterShadow(GuiGraphics context, Font font, MutableComponent text,
      MutableComponent shadowText) {
    context.drawString(font, shadowText, 1, 1, 5855577, false);
    context.drawString(font, text, 0, 0, 0xffffff, false);
  }

  private static void drawScaledComponent(GuiGraphics context, Font font, int x, int y, MutableComponent text,
      MutableComponent shadowText, float textScaling, int maxWidth, int targetWidth) {
    PoseStack pose = context.pose();
    float textWidth = font.width(text);
    float scale = Math.min(1.0F, maxWidth * textScaling / textWidth);
    scale *= textScaling;
    float centerX = x + targetWidth / 2.0F;
    pose.pushPose();
    pose.translate(centerX, (y + 4), 5.0F);
    pose.scale(scale, scale, 1.0F);
    pose.translate(-textWidth / 2.0F, -4.0F, 0.0F);
    drawSeasonWithLighterShadow(context, font, text, shadowText);
    pose.popPose();
  }

  public static void drawMapComponentSeason(GuiGraphics poseStack, Font font, int x, int y, int targetWidth,
      float textScaling, float globalScale) {
    if (CurrentMinimap.mapAtlasesLoaded()) {
      MutableComponent seasonCombined = CurrentSeason.getInstance(Minecraft.getInstance()).getSeasonHudText();
      MutableComponent shadowText = CurrentSeason.getInstance(Minecraft.getInstance()).getSeasonHudTextNoFormat();
      drawScaledComponent(poseStack, font, x, y, seasonCombined, shadowText, textScaling / globalScale, targetWidth,
                          (int) (targetWidth / globalScale));
    }
  }
}
