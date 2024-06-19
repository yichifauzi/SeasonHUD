package club.iananderson.seasonhud.impl.seasons;

import static club.iananderson.seasonhud.client.SeasonHUDClient.mc;

import club.iananderson.seasonhud.Common;
import club.iananderson.seasonhud.config.Config;
import club.iananderson.seasonhud.platform.Services;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class Calendar {

  public static boolean calendarLoaded = (Services.PLATFORM.getPlatformName().equals("Forge") || (
      Services.PLATFORM.getPlatformName().equals("Fabric") && Common.extrasLoaded()));

  public static boolean calendarFound() {
    LocalPlayer player = mc.player;

    if (Config.needCalendar.get() && calendarLoaded && player != null) {
      Inventory inv = player.inventory;
      int slot = findCalendar(inv, Services.SEASON.calendar()) + Services.SEASON.findCuriosCalendar(player,
          Services.SEASON.calendar());

      return (slot >= 0);
    } else {
      return true;
    }
  }

  private static int findCalendar(Inventory inv, ItemStack item) {
    for(int i = 0; i < inv.items.size(); ++i) {
      if ((!inv.items.get(i).isEmpty() && inv.items.get(i).sameItem(item))
          || (!inv.offhand.get(0).isEmpty() && inv.offhand.get(0).sameItem(item))) {
        return i;
      }
    }
    return -1;
  }
}