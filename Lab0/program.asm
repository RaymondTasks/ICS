;���뷨
		.ORIG	x3000
		AND		R1, R1, #0	;R1=0��R1��Ŀ��ֵ��λ��
		ADD		R2, R1, #1	;R2=1��R1������
LOOP	ADD		R3, R2, R2	;R3ΪR2����һλ��R0������
		AND		R4, R0, R3	;��ȡR0���ض�λ
		BRz		#1
		ADD		R1, R1, R2	;R0��λΪ1ʱ��R1�Ķ�ӦλҲ��1
		ADD		R2, R2, R2	;R2����
		BRp		LOOP		;R2Ϊx8000������ѭ��
		ADD		R0, R1, R4	;����R1�����ķ���λ
		HALT
		.END