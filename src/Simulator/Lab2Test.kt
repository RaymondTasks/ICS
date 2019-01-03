package Simulator

import java.io.*
import kotlin.random.Random


/**
 * lab2的测试程序
 */
fun main(args: Array<String>) {

//    val asmFile = File("D:\\Codes\\ICS\\labs\\lab2\\program.asm")
    val objFile = File("D:\\Codes\\ICS\\labs\\lab2\\program.obj")

//    assemble(asmFile, objFile)

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
            val startPC = Random.nextInt(0x3000, 0xC001)
            init(startPC, initArr)

            inputChars.clear()
            System.gc()     //防止爆内存

            val answer = lab2Main()
            setInputChars(inputChars)

            allCost += start(startPC)

            when (PC) {
                //如果没有发生栈溢出，则在起始位置+2的地方遇到HALT
                startPC + 2 -> {
                    if (R[0].toShort() != answer) {
                        println("char < $end, failed")
                        return
                    }
                }
                //发生了栈溢出
                else -> {
                    println("stack overflow or underflow")
                }
            }
        }

        println("char < $end, pass, avg = ${allCost.toDouble() / testNum}")
    }


}

//标准程序输入的字符流，相同的字符流传递给模拟程序
val inputChars = ArrayList<Char>()


fun lab2Main(): Short {
    val n = lab2GETC() - 48
    return lab2Func(n.toShort(), 0, 0, 0, 0, 0, 0)
}


fun lab2Func(n: Short, a: Short, b: Short, c: Short,
             d: Short, e: Short, f: Short): Short {
    val t = lab2GETC() - 48 + a + b + c + d + e + f
    if (n > 1) {
        val x = lab2Func((n - 1).toShort(), a, b, c, d, e, f)
        val y = lab2Func((n - 2).toShort(), a, b, c, d, e, f)
        return (x + y + t - 1).toShort()
    } else {
        return t.toShort()
    }
}

var startAscii = 48
var endAscii = 58

fun lab2GETC(): Short {
//    val c = Random.nextInt(0, 128).toChar()
//    val c = Random.nextInt(48, 58).toChar()
    val c = Random.nextInt(startAscii, endAscii).toChar()
    inputChars.add(c)
    return c.toShort()
}


//typedef int i16;
//typedef unsigned int u16;
//
//i16 func(i16 n, i16 a, i16 b, i16 c, i16 d, i16 e, i16 f){ //Lots of arguments, hah?
//    i16 t = GETC() - '0' + a + b + c + d + e + f;
//    if(n > 1){
//        i16 x = func(n - 1, a, b, c, d, e, f);
//        i16 y = func(n - 2, a ,b, c, d, e, f);
//        return x + y + t - 1;
//    }else{
//        return t;
//    }
//}
//
//i16 main(void){
//    i16 n = GETC() - '0';
//    return func(n, 0, 0, 0, 0, 0, 0);
//}
//
//_Noreturn void __start(){
//    /*
//        Here is where this program actually starts executing.
//        Complete this function to do some initialization in your compiled assembly.
//        TODO: Set up C runtime.
//    */
//    u16 __R0 = main(); //The return value of function main() should be moved to R0.
//    HALT();
//}