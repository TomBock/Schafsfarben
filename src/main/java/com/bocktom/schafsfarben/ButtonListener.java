package com.bocktom.schafsfarben;

import com.bocktom.schafsfarben.util.MSG;
import com.sk89q.worldedit.util.formatting.text.TextComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Locale;

public class ButtonListener implements Listener {

	private final Schafsfarben plugin;
	private final Location location;
	private final DyeColor color;

	public ButtonListener(Schafsfarben plugin, Location location, DyeColor color) {
		this.plugin = plugin;
		this.location = location;
		this.color = color;
	}

	@EventHandler
	public void onButtonPress(PlayerInteractEvent event) {
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock() != null) {
			Material material = event.getClickedBlock().getType();
			if(isButton(material)) {
				Location buttonLocation = event.getClickedBlock().getLocation();
				if(buttonLocation.equals(location)) {
					plugin.setColor(color);
					Component color = Component.translatable("color.minecraft." + this.color.name().toLowerCase(Locale.ROOT));
					Component text = MSG.get("onclick")
							.replaceText(m -> m.match("%color%").replacement(color));
					event.getPlayer().sendMessage(text);
				}
			}
		}
	}

	private boolean isButton(Material material) {
		return material == Material.STONE_BUTTON
				|| material == Material.ACACIA_BUTTON
				|| material == Material.BIRCH_BUTTON
				|| material == Material.DARK_OAK_BUTTON
				|| material == Material.JUNGLE_BUTTON
				|| material == Material.OAK_BUTTON
				|| material == Material.SPRUCE_BUTTON
				|| material == Material.CRIMSON_BUTTON
				|| material == Material.WARPED_BUTTON
				|| material == Material.CHERRY_BUTTON
				|| material == Material.BAMBOO_BUTTON
				|| material == Material.MANGROVE_BUTTON
				|| material == Material.POLISHED_BLACKSTONE_BUTTON;
	}

}
