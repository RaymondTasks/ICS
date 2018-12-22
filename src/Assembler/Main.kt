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
 * 获得.ORIG和.END中间的指令
 * 获得开始地址
 */
fun getStartAddr(insts: ArrayList<Instruction>): Int {
    var startIndex = -1
    var endIndex = -1
    for (i in insts.indices) {
        if (insts[i].Inst == ".ORIG") {
            if (startIndex == -1) {
                startIndex = i
            } else {
                //todo 出现了两个.ORIG
                throw Exception()
            }
        }
        if (insts[i].Inst == ".END") {
            if (endIndex == -1) {
                endIndex = i + 1
            } else {
                //todo 出现了两个.END
                throw Exception()
            }
        }
    }
    if (startIndex != -1 && endIndex != -1) {
        val startAddr = insts[startIndex].imm
        for (i in 0 until startIndex) {
            insts.removeAt(i)
        }
        for (i in endIndex until insts.size) {
            insts.removeAt(i)
        }
        return startAddr
    } else {
        //todo .ORIG和.END不闭合
        throw Exception()
    }
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
        if (it.label != null) {
            val labelAddr = table[it.label!!]
            if (labelAddr == null) {
                //todo 不存在的label
                throw Exception()
            } else {
                it.labelAddr = labelAddr
            }
        }
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
