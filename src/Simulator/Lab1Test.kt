package Simulator

import java.io.*
import kotlin.random.Random


/**
 * lab1的测试程序
 */
fun main(args: Array<String>) {
    val fin = DataInputStream(
            BufferedInputStream(
                    FileInputStream(
                            File("D:\\Codes\\ICS\\labs\\lab1\\program.obj"))))
    val insts = ArrayList<Int>()
    fin.readShort()
    while (true) {
        try {
            insts.add(fin.readShort().toInt())
        } catch (e: EOFException) {
            fin.close()
            break
        }
    }
    init(0x3000, insts.toIntArray())

    var allCost = 0.0
    val testNum = 1000000

    //随机测试
    for (i in 1..testNum) {
        R[0] = Random.nextInt(1, Short.MAX_VALUE.toInt())
        R[1] = Random.nextInt(1, Short.MAX_VALUE.toInt())
//        println("R[0]=x${R[0].toString(16).toUpperCase()}, " +
//                "R[1]=x${R[1].toString(16).toUpperCase()}")
        allCost += start(0x3000)
    }
    println(allCost / testNum)

    //遍历测试
//    for (i in 1 until Short.MAX_VALUE) {
//        for (j in 1 until Short.MAX_VALUE) {
////            println("i = $i, j = $j")
//            R[0] = i
//            R[1] = j
//            allCost += start(0x3000)
//        }
//    }
//    println(allCost / Short.MAX_VALUE / Short.MAX_VALUE)
}