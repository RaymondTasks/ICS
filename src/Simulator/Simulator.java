package Simulator;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class Simulator {
    private short R[] = new short[8];
    private short M[] = new short[1 << 16];
    private short IR, MDR;
    private int PC, MAR;
    private ArrayList<Short> breakPoints = new ArrayList<>();

    private int insts;

    private void resetInsts() {
        insts = 0;
    }

    public void reset() {
        PC = 0;
        IR = 0;
        MAR = 0;
        MDR = 0;
        Arrays.fill(R, (short) 0);
        Arrays.fill(M, (short) 0);
        n = false;
        z = false;
        p = false;
    }

    public void load(ObjectInputStream input) throws IOException {
        short start = input.readShort();
        short b;
        while ((b = input.readShort()) != -1) {
            M[start++] = b;
        }
        setPC(start);
    }

    public void setPC(short PC) {
        this.PC = PC;
    }

    public void setBreakPoint(short addr) {
        if (!breakPoints.contains(addr)) {
            breakPoints.add(addr);
        }
    }

    public void setReg(short index, short val) {
        R[index] = val;
    }

    private static final int ADD = 0001;
    private static final int AND = 0101;
    private static final int BR = 0000;
    private static final int JMP = 1100;
    private static final int JSR = 0100;
    private static final int LD = 0010;
    private static final int LDI = 1010;
    private static final int LDR = 0110;
    private static final int LEA = 1110;
    private static final int NOT = 1001;
    private static final int RTI = 1000;
    private static final int ST = 0011;
    private static final int STI = 1011;
    private static final int STR = 0111;
    private static final int TRAP = 1111;

    private int op;
    private boolean n, z, p;

    private int DR, SR1, SR2;

    private void exec() {
        if (breakPoints.contains(PC)) {
            return;
        }
        PC++;
        op = (IR >> 12) & 0b1111;
        DR = (IR >> 9) & 0b1111;
        SR1 = (IR >> 6) & 0b1111;
        if (op == ST || op == STI || op == STR) {
            SR2 = DR;
        } else {
            SR2 = IR & 0b1111;
        }
        switch (op) {
            case ADD:
                R[DR] = (short) (R[SR1] + (((IR >> 5) & 1) == 0 ?
                        R[SR2] : SEXT(5, IR)));
                refreshNZP();
                break;
            case AND:
                R[DR] = (short) (R[SR1] & (((IR >> 5) & 1) == 0 ?
                        R[SR2] : SEXT(5, IR)));
                refreshNZP();
                break;
            case BR:
                var tn = ((IR >> 11) & 1) == 1;
                var tz = ((IR >> 10) & 1) == 1;
                var tp = ((IR >> 9) & 1) == 1;
                if ((n && tn) || (z && tz) || (p && tp)) {
                    PC += SEXT(9, IR);
                }
                break;
            case JMP:
                PC = R[SR1];
                break;
            case JSR:
                R[7] = (short) PC;
                if (((IR >> 11) & 1) == 0) {
                    PC = R[SR1];
                } else {
                    PC += SEXT(11, IR);
                }
                break;
            case LD:
                R[DR] = M[PC + SEXT(9, IR)];
                refreshNZP();
                break;
            case LDI:
                R[DR] = M[M[PC + SEXT(9, IR)]];
                refreshNZP();
                break;
            case LDR:
                R[DR] = M[R[SR1] + SEXT(6, IR)];
                refreshNZP();
                break;
            case LEA:
                R[DR] = (short) (PC + SEXT(9, IR));
                refreshNZP();
                break;
            case NOT:
                R[DR] = (short) ~R[SR1];
                refreshNZP();
                break;
            case RTI:
                PC = R[7];
                break;
            case ST:
                M[PC + SEXT(9, IR)] = M[SR2];
                break;
            case STI:
                M[M[PC + SEXT(9, IR)]] = M[SR2];
                break;
            case STR:
                M[R[SR1] + SEXT(6, IR)] = R[SR2];
                break;
            case TRAP:
                R[7] = (short) PC;
                PC = M[ZEXT(8, IR)];
                break;
        }
    }

    private void refreshNZP() {
        n = R[DR] < 0;
        z = R[DR] == 0;
        p = R[DR] > 0;
    }

//    private void fetch() {
//        IR = M[PC];
//        PC++;
//    }
//
//    private void decode() {
//        op = (IR >> 12) & 0b1111;
//        DR = (IR >> 9) & 0b1111;
//        SR1 = (IR >> 6) & 0b1111;
//        if (op == ST || op == STI || op == STR) {
//            SR2 = DR;
//        } else {
//            SR2 = IR & 0b1111;
//        }
//    }
//
//    private void evaluateAddress() {
//        switch (op) {
//            case LD:
//            case LDI:
//            case ST:
//            case STI:
//            case LEA:
//                MAR = (short) (PC + SEXT(9, IR));
//                break;
//            case LDR:
//            case STR:
//                MAR = M[R[SR1] + SEXT(6, IR)];
//                break;
//        }
//    }
//
//    private void fetchOperands() {
//        switch (op) {
//            case LD:
//            case LDR:
//            case LDI:
//            case LEA:
//            case STI:
//                MDR = M[MAR];
//                break;
//        }
//
//    }
//
//    private void execute() {
//        switch (op) {
//            case ADD:
//                R[DR] = (short) (R[SR1] + (((IR >> 5) & 1) == 0 ?
//                        R[SR2] : SEXT(5, IR)));
//                break;
//            case AND:
//                R[DR] = (short) (R[SR1] & (((IR >> 5) & 1) == 0 ?
//                        R[SR2] : SEXT(5, IR)));
//                break;
//                case
//
//        }
//    }
//
//    private void storeResult() {
//
//    }
//

    private short SEXT(int len, short src) {
        short tmp = ZEXT(len, src);
        int sign = src & (1 << (len - 1));
        for (int i = len; i < 16; i++) {
            tmp |= (sign <<= 1);
        }
        return tmp;
    }

    private short ZEXT(int len, short src) {
        int mask = 0;
        for (int i = 0; i < len; i++) {
            mask <<= 1;
            mask++;
        }
        return (short) (src & mask);
    }


}
