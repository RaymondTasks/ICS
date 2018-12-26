package Simulator

fun lab2Test() {

}

//typedef int i16;
//typedef unsigned int u16;
//
//i16 func(i16 n, i16 a, i16 b, i16 c, i16 d, i16 e, i16 f){ //Lots of arguments, hah?
//    i16 t = GETC() - '0' + a + b + c + d + e + f;
//    if(n > 1){
//        i16 x = func(n - 1, a, b, c, d, e, f);
//        i16 y = func(n - 2, a ,b, c, d, e, f);
//        return x + y + t - 1;
//    }else{
//        return t;
//    }
//}
//
//i16 main(void){
//    i16 n = GETC() - '0';
//    return func(n, 0, 0, 0, 0, 0, 0);
//}
//
//_Noreturn void __start(){
//    /*
//        Here is where this program actually starts executing.
//        Complete this function to do some initialization in your compiled assembly.
//        TODO: Set up C runtime.
//    */
//    u16 __R0 = main(); //The return value of function main() should be moved to R0.
//    HALT();
//}


fun lab2Main(): Short {
    val n = lab2GETC() - 48
    return lab2Func(n.toShort(), 0, 0, 0, 0, 0, 0)
}

fun lab2Func(n: Short, a: Short, b: Short, c: Short,
             d: Short, e: Short, f: Short): Short {
    val t = lab2GETC() - 48 + a + b + c + d + e + f
    if (n > 1) {
        val x = lab2Func((n - 1).toShort(), a, b, c, d, e, f)
        val y = lab2Func((n - 2).toShort(), a, b, c, d, e, f)
        return (x + y + t - 1).toShort()
    } else {
        return t.toShort()
    }
}

fun lab2GETC(): Short {
}