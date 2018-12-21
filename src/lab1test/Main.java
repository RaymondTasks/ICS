package lab1test;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String args[]) throws IOException {
//        File f = new File("C:\\Users\\rayomnd\\Desktop\\LUT2.txt");
//        if (f.exists()) {
//            f.delete();
//        }
//        f.createNewFile();
//        var out = new FileWriter(f);
////        out.write("BASE\t.FILL\t#0\n");
////        for (int i = 1; i < 32768; i++) {
////            out.write("\t\t.FILL\t#" + (i >> 1) + "\n");
////        }
//        out.write("BASE2\t.FILL\t#0\n");
//        for (int i = 1; i < 32768; i++) {
//            int t = i;
//            while ((t & 1) == 0) {
//                t >>= 1;
//            }
//            out.write("\t\t.FILL\t#" + t + "\n");
//        }
//        out.write("\t\t.END");
//        out.close();


//        qmodtest();
//        long all = 0;
//        long max = 0;
//        long min = Long.MAX_VALUE;
//        int time = 0;
//        for (int i = 1; i < 32768; i++) {
//            for (int j = 1; j < 32768; j++) {
//                R[0] = (short) i;
//                R[1] = (short) j;
//                int t = exec7();
//                if (R[0] != gcd(i, j)) {
//                    System.out.println("Failed");
//                    System.out.println(i + " " + j);
//                    return;
//                } else {
//                    time++;
//                    all += t;
//                    if (t > max) {
//                        max = t;
//                    }
//                    if (t < min) {
//                        min = t;
//                    }
////                    System.out.println(i+" "+j+" "+time);
//                }
//            }
//        }
//        System.out.println("Success");
//        System.out.println("avg = " + ((double) all / time));
//        System.out.println("max = " + max);
//        System.out.println("min = " + min);
        var sc=new Scanner(System.in);
        for(;;){
            System.out.println(gcd(sc.nextShort(),sc.nextShort()));
        }
    }

    private static short M[] = new short[65536];
    private static short R[] = new short[8];

    private static int exec1() {  //辗转相减法
        int len = 0;
//        R[0] += 0;
        len += 2;
        if (R[0] <= 0) {
            R[0] = 0;
            len++;
            return len;
        }
        len++;
        if (R[1] <= 0) {
            R[0] = 0;
            len++;
            return len;
        }
        for (; ; ) {
            R[2] = (short) (~R[1]);
            R[2] += 1;
            R[2] += R[0];
            len += 4;
            if (R[2] == 0) {
                return len;
            }
            len++;
            if (R[2] > 0) {
                R[0] = R[2];
                len += 2;
                continue;
            }
            R[2] = (short) (~R[2]);
            R[1] = (short) (R[2] + 1);
            len += 3;
        }
    }

    private static int exec2() {  //LUT
        int len = 0;
//        R[0] += 0;
        len += 2;
        if (R[0] <= 0) {
            R[0] = 0;
            len++;
            return len;
        }
        len++;
        if (R[1] <= 0) {
            R[0] = 0;
            len++;
            return len;
        }
        R[6] = -110;
        len += 1;
        for (; ; ) {
            R[2] = (short) (R[0] - R[1]);
            len += 4;
            if (R[2] == 0) {
                return len;
            }
            len += 4;
            if (R[0] + R[6] <= 0 && R[1] + R[6] <= 0) {
                R[0] = (short) gcd((int) R[0], (int) R[1]);
                len += 15;
                return len;
            }
            len += 2;
            if (R[2] > 0) {
                R[0] = R[2];
                len += 2;
                continue;
            }
            R[1] = (short) (-R[2]);
            len += 3;
        }
    }

    private static int exec3() {  //辗转相除
        int len = 0;
//        R[0] += 0;
        len += 2;
        if (R[0] <= 0) {
            R[0] = 0;
            len++;
            return len;
        }
        len++;
        if (R[1] <= 0) {
            R[0] = 0;
            len++;
            return len;
        }
        for (; ; ) {
            len += 2;
            if (R[1] == 0) {
                return len;
            }
            R[2] = (short) -R[1];
            R[4] = R[0];
            len += 3;
            for (; ; ) {
                R[4] += R[2];
                len += 2;
                if (R[4] < 0) {
                    break;
                }
            }
            R[0] = R[1];
            R[1] += R[4];
            len += 3;
        }
    }

    private static int exec4() {  //优化mod的辗转相除
        int len = 2;
        if (R[0] <= 0) {
            R[0] = 0;
            len++;
            return len;
        }
        len += 2;
        if (R[1] <= 0) {
            R[0] = 0;
            len++;
            return len;
        }
        //合法输入
        for (; ; ) {
            //确保R0<=R1
            R[6] = (short) -R[0];
            len += 4;
            if (R[0] > R[1]) {
                short t = R[0];
                R[0] = R[1];
                R[1] = t;
                R[6] = (short) -R[0];
                len += 5;
            }
            //求mod开始
//            len += qmod();
            //求mod结束
            len += 2;
            if (R[1] == 0) {
                return len;
            }
        }
    }

    private static int mod() {
        int len = 0;
        R[2] = (short) -R[1];
        R[4] = R[0];
        len += 3;
        for (; ; ) {
            R[4] += R[2];
            len += 2;
            if (R[4] < 0) {
                break;
            }
        }
        R[1] += R[4];
        len++;
        return len;

    }

    //计算 R1 mod R0, 结果写入到 R1
//    private static int qmod() {
//        int len = 0;
//        for (; ; ) {
//            R[2] = R[6];
//            len += 2;
//            boolean flag = false;
//            for (; ; ) {
//                if (flag) {
//                    R[2] += R[2];
//                    len++;
//                }
//                flag = true;
//                len += 3;
//                if (R[1] + R[2] + R[2] < 0) {
//                    break;
//                }
//            }
//            R[1] += R[2];
//            len += 3;
//            if (R[1] + R[6] <= 0) {
//                break;
//            }
//        }
//        return len;
//    }

//    private static int exec5() {  //binary gcd with lut
//        int len = 2;
//        if (R[0] <= 0) {
//            R[0] = 0;
//            len++;
//            return len;
//        }
//        len += 2;
//        if (R[1] <= 0) {
//            R[0] = 0;
//            len++;
//            return len;
//        }
//        //合法输入
//        //uv全部右移
//        R[3] = 0;
//        len += 2;
//        for (; ; ) {
//            len += 2;
//            R[3] = (short) (R[0] & 1);
//            if (R[3] != 0) {
//                break;
//            }
//            len += 2;
//            R[3] = (short) (R[1] & 1);
//            if ((R[1] & 1) != 0) {
//                break;
//            }
//            R[2]++;
//            R[0] >>= 1;
//            R[1] >>= 1;
//            len += 6;
//        }
//        //u右移
//        for (; ; ) {
//            len += 2;
//            R[3] = (short) (R[0] & 1);
//            if (R[3] != 0) {
//                break;
//            }
//            R[0] >>= 1;
//            len += 3;
//        }
//
//        for (; ; ) {
//            //v右移
//            for (; ; ) {
//                len += 2;
//                R[3] = (short) (R[1] & 1);
//                if ((R[1] & 1) != 0) {
//                    break;
//                }
//                R[1] >>= 1;
//                len += 3;
//            }
//            R[3] = (short) (R[1] - R[0]);
//            len += 4;
//            if (R[3] < 0) {
//                R[4] = R[0];
//                R[0] = R[1];
//                R[1] = R[4];
//                R[3] = (short) -R[3];
//                len += 5;
//            }
//            R[1] =R[3];
//            len += 2;
//            if (R[1] == 0) {
//                break;
//            }
//        }
//        len += 2;
//        boolean flag = false;
//        for (; ; ) {
//            if (flag) {
//                R[0] += R[0];
//                R[2]--;
//                len += 2;
//            }
//            flag = true;
//            len++;
//            if (R[2] == 0) {
//                break;
//            }
//        }
//        return len;
//    }

    private static int exec6() {  //binary gcd with lut 2
        int len = 2;
        if (R[0] <= 0) {
            R[0] = 0;
            len++;
            return len;
        }
        len += 2;
        if (R[1] <= 0) {
            R[0] = 0;
            len++;
            return len;
        }
        //合法输入
        //uv全部右移
        R[3] = 0;
        len += 2;
        for (; ; ) {
            len += 2;
            R[3] = (short) (R[0] & 1);
            if (R[3] != 0) {
                break;
            }
            len += 2;
            R[3] = (short) (R[1] & 1);
            if ((R[1] & 1) != 0) {
                break;
            }
            R[2]++;
            R[0] >>= 1;
            R[1] >>= 1;
            len += 6;
        }
        //u右移
        for (; ; ) {
            len += 2;
            R[3] = (short) (R[0] & 1);
            if (R[3] != 0) {
                break;
            }
            R[0] >>= 1;
            len += 3;
        }

        for (; ; ) {
            //v右移
            for (; ; ) {
                len += 2;
                R[3] = (short) (R[1] & 1);
                if ((R[1] & 1) != 0) {
                    break;
                }
                R[1] >>= 1;
                len += 3;
            }
            //binary gcd 是对辗转相减的改进
            R[3] = (short) (R[1] - R[0]);
            len += 4;
            if (R[3] < 0) {
                R[4] = R[0];
                R[0] = R[1];
                R[1] = R[4];
                R[3] = (short) -R[3];
                len += 5;
            }
            R[1] = R[3];
            len += 2;
            R[1] -= R[0];
            len += 3;
//            R[3] = R[0];
//            R[0] = R[1];
//            R[1] = R[3];
//            R[3] = (short) -R[0];
//            len += 5;
//            //辗转相除
//            for (; ; ) {
//                R[4] = R[3];
//                len += 2;
//                boolean flag = false;
//                for (; ; ) {
//                    if (flag) {
//                        R[4] += R[4];
//                        len++;
//                    }
//                    flag = true;
//                    R[5] = (short) (R[1] + R[4]);
//                    R[5] += R[4];
//                    len += 3;
//                    if (R[5] < 0) {
//                        break;
//                    }
//                }
//                R[1] += R[4];
//                R[5] = (short) (R[1] + R[3]);
//                len += 3;
//                if (R[5] < 0) {
//                    break;
//                }
//            }
//            len += 2;


            len++;
            if (R[1] == 0) {
                break;
            }
        }
        len += 2;
        boolean flag = false;
        for (; ; ) {
            if (flag) {
                R[0] += R[0];
                R[2]--;
                len += 2;
            }
            flag = true;
            len++;
            if (R[2] == 0) {
                break;
            }
        }
        return len;
    }

    private static int qmod2() {
        R[3] = (short) -R[0];
        int len = 2;
        for (; ; ) {
            len += 2;
            boolean flag = false;
            for (; ; ) {
                if (flag) {
                    R[2] += R[2];
                    len++;
                }
                flag = true;
                len += 3;
                if (R[1] + R[2] + R[2] < 0) {
                    break;
                }
            }
            R[1] += R[2];
            len += 3;
            if (R[1] + R[6] <= 0) {
                break;
            }
        }
        return len;
    }

    private static int exec7() {  //LUT binary gcd改进版
        int len = 2;
        if (R[0] <= 0) {
            R[0] = 0;
            len++;
            return len;
        }
        len += 2;
        if (R[1] <= 0) {
            R[0] = 0;
            len++;
            return len;
        }
        //合法输入
        R[2] = 0;
        R[3] = 1;
        len += 3;
        for (; ; ) {
            R[4] = (short) (R[0] & R[3]);
            len += 2;
            if (R[4] > 0) {
                break;
            }
            R[4] = (short) (R[1] & R[3]);
            len += 2;
            if (R[4] > 0) {
                break;
            }
            R[2]++;
            R[3]+=R[3];
            len += 3;
        }
        R[0] = LUT2(R[0]);
        len += 2;
        R[5] = (short) -R[0];
        len += 2;
        for (; ; ) {
            R[1] = LUT2(R[1]);
            len += 2;
            R[3] = (short) (R[5] + R[1]);
            len += 2;
            if (R[3] < 0) {
                R[3] = R[0];
                R[0] = R[1];
                R[1] = R[3];
                R[5] = (short) -R[0];
                len += 5;
            }
            R[1] += R[5];
            len += 2;
            if (R[1] == 0) {
                break;
            }
        }
        len += 2;
        if (R[2] == 0) {
            return len;
        }
        for (; ; ) {
            R[0] += R[0];
            R[2]--;
            len += 3;
            if (R[2] == 0) {
                return len;
            }
        }

    }

    private static short LUT2(short i) {
        short t = i;
        while ((t & 1) == 0) {
            t >>= 1;
        }
        return t;
    }


//    private static void qmodtest() {
//        long all = 0;
//        long max = 0;
//        int time = 0;
//        for (int i = 1; i < 32768; i++) {
//            for (int j = 1; j <= i; j++) {
//                R[0] = (short) i;
//                R[1] = (short) j;
//                int len = qmod();
////                System.out.println(time);
//                if (R[0] == i % j) {
//                    all += len;
//                    time++;
//                    if (len > max) {
//                        max = len;
//                    }
//                } else {
//                    System.out.println("Failed");
//                    System.out.println(i + " " + j);
//                }
//            }
//        }
//        System.out.println("Success");
//        System.out.println("avg = " + ((double) all / time));
//        System.out.println("max = " + max);
//    }

    private static int gcd(int a, int b) {
        if (a <= 0 || b <= 0) {
            return 0;
        }
        for (; ; ) {
            if (a == b) {
                return a;
            } else if (a > b) {
                a -= b;
            } else {
                b -= a;
            }
        }
    }
}
