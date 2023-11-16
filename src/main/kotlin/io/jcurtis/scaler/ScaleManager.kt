package io.jcurtis.scaler

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.entity.BlockDisplay
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import java.util.UUID
import kotlin.math.round

object ScaleManager {
    val selections = mutableMapOf<UUID, Selection>()
    val scales = mutableMapOf<String, UUID>()

    fun setSelection(player: Player, point: Int, loc: Location): Selection {
        val selection: Selection

        if (selections.containsKey(player.uniqueId)) {
            selection = selections[player.uniqueId]!!
        } else {
            selection = Selection()
            selections[player.uniqueId] = selection
        }

        val roundedLoc = Location(loc.world, round(loc.x), round(loc.y), round(loc.z))

        if (point == 0) {
            selection.reset()
            selection.pointA = roundedLoc
            player.sendMessage("Point A set to ${selection.pointA?.x}, ${selection.pointA?.y}, ${selection.pointA?.z}")
        } else if (point == 1) {
            selection.pointB = roundedLoc
            player.sendMessage("Point B set to ${selection.pointB?.x}, ${selection.pointB?.y}, ${selection.pointB?.z}")
        }

        return selection
    }

    fun scaleSelection(player: Player, scale: Float = 1f) {
        val selection = selections[player.uniqueId] ?: run {
            player.sendMessage("You have no selection!")
            return
        }
        val blocks = selection.getBlocks()
        val loc = player.location
        loc.yaw = 0f
        loc.pitch = 0f

        for (block in blocks) {
            placeBlock(block, loc, selection.pointA!!, scale, "scale-${scales.size}")
        }

        scales["scale-${scales.size}"] = player.uniqueId
        player.sendMessage("Scale has been created!")
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