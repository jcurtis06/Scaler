package io.jcurtis.scaler

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.entity.BlockDisplay
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import java.util.UUID

object ScaleManager {
    val selections = mutableMapOf<UUID, Selection>()
    val scales = mutableMapOf<String, UUID>()

    fun scaleSelection(player: Player, scale: Float = 1f) {
        val selection = selections[player.uniqueId] ?: return
        Bukkit.getLogger().info("Scaling selection for ${player.name} with scale $scale")
        val blocks = selection.getBlocks()
        val loc = player.location
        loc.yaw = 0f
        loc.pitch = 0f

        for (block in blocks) {
            placeBlock(block, loc, selection.pointA!!, scale, "scale-${scales.size}")
            Bukkit.getLogger().info("Placed block at ${block.location}")
        }

        scales["scale-${scales.size}"] = player.uniqueId
    }

    fun deleteScale(player: Player, scale: String) {
        if (!scales.containsKey(scale)) {
            player.sendMessage("That scale does not exist!")
            return
        }
        if (scales[scale] != player.uniqueId) {
            player.sendMessage("You do not own this scale!")
            return
        }

        for (entity in player.world.entities) {
            if (entity.customName == scale) {
                entity.remove()
            }
        }

        scales.remove(scale)
        player.sendMessage("Scale $scale has been deleted!")
    }

    fun undoScale(player: Player) {
        // get the last scale that the player created
        val lastScale = scales.keys.lastOrNull { scales[it] == player.uniqueId } ?: run {
            player.sendMessage("You have no scales to undo!")
            return
        }

        deleteScale(player, lastScale)
    }

    private fun placeBlock(block: Block, toLoc: Location, origin: Location, scale: Float, name: String) {
        val blockRelativeLocation = block.location.clone().subtract(origin)
        blockRelativeLocation.multiply(scale.toDouble())

        val displayLocation = toLoc.clone().add(blockRelativeLocation)

        val world = Bukkit.getWorld(block.world.uid)
        val display = world?.spawnEntity(displayLocation, EntityType.BLOCK_DISPLAY) as? BlockDisplay
        display?.block = block.blockData

        val transform = display?.transformation
        transform?.scale?.set(scale, scale, scale)
        display?.transformation = transform!!

        display.customName = name
    }
}