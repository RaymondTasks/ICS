package Simulator

import java.io.*

/**
 * 部分函数直接调用lab2
 */

fun main(args: Array<String>) {

    val objFile = File("D:\\Codes\\ICS\\labs\\lab3\\program.obj")
//    val objFile = File("D:\\Codes\\ICS\\labs\\lab3\\program_original.obj")
//    val objFile = File("C:\\Users\\rayomnd\\Desktop\\lab3.obj")

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

    var allCost = 0L
    val testNum = 100

    for (end in 48..85) {
        startAscii = 0
        endAscii = end
        for (count in 1..testNum) {
            init(0x3000, initArr)

            inputChars.clear()
            System.gc()     //防止爆内存

            val answer = lab2Main()
            setInputChars(inputChars)

            allCost += start(0x3000)

            if (M[R[6]].toShort() != answer) {
                println("char < $end, failed")
                return
            }

        }

        println("char < $end, pass, avg = ${allCost.toDouble() / testNum}")
    }


}