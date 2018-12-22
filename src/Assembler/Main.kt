package Assembler

/**
 * main函数
 * 汇编选项
 * [-o] [输出路径]
 * [-b] 生成文本形式的二进制码
 * [-h] 生成文本形式的十六进制码
 * [--help] 显示帮助
 */
fun maim(args: Array<String>) {

}

/**
 * 格式化汇编码并转化为指令对象
 * 指令和寄存器忽略大小写
 * label和字符串大小写敏感
 * @param asm 未格式化的汇编码
 * @return 指令对象列表
 */
fun format(asm: String): ArrayList<Instruction> {
    val insts = ArrayList<Instruction>()

    val originalLines = ArrayList<StringBuilder>()
    //按行分割
    asm.split(Regex("\\n+"))
            .forEach {
                originalLines.add(StringBuilder(it))
            }

    //生成指令对象
    originalLines.forEach {

        if (Regex("\\.STRINGZ", RegexOption.IGNORE_CASE) in it) {
            //存在字符串,需要特殊对待
            var inString = false       //在字符串中
            var a = -1      //字符串左边界（包括）
            var b = -1      //字符串右边界（不包括）
            //寻找字符串的范围
            for (i in 0 until it.length) {
                if (!inString) {
                    when (it[i]) {
                        ';' -> it.delete(i, it.length)      //去注释
                        '\'', '"' -> {          //字符串内容开始
                            if (a == -1) {
                                inString = true
                                a = i
                            } else {
                                //todo 出现了两个字符串
                                throw Exception()
                            }
                        }
                    }
                } else {
                    if (it[i] == it[a] && it[i - 1] != '\\') {
                        inString = false
                        b = i + 1               //字符串内容结束
                    }
                }
            }
            if (a == -1 || b == -1) {
                //todo 字符串未闭合
                throw Exception()
            }
            val tmp = ArrayList<String>()
            tmp.addAll(it.substring(0, a)
                    .replace(Regex("(\\s*;.*$)|(^\\s+)|(\\s+$)"), "")
                    .split(Regex("(\\s*,\\s*)|(\\s+)")))
            tmp.add(it.substring(a, b))
            tmp.addAll(it.substring(b)
                    .replace(Regex("(\\s*;.*$)|(^\\s+)|(\\s+$)"), "")
                    .split(Regex("(\\s*,\\s*)|(\\s+)")))
            //todo ???为什么下面这句不能加
            insts.add(Instruction(tmp.toTypedArray()))
        } else {
            //非.STRINGZ
            //去注释、首位空格
            it.replace(Regex("(\\s*;.*$)|(^\\s+)|(\\s+$)"), "")
            //分割字段
            if (it.toString() != "") {  //排除空行
                //添加一条有效指令
                insts.add(Instruction(
                        it.split(Regex("(\\s*,\\s*)|(\\s+)"))
                                .toTypedArray()))
            }
        }

    }
    return insts
}

/**
 * 创建符号链接表
 * @param insts 指令对象列表,去除了.START和.END
 * @param start 第一条指令所在的位置
 * @return 符号链接表
 */
fun creatSymbolTable(insts: ArrayList<Instruction>, start: Int): HashMap<String, Int> {
    var addr = start
    val table = HashMap<String, Int>()
    insts.forEach {
        it.addr = addr
        if (it.label != null) {
            if (it.label in table) {
                //todo label重复错误
                throw Exception()
            } else {
                //添加一个label
                table.put(it.label!!, addr)
            }
        }
        //更新label
        addr += it.length
    }
    insts.forEach {
        it.symbolTable = table
    }
    return table
}

/**
 * 生成机器码
 * @param insts 指令对象列表,去除了.START和.END,更新了label
 * @return 机器码序列
 */
fun generateMachineCode(insts: ArrayList<Instruction>): ArrayList<Short> {
    val array = ArrayList<Short>()
    insts.forEach {
        it.toMachineCode().forEach { code ->
            array.add(code)
        }
    }
    return array
}


///**
// * 对汇编码进行初步的格式化
// * @param asm 未格式化的汇编码
// */
//fun format(asm: StringBuilder): Array<String> {
//    //去注释
//    //todo 不能去除""中的分号
//    asm.replace(Regex(";.*\n"), " ")
//    asm.replace(Regex("(^\\s+)|(\\s+$)"), "")
//    //依照分隔符分割
//    //todo ""引号中的要特殊处理
//    return asm.split(Regex("(\\s*,\\s*)|(\\s+)"), Int.MAX_VALUE).toTypedArray()
//}
//
///**
// * 根据格式化后的汇编码转化为指令对象
// * 汇编码是大小写敏感的
// * @param asm 格式化后的汇编码
// */
//@kotlin.ExperimentalUnsignedTypes
//fun format(asm: Array<String>): Array<Instruction> {
//    var inline: Boolean = false     //标志是否在一条指令中
//    var haveLabel: Boolean = false
//    var type: Instruction
//
//    asm.forEachIndexed { i, str ->
//        if (inline) {
//
//        } else {
//            //下一条指令
//            inline = true
//            when (str.toUpperCase()) {
//                in instTable -> type = Instruction.valueOf(str.toUpperCase())
//                in BRTable ->
//                in pseudoTable ->
//                in asmInstTable ->
//                else ->
//            }
//            if (instTable.contains(str)) {
//                haveLabel = false
//                //BR需要单独处理
//                type = Instruction.valueOf(str.toUpperCase())
////                type = when (str) {
////                    "ADD" -> Instruction.ADD
////                    "AND" -> Instruction.AND
////                    "NOT" -> Instruction.NOT
////                    "BR", "BRn", "BRz", "BRp", "BRnz", "BRzn", "BRnp", "BRpn", "BRzp", "BRpz",
////                    "BRnzp", "BRnpz", "BRznp", "BRzpn", "BRpnz", "BRpzn" -> Instruction.BR
////                    "JMP" -> Instruction.JMP
////                    "JSR"->Instruction.JSR
////                    "JSRR"->
////                    else -> throw Exception()  //todo 抛出错误
////                }
//            } else if () else {
//                haveLabel = true
//            }
//        }
//    }
//}
//
//
//@kotlin.ExperimentalUnsignedTypes
//fun creatSymbolTable(insts: Array<Instruction>): HashMap<String, UShort> {
//    val map = HashMap<String, UShort>()
//
//}
//
////指令列表
//val instTable = hashSetOf("ADD", "AND", "NOT",
//        "JMP", "JSR", "JSRR", "RET", "RTI",
//        "LD", "LDI", "LDR", "LEA",
//        "ST", "STI", "STR",
//        "TRAP")
//val BRTable = hashSetOf("BR",
//        "BRN", "BRZ", "BRP",
//        "BRNZ", "BRZN", "BRNP", "BRPN", "BRZP", "BRPZ",
//        "BRNZP", "BRNPZ", "BRZNP", "BRZPN", "BRPNZ", "BRPZN")
//val regTable = hashSetOf("R0", "R1", "R2", "R3", "R4", "R5", "R6", "R7")
//val pseudoTable = hashSetOf("GETC", "OUT", "PUTS", "IN", "PUTSP", "HALT")
//val asmInstTable = hashSetOf(".FILL", ".STRINGZ", ".BLKW", ".START", ".END")
//
//@kotlin.ExperimentalUnsignedTypes
//enum class Instruction {
//    ADD {
//        val opCode = 0b0001u
//        override fun toMachineCode(): UShort {
//            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//        }
//    },
//    AND {
//        val opCode = 0b0101u
//        override fun toMachineCode(): UShort {
//            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//        }
//    },
//    NOT {
//        override fun toMachineCode(): UShort {
//            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//        }
//    },
//    //需要特殊对待
//    BR {
//        override fun toMachineCode(): UShort {
//            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//        }
//    },
//    JMP {
//
//    },
//    JSR {
//
//    },
//    JSRR {
//
//    },
//    RET {
//
//    },
//    RTI {
//
//    },
//    LD {
//
//    },
//    LDR {
//
//    },
//    LDI {
//
//    },
//    LEA {
//
//    },
//    ST {
//
//    },
//    STR {
//
//    },
//    STI {
//
//    },
//    TRAP {
//
//    },
//    _FILL {
//
//    },
//    _STRINGZ {
//
//    },
//    _BLKW {
//
//    },
//    _START {
//
//    },
//    _END {
//
//    }
//
//    abstract fun toMachineCode: UShort {
//
//    }
//
//}



