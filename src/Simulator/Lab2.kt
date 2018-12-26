package Simulator

import Assembler.assemble
import java.io.*
import kotlin.random.Random

fun lab2Test() {
    val asmFile = File("D:\\Codes\\ICS\\labs\\lab2\\program.asm")
    val objFile = File("D:\\Codes\\ICS\\labs\\lab2\\program2.obj")

    assemble(asmFile, objFile)

    val fin = DataInputStream(
            BufferedInputStream(
                    FileInputStream(objFile)))
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
    val initArr = insts.toIntArray()

    var allCost = 0.0
    val testNum = 10000

    for (count in 1..testNum) {
        val startPC = Random.nextInt(0x3000, 0xC001)
        init(startPC, initArr)

        inputChars.clear()
        val answer = lab2Main()
        setInputChars(inputChars)

        allCost += start(startPC)

        when (R[5]) {
            0 -> {
                if (R[0].toShort() == answer) {
//                    println("pass")
                } else {
                    println("filed")
                    return
                }
            }
            1 -> println("overflow")
            2 -> println("underflow")
        }
    }

    println("avg = ${allCost / testNum}")

}


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

val inputChars = ArrayList<Char>()

fun lab2GETC(): Short {
//    val c = Random.nextInt(0, 128).toChar()
    val c = Random.nextInt(48, 58).toChar()
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