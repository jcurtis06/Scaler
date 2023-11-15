package io.jcurtis.scaler

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ScaleCMD: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (sender !is Player) return false

        ScaleManager.scaleSelection(sender, args?.get(0)?.toFloat() ?: 1f)

        return true
    }
}