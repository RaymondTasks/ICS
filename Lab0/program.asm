;掩码法
		.ORIG	x3000
		AND		R1, R1, #0	;R1=0，R1是目标值的位置
		ADD		R2, R1, #1	;R2=1，R1的掩码
LOOP	ADD		R3, R2, R2	;R3为R2左移一位，R0的掩码
		AND		R4, R0, R3	;提取R0的特定位
		BRz		#1
		ADD		R1, R1, R2	;R0此位为1时，R1的对应位也置1
		ADD		R2, R2, R2	;R2左移
		BRp		LOOP		;R2为x8000是跳出循环
		ADD		R0, R1, R4	;补上R1最左侧的符号位
		HALT
		.END