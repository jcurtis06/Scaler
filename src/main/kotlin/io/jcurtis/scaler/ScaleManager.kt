package io.jcurtis.scaler

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.BlockDisplay
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import java.util.UUID

object ScaleManager {
    val selections = mutableMapOf<UUID, Selection>()

    fun scaleSelection(player: Player, scale: Float = 1f) {
        val selection = selections[player.uniqueId] ?: return
        Bukkit.getLogger().info("Scaling selection for ${player.name} with scale $scale")
        val blocks = selection.getBlocks()
        val loc = player.location
        loc.yaw = 0f
        loc.pitch = 0f

        for (block in blocks) {
            placeBlock(block, loc, selection.pointA!!, scale)
            Bukkit.getLogger().info("Placed block at ${block.location}")
        }
    }

    private fun placeBlock(block: Block, toLoc: Location, origin: Location, scale: Float) {
        val blockRelativeLocation = block.location.clone().subtract(origin)
        blockRelativeLocation.multiply(scale.toDouble())

        val displayLocation = toLoc.clone().add(blockRelativeLocation)

        val world = Bukkit.getWorld(block.world.uid)
        val display = world?.spawnEntity(displayLocation, EntityType.BLOCK_DISPLAY) as? BlockDisplay
        display?.block = block.blockData

        val transform = display?.transformation
        transform?.scale?.set(scale, scale, scale)
        display?.transformation = transform!!
    }
}