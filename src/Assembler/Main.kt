package Assembler

import java.io.*

/**
 * main函数
 * 汇编选项
 * [-o] 输出路径 默认输出到
 */
fun main(args: Array<String>) {
//    regexTest()
    when (args.size) {
        1 -> {
            if (args[0].startsWith('-')) {
                println("Illegal parameters")
            } else {
                val file = File(args[0])
                if (!file.exists()) {
                    println("No input files")
                } else {
                    val input = File(args[0])
                    assemble(input, File(input.parent + "\\" + "program.obj"))
                }
            }
        }
        3 -> {
            if (args[0].startsWith('-')
                    || args[1] != "-o"
                    || args[2].startsWith('-')) {
                println("Illegal parameters")
            } else {
                val file = File(args[0])
                if (!file.exists()) {
                    println("No input files")
                } else {
                    assemble(File(args[0]), File(args[2]))
                }
            }
        }
        else -> {
            println("Illegal parameters")
        }
    }

}

fun assemble(input: File, output: File) {
    val fin = BufferedReader(FileReader(input))
    val insts = format(fin.readLines().toTypedArray())
    val start = creatSymbolTable(insts)
    val out = DataOutputStream(
            BufferedOutputStream(
                    FileOutputStream(output)))
    generateMachineCode(insts, start).forEach {
        println(it.toInt().toString(16))
        out.writeShort(it.toInt())
    }
    out.flush()
}


fun regexTest() {
    println("#-123".matches(Regex("^#\\-?\\d+$")))
    println("XADf".matches(Regex("^x[\\dA-F]+$", RegexOption.IGNORE_CASE)))
    "   AdD   R0, r0, #-16; cg  ;27r2263"
            .replace(Regex("(\\s*;.*$)|(^\\s+)|(\\s+$)"), "")
            .split(Regex("(\\s*,\\s*)|(\\s+)")).forEach {
                println(it)
            }
}

/**
 * 格式化汇编码并转化为指令对象
 * 指令和寄存器忽略大小写
 * label和字符串大小写敏感
 * @param asm 未格式化的汇编码
 * @return 指令对象列表
 */
fun format(lines: Array<String>): ArrayList<Instruction> {
    val insts = ArrayList<Instruction>()

    //按行分割
//    val lines = asm.split(Regex("\\n\\s*")).toTypedArray()

//    asm.split(Regex("\\n\\s*")).forEach {
//        originalLines.add(StringBuilder(it))
//    }

    //生成指令对象
    lines.forEach {

        if (Regex("\\.STRINGZ", RegexOption.IGNORE_CASE) in it) {
            //可能存在字符串,需要特殊对待
            var inString = false       //在字符串中
            var a = -1      //字符串左边界（包括）
            var b = -1      //字符串右边界（不包括）
            //寻找字符串的范围
            findStr@ for (i in 0 until it.length) {
                if (!inString) {
                    when (it[i]) {
                        ';' -> break@findStr    //找到注释，跳出
                        '\'', '"' -> {          //字符串内容开始
                            if (a == -1) {
                                inString = true
                                a = i
                            } else {
                                //todo 字符串闭合错误
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
//            println(a)
//            println(b)
            if (a == -1 || b == -1) {
                //todo 字符串未闭合
                throw Exception()
            }
            val words = ArrayList<String>()

            var asmPart = it.substring(0, a)
                    .replace(Regex("(\\s*;.*$)|(^\\s+)|(\\s+$)"), "")
            if (asmPart != "") {
                words.addAll(
                        asmPart.split(Regex("(\\s*,\\s*)|(\\s+)")))
            }

            words.add(it.substring(a, b))

            asmPart = it.substring(b)
                    .replace(Regex("(\\s*;.*$)|(^\\s+)|(\\s+$)"), "")
            if (asmPart != "") {
                words.addAll(asmPart.split(Regex("(\\s*,\\s*)|(\\s+)")))
            }

            insts.add(Instruction(words.toTypedArray()))

        } else {
            //非.STRINGZ
            //去注释、首位空格
            val str = it.replace(Regex("(\\s*;.*$)|(^\\s+)|(\\s+$)"), "")
//            println(str)
            //分割字段
            if (str != "") {  //排除空行
                //添加一条有效指令
                insts.add(Instruction(
                        str.split(Regex("(\\s*,\\s*)|(\\s+)"))
                                .toTypedArray()))
            }
        }

    }
    return insts
}

/**
 * 获得.ORIG和.END中间的指令
 * 获得开始地址
 */
//fun getStartAddr(insts: ArrayList<Instruction>): Int {
//    val startIndex1 = insts.indexOfFirst {
//        it.Inst == ".ORIG"
//    }
//    val startIndex2 = insts.indexOfLast {
//        it.Inst == ".ORIG"
//    }
//    if (startIndex1 == -1 || startIndex1 != startIndex2) {
//        return -1
//    }
//
//    val endIndex1 = insts.indexOfLast {
//        it.Inst == ".END"
//    }
//    val endIndex2 = insts.indexOfFirst {
//        it.Inst == ".END"
//    }
//    if (endIndex1 == -1 || endIndex1 != endIndex2) {
//        return -1
//    }
//
//    val tmp = insts[startIndex1].imm
//    while (insts.size > endIndex1) {
//        insts.removeAt(endIndex1)
//    }
//    for (i in 0..startIndex1) {
//        insts.removeAt(0)
//    }
//    return tmp
//}

/**
 * 创建符号链接表
 * @param insts 指令对象列表,去除了.START和.END
 * @param start 第一条指令所在的位置
 * @return 符号链接表
 */
fun creatSymbolTable(insts: ArrayList<Instruction>): Int {
    val startIndex1 = insts.indexOfFirst {
        it.Inst == ".ORIG"
    }
    val startIndex2 = insts.indexOfLast {
        it.Inst == ".ORIG"
    }
    if (startIndex1 == -1 || startIndex1 != startIndex2) {
        throw Exception()
    }

    val endIndex1 = insts.indexOfLast {
        it.Inst == ".END"
    }
    val endIndex2 = insts.indexOfFirst {
        it.Inst == ".END"
    }
    if (endIndex1 == -1 || endIndex1 != endIndex2) {
        throw Exception()
    }

    //获得起始地址
    val start = insts[startIndex1].imm
    var addr = start

    while (insts.size > endIndex1) {
        insts.removeAt(endIndex1)
    }
    for (i in 0..startIndex1) {
        insts.removeAt(0)
    }

    val table = HashMap<String, Int>()
    insts.forEach {
        it.addr = addr
        if (it.addrLabel != null) {
            if (it.addrLabel in table) {
                //todo label重复错误
                throw Exception()
            } else {
                //添加一个label
                table[it.addrLabel!!] = addr
            }
        }
        //更新label
        addr += it.length
    }

    insts.forEach {
        it.replaceLabel(table)
    }

    return start
}

/**
 * 生成机器码
 * @param insts 指令对象列表,去除了.START和.END,更新了label
 * @return 机器码序列
 */
fun generateMachineCode(insts: ArrayList<Instruction>, start: Int): ArrayList<Short> {
    val array = arrayListOf(start.toShort())
    insts.forEach {
        it.toMachineCode().forEach { code ->
            array.add(code)
        }
    }
    return array
}
