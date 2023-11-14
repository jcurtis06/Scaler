package io.jcurtis.scaler

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.BlockDisplay
import org.bukkit.entity.EntityType

object ScaleManager {
    var pointA: Location? = null
    var pointB: Location? = null

    fun scaleTo(loc: Location, scale: Float = 1f) {
        val blocks = getBlocksInRegion(pointA!!, pointB!!)
        loc.yaw = 0f
        loc.pitch = 0f

        for (block in blocks) {
            placeBlock(block, loc, scale)
        }
    }

    private fun placeBlock(block: Block, center: Location, scale: Float) {
        val blockRelativeLocation = block.location.clone().subtract(pointA!!)
        blockRelativeLocation.multiply(scale.toDouble())

        val displayLocation = center.clone().add(blockRelativeLocation)

        val world = Bukkit.getWorld(block.world.uid)
        val display = world?.spawnEntity(displayLocation, EntityType.BLOCK_DISPLAY) as? BlockDisplay
        display?.block = block.blockData

        val transform = display?.transformation
        transform?.scale?.set(scale, scale, scale)
        display?.transformation = transform!!
    }

    private fun getBlocksInRegion(a: Location, b: Location): List<Block> {
        val blocks = mutableListOf<Block>()
        val world = a.world

        val minX = Math.min(a.blockX, b.blockX)
        val minY = Math.min(a.blockY, b.blockY)
        val minZ = Math.min(a.blockZ, b.blockZ)

        val maxX = Math.max(a.blockX, b.blockX)
        val maxY = Math.max(a.blockY, b.blockY)
        val maxZ = Math.max(a.blockZ, b.blockZ)

        for (x in minX..maxX) {
            for (y in minY..maxY) {
                for (z in minZ..maxZ) {
                    val block = world.getBlockAt(x, y, z)
                    if (block.type == Material.AIR) continue
                    blocks.add(world.getBlockAt(x, y, z))
                }
            }
        }

        return blocks
    }
}