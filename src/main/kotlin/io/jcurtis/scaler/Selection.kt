package io.jcurtis.scaler

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import kotlin.math.max
import kotlin.math.min

class Selection {
    var pointA: Location? = null
    var pointB: Location? = null

    fun getBlocks(): List<Block> {
        if (pointA == null || pointB == null) return emptyList()

        val blocks = mutableListOf<Block>()
        val world = pointA!!.world

        val minX = min(pointA!!.blockX, pointB!!.blockX)
        val minY = min(pointA!!.blockY, pointB!!.blockY)
        val minZ = min(pointA!!.blockZ, pointB!!.blockZ)

        val maxX = max(pointA!!.blockX, pointB!!.blockX)
        val maxY = max(pointA!!.blockY, pointB!!.blockY)
        val maxZ = max(pointA!!.blockZ, pointB!!.blockZ)

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