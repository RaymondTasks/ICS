;R4��ȫ������ָ��
;R5��ָ֡��
;R6��ȫ��ջָ��

;����ʵ�κͷ���ֵ��ͨ��ջ����

;R0~R3�Ǳ����������𱣴�ļĴ���

;ͨ��ȫ������ָ������һЩ������д������


			.ORIG x3000

			;��ʼ������
			;��ʼ��ջָ��
			;R6=������ʼ��ַx5-1
			;���ֳ�ʼ����ʽ������©����д������
INIT_CODE	LEA 	R6, #-1
			ADD 	R5, R6, #0
			ADD 	R6, R6, R6
			ADD 	R6, R6, R6
			ADD 	R6, R6, R5
			ADD 	R6, R6, #-1

			ADD 	R5, R5, R5	;????�������һ��

			;��ʼ��ָ֡��R5=R6
			ADD		R5, R6, #0

			;��ʼ��ȫ������ָ��R4
			LD 		R4, GLOBAL_DATA_POINTER

			;����main
			;�˴�����JSRR��д������
			LD 		R7, GLOBAL_MAIN_POINTER
			JSRR 	R7

			HALT

			;������һ��GLOBAL_DATA_POINTER��С�������ͨ����࣬д������

			;ȫ��������ʼ��
GLOBAL_DATA_POINTER 	.FILL	func
			;main����λ��
GLOBAL_MAIN_POINTER 	.FILL	main



;;;;;;;;;;;;;;;;;;;;;;;;;;;;func;;;;;;;;;;;;;;;;;;;;;;;;;;;;
			
			;R7,R5ѹջ
lc3_func	ADD 	R6, R6, #-2
			STR 	R7, R6, #0
			ADD 	R6, R6, #-1
			STR 	R5, R6, #0

			;ָ֡��
			;֡�ռ���3����ַ��λ��
			ADD 	R5, R6, #-1
			ADD 	R6, R6, #-3

			;���� getchar()
			ADD 	R0, R4, #4
			LDR 	R0, R0, #0
			JSRR 	R0

			;����ֵ��ջ
			LDR 	R7, R6, #0
			ADD 	R6, R6, #1

			;R3='0'
			ADD 	R3, R4, #8
			LDR 	R3, R3, #0

			;GETC()�Ľ��ѹջ
			ADD 	R6, R6, #-1
			STR 	R0, R6, #0

			;'0'ѹջ
			ADD 	R6, R6, #-1
			STR 	R3, R6, #0

			;R0=GETC()-'0'
			NOT 	R3, R3
			ADD 	R3, R3, #1
			ADD 	R0, R7, R3

			;'0'��ջ
			LDR 	R3, R6, #0
			ADD 	R6, R6, #1

			;R7=GETC()-'0'
			ADD 	R7, R0, #0

			;GETC()�Ľ����ջ
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

			;����t
			STR 	R7, R5, #0

			;��ȡn
			LDR 	R7, R5, #4

			;��ȡ1
			ADD 	R3, R4, #7
			LDR 	R3, R3, #0

			;R7=1-n
			NOT 	R7, R7
			ADD 	R7, R7, #1
			ADD 	R7, R7, R3

			BRn 	L7

			;��ת�� lc3_L3_lab2 (else)
			ADD 	R7, R4, #1
			LDR 	R7, R7, #0
			JMP 	R7

			;if(n>1)
L7			;׼���������õĲ��� n-1,a,b,c,d,e,f
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

			;ȡ��n
			LDR 	R7, R5, #4

			;ȡ��1
			ADD 	R3, R4, #7
			LDR 	R3, R3, #0

			;R0ѹջ
			ADD 	R6, R6, #-1
			STR 	R0, R6, #0

			;1ѹջ
			ADD 	R6, R6, #-1
			STR 	R3, R6, #0

			;R0=n-1
			NOT 	R3, R3
			ADD 	R3, R3, #1
			ADD 	R0, R7, R3

			;1��ջ
			LDR 	R3, R6, #0
			ADD 	R6, R6, #1

			;R7=n-1
			ADD 	R7, R0, #0

			;R0��ջ
			LDR 	R0, R6, #0
			ADD 	R6, R6, #1

			;����ʵ��n-1
			ADD 	R6, R6, #-1
			STR 	R7, R6, #0

			;����func
			ADD 	R0, R4, #0
			LDR 	R0, R0, #0
			JSRR 	R0

			;ȡ������ֵ������֡��
			LDR 	R7, R6, #0
			ADD 	R6, R6, #1
			STR 	R7, R5, #-1

			;�ظ��������裬n-1��Ϊn-2
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

			;ȡ������ֵ������֡��
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

			;R0ѹջ
			ADD 	R6, R6, #-1
			STR 	R0, R6, #0

			;R3ѹջ
			ADD 	R6, R6, #-1
			STR 	R3, R6, #0

			;R0=x+y+t-1
			NOT 	R3, R3
			ADD 	R3, R3, #1
			ADD 	R0, R7, R3

			;R3��ջ
			LDR 	R3, R6, #0
			ADD 	R6, R6, #1

			;R7=x+y+t-1
			ADD 	R7, R0, #0

			;R0��ջ
			LDR 	R0, R6, #0
			ADD 	R6, R6, #1

			;��ת�� lc3_L1_lab2
			ADD 	R0, R4, #2
			LDR 	R0, R0, #0
			JMP 	R0

			;else
lc3_L3_lab2	LDR 	R7, R5, #0

			;return
			;���淵��ֵ
lc3_L1_lab2	STR 	R7, R5, #3
			;�ָ�R5��R6
			ADD 	R6, R5, #1
			LDR 	R5, R6, #0
			;�ָ�R7
			ADD 	R6, R6, #1
			LDR 	R7, R6, #0
			ADD 	R6, R6, #1

			RET


;;;;;;;;;;;;;;;;;;;;;;;;;;;;main;;;;;;;;;;;;;;;;;;;;;;;;;;;;
		
			;R7,R5ѹջ
			;��ַ��ߴ�Ԥ����һ���ռ䣬���ڴ�ŷ���ֵ
main		ADD 	R6, R6, #-2
			STR 	R7, R6, #0
			ADD 	R6, R6, #-1
			STR 	R5, R6, #0

			;R5��ָ֡��
			;R5��ʼλ����R7,R5ѹջ��R6-1��λ��
			ADD 	R5, R6, #-1

			;????��һ����ַ�Ŀ�λ��Ϊ���Ժ󴢴� getchar() - '0'
			ADD 	R6, R6, #-1

			;���� getchar()
			ADD 	R0, R4, #4
			LDR 	R0, R0, #0
			JSRR 	R0

			;����ֵ��ջ����ŵ�R7
			LDR 	R7, R6, #0
			ADD 	R6, R6, #1

			;R3='0'
			ADD 	R3, R4, #8
			LDR 	R3, R3, #0

			;R0��GETC����ֵ��ѹջ
			ADD 	R6, R6, #-1
			STR 	R0, R6, #0

			;R3��'0'��ѹջ
			ADD 	R6, R6, #-1
			STR 	R3, R6, #0

			;R0=R7-R3
			NOT 	R3, R3
			ADD 	R3, R3, #1
			ADD 	R0, R7, R3

			;R3��'0'����ջ
			LDR 	R3, R6, #0
			ADD 	R6, R6, #1

			;R7=R0= getchar() - '0'
			ADD 	R7, R0, #0

			;R0��GETC����ֵ����ջ
			LDR 	R0, R6, #0
			ADD 	R6, R6, #1

			;getchar() - '0' ���浽ָ֡��ָ��ĵط�
			STR 	R7, R5, #0

			;׼���������õĲ���

			;R7=0
			ADD 	R7, R4, #5
			LDR 	R7, R7, #0
			;0ѹջ6��
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

			;ȡ�� getchar() - '0'
			LDR 	R7, R5, #0

			;getchar() - '0' ѹջ
			ADD 	R6, R6, #-1
			STR 	R7, R6, #0

			;����func
			ADD 	R0, R4, #0
			LDR 	R0, R0, #0
			JSRR 	R0

			;ȡ������ֵ
			LDR 	R7, R6, #0
			ADD 	R6, R6, #1

			

			;���淵��ֵ
lc3_L8_lab2	STR 	R7, R5, #3

			;׼������
			;�ָ�ջָ��
			ADD 	R6, R5, #1

			;R5��ջ
			LDR 	R5, R6, #0
			ADD 	R6, R6, #1

			;R7��ջ
			LDR 	R7, R6, #0
			ADD 	R6, R6, #1

			RET


;;;;;;;;;;;;;;;;;;;;;;;;;;;;getchar;;;;;;;;;;;;;;;;;;;;;;;;;;;;

			;R7,R0ѹջ
lc3_getchar	STR 	R7, R6, #-3
			STR 	R0, R6, #-2

			GETC
			OUT		;����

			;GETC�Ľ��ѹջ
			STR 	R0, R6, #-1

			;�ָ�R0
			LDR 	R0, R6, #-2
			;�ָ�R7
			LDR 	R7, R6, #-3

			;ջָ��-1�����ڴ�ŷ���ֵ
			ADD 	R6, R6, #-1

			RET


			;ȫ����������
			;ͨ��R4��ȡ����
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
