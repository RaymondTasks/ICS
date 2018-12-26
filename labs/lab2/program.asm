;函数实参放在R0~R4
;如果参数多于5个
;R4存放剩余参数的内存地址
;R0存放返回值
;被调函数保证R1~R4的不变性
;如果没有返回值，R0也保证不变
;TRAP也遵守以上规则

;正常返回时R5=0
;发生栈上溢R5=1
;发生栈下溢R5=2

			.ORIG	x3000

start		LD 		R6, stackinit
			JSR		main
			HALT

stackinit 	.FILL	xF001
stackmax	.FILL	xD000

Nstackinit	.FILL	x0FFF
Nstackmax	.FILL	x3000

			
			;main函数
main 		ADD		R6, R6, #-8
			;检查栈上溢
			LD 		R5, Nstackmax
			ADD		R5, R5, R6
			BRn 	mainOF

			;main的栈空间
			STR 	R1, R6, #0
			STR 	R2, R6, #1
			STR 	R3, R6, #2
			STR 	R4, R6, #3
			STR 	R7, R6, #7

			GETC
			;OUT

			ADD		R0, R0, #-16	;n = GETC() - '0'
			ADD		R0, R0, #-16
			ADD		R0, R0, #-16

			AND		R1, R1, #0	;a=0
			AND		R2, R2, #0	;b=0
			AND		R3, R3, #0	;c=0
			STR 	R1, R6, #4	;d=0
			STR 	R1, R6, #5	;e=0
			STR 	R1, R6, #6	;f=0
			ADD 	R4, R6, #4

			JSR		func
			;判断溢出
			ADD		R5, R5, #0
			BRnp	mainXF

			;恢复寄存器
			LDR 	R1, R6, #0
			LDR 	R2, R6, #1
			LDR 	R3, R6, #2
			LDR 	R4, R6, #3
			LDR 	R7, R6, #7
			ADD		R6, R6, #8

			;检查栈下溢
			LD		R5, Nstackinit
			ADD		R5, R5, R6
			BRp		mainUF
			
			;正常返回
			AND		R5, R5, #0
			RET

			;调用函数发生溢出
mainXF		LDR 	R1, R6, #0
			LDR 	R2, R6, #1
			LDR 	R3, R6, #2
			LDR 	R4, R6, #3
			LDR 	R7, R6, #7
			ADD		R6, R6, #8
			RET

			;栈上溢
mainOF		ADD		R6, R6, #8
			AND		R5, R5, #0
			ADD		R5, R5, #1
			RET

			;栈下溢
mainUF		AND		R5, R5, #0
			ADD		R5, R5, #2
			RET



			;func函数
			;R0=n,R1=a,R2=b,R3=c,M[R4]=d,M[R4+1]=e,M[R4+2]=f
func 		ADD		R6, R6, #-4
			;检查栈上溢
			LD 		R5, Nstackmax
			ADD		R5, R5, R6
			BRn 	funcOF

			;栈空间从上至下分别为n,t,x,R7
			STR 	R0, R6, #0
			STR 	R7, R6, #3

			GETC
			;OUT

			;t = GETC() - '0' + a + b + c + d + e + f
			ADD		R0, R0, #-16
			ADD		R0, R0, #-16
			ADD		R0, R0, #-16
			ADD		R0, R0, R1
			ADD		R0, R0, R2
			ADD		R0, R0, R3
			LDR		R5, R4, #0
			ADD		R0, R0, R5
			LDR		R5, R4, #1
			ADD		R0, R0, R5
			LDR		R5, R4, #2
			ADD		R0, R0, R5

			;分支判断
			LDR 	R5, R6, #0
			ADD		R5, R5, #-1
			BRnz 	return

			;储存t
			STR 	R0, R6, #1
			ADD		R0, R5, #0

			;计算x
			JSR 	func
			;判断溢出
			ADD		R5, R5, #0
			BRnp	funcXF

			;储存x
			STR 	R0, R6, #2
			ADD		R0, R0, #-1

			LDR 	R0, R6, #0
			ADD		R0, R0, #-2
			;计算y
			JSR 	func
			;判断溢出
			ADD		R5, R5, #0
			BRnp	funcXF

			;x+y+t-1
			LDR		R5, R6, #1
			ADD		R0, R0, R5
			LDR		R5, R6, #2
			ADD		R0, R0, R5
			ADD		R0, R0, #-1

return		LDR 	R7, R6, #3
			ADD		R6, R6, #4
			;检查栈下溢
			LD 		R5, Nstackinit
			ADD		R5, R5, R6
			BRp 	funcUF
			
			;无异常返回
			AND		R5, R5, #0
			RET

			;递归函数发生溢出
funcXF		LDR 	R7, R6, #3
			ADD		R6, R6, #4
			RET

			;栈上溢
funcOF		ADD		R6, R6, #4
			AND		R5, R5, #0
			ADD		R5, R5, #1
			RET

			;栈下溢
funcUF		AND		R5, R5, #0
			ADD		R5, R5, #2
			RET


			.END
