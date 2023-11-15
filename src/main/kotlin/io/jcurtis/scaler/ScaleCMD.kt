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

        when {
            args.isEmpty() || args[0].equals("wand", ignoreCase = true) -> {
                val tool = ItemStack(Material.STICK)
                val meta = tool.itemMeta ?: return false
                sender.sendMessage("HI")
                meta.setDisplayName("§6Region Selection Tool")
                meta.lore = listOf(
                    "§7Left click to set point A",
                    "§7Right click to set point B",
                    "§7Use §6/scaler <factor> §7to scale the selection at your location",
                    "§7Use §6/scaler §7to get another selection tool"
                )
                tool.itemMeta = meta

                sender.inventory.addItem(tool)
            }
            args.size == 2 && args[0].equals("scale", ignoreCase = true) -> {
                val scale = args[1].toFloatOrNull() ?: return false
                ScaleManager.scaleSelection(sender, scale)
            }
            args.size == 1 && args[0].equals("undo", ignoreCase = true) -> {
                ScaleManager.undoScale(sender)
            }
            args.size == 2 && args[0].equals("delete", ignoreCase = true) -> {
                ScaleManager.deleteScale(sender, args[1])
            }
            else -> return false
        }

        return true
    }
}