package com.banworld;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class project extends JavaPlugin implements Listener {
	
	public void onEnable() {
		saveDefaultConfig();
		reloadConfig();
		Bukkit.getPluginCommand("BanWorld").setExecutor(this);
		Bukkit.getPluginManager().registerEvents(this, (Plugin)this );
		
		
		
		new BukkitRunnable(){
			public void run() {
				for(Player p:Bukkit.getOnlinePlayers()) {
					if(p.isOp()) {
						continue;
					}
					boolean check = false;
					for(int i = 0;i<getConfig().getStringList("BanWorldList").size();i++) {
						String temp = getConfig().getStringList("BanWorldList").get(i);
						if(p.getWorld().getName().equalsIgnoreCase(temp)) {
							check = true;
						}
					}
					
					if(check == true) {
						p.teleport(Bukkit.getWorld("world").getSpawnLocation());
						p.sendMessage("当前世界为黑名单世界,为您传送至主世界.");
					}
				}
			}
			
		}.runTaskTimer(this, 20L,20L);

	}
	
	@EventHandler
	public void onTP(PlayerTeleportEvent e) {
		Player p = e.getPlayer();
		World worldto = e.getTo().getWorld();
		boolean check = false;
		for(int i = 0;i<getConfig().getStringList("BanWorldList").size();i++) {
			String temp = getConfig().getStringList("BanWorldList").get(i);
			if(worldto.getName().equalsIgnoreCase(temp)) {
				check = true;
			}
		}
		
		if(check == true) {
			if(p.isOp()) {
				p.sendMessage("当前世界为黑名单世界,但是您是OP,传送将继续进行.");
			}else {
				e.setCancelled(true);
				p.sendMessage("当前世界为黑名单世界,传送已取消.");
			}
		}
	}
	
	
	@EventHandler
	public boolean onCommand(CommandSender sender,Command cmd,String Label,String[] args) {
		
		
		if(args.length == 0) {
			sender.sendMessage("/BanWorld Add    ->增加当前世界进入黑名单");
			sender.sendMessage("/BanWorld Remove ->移除当前世界进入黑名单");
			sender.sendMessage("/BanWorld Reload ->重载配置文件");
			return false;
		}
		
		
		if(args.length == 1 && args[0].equalsIgnoreCase("Add")) {
			Player p = (Player)sender;
			if(!p.isOp()) {
				p.sendMessage("权限不足");
				return false;
			}
			List<String> list = getConfig().getStringList("BanWorldList");
			list.add(p.getWorld().getName());
		    getConfig().set("BanWorldList", list);
		    saveConfig();
		    reloadConfig();
			p.sendMessage("添加" + p.getWorld().getName() + "成功!");
			return false;
		}
		
		if(args.length == 1 && args[0].equalsIgnoreCase("Remove")) {
			Player p = (Player)sender;
			if(!p.isOp()) {
				p.sendMessage("权限不足");
				return false;
			}
			List<String> list = getConfig().getStringList("BanWorldList");
			list.remove(p.getWorld().getName());
		    getConfig().set("BanWorldList", list);
		    saveConfig();
		    reloadConfig();
			p.sendMessage("移除" + p.getWorld().getName() + "成功!");
			return false;
		}
		if(args.length == 1 && args[0].equalsIgnoreCase("Reload")) {
			Player p = (Player)sender;
			if(!p.isOp()) {
				p.sendMessage("权限不足");
				return false;
			}
		    reloadConfig();
		    p.sendMessage("重载成功!");
		    return false;
		}
		
		return false;
	}
  
}
