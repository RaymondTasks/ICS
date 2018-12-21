package Assembler;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    /**
     * -o --output 输出路径，默认'.'
     * -b --bin 输出二进制的文本
     * -h --hex 输出十六进制的文本
     *
     * @param args 输入参数
     */
    public static void main(String args[]) {

    }

    /**
     * 格式化代码
     * 去注释
     * 去每行开头空格
     *
     * @param lines 每行代码组成的字符串数组
     */
    private String[][] format(String lines[]) {
        var ret = new String[lines.length][];
        for (int i = 0; i < lines.length; i++) {
            ret[i] = lines[i]
                    .replaceAll("(;.*)|(^\\s+)|(\\s+$)", "")
                    .split("(\\s*,\\s*)|(\\s+)");
        }
        return ret;
    }

    /**
     * 创建符号表
     *
     * @param asm 格式化代码行
     * @return 符号表
     */
    private HashMap<String, Short> creatSymbolTable(String asm[][], short start) {
        var table = new HashMap<String, Short>();
        short addr = start;
        for (int i = 0; i < asm.length; i++) {
            if (asm[i].length != 0) {
                String str0 = asm[i][0].toUpperCase();
                if (InstTable.containsKey(str0) || PseudoInstTable.containsKey(str0) || AssemblerInstTable.containsKey(str0)) {
                    addr += updateAddress(str0, asm[i][1]);
                } else {
                    String str1 = asm[i][1].toUpperCase();
                    if (InstTable.containsKey(str1) || PseudoInstTable.containsKey(str1) || AssemblerInstTable.containsKey(str1)) {
                        table.put(asm[i][0], addr);
                        addr += updateAddress(str1, asm[i][2]);
                        var tmp = new String[asm[i].length - 1];
                        System.arraycopy(asm[i], 1, tmp, 0, asm[i].length - 1);
                        asm[i] = tmp;
                    } else {
                        //todo 非法指令,中断汇编
                    }
                }
            }
        }
        return table;
    }

    private short[] assemble(String asm[][], HashMap<String, Short> symbolTable) {
        var list = new ArrayList<Short>();
        for (int i = 0; i < asm.length; i++) {
            if (asm[i].length != 0) {
                if (InstTable.containsKey(asm[i][0].toUpperCase())) {
                    switch (InstTable.get(asm[i][0].toUpperCase())) {
                        case ADD:
                        case AND:
                    }
                } else if (PseudoInstTable.containsKey(asm[i][0])) {

                } else if (AssemblerInstTable.containsKey(asm[i][0])) {

                }
            }
        }
    }

    /**
     * @param asmStr 判定为指令，伪指令，或者编译器命令的字符串
     * @return addr 应该增加的值
     */
    private short updateAddress(String asmStr, String next) {
        if (InstTable.containsKey(asmStr) || PseudoInstTable.containsKey(asmStr)) {
            return 1;
        } else if (AssemblerInstTable.containsKey(asmStr)) {
            switch (AssemblerInstTable.get(asmStr)) {
                case _FILL:
                    return 1;
                case _BLKW:
                    return parseNumber(next);
                case _STRINGZ:
                    return (short) (parseString(next).length() + 1);
            }
        } else {
            //todo 输入非指令
        }
    }

    /**
     * 解析 .STRINGZ后的字符串
     *
     * @param asmStr
     * @return
     */
    private static String parseString(String asmStr) {
        if ((asmStr.charAt(0) == '\'' && asmStr.charAt(asmStr.length() - 1) == '\'')
                && (asmStr.charAt(0) == '\"' && asmStr.charAt(asmStr.length() - 1) == '\"')) {
            return asmStr.substring(1, asmStr.length() - 1).replaceAll("\\", "");
        } else {
            //todo 非法形式
        }
    }

    /**
     * 解析汇编码中的数字
     *
     * @param asmStr #开头的10进制，xX开头的16进制
     * @return 数字
     */
    private static short parseNumber(String asmStr) {
        if (asmStr.charAt(0) == '#') {
            return Short.parseShort(asmStr.substring(1), 10);
        } else if (asmStr.charAt(0) == 'x' || asmStr.charAt(0) == 'X') {
            return Short.parseShort(asmStr.substring(1), 16);
        } else {
            //todo 非法数字
        }
    }

    /**
     * 指令枚举
     */
    private enum InstType {
        ADD, AND, NOT,
        BR, BRn, BRz, BRp, BRnz, BRnp, BRzp, BRnzp,
        JMP, JSR, JSRR,
        LD, LDI, LDR, LEA,
        RET, RTI,
        ST, STI, STR,
        TRAP
    }

    /**
     * 伪指令枚举
     */
    private enum PseudoInstType {
        GETC, OUT, PUTS, IN, PUTSP, HALT
    }

    /**
     * 汇编器指令枚举
     */
    private enum AssemblerInstType {
        _FILL, _BLKW, _STRINGZ
    }

    /**
     * 寄存器枚举
     */
    private enum RegType {
        R0, R1, R2, R3, R4, R5, R6, R7
    }

    private static final HashMap<String, InstType> InstTable = new HashMap<>();
    private static final HashMap<String, PseudoInstType> PseudoInstTable = new HashMap<>();
    private static final HashMap<String, AssemblerInstType> AssemblerInstTable = new HashMap<>();
    private static final HashMap<String, RegType> RegTable = new HashMap<>();

    static {
        InstTable.put("ADD", InstType.ADD);
        InstTable.put("AND", InstType.AND);

        InstTable.put("BR", InstType.BR);
        InstTable.put("BRN", InstType.BRn);
        InstTable.put("BRZ", InstType.BRz);
        InstTable.put("BRP", InstType.BRp);
        InstTable.put("BRNZ", InstType.BRnz);
        InstTable.put("BRZN", InstType.BRnz);
        InstTable.put("BRNP", InstType.BRnp);
        InstTable.put("BRPN", InstType.BRnzp);
        InstTable.put("BRZP", InstType.BRzp);
        InstTable.put("BRPZ", InstType.BRzp);
        InstTable.put("BRNZP", InstType.BRnzp);
        InstTable.put("BRNPZ", InstType.BRnzp);
        InstTable.put("BRZNP", InstType.BRnzp);
        InstTable.put("BRZPN", InstType.BRnzp);
        InstTable.put("BRPNZ", InstType.BRnzp);
        InstTable.put("BRPZN", InstType.BRnzp);

        InstTable.put("JMP", InstType.JMP);
        InstTable.put("JSR", InstType.JSR);
        InstTable.put("JSRR", InstType.JSRR);
        InstTable.put("LD", InstType.LD);
        InstTable.put("LDI", InstType.LDI);
        InstTable.put("LDR", InstType.LDR);
        InstTable.put("LEA", InstType.LEA);
        InstTable.put("NOT", InstType.NOT);
        InstTable.put("RET", InstType.RET);
        InstTable.put("RTI", InstType.RTI);
        InstTable.put("ST", InstType.ST);
        InstTable.put("STI", InstType.STI);
        InstTable.put("STR", InstType.STR);
        InstTable.put("TRAP", InstType.TRAP);

        PseudoInstTable.put("GETC", PseudoInstType.GETC);
        PseudoInstTable.put("OUT", PseudoInstType.OUT);
        PseudoInstTable.put("PUTS", PseudoInstType.PUTS);
        PseudoInstTable.put("IN", PseudoInstType.IN);
        PseudoInstTable.put("PUTSP", PseudoInstType.PUTSP);
        PseudoInstTable.put("HALT", PseudoInstType.HALT);

        AssemblerInstTable.put(".FILL", AssemblerInstType._FILL);
        AssemblerInstTable.put(".BLKW", AssemblerInstType._BLKW);
        AssemblerInstTable.put(".STRINGZ", AssemblerInstType._STRINGZ);

        RegTable.put("R0", RegType.R0);
        RegTable.put("R1", RegType.R1);
        RegTable.put("R2", RegType.R2);
        RegTable.put("R3", RegType.R3);
        RegTable.put("R4", RegType.R4);
        RegTable.put("R5", RegType.R5);
        RegTable.put("R6", RegType.R6);
        RegTable.put("R7", RegType.R7);
    }
//
//
////    private static boolean isBR(String line[]) {
////        //todo
////    }
////
////    private static boolean is_STRINGZ(String line[]) {
////        //todo
////    }
//
//    /**
//     * @param line
//     * @return
//     */
////    private static String[] parseBR(String line[]) {
////        //todo
////    }
////
////    private static String[] parse_STRINGZ(String line) {
////        //todo
////    }
//
//    /**
//     * 格式化汇编代码
//     * 去注释，每行开头结尾的空格，空行，转大写
//     * 然后按空格拆分
//     * BR和.STRINGZ需要特别处理
//     *
//     * @param lines 未格式化的汇编代码
//     * @return 格式化的汇编码
//     */
//    private static String[][] format(String lines[]) {
//        var list = new ArrayList<String[]>();
//        for (int i = 0; i < lines.length; i++) {
//            //去注释
//            lines[i] = lines[i].replaceFirst(";.*", "");
//            //去开头结尾空格
//            lines[i] = lines[i].replaceFirst("(^\\s+)|(\\s+$)", "");
//            //去空行，拆分空格，BR和.STRINGZ特殊处理
//            if (lines[i].length() != 0) {
//                //分三类讨论，含有BR，含有.STRINGZ，其他
////                var tmp = lines[i].split("\\s+");
////                if (isBR(tmp)) {
////                    tmp = parseBR(tmp);
////                } else if (is_STRINGZ(tmp)) {
////                    tmp = parse_STRINGZ(lines[i]);
////                }
////                list.add(tmp);
//            }
//        }
//        return (String[][]) list.toArray();
//    }
//
//    /**
//     * 接受2 8 10 16进制的输入
//     *
//     * @param str 文本格式的数字
//     * @return str代表的数字
//     */
////    private static short parseInt(String str) {
////        //todo
////    }
//
//    /**
//     * 解析文本，接受" " 或者 ' '
//     *
//     * @param str 带引号文本
//     * @return 去除引号
//     */
////    private static String parseString(String str) {
////        //todo
////    }
//
//
//    /**
//     * 创建符号表，并去除Label
//     * .ORIG和.END已去除
//     * .FILL 在这行填充立即数
//     * .BLKW 留一定长度空位
//     * .STRINGZ 写入字符串，最后的地址写入x0000
//     *
//     * @param lines 格式化的汇编码
//     * @return 符号表
//     */
//    private static Hashtable<String, Short> creatSymbolTable(String lines[][], short start) {
//        var table = new Hashtable<String, Short>();
//        short addr = start;
//        for (int i = 0; i < lines.length; i++) {
//            var t = InstTable.get(lines[i][0]);
//            //识别address label
//            if (t == null) {
//                table.put(lines[i][0], addr);
//                var tmp = new String[lines[i].length - 1];
//                for (int j = 0; j < tmp.length; j++) {
//                    tmp[i] = lines[i][j + 1];
//                }
//                lines[i] = tmp;
//                t = InstTable.get(tmp[0]);
//                if (t == null) {
//                    //todo exception 非法指令处理
//                }
//            }
//            //计算地址
////            switch (t) {
////                case _BLKW:
////                    addr += parseInt(lines[i][1]) - 1;
////                    break;
////                case _STRINGZ:
////                    addr += parseString(lines[i][1]).length();
////                    break;
////            }
////            addr++;
//        }
//        return table;
//    }

//    private static
}
