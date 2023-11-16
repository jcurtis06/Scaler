package io.jcurtis.scaler

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player

class ScaleCMDTabCompleter: TabCompleter {
    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): MutableList<String> {
        val completions = mutableListOf<String>()

        if (sender !is Player || !command.name.equals("scaler", ignoreCase = true)) {
            return completions
        }

        when (args.size) {
            1 -> {
                completions.addAll(listOf("wand", "scale", "set", "undo", "delete"))
            }
            2 -> {
                when (args[0].lowercase()) {
                    "scale" -> completions.add("<factor>")
                    "set" -> completions.addAll(listOf("a", "b"))
                    "delete" -> {
                        val scales = ScaleManager.scales.filter { it.value == sender.uniqueId }
                        completions.addAll(scales.keys)
                    }
                }
            }
            3, 4, 5 -> {
                if (args[0].equals("set", ignoreCase = true)) {
                    completions.addAll(listOf("<x>", "<y>", "<z>"))
                }
            }
        }

        return completions
    }
}