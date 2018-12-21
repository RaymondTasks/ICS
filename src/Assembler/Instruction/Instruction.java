//package Assembler.Instruction;
//
//public class Instruction {
//    public
//
//    protected static final byte R0 = 0, R1 = 1, R2 = 2, R3 = 3,
//            R4 = 4, R5 = 5, R6 = 6, R7 = 7;
//
//    private Type type;
//
//    public Type getType() {
//        return type;
//    }
//
//    private Instruction(Type type) {
//        this.type = type;
//    }
//
//    private byte DR, SR, SR1, SR2, BaseR;
//    private int imm, PCoffset;
//    private int trapvect;
//    private boolean n, z, p;
//
//    /**
//     * 接受格式化后的指令，生成对应的指令实例
//     *
//     * @param str 格式化的指令
//     */
//    public Instruction(String str[]) {
//        switch (str[0]) {
//            case "ADD":
//                if (str[3].charAt(0) == 'R') {  //不带立即数的类型
//                    type = Type.ADD;
//                    SR2 = StringToReg(str[3]);
//                } else {
//                    type = Type.ADD_imm;
//                    imm = StringToInt(str[3]);
//                }
//                DR = StringToReg(str[1]);
//                SR1 = StringToReg(str[2]);
//                break;
//            case "AND":
//                if (str[3].charAt(0) == 'R') {  //不带立即数的类型
//                    type = Type.AND;
//                    SR2 = StringToReg(str[3]);
//                } else {
//                    type = Type.AND_imm;
//                    imm = StringToInt(str[3]);
//                }
//                DR = StringToReg(str[1]);
//                SR1 = StringToReg(str[2]);
//                break;
//            case "NOT":
//                type = Type.NOT;
//                DR = StringToReg(str[1]);
//                SR = StringToReg(str[2]);
//                break;
//            case "BR":
//            case "BRN":
//            case "BRZ":
//            case "BRP":
//            case "BRNZ":
//            case "BRNP":
//            case "BRZP":
//            case "BRNZP":
//                type = Type.BR;
//                parseBR(str[0].substring(2));
//                break;
//            case "JMP":
//                type = Type.JMP;
//                BaseR = StringToReg(str[1]);
//                break;
//            case "JSR":
//                type = Type.JSR;
//                PCoffset = StringToInt(str[1]);
//                break;
//            case "JSRR":
//                type = Type.JSRR;
//                BaseR = StringToReg(str[1]);
//                break;
//            case "LD":
//                type = Type.LD;
//                DR = StringToReg(str[1]);
//                PCoffset = StringToInt(str[2]);
//                break;
//            case "LDI":
//                type = Type.LDI;
//                DR = StringToReg(str[1]);
//                PCoffset = StringToInt(str[2]);
//                break;
//            case "LDR":
//                type = Type.LDR;
//                DR = StringToReg(str[1]);
//                BaseR = StringToReg(str[2]);
//                PCoffset = StringToInt(str[3]);
//                break;
//            case "ST":
//                type = Type.ST;
//                SR = StringToReg(str[1]);
//                PCoffset = StringToInt(str[2]);
//                break;
//            case "STI":
//                type = Type.STI;
//                SR = StringToReg(str[1]);
//                PCoffset = StringToInt(str[2]);
//                break;
//            case "STR":
//                type = Type.STR;
//                SR = StringToReg(str[1]);
//                BaseR = StringToReg(str[2]);
//                PCoffset = StringToInt(str[3]);
//                break;
//            case "LEA":
//                type = Type.LEA;
//                DR = StringToReg(str[1]);
//                PCoffset = StringToInt(str[2]);
//                break;
//            case "RET":
//                type = Type.RET;
//                break;
//            case "RTI":
//                type = Type.RTI;
//                break;
//            case "TRAP":
//                type = Type.TRAP;
//                trapvect = StringToInt(str[1]);
//                break;
//            default:
//                //todo 错误处理
//        }
//    }
//
//    public short toMachineCode() {
//        short code = 0;
//        switch (type) {
//            case ADD:
//                code += 0b0001 << 12;
//                code += DR << 9;
//                code += SR1 << 6;
//                code += SR2;
//                break;
//            case ADD_imm:
//                code += 0b0001 << 12;
//                code += DR << 9;
//                code += SR1 << 6;
//                code += 1 << 5;
//                code += imm;
//                break;
//            case AND:
//                code += 0b0101 << 12;
//                code += DR << 9;
//                code += SR1 << 6;
//                code += SR2;
//                break;
//            case AND_imm:
//                code += 0b0101 << 12;
//                code += DR << 9;
//                code += SR1 << 6;
//                code += 1 << 5;
//                code += imm;
//                break;
//            case NOT:
//                code += 0b1001 << 12;
//                code += DR << 9;
//                code += SR << 6;
//                code += 0b111111;
//                break;
//            case BR:
//                code += (n ? 1 : 0) << 11;
//                code += (z ? 1 : 0) << 10;
//                code += (p ? 1 : 0) << 9;
//                code += PCoffset & 0b111111111;
//                break;
//            case JMP:
//                code += 0b1100 << 12;
//                code += BaseR << 6;
//                break;
//            case JSR:
//                code += 0b01001 << 11;
//                code += PCoffset & 0b11111111111;
//                break;
//            case JSRR:
//                code += 0b01000 << 11;
//                code += BaseR << 6;
//                break;
//            case LD:
//                code += 0b0010 << 12;
//                code += DR << 9;
//                code += PCoffset & 0b111111111;
//                break;
//            case LDI:
//                code += 0b1010 << 12;
//                code += DR << 9;
//                code += PCoffset & 0b111111111;
//                break;
//            case LDR:
//                code += 0b0110 << 12;
//                code += DR << 9;
//                code += BaseR << 6;
//                code += PCoffset & 0b111111;
//                break;
//            case ST:
//                break;
//            case STI:
//                break;
//            case STR:
//                break;
//            case LEA:
//                break;
//            case RET:
//                break;
//            case RTI:
//                break;
//            case TRAP:
//                break;
//        }
//        return code;
//    }
//
//    public String toHexString() {
//
//    }
//
//    public String toBinString() {
//
//    }
//
//    /**
//     * @param nzp nzp组合
//     */
//    private void parseBR(String nzp) {
//        n = false;
//        z = false;
//        p = false;
//        for (char c : nzp.toCharArray()) {
//            switch (c) {
//                case 'n':
//                    n = true;
//                    break;
//                case 'z':
//                    z = true;
//                    break;
//                case 'p':
//                    p = true;
//                    break;
//            }
//        }
//    }
//
//    private static byte StringToReg(String s) {
//        switch (s) {
//            case "R0":
//                return R0;
//            case "R1":
//                return R1;
//            case "R2":
//                return R2;
//            case "R3":
//                return R3;
//            case "R4":
//                return R4;
//            case "R5":
//                return R5;
//            case "R6":
//                return R6;
//            case "R7":
//                return R7;
//            default:
//                //todo 异常处理
//        }
//        return -1;
//    }
//
//    private static int StringToInt(String s) {
//        //todo
//        return 0;
//    }
//
//    /**
//     * 除了以上Type列出的指令，还有伪指令，汇编器指令
//     * 除了机械化的转化，还有跳转所涉及的地址计算
//     * @param str 一行未格式化的指令
//     * @return 格式化的指令
//     */
//    public static String[] formatInstruction(String str) {
//        str = str.substring(0, str.indexOf(';'));  //去除注释
//    }
//}
