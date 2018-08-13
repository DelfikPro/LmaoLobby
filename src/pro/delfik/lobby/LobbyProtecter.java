package pro.delfik.lobby;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class LobbyProtecter implements Listener {
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if (e.getAction() == Action.PHYSICAL) return;
		if (e.getPlayer().getGameMode() == GameMode.CREATIVE) return;
		e.setCancelled(true);
		if(e.getClickedBlock() == null)return;
		if(e.getClickedBlock().getType() == Material.ENDER_PORTAL_FRAME)
			e.getPlayer().openInventory(PurchaseTraining.getInv());
	}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		if (e.getPlayer().getGameMode() == GameMode.CREATIVE) return;
		e.setCancelled(true);
	}
	@EventHandler
	public void onPickup(PlayerPickupItemEvent e) {
		if (e.getPlayer().getGameMode() == GameMode.CREATIVE) return;
		e.setCancelled(true);
	}
	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if (e.getEntity().getLocation().getY() < 100 && e.getCause() == EntityDamageEvent.DamageCause.FALL) e.setDamage(42);
		else {
			((Player) e.getEntity()).setHealth(20);
			e.setCancelled(true);
		}
	}
}
