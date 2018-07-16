package pro.delfik.lobby;

import lib.I;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import pro.delfik.lmao.core.Registrar;
import pro.delfik.lobby.serverserlector.ServerSelector;

public class Lobby extends JavaPlugin {
	
	private static Lobby instance;
	private static Location spawn;
	private static World world;
	
	public static Lobby getInstance() {
		return instance;
	}
	
	@Override
	public void onEnable() {
		instance = this;
		world = initWorld();
		spawn = world.getSpawnLocation().add(0.5, 0.5, 0.5);
		Registrar r = new Registrar(this);
		r.regEvent(new OnlineHandler());
		r.regEvent(new Training());
		r.regEvent(new LobbyProtecter());
		r.regEvent(new ServerSelector());
		I.delay(ServerSelector::init, 10);
	}
	
	@Override
	public void onDisable() {
		Training.junk.forEach(loc -> loc.getBlock().setType(Material.AIR));
	}
	
	private static World initWorld() {
		return Bukkit.getWorlds().get(0);
	}
	
	public static Location spawnLocation() {
		return spawn;
	}
}
