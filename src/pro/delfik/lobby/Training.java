package pro.delfik.lobby;

import net.minecraft.server.v1_8_R1.BlockPosition;
import net.minecraft.server.v1_8_R1.WorldServer;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
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

	public static void setCategory(Player player, Category category) {
		selected.put(player, category);
		for (int i = 0; i < category.items.length; i++) player.getInventory().setItem(i + 1, category.items[i]);
		I.delay(player::updateInventory, 1);
		I.delay(() -> player.setAllowFlight(category == Category.NONE), 1);
	}

	@EventHandler
	public void event(PlayerJoinEvent event) {
		setCategory(event.getPlayer(), Category.NONE);
	}

	@EventHandler
	public void event(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		selected.remove(player.getPlayer());
		dropped.remove(player.getPlayer());
	}

	@EventHandler
	public void event(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if(player.getGameMode() != GameMode.SURVIVAL)return;
		Location to = event.getTo();
		if(to == null) return;
		Category category = selected.get(player);
		Category actual = Category.get(to);
		if(category != actual) setCategory(player, actual);
		if(category == null) return;
		if(to.getY() < category.respawnAltitude){
			player.teleport(category.spawn);
			setCategory(player, category);
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void event(PlayerInteractEvent event) {
		if(event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
		Player player = event.getPlayer();
		Category category = Category.get(event.getMaterial());
		Location placeAt = event.getClickedBlock().getRelative(event.getBlockFace()).getLocation();
		if(player.isDead()) return;
		if(category == Category.NONE) return;
		if(category == Category.FASTBRIDGE &&
				placeAt.getBlockZ() <= -87) event.setCancelled(false);
		else if(category == Category.WATERDROP &&
				placeAt.getY() < 100) event.setCancelled(false);
	}

	@EventHandler
	public void event(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		if(player.getGameMode() != GameMode.SURVIVAL) return;
		Block block = event.getBlockPlaced();
		Location loc = block.getLocation();
		switch (block.getType()){
			case SANDSTONE:
				build(player, loc);
				break;
			case SLIME_BLOCK:
			case WEB:
			case LADDER:
				if(!dropped.contains(player) && !player.isDead()) drop(player, loc);
				else event.setCancelled(true);
				break;
		}
	}

	@EventHandler
	public void event(PlayerBucketEmptyEvent event) {
		Player player = event.getPlayer();
		if(player.isDead() || dropped.contains(player)){
			event.setCancelled(true);
			return;
		}
		Location loc = event.getBlockClicked().getRelative(event.getBlockFace()).getLocation();
		drop(player, loc);
	}

	private static void drop(Player player, Location where) {
		if(player.isDead()){
			where.getBlock().setType(Material.AIR);
			return;
		}
		junk.add(where);
		dropped.add(player);
		I.delay(() -> {
			junk.remove(where);
			where.getBlock().setType(Material.AIR);
			if(dropped.remove(player)){
				player.teleport(Category.WATERDROP.spawn);
				player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1, 1.2f);
				setCategory(player, Category.WATERDROP);
			}
		}, 15);
	}

	private static void build(Player player, Location block) {
		junk.add(block);
		if(nmsWorld == null) nmsWorld = ((CraftWorld) player.getWorld()).getHandle();
		I.delay(() -> {
			nmsWorld.setAir(new BlockPosition(block.getBlockX(), block.getBlockY(), block.getBlockZ()), true);
			junk.remove(block);
		}, 100);
	}

	@EventHandler
	public void event(PlayerDeathEvent event) {
		event.setKeepInventory(true);
		event.setDroppedExp(0);
		event.setDeathMessage("");
		Player player = event.getEntity();
		dropped.remove(player);
		setCategory(player, Category.WATERDROP);
	}

	@EventHandler
	public void event(PlayerRespawnEvent event) {
		Player player = event.getPlayer();
		event.setRespawnLocation(selected.get(player).spawn);
		setCategory(player, selected.get(player));
	}

	protected enum Category {
		NONE(U.toLocation(Lobby.config.getString("spawn"), Lobby.getWorld()), 100, Items.NULL, Items.NULL, Items.NULL, Items.NULL),
		WATERDROP(U.toLocation(Lobby.config.getString("drop"), Lobby.getWorld()), 0, Items.WATER_BUCKET, Items.WEB, Items.LADDER, Items.SLIME_BLOCK),
		FASTBRIDGE(U.toLocation(Lobby.config.getString("bridge"), Lobby.getWorld()), 184, Items.SANDSTONE_BLOCKS, Items.SANDSTONE_BLOCKS, Items.SANDSTONE_BLOCKS, Items.SANDSTONE_BLOCKS);

		protected final ItemStack[] items;
		protected final Location spawn;
		protected final double respawnAltitude;

		Category(Location spawn, double respawnAltitude, ItemStack... items) {
			this.items = items;
			this.spawn = spawn;
			this.respawnAltitude = respawnAltitude;
		}

		public static Category get(Location to) {
			if(to.getZ() >= 56) return WATERDROP;
			if(to.getZ() <= -62) return FASTBRIDGE;
			return NONE;
		}

		public static Category get(Material interactedItem) {
			switch (interactedItem){
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
