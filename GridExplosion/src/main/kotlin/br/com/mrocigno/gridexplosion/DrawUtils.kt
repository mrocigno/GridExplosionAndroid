package br.com.mrocigno.gridexplosion

import kotlin.math.max
import kotlin.math.min

object DrawUtils {

    fun midPointCircleDraw(center: Pair<Int, Int>, radius: Int): List<Triple<Int, Int, Int>> {

        val temp = mutableListOf<Pair<Int, Int>>()
        val (centerX, centerY) = center

        temp.add(point(centerX - radius, centerY))
        temp.add(point(centerX + radius, centerY))

        var x = radius
        var y = 0

        temp.add(point(x + centerX, y + centerY))

        // When radius is zero only a single
        // point will be printed
        if (radius > 0) {
            temp.add(point(x + centerX, -y + centerY))
            temp.add(point(y + centerX, x + centerY))
            temp.add(point(-y + centerX, x + centerY))
        }

        // Initialising the value of P
        var p = 1 - radius
        while (x > y) {
            y++

            // Mid-point is inside or on the perimeter
            if (p <= 0) p += (2 * y) + 1 else {
                x--
                p = p + 2 * y - 2 * x + 1
            }

            // All the perimeter points have already
            // been printed
            if (x < y) break

            // Printing the generated point and its
            // reflection in the other octants after
            // translation
            temp.add(point(x + centerX, y + centerY))
            temp.add(point(-x + centerX, y + centerY))
            temp.add(point(x + centerX, -y + centerY))
            temp.add(point(-x + centerX, -y + centerY))

            // If the generated point is on the
            // line x = y then the perimeter points
            // have already been printed
            if (x != y) {
                temp.add(point(y + centerX, x + centerY))
                temp.add(point(-y + centerX, x + centerY))
                temp.add(point(y + centerX, -x + centerY))
                temp.add(point(-y + centerX, -x + centerY))
            }
        }

        return temp.groupBy { it.second }.map { entry ->
            val firstPoint = entry.value.minOf { it.first }
            val lastPoint = entry.value.maxOf { it.first }
            Triple(entry.key, min(firstPoint, lastPoint), max(firstPoint, lastPoint))
        }
    }

    private fun point(x: Int, y: Int): Pair<Int, Int> {
        return max(0, x) to max(0, y)
    }
}

fun main() {
    DrawUtils.midPointCircleDraw(0 to 0, 10)
}
