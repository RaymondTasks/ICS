package Assembler

fun maim(args: Array<String>) {

}

/**
 * 对汇编码进行初步的格式化
 * @param asm 未格式化的汇编码
 */
fun format(asm: StringBuilder): Array<String> {
    //去注释
    //todo 不能去除""中的分号
    asm.replace(Regex(";.*\n"), " ")
    asm.replace(Regex("(^\\s+)|(\\s+$)"), "")
    //依照分隔符分割
    //todo ""引号中的要特殊处理
    return asm.split(Regex("(\\s*,\\s*)|(\\s+)"), Int.MAX_VALUE).toTypedArray()
}

/**
 * 根据格式化后的汇编码转化为指令对象
 * 汇编码是大小写敏感的
 * @param asm 格式化后的汇编码
 */
@kotlin.ExperimentalUnsignedTypes
fun format(asm: Array<String>): Array<Instruction> {
    var inline: Boolean = false     //标志是否在一条指令中
    var haveLabel: Boolean = false
    var type: Instruction

    asm.forEachIndexed { i, str ->
        if (inline) {

        } else {
            //下一条指令
            inline = true
            when (str.toUpperCase()) {
                in instTable -> type = Instruction.valueOf(str.toUpperCase())
                in BRTable ->
                in pseudoTable ->
                in asmInstTable ->
                else ->
            }
            if (instTable.contains(str)) {
                haveLabel = false
                //BR需要单独处理
                type = Instruction.valueOf(str.toUpperCase())
//                type = when (str) {
//                    "ADD" -> Instruction.ADD
//                    "AND" -> Instruction.AND
//                    "NOT" -> Instruction.NOT
//                    "BR", "BRn", "BRz", "BRp", "BRnz", "BRzn", "BRnp", "BRpn", "BRzp", "BRpz",
//                    "BRnzp", "BRnpz", "BRznp", "BRzpn", "BRpnz", "BRpzn" -> Instruction.BR
//                    "JMP" -> Instruction.JMP
//                    "JSR"->Instruction.JSR
//                    "JSRR"->
//                    else -> throw Exception()  //todo 抛出错误
//                }
            } else if () else {
                haveLabel = true
            }
        }
    }
}


@kotlin.ExperimentalUnsignedTypes
fun creatSymbolTable(insts: Array<Instruction>): HashMap<String, UShort> {
    val map = HashMap<String, UShort>()

}

//指令列表
val instTable = hashSetOf("ADD", "AND", "NOT",
        "JMP", "JSR", "JSRR", "RET", "RTI",
        "LD", "LDI", "LDR", "LEA",
        "ST", "STI", "STR",
        "TRAP")
val BRTable = hashSetOf("BR",
        "BRN", "BRZ", "BRP",
        "BRNZ", "BRZN", "BRNP", "BRPN", "BRZP", "BRPZ",
        "BRNZP", "BRNPZ", "BRZNP", "BRZPN", "BRPNZ", "BRPZN")
val regTable = hashSetOf("R0", "R1", "R2", "R3", "R4", "R5", "R6", "R7")
val pseudoTable = hashSetOf("GETC", "OUT", "PUTS", "IN", "PUTSP", "HALT")
val asmInstTable = hashSetOf(".FILL", ".STRINGZ", ".BLKW", ".START", ".END")

@kotlin.ExperimentalUnsignedTypes
enum class Instruction {
    ADD {
        val opCode = 0b0001u
        override fun toMachineCode(): UShort {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    },
    AND {
        val opCode = 0b0101u
        override fun toMachineCode(): UShort {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    },
    NOT {
        override fun toMachineCode(): UShort {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    },
    //需要特殊对待
    BR {
        override fun toMachineCode(): UShort {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    },
    JMP {

    },
    JSR {

    },
    JSRR {

    },
    RET {

    },
    RTI {

    },
    LD {

    },
    LDR {

    },
    LDI {

    },
    LEA {

    },
    ST {

    },
    STR {

    },
    STI {

    },
    TRAP {

    },
    _FILL {

    },
    _STRINGZ {

    },
    _BLKW {

    },
    _START {

    },
    _END {

    }

    abstract fun toMachineCode: UShort {

    }

}



