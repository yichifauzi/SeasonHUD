package club.iananderson.seasonhud.platform;

import club.iananderson.seasonhud.Common;
import club.iananderson.seasonhud.config.Config;
import club.iananderson.seasonhud.platform.services.ISeasonHelper;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import sereneseasons.api.SSItems;
import sereneseasons.api.season.ISeasonState;
import sereneseasons.api.season.SeasonHelper;
import sereneseasons.init.ModConfig;

import java.util.Objects;
import java.util.Optional;

import static club.iananderson.seasonhud.client.SeasonHUDClient.mc;

public class FabricSeasonHelper implements ISeasonHelper {
	@Override
	public  boolean isTropicalSeason() {
		boolean showTropicalSeasons = Config.showTropicalSeason.get();
		boolean isInTropicalSeason = SeasonHelper.usesTropicalSeasons(Objects.requireNonNull(mc.level).getBiome(mc.player.getOnPos()));

		return showTropicalSeasons && isInTropicalSeason;
	}

	@Override
	public boolean isSeasonTiedWithSystemTime() {
		return false;
	}

	@Override
	public  String getCurrentSeasonState(){
		//Return the tropical season if enabled in the config and the player is in a biome labeled as tropical
		if (isTropicalSeason()) {
			if(Config.showSubSeason.get()){
				return SeasonHelper.getSeasonState(Objects.requireNonNull(mc.level)).getTropicalSeason().toString();
			}
			else return SeasonHelper.getSeasonState(Objects.requireNonNull(mc.level)).getTropicalSeason().toString()
					.substring(SeasonHelper.getSeasonState(Objects.requireNonNull(mc.level)).getTropicalSeason().toString().length() - 3);
		}
		//Return the sub-season instead if enabled in the config
		else if (Config.showSubSeason.get()) {
			return SeasonHelper.getSeasonState(Objects.requireNonNull(mc.level)).getSubSeason().toString();
		}
		//Return just the season
		else {
			return SeasonHelper.getSeasonState(Objects.requireNonNull(mc.level)).getSeason().toString();
		}
	}

	@Override
	public  String getSeasonFileName() {
		if(isTropicalSeason()){
			return getCurrentSeasonState().toLowerCase().substring(getCurrentSeasonState().toLowerCase().length() - 3);
		}

		if(Config.showSubSeason.get()){
			return SeasonHelper.getSeasonState(Objects.requireNonNull(mc.level)).getSeason().toString().toLowerCase();
		}

		else {
			return getCurrentSeasonState().toLowerCase();
		}
	}

	@Override
	public int getDate() {
		ISeasonState seasonState = SeasonHelper.getSeasonState(Objects.requireNonNull(mc.level));
		int subSeasonDuration = ModConfig.seasons.subSeasonDuration;

		int seasonDay = seasonState.getDay(); //total day out of 24 * 4 = 96

		int seasonDate = (seasonDay % (subSeasonDuration * 3)) + 1; //24 days in a season (8 days * 3 weeks)
		int subDate = (seasonDay % subSeasonDuration) + 1; //8 days in each subSeason (1 week)
		int subTropDate = ((seasonDay + (subSeasonDuration * 3)) % (subSeasonDuration * 2)) + 1; //16 days in each tropical "subSeason". Starts are "Early Dry" (Summer 1), so need to offset 24 days (Spring 1 -> Summer 1)

		if(Services.SEASON.isTropicalSeason()){
			if(!Config.showSubSeason.get()){
				subTropDate = ((seasonDay + (subSeasonDuration * 3)) % (subSeasonDuration * 6)) + 1;
			}
			return subTropDate;
		}
		else if(Config.showSubSeason.get()){
			return subDate;
		}
		else return seasonDate;
	}

	@Override
	public int seasonDuration() {
		int seasonDuration = ModConfig.seasons.subSeasonDuration * 3;

		if(isTropicalSeason()){
			seasonDuration = seasonDuration * 2;
		}

		if(Config.showSubSeason.get()){
			seasonDuration = seasonDuration / 3;
		}

		return seasonDuration;
	}

	@Override
	public Item calendar() {
		return SSItems.CALENDAR;
	}

	@Override
	public int findCuriosCalendar(Player player, Item item) {
		if (Common.curiosLoaded() && Common.extrasLoaded()) {
			Optional<TrinketComponent> findCalendar = TrinketsApi.getTrinketComponent(player);
			if(findCalendar.get().isEquipped(item)){
				return 1;
			}
			else return 0;
		}
		else return 0;
	}
}