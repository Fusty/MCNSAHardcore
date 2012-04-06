package com.mcnsa.hardcore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger; 


import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.mcnsa.hardcore.slapi;

/**
 *
 * @author fusty
 */

public class HardCore extends JavaPlugin{
	private static final Logger log = Logger.getLogger("Minecraft");
	private FileConfiguration customConfig = null;
	private File customConfigFile = null;
	public static String world;
	public final playerListener playerListener = new playerListener();
	
	
	public void onEnable(){
		//Setup listener
    	PluginManager pm = getServer().getPluginManager();
    	pm.registerEvents(this.playerListener, this);
		//Check for existing config, create if not present
    	File config;
        config = new File("plugins/MCNSAHardcore/config.yml");
        
        if(!config.exists()){
        	saveDefaultConfig();
        }
        world = getCustomConfig().getString("worldName");
        File hardCoreStats;
        File dir;
        hardCoreStats = new File("plugins/MCNSAHardcore/hardCoreStats.dat");
        dir = new File("plugins/MCNSAHardcore");
        if(!hardCoreStats.exists()){
        	try {
        		dir.mkdirs();
        		hardCoreStats.createNewFile();
        		Map<String, String[]> stats = new HashMap<String, String[]>();
        		try {
    				slapi.save(stats, "plugins/MCNSAHardcore/stats.dat");
    			} catch (Exception e) {
    			}
			} catch (IOException e) {
			}
        }
	}
	
	public void reloadCustomConfig() {
        if (customConfigFile == null) {
        	customConfigFile = new File(getDataFolder(), "config.yml");
        }
        customConfig = YamlConfiguration.loadConfiguration(customConfigFile);
     
        // Look for defaults in the jar
        InputStream defConfigStream = getResource("config.yml");
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            customConfig.setDefaults(defConfig);
        }
    }
    
    public FileConfiguration getCustomConfig() {
        if (customConfig == null) {
            reloadCustomConfig();
        }
        return customConfig;
    }
    
    public void saveCustomConfig() {
        if (customConfig == null || customConfigFile == null) {
        return;
        }
        try {
            customConfig.save(customConfigFile);
        } catch (IOException ex) {
            Logger.getLogger(JavaPlugin.class.getName()).log(Level.SEVERE, "Could not save config to " + customConfigFile, ex);
        }
    }
    
    public void saveDefaultConfig() {
    	try {
    		File dir;
    		dir = new File("plugins/MCNSAHardcore");
    		dir.mkdirs();
    		File f=new File("plugins/MCNSAHardcore/config.yml");
    		InputStream inputStream= getResource("config.yml");
	    	OutputStream out=new FileOutputStream(f);
	    	byte buf[]=new byte[1024];
	    	int len;
	    	while((len=inputStream.read(buf))>0)
	    		out.write(buf,0,len);
	    	out.close();
	    	inputStream.close();
    	}
    	catch (IOException e){
            log.log(Level.SEVERE, "[][]Error Saving Default config.yml "+e); 
    	}
    }    
    
    @Override
    public void onDisable(){
        log.info("[MCNSAHardCore] HARDCORE SHUTDOWN COMPLETE! TOTALLY BADASSSSSSS!");
    }
}
