package Simulator

import java.io.DataInputStream
import java.io.EOFException

/**
 * 初始化
 * @param input obj文件数据流
 */
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

/**
 * 开始执行
 * @return 执行到HALT是的总cost
 */
fun start(): Int {
    var cost = 0
    while (true) {
        if (PC in breakPoints) {
            break
        }
        if (M[PC] and 0b1_111_111_111_111_111 == 0xF025) {  //HALT
            break
        }
//        if (M[PC] and 0b1_111_111_111_111_111 == 0xC1C0) {  //RET
//            break
//        }
//        println("PC=x${PC.toString(16).toUpperCase()} " +
//                "exec x${M[PC].toString(16).toUpperCase()}")
        try {
            cost += exec(M[PC])
        } catch (e: Exception) {
            e.printStackTrace()
            print(e.message)
            break
        }
    }
    return cost
}

//用于调试的 break point
val breakPoints = hashSetOf<Int>()

val R = IntArray(8)
val M = IntArray(65536)

var PSR: Int = 0
var PC: Int = 0x3000

/**
 * 执行指令
 * TRAP视为原子指令
 * @return 执行的cost
 */
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
            R[7] = PC + 1
            PC = when ((inst ushr 11) and 1) {
                0 -> R[R2]
                else -> PC + getImm(inst, 11)
            }
        }
        //RTI
        0b1000 -> when ((PSR ushr 15) and 1) {
            //todo RTI指令暂定
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
            //TRAP的cost单独计算
            val trapVector = inst and 0b11_111_111
            R[7] = PC + 1
            PC = M[trapVector]
            return trap(trapVector) + costTable[0b1111] as Int
        }

    }

    return costTable[op] as Int

}

/**
 * 从指令中提取有符号立即数
 * @param inst 指令
 * @param length 立即数位数
 * @return 立即数
 */
fun getImm(inst: Int, length: Int): Int {
    var imm = when ((inst ushr (length - 1)) and 1) {
        0 -> 0
        else -> (-1) shl length
    }
    imm += inst and ((-1) ushr (Int.SIZE_BITS - length))
    return imm
}

/**
 * 更新nzp
 */
fun updateNZP(num: Int) {
    PSR = (PSR and ((-1) shl 3)) +
            if (num > 0) {
                0b001
            } else if (num == 0) {
                0b010
            } else {
                0b100
            }
}

/**
 * 原子化TRAP调用
 */
fun trap(trapVector: Int): Int {
    when (trapVector) {
        0x21 -> {

        }
        0x22 -> {

        }
        0x23 -> {

        }
        0x24 -> {

        }
        0x25 -> {

        }
    }
}

//指令cost列表
val costTable = hashMapOf(
        Pair(0b0001, 1),    //ADD
        Pair(0b0101, 1),    //AND
        Pair(0b1001, 1),    //NOT
        Pair(0b1110, 1),    //LEA

        Pair(0b0000, 2),    //BR
        Pair(0b1100, 2),    //JMP
        Pair(0b0100, 2),    //JSR & JSRR
        Pair(0b1111, 2),    //TRAP

        Pair(0b0010, 4),    //LD
        Pair(0b0011, 4),    //ST
        Pair(0b0110, 4),    //LDR
        Pair(0b0111, 4),    //STR

        Pair(0b1010, 8),    //LDI
        Pair(0b1011, 8),    //STI

        Pair(0b1000, 8) //todo RTI?
)