package pro.delfik.lobby.serverserlector;

import lib.Generate;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import pro.delfik.lobby.serverserlector.serverinfo.ServerInfo;

import java.util.ArrayList;
import java.util.List;

public enum ServerType {
	LOBBY("LOBBY", "Лобби", Generate.itemstack(Material.SEA_LANTERN, 1, 0, "§a§lЛобби", "§eМесто, где можно пообщаться с другими игроками,", "§eНайти себе друзей или просто", "§eВесело провести время")),
	BEDWARS("BW", "BedWars", Generate.itemstack(Material.BED, 1, 0, "§c§lBedWars", "§eВсем известные кроватные войны")),
	STICKFIGHT("SF", "StickFight", Generate.itemstack(Material.STICK, 1, 0, "§6§lStickFight","§eМини-игра, в которой тебе придётся", "§eОбхитрять противника и скидывать его!")),
	BUILD("B", "Билд", Generate.itemstack(Material.GRASS, 1, 0, "§d§lСтроительные сервера", "§eДоступно с ранга §2Строитель")),
	TEST("TEST", "Тестовый сервер", Generate.itemstack(Material.COMMAND, 1, 0, "§d§lСервер для администрации.", "§eВсё самое секретное", "§eПроисходит именно здесь!"));
	
	public final List<ServerInfo> infos = new ArrayList<>();
	public final ItemStack item;
	public final String name;
	public final String shortcut;
	
	ServerType(String shortcut, String name, ItemStack item) {
		this.item = item;
		this.name = name;
		this.shortcut = shortcut;
	}
	
	public static ServerType parse(String s) {
		switch (s.toUpperCase()) {
			case "BW": return BEDWARS;
			case "LOBBY": return LOBBY;
			case "SF": return STICKFIGHT;
			case "B": return BUILD;
			case "TEST": return TEST;
			default: return null;
		}
	}
	
	@Override
	public String toString() {
		return name;
	}
}
