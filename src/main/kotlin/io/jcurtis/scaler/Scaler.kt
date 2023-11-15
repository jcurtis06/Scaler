package io.jcurtis.scaler

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.plugin.java.JavaPlugin

@Suppress("unused")
class Scaler: JavaPlugin(), Listener {
    override fun onEnable() {
        server.pluginManager.registerEvents(this, this)

        getCommand("scaler")?.setExecutor(ScaleCMD())

        logger.info("Scaler has been enabled!")
    }

    @EventHandler
    fun playerInteract(e: PlayerInteractEvent) {
        val p = e.player

        if (p.inventory.itemInMainHand.itemMeta?.displayName != "§6Region Selection Tool") return
        if (e.action != Action.LEFT_CLICK_BLOCK && e.action != Action.RIGHT_CLICK_BLOCK) return

        e.isCancelled = true
        val selection: Selection

        if (ScaleManager.selections.containsKey(p.uniqueId)) {
            selection = ScaleManager.selections[p.uniqueId]!!
        } else {
            selection = Selection()
            ScaleManager.selections[p.uniqueId] = selection
        }

        if (e.action == Action.LEFT_CLICK_BLOCK) {
            selection.reset()
            selection.pointA = e.clickedBlock?.location
            p.sendMessage("Point A set to ${selection.pointA?.x}, ${selection.pointA?.y}, ${selection.pointA?.z}")
        } else if (e.action == Action.RIGHT_CLICK_BLOCK) {
            selection.pointB = e.clickedBlock?.location
            p.sendMessage("Point B set to ${selection.pointB?.x}, ${selection.pointB?.y}, ${selection.pointB?.z}")
        }
    }
}