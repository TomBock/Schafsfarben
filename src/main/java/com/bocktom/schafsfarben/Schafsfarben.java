package com.bocktom.schafsfarben;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Sheep;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.BoundingBox;

import java.util.Locale;

public final class Schafsfarben extends JavaPlugin {

	public static Schafsfarben plugin;
	private WorldGuard worldGuard;
	private ProtectedRegion region;
	private World world;

	@Override
	public void onEnable() {
		plugin = this;
		worldGuard = WorldGuard.getInstance();

		loadDefaultConfig();
		registerListeners();
		loadRegion();
	}

	private void loadDefaultConfig() {
		getConfig().options().copyDefaults(true);
		saveConfig();
	}

	private void registerListeners() {
		FileConfiguration config = getConfig();
		for (String key : config.getConfigurationSection("buttons").getKeys(false)) {
			ConfigurationSection section = config.getConfigurationSection("buttons." + key);
			if (section == null) {
				continue;
			}

			String worldName = section.getString("world");
			int x = section.getInt("x");
			int y = section.getInt("y");
			int z = section.getInt("z");
			if(worldName == null) {
				getLogger().warning("Missing world name for " + key);
				continue;
			}
			world = getServer().getWorld(worldName);
			if(world == null) {
				getLogger().warning("World " + worldName + " not found for " + key);
				continue;
			}
			Location location = new Location(world, x, y, z);
			DyeColor color;
			try {
				color = DyeColor.valueOf(key.toUpperCase(Locale.ROOT));
			} catch (IllegalArgumentException e) {
				getLogger().warning("Invalid color for " + key);
				continue;
			}

			getServer().getPluginManager().registerEvents(new ButtonListener(this, location, color), this);
		}
	}

	private void loadRegion() {
		String regionName = getConfig().getString("region");
		if (regionName == null) {
			getLogger().warning("Missing region name in config");
			return;
		}
		if(world == null)
			return;

		com.sk89q.worldedit.world.World weWorld = worldGuard.getPlatform().getMatcher().getWorldByName(world.getName());
		RegionManager regionManager = worldGuard.getPlatform().getRegionContainer().get(weWorld);
		if (regionManager == null) {
			getLogger().warning("Region manager not found for world " + world);
			return;
		}
		region = regionManager.getRegion(regionName);
		if (region == null) {
			getLogger().warning("Region " + regionName + " not found");
		}
	}

	public void setColor(DyeColor color) {
		if(region == null)
			return;

		BlockVector3 min = region.getMinimumPoint();
		BlockVector3 max = region.getMaximumPoint();
		BoundingBox boundingBox = new BoundingBox(min.x(), min.y(), min.z(), max.x(), max.y(), max.z());
		for (Entity entity : world.getNearbyEntities(boundingBox)) {
			if(entity instanceof Sheep sheep) {
				sheep.setColor(color);
			}
		}
	}
}
