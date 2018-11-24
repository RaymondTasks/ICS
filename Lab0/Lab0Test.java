public class Lab0Test {
    public static void main(String args[]) {
        short[] R = new short[8];  //8个寄存器
        for (int i = Short.MIN_VALUE; i <= Short.MAX_VALUE; i++) {
            R[0] = (short) i;
            //被测试代码开始
            R[1] = (short) (R[1] & 0);              //AND	R1, R1, #0
            R[2] = (short) (R[1] + 1);              //ADD	R2, R1, #1
            do {
                R[3] = (short) (R[2] + R[2]);       //ADD	R3, R2, R2
                R[4] = (short) (R[0] & R[3]);       //AND	R4, R0, R3
                if (R[4] != 0)                      //BRz	#1
                    R[1] = (short) (R[1] + R[2]);   //ADD	R1, R1, R2
                R[2] = (short) (R[2] + R[2]);       //ADD	R2, R2, R2
            } while (R[2] > 0);                     //BRp	LOOP
            R[0] = (short) (R[1] + R[4]);           //ADD	R0, R1, R4
            //被测试代码结束
            if (R[0] != (i >> 1)) {
                System.out.println("Test Failed");
                return;
            }
        }
        System.out.println("Test Success");
    }
}