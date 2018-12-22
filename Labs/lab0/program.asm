;掩码法
		.ORIG	x3000
		AND		R1, R1, #0	;R1置为0，是结果的储存位置
		ADD		R2, R1, #1	;R2初始置为1，是R1的掩码
				;循环开始
LOOP	ADD		R3, R2, R2	;R3为R2左移一位，是R0的掩码
		AND		R4, R0, R3	;R4为提取R0的特定位
		BRz		#1				;R0此位为0时不需要进行操作
		ADD		R1, R1, R2	;R0此位为1时，用掩码将R1对应位的右边一位置为1
		ADD		R2, R2, R2	;R2左移
		BRp		LOOP			;R2为x8000时跳出循环
		;跳出循环时，R4储存着R0的符号位
		ADD		R0, R1, R4	;补上R1最左侧的符号位
		HALT					;结束
		.END