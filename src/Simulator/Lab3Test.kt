package Simulator

import java.io.*
import kotlin.random.Random

/**
 * 部分函数直接调用lab2
 */

fun main(args: Array<String>) {

    val objFile = File("C:\\Users\\rayomnd\\Desktop\\lab2.obj")

    val insts = ArrayList<Int>()
    DataInputStream(
            BufferedInputStream(
                    FileInputStream(objFile))).apply {
        readShort()
        try {
            while (true) {
                insts.add(readShort().toInt())
            }
        } catch (e: EOFException) {
            close()
        }
    }

    val initArr = insts.toIntArray()

    var allCost = 0.0
    val testNum = 100

    for (end in 48..80) {
        startAscii = 0
        endAscii = end
        for (count in 1..testNum) {
            val startPC = Random.nextInt(0x3000, 0xC001)
            init(startPC, initArr)

            inputChars.clear()
            val answer = lab2Main()
            setInputChars(inputChars)

            allCost += start(startPC)

            if (R[0].toShort() != answer) {
                println("char < $endAscii, failed")
                return
            }

        }

        println("char < $endAscii, pass, avg = ${allCost / testNum}")
    }


}