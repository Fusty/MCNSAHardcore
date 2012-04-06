package com.mcnsa.hardcore;

import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

public class playerListener {
	private Logger log;
	private slapi slapi;
	private HardCore hardCore = new HardCore();
	@EventHandler
	public void playerDeath(PlayerDeathEvent event){
		if (event.getEntity().getWorld().toString().equalsIgnoreCase(HardCore.world)){
			
		}
		
	}
	
}
