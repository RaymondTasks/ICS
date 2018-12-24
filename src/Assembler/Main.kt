package Assembler

import java.io.*

/**
 * main函数
 */
fun main(args: Array<String>) {
    if (args.size == 1) {
        val input = File(args[0])
        if (!input.exists()) {
            println("No input files")
        } else {
            assemble(input, File("${input.parent}/${input.nameWithoutExtension}.obj"))
        }
    } else {
        println("No input files")
    }

}

fun assemble(input: File, output: File) {
    val fin = BufferedReader(FileReader(input))

    //格式化
    val insts = format(fin.readLines().toTypedArray())

    //创建符号链接表
    val start = creatSymbolTable(insts)

    val out = DataOutputStream(
            BufferedOutputStream(
                    FileOutputStream(output)))
    //生成机器码
    generateMachineCode(insts, start).forEach {
        println(it.toInt().toString(16))
        out.writeShort(it.toInt())
    }
    out.flush()
}

/**
 * 格式化汇编码并转化为指令对象
 * 指令和寄存器忽略大小写
 * label和字符串大小写敏感
 * @param lines 未格式化的汇编码
 * @return 指令对象列表
 */
fun format(lines: Array<String>): ArrayList<Instruction> {
    val insts = ArrayList<Instruction>()

    //生成指令对象
    lines.forEachIndexed() { lineNumber, it ->

        if (it.indexOf('"') != -1 || it.indexOf('\'') != -1) {

            //可能存在字符串,需要特殊处理

            var a = -1      //字符串左边界（包括）
            var b = -1      //字符串右边界（不包括）

            //寻找字符串的范围
            findStr@
            for (i in 0 until it.length) {
                if (a == -1) {
                    when (it[i]) {
                        ';' -> break@findStr
                        '\'', '"' -> {
                            a = i
                        }
                    }
                } else if (it[i] == it[a] && it[i - 1] != '\\') {
                    b = i + 1               //字符串内容结束
                    break@findStr

                }
            }

            if (a == -1 || b == -1) {
                //todo 字符串未闭合
                throw Exception()
            }

            val left = it.substring(0, a)
            val string = it.substring(a, b)
            val right = it.substring(b)

            //字符串左右要有空格和别的字段隔离
            if (left.isNotEmpty() && !left.matches(Regex("\\s$"))) {
                throw Exception()
            }
            if (right.isNotEmpty() && !right.matches(Regex("^\\s"))) {
                throw Exception()
            }

            val words = ArrayList<String>()

            words.addAll(left
                    .replace(Regex("(\\s*;.*$)|(^\\s+)|(\\s+$)"), "")
                    .split(Regex("(\\s*,\\s*)|(\\s+)"))
                    .toTypedArray())
            words.add(string)
            words.addAll(right
                    .replace(Regex("(\\s*;.*$)|(^\\s+)|(\\s+$)"), "")
                    .split(Regex("(\\s*,\\s*)|(\\s+)"))
                    .toTypedArray())

            insts.add(Instruction(words.toTypedArray(), it, lineNumber))

        } else {

            //去注释、首位空格
            val str = it.replace(Regex("(\\s*;.*$)|(^\\s+)|(\\s+$)"), "")
            //分割字段
            if (str.isNotEmpty()) {  //排除空行
                //添加一条指令
                insts.add(Instruction(str
                        .split(Regex("(\\s*,\\s*)|(\\s+)"))
                        .toTypedArray()
                        , it, lineNumber))
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
