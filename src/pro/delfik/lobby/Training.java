package pro.delfik.lobby;

import net.minecraft.server.v1_8_R1.BlockPosition;
import net.minecraft.server.v1_8_R1.WorldServer;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import pro.delfik.lmao.outward.item.I;
import pro.delfik.lmao.util.U;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Training implements Listener {

	protected static final HashMap<Player, Category> selected = new HashMap<>();
	protected static final Set<Location> junk = new HashSet<>();
	private static final Set<Player> dropped = new HashSet<>();
	private static WorldServer nmsWorld;
	
	public static void setCategory(Player p, Category category) {
		selected.put(p, category);
		for (int i = 0; i < category.items.length; i++) p.getInventory().setItem(i + 1, category.items[i]);
		I.delay(p::updateInventory, 1);
		I.delay(() -> p.setAllowFlight(category == Category.NONE), 1);
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		setCategory(e.getPlayer(), Category.NONE);
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		selected.remove(e.getPlayer());
		dropped.remove(e.getPlayer());
		
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		Category category = selected.get(e.getPlayer());
		Category actual = Category.get(e.getTo());
		if (category != actual) setCategory(e.getPlayer(), actual);
		if(e.getTo() == null)return;
		if(category == null)return;
		if (e.getTo().getY() < category.respawnAltitude) {
			e.getPlayer().teleport(category.spawn);
			setCategory(e.getPlayer(), category);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onInteract(PlayerInteractEvent e){
		if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
		Player p = e.getPlayer();
		Category c = Category.get(e.getMaterial());
		Location placeAt = e.getClickedBlock().getRelative(e.getBlockFace()).getLocation();
		if (p.isDead()) return;
		if (c == Category.NONE) return;
		if (c == Category.FASTBRIDGE) {
			if (placeAt.getBlockZ() <= -87) e.setCancelled(false);
			return;
		}
		if (c == Category.WATERDROP) {
			if (placeAt.getY() < 100) e.setCancelled(false);
		}
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		if (e.getPlayer().getGameMode() != GameMode.SURVIVAL) return;
		Location loc = e.getBlockPlaced().getLocation();
		switch (e.getBlockPlaced().getType()) {
			case SANDSTONE:
				build(e.getPlayer(), loc);
				break;
			case SLIME_BLOCK:
			case WEB:
			case LADDER:
				if (!dropped.contains(e.getPlayer()) && !e.getPlayer().isDead()) drop(e.getPlayer(), loc);
				else e.setCancelled(true);
				break;
		}
	}
	
	@EventHandler
	public void onBucketEmpty(PlayerBucketEmptyEvent e) {
		if (e.getPlayer().isDead() || dropped.contains(e.getPlayer())) {
			e.setCancelled(true);
			return;
		}
		Location loc = e.getBlockClicked().getRelative(e.getBlockFace()).getLocation();
		drop(e.getPlayer(), loc);
	}
	
	
	private static void drop(Player p, Location where) {
		if (p.isDead()) {
			where.getBlock().setType(Material.AIR);
			return;
		}
		junk.add(where);
		dropped.add(p);
		I.delay(() -> {
			junk.remove(where);
			where.getBlock().setType(Material.AIR);
			if (dropped.remove(p)) {
				p.teleport(Category.WATERDROP.spawn);
				p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1, 1.2f);
				setCategory(p, Category.WATERDROP);
			}
		}, 15);
	}
	
	private static void build(Player p, Location block) {
		junk.add(block);
		if (nmsWorld == null) nmsWorld = ((CraftWorld) p.getWorld()).getHandle();
		I.delay(() -> {
			nmsWorld.setAir(new BlockPosition(block.getBlockX(), block.getBlockY(), block.getBlockZ()), true);
			junk.remove(block);
		}, 100);
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		e.setKeepInventory(true);
		e.setDroppedExp(0);
		e.setDeathMessage("");
		dropped.remove(e.getEntity());
		setCategory(e.getEntity(), Category.WATERDROP);
	}
	
	@EventHandler
	public void onRespawn(PlayerRespawnEvent e) {
		e.setRespawnLocation(selected.get(e.getPlayer()).spawn);
		setCategory(e.getPlayer(), selected.get(e.getPlayer()));
	}
	
	protected enum Category {
		NONE(U.toLocation(Lobby.config.getString("spawn"), Lobby.getWorld()), 100, Items.NULL, Items.NULL, Items.NULL, Items.NULL),
		WATERDROP(U.toLocation(Lobby.config.getString("drop"), Lobby.getWorld()), 0, Items.WATER_BUCKET, Items.WEB, Items.LADDER, Items.SLIME_BLOCK),
		FASTBRIDGE(U.toLocation(Lobby.config.getString("bridge"), Lobby.getWorld()), 184, Items.SANDSTONE_BLOCKS, Items.SANDSTONE_BLOCKS, Items.SANDSTONE_BLOCKS, Items.SANDSTONE_BLOCKS),
		PURCHASE(U.toLocation(Lobby.config.getString("purchase"), Lobby.getWorld()), 100, Items.NULL, Items.NULL, Items.NULL, Items.NULL);
		
		
		protected final ItemStack[] items;
		protected final Location spawn;
		protected final double respawnAltitude;
		
		Category(Location spawn, double respawnAltitude, ItemStack... items) {
			this.items = items;
			this.spawn = spawn;
			this.respawnAltitude = respawnAltitude;
		}
		
		public static Category get(Location to) {
			if (to.getZ() >= 56) return WATERDROP;
			if (to.getZ() <= -62) return FASTBRIDGE;
			return NONE;
		}
		public static Category get(Material interactedItem) {
			switch (interactedItem) {
				case SANDSTONE:
					return FASTBRIDGE;
				case SLIME_BLOCK:
				case WEB:
				case WATER_BUCKET:
				case LADDER:
					return WATERDROP;
				default:
					return NONE;
			}
		}
	}

}
