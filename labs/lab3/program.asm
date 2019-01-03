;R4是全局数据指针
;R5是帧指针
;R6是全局栈指针

;函数实参和返回值都通过栈传递

;R0~R3是被调函数负责保存的寄存器

;通过全局数据指针引用一些常量，写进报告


			.ORIG x3000

			;初始化代码
			;初始化栈指针
			;R6=程序起始地址x5-1
			;这种初始化方式有严重漏洞，写进报告
INIT_CODE	LEA 	R6, #-1
			ADD 	R5, R6, #0
			ADD 	R6, R6, R6
			ADD 	R6, R6, R6
			ADD 	R6, R6, R5
			ADD 	R6, R6, #-1

			ADD 	R5, R5, R5	;????无意义的一句

			;初始化帧指针R5=R6
			ADD		R5, R6, #0

			;初始化全局数据指针R4
			LD 		R4, GLOBAL_DATA_POINTER

			;调用main
			;此处用了JSRR，写进报告
			LD 		R7, GLOBAL_MAIN_POINTER
			JSRR 	R7

			HALT

			;修正了一个GLOBAL_DATA_POINTER的小问题才能通过汇编，写进报告

			;全局数据起始处
GLOBAL_DATA_POINTER 	.FILL	func
			;main函数位置
GLOBAL_MAIN_POINTER 	.FILL	main



;;;;;;;;;;;;;;;;;;;;;;;;;;;;func;;;;;;;;;;;;;;;;;;;;;;;;;;;;
			
			;R7,R5压栈
lc3_func	ADD 	R6, R6, #-2
			STR 	R7, R6, #0
			ADD 	R6, R6, #-1
			STR 	R5, R6, #0

			;帧指针
			;帧空间有3个地址的位置
			ADD 	R5, R6, #-1
			ADD 	R6, R6, #-3

			;调用 getchar()
			ADD 	R0, R4, #4
			LDR 	R0, R0, #0
			JSRR 	R0

			;返回值出栈
			LDR 	R7, R6, #0
			ADD 	R6, R6, #1

			;R3='0'
			ADD 	R3, R4, #8
			LDR 	R3, R3, #0

			;GETC()的结果压栈
			ADD 	R6, R6, #-1
			STR 	R0, R6, #0

			;'0'压栈
			ADD 	R6, R6, #-1
			STR 	R3, R6, #0

			;R0=GETC()-'0'
			NOT 	R3, R3
			ADD 	R3, R3, #1
			ADD 	R0, R7, R3

			;'0'出栈
			LDR 	R3, R6, #0
			ADD 	R6, R6, #1

			;R7=GETC()-'0'
			ADD 	R7, R0, #0

			;GETC()的结果出栈
			LDR 	R0, R6, #0
			ADD 	R6, R6, #1

			;t = GETC() - '0' + a + b + c + d + e + f
			LDR 	R3, R5, #5
			ADD 	R7, R7, R3
			LDR 	R3, R5, #6
			ADD 	R7, R7, R3
			LDR 	R3, R5, #7
			ADD 	R7, R7, R3
			LDR 	R3, R5, #8
			ADD 	R7, R7, R3
			LDR 	R3, R5, #9
			ADD 	R7, R7, R3
			LDR 	R3, R5, #10
			ADD 	R7, R7, R3

			;保存t
			STR 	R7, R5, #0

			;读取n
			LDR 	R7, R5, #4

			;读取1
			ADD 	R3, R4, #7
			LDR 	R3, R3, #0

			;R7=1-n
			NOT 	R7, R7
			ADD 	R7, R7, #1
			ADD 	R7, R7, R3

			BRn 	L7

			;跳转到 lc3_L3_lab2 (else)
			ADD 	R7, R4, #1
			LDR 	R7, R7, #0
			JMP 	R7

			;if(n>1)
L7			;准备函数调用的参数 n-1,a,b,c,d,e,f
			LDR 	R7, R5, #10
			ADD 	R6, R6, #-1
			STR 	R7, R6, #0
			LDR 	R7, R5, #9
			ADD 	R6, R6, #-1
			STR 	R7, R6, #0
			LDR 	R7, R5, #8
			ADD 	R6, R6, #-1
			STR 	R7, R6, #0
			LDR 	R7, R5, #7
			ADD 	R6, R6, #-1
			STR 	R7, R6, #0
			LDR 	R7, R5, #6
			ADD 	R6, R6, #-1
			STR 	R7, R6, #0
			LDR 	R7, R5, #5
			ADD 	R6, R6, #-1
			STR 	R7, R6, #0

			;取出n
			LDR 	R7, R5, #4

			;取出1
			ADD 	R3, R4, #7
			LDR 	R3, R3, #0

			;R0压栈
			ADD 	R6, R6, #-1
			STR 	R0, R6, #0

			;1压栈
			ADD 	R6, R6, #-1
			STR 	R3, R6, #0

			;R0=n-1
			NOT 	R3, R3
			ADD 	R3, R3, #1
			ADD 	R0, R7, R3

			;1出栈
			LDR 	R3, R6, #0
			ADD 	R6, R6, #1

			;R7=n-1
			ADD 	R7, R0, #0

			;R0出栈
			LDR 	R0, R6, #0
			ADD 	R6, R6, #1

			;保存实参n-1
			ADD 	R6, R6, #-1
			STR 	R7, R6, #0

			;调用func
			ADD 	R0, R4, #0
			LDR 	R0, R0, #0
			JSRR 	R0

			;取出返回值，放入帧中
			LDR 	R7, R6, #0
			ADD 	R6, R6, #1
			STR 	R7, R5, #-1

			;重复上述步骤，n-1变为n-2
			LDR 	R7, R5, #10
			ADD 	R6, R6, #-1
			STR 	R7, R6, #0
			LDR 	R7, R5, #9
			ADD 	R6, R6, #-1
			STR 	R7, R6, #0
			LDR 	R7, R5, #8
			ADD 	R6, R6, #-1
			STR 	R7, R6, #0
			LDR 	R7, R5, #7
			ADD 	R6, R6, #-1
			STR 	R7, R6, #0
			LDR 	R7, R5, #6
			ADD 	R6, R6, #-1
			STR 	R7, R6, #0
			LDR 	R7, R5, #5
			ADD 	R6, R6, #-1
			STR 	R7, R6, #0
			LDR 	R7, R5, #4
			ADD 	R3, R4, #6
			LDR 	R3, R3, #0
			ADD 	R6, R6, #-1
			STR 	R0, R6, #0
			ADD 	R6, R6, #-1
			STR 	R3, R6, #0
			NOT 	R3, R3
			ADD 	R3, R3, #1
			ADD 	R0, R7, R3
			LDR 	R3, R6, #0
			ADD 	R6, R6, #1
			ADD 	R7, R0, #0
			LDR 	R0, R6, #0
			ADD 	R6, R6, #1
			ADD 	R6, R6, #-1
			STR 	R7, R6, #0
			ADD 	R0, R4, #0
			LDR 	R0, R0, #0
			JSRR 	R0

			;取出返回值，放入帧中
			LDR 	R7, R6, #0
			ADD 	R6, R6, #1
			STR 	R7, R5, #-2

			;R7=x+y+t
			LDR 	R7, R5, #-1
			LDR 	R3, R5, #-2
			ADD 	R7, R7, R3
			LDR 	R3, R5, #0
			ADD 	R7, R7, R3

			;R3=1
			ADD 	R3, R4, #7
			LDR 	R3, R3, #0

			;R0压栈
			ADD 	R6, R6, #-1
			STR 	R0, R6, #0

			;R3压栈
			ADD 	R6, R6, #-1
			STR 	R3, R6, #0

			;R0=x+y+t-1
			NOT 	R3, R3
			ADD 	R3, R3, #1
			ADD 	R0, R7, R3

			;R3出栈
			LDR 	R3, R6, #0
			ADD 	R6, R6, #1

			;R7=x+y+t-1
			ADD 	R7, R0, #0

			;R0出栈
			LDR 	R0, R6, #0
			ADD 	R6, R6, #1

			;跳转到 lc3_L1_lab2
			ADD 	R0, R4, #2
			LDR 	R0, R0, #0
			JMP 	R0

			;else
lc3_L3_lab2	LDR 	R7, R5, #0

			;return
			;保存返回值
lc3_L1_lab2	STR 	R7, R5, #3
			;恢复R5，R6
			ADD 	R6, R5, #1
			LDR 	R5, R6, #0
			;恢复R7
			ADD 	R6, R6, #1
			LDR 	R7, R6, #0
			ADD 	R6, R6, #1

			RET


;;;;;;;;;;;;;;;;;;;;;;;;;;;;main;;;;;;;;;;;;;;;;;;;;;;;;;;;;
		
			;R7,R5压栈
			;地址最高处预留了一个空间，用于存放返回值
main		ADD 	R6, R6, #-2
			STR 	R7, R6, #0
			ADD 	R6, R6, #-1
			STR 	R5, R6, #0

			;R5是帧指针
			;R5起始位置是R7,R5压栈后R6-1的位置
			ADD 	R5, R6, #-1

			;????留一个地址的空位，为了以后储存 getchar() - '0'
			ADD 	R6, R6, #-1

			;调用 getchar()
			ADD 	R0, R4, #4
			LDR 	R0, R0, #0
			JSRR 	R0

			;返回值出栈，存放到R7
			LDR 	R7, R6, #0
			ADD 	R6, R6, #1

			;R3='0'
			ADD 	R3, R4, #8
			LDR 	R3, R3, #0

			;R0（GETC返回值）压栈
			ADD 	R6, R6, #-1
			STR 	R0, R6, #0

			;R3（'0'）压栈
			ADD 	R6, R6, #-1
			STR 	R3, R6, #0

			;R0=R7-R3
			NOT 	R3, R3
			ADD 	R3, R3, #1
			ADD 	R0, R7, R3

			;R3（'0'）出栈
			LDR 	R3, R6, #0
			ADD 	R6, R6, #1

			;R7=R0= getchar() - '0'
			ADD 	R7, R0, #0

			;R0（GETC返回值）出栈
			LDR 	R0, R6, #0
			ADD 	R6, R6, #1

			;getchar() - '0' 储存到帧指针指向的地方
			STR 	R7, R5, #0

			;准备函数调用的参数

			;R7=0
			ADD 	R7, R4, #5
			LDR 	R7, R7, #0
			;0压栈6次
			ADD 	R6, R6, #-1
			STR 	R7, R6, #0
			ADD 	R6, R6, #-1
			STR 	R7, R6, #0
			ADD 	R6, R6, #-1
			STR 	R7, R6, #0
			ADD 	R6, R6, #-1
			STR 	R7, R6, #0
			ADD 	R6, R6, #-1
			STR 	R7, R6, #0
			ADD 	R6, R6, #-1
			STR 	R7, R6, #0

			;取回 getchar() - '0'
			LDR 	R7, R5, #0

			;getchar() - '0' 压栈
			ADD 	R6, R6, #-1
			STR 	R7, R6, #0

			;调用func
			ADD 	R0, R4, #0
			LDR 	R0, R0, #0
			JSRR 	R0

			;取出返回值
			LDR 	R7, R6, #0
			ADD 	R6, R6, #1

			

			;储存返回值
lc3_L8_lab2	STR 	R7, R5, #3

			;准备返回
			;恢复栈指针
			ADD 	R6, R5, #1

			;R5出栈
			LDR 	R5, R6, #0
			ADD 	R6, R6, #1

			;R7出栈
			LDR 	R7, R6, #0
			ADD 	R6, R6, #1

			RET


;;;;;;;;;;;;;;;;;;;;;;;;;;;;getchar;;;;;;;;;;;;;;;;;;;;;;;;;;;;

			;R7,R0压栈
lc3_getchar	STR 	R7, R6, #-3
			STR 	R0, R6, #-2

			GETC
			OUT		;回显

			;GETC的结果压栈
			STR 	R0, R6, #-1

			;恢复R0
			LDR 	R0, R6, #-2
			;恢复R7
			LDR 	R7, R6, #-3

			;栈指针-1，用于存放返回值
			ADD 	R6, R6, #-1

			RET


			;全局数据区域
			;通过R4读取内容
func 		.FILL 	lc3_func
L3_lab2 	.FILL 	lc3_L3_lab2	;if
L1_lab2 	.FILL 	lc3_L1_lab2	;else
L8_lab2 	.FILL 	lc3_L8_lab2	;return
getchar 	.FILL 	lc3_getchar
L9_lab2 	.FILL 	#0
L6_lab2 	.FILL 	#2
L5_lab2 	.FILL 	#1
L2_lab2 	.FILL 	#48
			.END
