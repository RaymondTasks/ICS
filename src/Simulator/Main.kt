package Simulator

import java.io.*
import kotlin.random.Random

fun main(args: Array<String>) {

    val fin = DataInputStream(
            BufferedInputStream(
                    FileInputStream(
                            File("D:\\Codes\\ICS\\labs\\lab1\\program.obj"))))
    init(fin)
    fin.close()
    var allCost = 0
    val testNum = 100000
    for (i in 1..testNum) {
        PC = 0x3000
        R[0] = Random.nextInt(1, 32768)
        R[1] = Random.nextInt(1, 32768)
        allCost += start()
    }
    println(allCost.toDouble() / testNum)
//    for (i in 1..32767) {
//        for (j in 1..32767) {
////            println("i = $i, j = $j")
//            PC = 0x3000
//            R[0] = i
//            R[1] = j
//            allCost += start()
//        }
//    }
//    println(allCost.toDouble() / 32767 / 32767)
}

fun init(input: DataInputStream) {
    PC = input.readShort().toInt()
    var addr = PC
    while (true) {
        val s: Int
        try {
            s = input.readShort().toInt()
        } catch (e: EOFException) {
            break
        }
        M[addr++] = s
    }
}

fun start(): Int {
    var cost = 0
    while (true) {
        if (PC in breakPoints) {
            break
        }
        if (M[PC] and 0b1_111_111_111_111_111 == 0xF025) {  //HALT
            break
        }
        if (M[PC] and 0b1_111_111_111_111_111 == 0xC1C0) {  //RET
            break
        }
//        println("PC=x${PC.toString(16).toUpperCase()} exec x${M[PC].toString(16).toUpperCase()}")
        try {
            cost += exec(M[PC])
        } catch (e: Exception) {
            print(e.message)
            break
        }
    }
    return cost
}

val breakPoints = hashSetOf<Int>()


val R = IntArray(8)
val M = IntArray(65536)

var PSR: Int = 0
var PC: Int = 0x3000

fun exec(inst: Int): Int {

    PC++

    val op = (inst shr 12) and 0b1111
    val R1 = (inst shr 9) and 0b111
    val R2 = (inst shr 6) and 0b111
    val R3 = inst and 0b111

    when (op) {
        //ADD
        0b0001 -> {
            R[R1] = R[R2] +
                    when ((inst shr 5) and 1) {
                        0 -> R[R3]
                        else -> getImm(inst, 5)
                    }
            updateNZP(R[R1])
        }

        //AND
        0b0101 -> {
            R[R1] = R[R2] and
                    when ((inst shr 5) and 1) {
                        0 -> R[R3]
                        else -> getImm(inst, 5)
                    }
            updateNZP(R[R1])
        }

        //NOT
        0b1001 -> {
            R[R1] = R[R2].inv()
            updateNZP(R[R1])
        }
        //BR
        0b0000 -> {
            if (R1 and (PSR and 0b111) != 0) {
                PC += getImm(inst, 9)
            }
        }
        //JMP
        0b1100 -> PC = R[R2]
        //JSR & JSRR
        0b0100 -> {
            R[7] = PC
            PC = when ((inst ushr 11) and 1) {
                0 -> R[R2]
                else -> PC + getImm(inst, 11)
            }
        }
        //RTI
        0b1000 -> when ((PSR ushr 15) and 1) {
            0 -> {
                PC = M[R[6]]
                R[6]++
                PSR = M[R[6]]
                R[6]++
            }
            else -> throw Exception("RTI exception")
        }
        //LD
        0b0010 -> {
            R[R1] = M[PC + getImm(inst, 9)]
            updateNZP(R[R1])
        }
        //LDI
        0b1010 -> {
            R[R1] = M[M[PC + getImm(inst, 9)]]
            updateNZP(R[R1])
        }
        //ST
        0b0011 -> M[PC + getImm(inst, 9)] = R[R1]
        //STI
        0b1011 -> M[M[PC + getImm(inst, 9)]] = R[R1]
        //LDR
        0b0110 -> {
            R[R1] = M[R[R2] + getImm(inst, 6)]
            updateNZP(R[R1])
        }
        //STR
        0b0111 -> M[R[R2] + getImm(inst, 6)] = R[R1]
        //LEA
        0b1110 -> {
            R[R1] = PC + getImm(inst, 9)
            updateNZP(R[R1])
        }
        //TRAP
        0b1111 -> {
            PC = M[inst and 0b11_111_111]
            trap()
        }

    }

//    return costTable[op] as Int
    return 1
}


fun getImm(inst: Int, length: Int): Int {
    var imm = when ((inst ushr (length - 1)) and 1) {
        0 -> 0
        else -> (-1) shl length
    }
    imm += inst and ((-1) ushr (32 - length))
    return imm
}

fun updateNZP(num: Int) {
    PSR = PSR and ((-1) shl 3)
    PSR += if (num > 0) {
        0b001
    } else if (num == 0) {
        0b010
    } else {
        0b100
    }
}

fun trap() {

}


val costTable = hashMapOf<Int, Int>(

)