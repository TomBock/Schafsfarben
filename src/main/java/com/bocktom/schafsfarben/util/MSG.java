package com.bocktom.schafsfarben.util;

import com.bocktom.schafsfarben.Schafsfarben;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class MSG {

	public static Component get(String key) {
		String msg = Schafsfarben.plugin.getConfig().getString("msg." + key);
		return MiniMessage.miniMessage().deserialize(msg);
	}

}
