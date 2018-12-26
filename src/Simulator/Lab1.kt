package Simulator

import java.io.BufferedInputStream
import java.io.DataInputStream
import java.io.File
import java.io.FileInputStream
import kotlin.random.Random

fun lab1Test() {
    val fin = DataInputStream(
            BufferedInputStream(
                    FileInputStream(
                            File("D:\\Codes\\ICS\\labs\\lab1\\program.obj"))))
    init(fin)
    fin.close()
    var allCost = 0.0
    val testNum = 1000000

    //随机测试
    for (i in 1..testNum) {
        PC = 0x3000
        R[0] = Random.nextInt(1, Short.MAX_VALUE.toInt())
        R[1] = Random.nextInt(1, Short.MAX_VALUE.toInt())
//        println("R[0]=x${R[0].toString(16).toUpperCase()}, " +
//                "R[1]=x${R[1].toString(16).toUpperCase()}")
        allCost += start()
    }
    println(allCost / testNum)

    //遍历测试
//    for (i in 1 until Short.MAX_VALUE) {
//        for (j in 1 until Short.MAX_VALUE) {
////            println("i = $i, j = $j")
//            PC = 0x3000
//            R[0] = i
//            R[1] = j
//            allCost += start()
//        }
//    }
//    println(allCost / Short.MAX_VALUE / Short.MAX_VALUE)
}