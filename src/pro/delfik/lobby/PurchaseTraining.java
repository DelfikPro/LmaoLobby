package pro.delfik.lobby;

import lib.Generate;
import lib.gui.AdvancedGUI;
import lib.gui.GUI;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import pro.delfik.lobby.purchase.TradeSection;

public class PurchaseTraining {
	
	private static final GUI gui = new AdvancedGUI("Симулятор магазина", 3, PurchaseTraining::processEvent);
	
	static {
		gui.getInventory().setItem(2, Items.SANDSTONE_BLOCKS);
		gui.getInventory().setItem(14, Items.WEB);
	}
	
	private static void processEvent(InventoryClickEvent e) {
		e.getWhoClicked().sendMessage("§7[Event] §aслот(§f" + e.getSlot() + "§a) клик(§f" + e.getClick().name()
											  + "§a) действие(§f" + e.getAction().name() + "§a) ключ(§f" + e.getHotbarButton() + "§a)");
		int clickedNumber = e.getHotbarButton();
		if (e.getClick() != ClickType.NUMBER_KEY || clickedNumber != -1) {
			GUI.Action a = gui.get(e.getRawSlot());		// ОБОЖАЮ ОДНОБУКВЕННЫЕ МЕТОДЫ И ПЕРЕМЕННЫЕ!!!
			if (a != null) a.getC().accept(((Player) e.getWhoClicked()));
			return;
		}


	}
	
	public static Inventory getInv() {
		return gui.getInventory();
	}


}
