;函数调用的实参放在R0~R5
;如果参数多于6个
;则R5存放剩余参数的内存起始地址
;R6是栈指针
;函数返回值存放在R0
;被调函数保证R1~R5在调用前后不改变
;若函数没有返回值，则R0也保证不变
;TRAP也遵守以上规则
;函数调用者在调用函数或者TRAP前把R7和其他要保存的数据压栈
;返回后把R7出栈
;压栈和出栈时都要对栈上溢或下溢情况进行判断

			.ORIG	x3000
			;程序起始点
start		LD 		R6, Stackinit
			JSR		main
			HALT
Stackinit	.FILL 	x4000	;空栈时栈顶
Stackmax	.FILL 	x3FFB	;栈满时的栈顶

			;main函数
			;返回0代表正常结束
			;返回非0代表异常
			;此处规定1代表发生了stack overflow
			;2代表stack underflow
main		ADD 	R6, R6, #-6
			STR 	R1, R6, #0
			STR 	R2, R6, #1
			STR 	R3, R6, #2
			STR 	R4, R6, #3
			STR 	R5, R6, #4
			STR 	R7, R6, #5

			GETC

			LD 		R7, Char0N
			ADD 	R0, R0, R7	;n
			AND		R1, R1, #0	;a
			AND		R2, R2, #0	;b
			AND		R3, R3, #0	;c
			AND		R4, R4, #0	;d
			LEA		R5, Params

			JSR 	func

			LDR 	R1, R6, #0
			LDR 	R2, R6, #1
			LDR 	R3, R6, #2
			LDR 	R4, R6, #3
			LDR 	R5, R6, #4
			LDR 	R7, R6, #5
			ADD 	R6, R6, #6

			AND 	R0, R0, #0	;main正常返回
			RET

Params		.FILL	#0			;e
			.FILL	#0			;f
Char0Nmain	.FILL	#-48 		;0的ascii码的相反数
			;main函数结束


			;func函数
func		ADD 	R6, R6, #2
			STR 	R0, R6, #0	;保存n
			STR 	R7, R6, #1	;保存R7

			GETC

			;计算t，存放在R0
			LD 		R7, Char0Nfunc
			ADD 	R0, R0, R7 	;-'0'
			ADD 	R0, R0, R1 	;+a
			ADD 	R0, R0, R2 	;+b
			ADD 	R0, R0, R3 	;+c
			ADD 	R0, R0, R4 	;+d
			LDR 	R7, R5, #0
			ADD 	R0, R0, R7 	;+e
			LDR 	R7, R5, #1
			ADD 	R0, R0, R7 	;+f

			;取回n
			LDR 	R7, R6, #0

			;分支判断
			ADD 	R7, R7, #-1
			BRnz	else

			; n > 1
			;保存t
if			ADD 	R6, R6, #-1
			STR 	R0, R6, #0

			;计算x
			ADD 	R0, R7, #0
			JSR 	func

			;保存x
			ADD 	R6, R6, #-1
			STR 	R0, R6, #0

			;计算y
			LDR 	R7, R6, #1
			ADD 	R0, R0, #-2
			JSR 	func

			;计算x+y+t-1
			LDR 	R7, R6, #0
			ADD		R0, R0, R7
			LDR 	R7, R6, #1
			ADD		R0, R0, R7
			ADD		R0, R0, #-1

			;返回
			LDR 	R7, R6, #2
			ADD 	R6, R6, #3
			RET

			;else
else		LDR 	R7, R6, #1	;跳过没取出的n
			ADD 	R6, R6, #2
			RET

Char0Nfunc	.FILL	#-48
			;func函数结束
			

			;压栈子程序
			;R0存放待压的数
			;
PUSH		

			;出栈子程序
POP



.END