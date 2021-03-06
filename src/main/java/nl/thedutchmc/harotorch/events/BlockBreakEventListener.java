package nl.thedutchmc.harotorch.events;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;
import nl.thedutchmc.harotorch.HaroTorch;
import nl.thedutchmc.harotorch.lang.LangHandler;
import nl.thedutchmc.harotorch.torch.Torch;
import nl.thedutchmc.harotorch.torch.TorchHandler;

public class BlockBreakEventListener implements Listener {

	@EventHandler
	public void onBlockBreakEvent(BlockBreakEvent event) {
		
		Location loc = event.getBlock().getLocation();
		Location loc_y_plus_1 = new Location(loc.getWorld(), loc.getX(), loc.getY() + 1, loc.getZ());
		
		if(TorchHandler.isTorch(loc_y_plus_1)) {
			
			event.getPlayer().sendMessage(HaroTorch.getMessagePrefix() + ChatColor.GOLD + LangHandler.activeLang.getLangMessages().get("blockBreakNotAllowedTorchOntop"));
			event.setCancelled(true);
			
			return;
		}
		
		
		if(!TorchHandler.isTorch(loc)) {
			return;
		}
		
		UUID torchOwner = TorchHandler.getTorchOwner(loc);
		if(!event.getPlayer().getUniqueId().equals(torchOwner)) {
			event.getPlayer().sendMessage(HaroTorch.getMessagePrefix() + ChatColor.RED + LangHandler.activeLang.getLangMessages().get("blockBreakNotAllowedOwnerMismatch"));
			
			event.setCancelled(true);
			
			return;
		}
		
		event.setDropItems(false);
		
		Torch t = TorchHandler.getTorch(loc);
		TorchHandler.removeTorch(t);
		
		ItemStack torchStack = TorchHandler.getTorch(1);
		
		event.getBlock().getWorld().dropItemNaturally(loc, torchStack);
		
		event.getPlayer().sendMessage(HaroTorch.getMessagePrefix() + ChatColor.GOLD + LangHandler.activeLang.getLangMessages().get("torchBroken"));
	}
}
