package io.jcurtis.scaler

import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class ScaleCMD: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) return false

        if (args.isEmpty()) {
            val tool = ItemStack(Material.STICK)
            val meta = tool.itemMeta ?: return false
            meta.setDisplayName("§6Region Selection Tool")
            meta.lore = listOf(
                "§7Left click to set point A",
                "§7Right click to set point B",
                "§7Use §6/scale <factor> §7to scale the selection at your location",
                "§7Use §6/scale §7to get another selection tool"
            )
            tool.itemMeta = meta
            sender.inventory.addItem(tool)
        } else {
            val scale = args[0].toFloatOrNull() ?: return false
            ScaleManager.scaleSelection(sender, scale)
        }

        return true
    }
}