package Assembler

fun main(args:Array<String>){
//    regexTest()
}

fun regexTest() {
    println("#-123".matches(Regex("^#-?\\d+$")))
    println("XADf".matches(Regex("^x[\\dA-F]+$", RegexOption.IGNORE_CASE)))
    "   AdD   R0, r0, #-16; cg  ;27r2263"
            .replace(Regex("(\\s*;.*$)|(^\\s+)|(\\s+$)"), "")
            .split(Regex("(\\s*,\\s*)|(\\s+)"))
            .forEach {
                println(it)
            }
}