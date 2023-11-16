package io.jcurtis.scaler

import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.util.UUID

@Suppress("unused")
class Scaler: JavaPlugin(), Listener {
    companion object {
        var scales = YamlConfiguration()
    }

    override fun onEnable() {
        createConfigs()
        server.pluginManager.registerEvents(this, this)

        getCommand("scaler")?.setExecutor(ScaleCMD())
        getCommand("scaler")?.tabCompleter = ScaleCMDTabCompleter()

        for (scale in scales.getKeys(false)) {
            ScaleManager.scales[scale] = UUID.fromString(scales.getString(scale)!!)
        }

        logger.info("Scaler has been enabled!")
    }

    override fun onDisable() {
        scales.getKeys(false).forEach { scales.set(it, null) }

        for (scale in ScaleManager.scales) {
            logger.info("Saving scale ${scale.key}...")
            scales.set(scale.key, scale.value.toString())
        }

        scales.save(File(dataFolder, "scales.yml"))
    }

    @EventHandler
    fun playerInteract(e: PlayerInteractEvent) {
        val p = e.player

        if (p.inventory.itemInMainHand.itemMeta?.displayName != "§6Region Selection Tool") return
        if (e.action != Action.LEFT_CLICK_BLOCK && e.action != Action.RIGHT_CLICK_BLOCK) return

        e.isCancelled = true

        if (e.action == Action.LEFT_CLICK_BLOCK) {
            ScaleManager.setSelection(p, 0, e.clickedBlock?.location!!)
        } else if (e.action == Action.RIGHT_CLICK_BLOCK) {
            ScaleManager.setSelection(p, 1, e.clickedBlock?.location!!)
        }
    }

    private fun createConfigs() {
        saveDefaultConfig()

        val customConfig = File(dataFolder, "scales.yml")
        if (!customConfig.exists()) {
            customConfig.parentFile.mkdirs()
            saveResource("scales.yml", false)
        }

        scales = YamlConfiguration.loadConfiguration(File(dataFolder, "scales.yml"))
    }
}