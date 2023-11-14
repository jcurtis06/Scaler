package io.jcurtis.scaler

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.plugin.java.JavaPlugin

@Suppress("unused")
class Scaler: JavaPlugin(), Listener {
    override fun onEnable() {
        // register events
        server.pluginManager.registerEvents(this, this)

        getCommand("scaler")?.setExecutor(ScalerCMD())

        logger.info("Scaler has been enabled!")
    }

    @EventHandler
    fun playerInteract(e: PlayerInteractEvent) {
        val p = e.player

        if (p.inventory.itemInMainHand.type != Material.STICK) return

        if (e.action == Action.LEFT_CLICK_BLOCK) {
            ScaleManager.pointA = e.clickedBlock?.location
            p.sendMessage("Point A set to ${ScaleManager.pointA!!.blockX}, ${ScaleManager.pointA!!.blockY}, ${ScaleManager.pointA!!.blockZ}")
        } else if (e.action == Action.RIGHT_CLICK_BLOCK) {
            ScaleManager.pointB = e.clickedBlock?.location
            p.sendMessage("Point B set to ${ScaleManager.pointB!!.blockX}, ${ScaleManager.pointB!!.blockY}, ${ScaleManager.pointB!!.blockZ}")
        }
    }
}