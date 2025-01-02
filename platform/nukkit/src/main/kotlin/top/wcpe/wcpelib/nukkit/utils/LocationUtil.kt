package top.wcpe.wcpelib.nukkit.utils

import cn.nukkit.block.Block
import cn.nukkit.level.Location

/**
 * 由 WCPE 在 2022/1/25 17:37 创建
 *
 * Created by WCPE on 2022/1/25 17:37
 *
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.0.9-alpha-dev-2
 */
object LocationUtil {

    @JvmStatic
    fun isInRadius(
        targetLocation: Location,
        entityLocation: Location,
        radius: Double
    ): Boolean {
        return targetLocation.distance(entityLocation) < radius
    }

    @JvmStatic
    fun isInAABB(needGo: Location, AA: Location, BB: Location): Boolean {
        val xMax = AA.getX().coerceAtLeast(BB.getX()) + 1
        val xMin = AA.getX().coerceAtMost(BB.getX())
        val yMax = AA.getY().coerceAtLeast(BB.getY())
        val yMin = AA.getY().coerceAtMost(BB.getY())
        val zMax = AA.getZ().coerceAtLeast(BB.getZ()) + 1
        val zMin = AA.getZ().coerceAtMost(BB.getZ())
        return (needGo.getX() in xMin..xMax && needGo.getY() >= yMin && needGo.getY() <= yMax
                && needGo.getZ() >= zMin && needGo.getZ() <= zMax)
    }

    @JvmStatic
    fun getAllLocation(a: Location, b: Location): List<Location> {
        val maxX = a.floorX.coerceAtLeast(b.floorX)
        val maxY = a.floorY.coerceAtLeast(b.floorY)
        val maxZ = a.floorZ.coerceAtLeast(b.floorZ)
        val minX = a.floorX.coerceAtMost(b.floorX)
        val minY = a.floorY.coerceAtMost(b.floorY)
        val minZ = a.floorZ.coerceAtMost(b.floorZ)
        val list: MutableList<Location> = mutableListOf()
        for (y in minY..maxY) {
            for (z in minZ..maxZ) {
                for (x in minX..maxX) {
                    list.add(Location(x.toDouble(), y.toDouble(), z.toDouble(), a.getLevel()))
                }
            }
        }
        return list
    }

    @JvmStatic
    fun getAllBlock(a: Location, b: Location): List<Block> {
        return getAllLocation(a, b).map { it.levelBlock }
    }
}