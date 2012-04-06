package com.mcnsa.hardcore;

import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class playerListener implements Listener {
	private Logger log;
	private slapi slapi;
	private HardCore hardCore = new HardCore();
	
	@EventHandler
	public void playerJoin(PlayerJoinEvent event){
		//Upon login, make sure they have a set of stats
		//Load up HashMap
		Map<String, String[]> stats;
        try {
			stats = (Map<String, String[]>)slapi.load("plugins/MCNSAHardcore/stats.dat");
		} catch (Exception e) {
			return;
		}
        Player player = event.getPlayer();
        
        if (!stats.containsKey(player.toString())){
        	//Build some blank stats and save them  [0]kills [1]deaths
        	String[] newStats = new String[]{"0","0"};
        	stats.put(player.toString(), newStats);
        	try {
    			slapi.save(stats, "plugins/MCNSAHardcore/stats.dat");
    		} catch (Exception e) {
    		}
        	return;
        }
	}
	
	
	@EventHandler
	public void playerDeath(PlayerDeathEvent event){
		//Only handle HardCore deaths  \m/
		if (event.getEntity().getWorld().toString().equalsIgnoreCase(HardCore.world)){
			Player player = event.getEntity();
			//killer could be null on PvE deaths
			Player killer = player.getKiller();
			
			//Load up HashMap
			Map<String, String[]> stats;
	        try {
				stats = (Map<String, String[]>)slapi.load("plugins/MCNSAHardcore/stats.dat");
			} catch (Exception e) {
				return;
			}
	        
	        //PvE
	        if(killer == null){
	        	String[] oldPlayerStats = stats.get(player.toString());
	        	//Wipe kills, increment deaths.
	        	int deaths = Integer.valueOf(oldPlayerStats[1]);
	        	deaths = deaths++;
	        	String[] newPlayerStats = new String[]{"0",""+deaths};
	        	stats.put(player.toString(), newPlayerStats);
	        	try {
	    			slapi.save(stats, "plugins/MCNSAHardcore/stats.dat");
	    		} catch (Exception e) {
	    		}
	        	returnMessage(player, "&aLooks like you've &cDIED!  &6Kills[&c"+newPlayerStats[0]+"&6]  Deaths[&c"+newPlayerStats[1]+"&6]");
	        	return;
	        	
	        }
	        //PvP
	        if(killer != null){
	        	String[] oldPlayerStats = stats.get(player.toString());
	        	//Wipe kills, increment deaths.
	        	int deaths = Integer.valueOf(oldPlayerStats[1]);
	        	deaths = deaths++;
	        	String[] newPlayerStats = new String[]{"0",""+deaths};
	        	stats.put(player.toString(), newPlayerStats);
	        	
	        	String[] oldKillerStats = stats.get(killer.toString());
	        	//Increment kills
	        	int kills = Integer.valueOf(oldKillerStats[0]);
	        	kills = kills++;
	        	String deathNoChange = oldKillerStats[1];
	        	String[] newKillerStats = new String[]{""+kills,deathNoChange};
	        	stats.put(killer.toString(), newKillerStats);
	        	try {
	    			slapi.save(stats, "plugins/MCNSAHardcore/stats.dat");
	    		} catch (Exception e) {
	    		}
	        	//Message everyone involved
	        	returnMessage(player, "&aLooks like &e"+killer.toString()+"&a killed you.  &6Kills[&c"+newPlayerStats[0]+"&6]  Deaths[&c"+newPlayerStats[1]+"&6]");
	        	returnMessage(killer,"&aGOT ONE! &e"+player.toString()+" &a, specifically.  &6Kills[&c"+newKillerStats[0]+"&6]  Deaths[&c"+newKillerStats[1]+"&6]");
	        	return;
	        }

		}
		
		
	}
	public void returnMessage(CommandSender sender, String message) {
		if(sender instanceof Player) {
			sender.sendMessage(processColours(message));
		}
		else {
			sender.sendMessage(message);//plugin.stripColours(message));
		}
	}
	// allow for colour tags to be used in strings..
	public String processColours(String str) {
		return str.replaceAll("(&([a-f0-9]))", "\u00A7$2");
	}
	
}
