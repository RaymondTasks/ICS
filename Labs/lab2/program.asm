;函数调用的实参放在R0~R5
;如果参数多于6个
;则R5存放剩余参数的内存地址
;例如
paranum	.FILL	#3
paran
;R6是栈指针
;R7存放函数返回地址
;被调函数保证R6的不变性
;R0存放函数返回值
		.ORIG	x3000
start	ST 		R7,	SaveR7
		JSR		main
		LD		R7, SaveR7
		HALT

;main的返回值存放在R0
main	

func	
SaveR7	.BLKW	#1	``zxz