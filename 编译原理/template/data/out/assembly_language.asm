.text
    li t0, 0		#  (MOV, f0, 0)
    li t1, 1		#  (MOV, f1, 1)
    add t2, t1, t0		#  (ADD, $0, f1, f0)
    mv t3, t2		#  (MOV, f2, $0)
    add t2, t3, t1		#  (ADD, $1, f2, f1)
    mv t4, t2		#  (MOV, f3, $1)
    add t2, t4, t3		#  (ADD, $2, f3, f2)
    mv t5, t2		#  (MOV, f4, $2)
    add t2, t5, t4		#  (ADD, $3, f4, f3)
    mv t6, t2		#  (MOV, f5, $3)
    add t2, t6, t5		#  (ADD, $4, f5, f4)
    sw t6, 0(zero)		#  
    mv t6, t2		#  (MOV, f6, $4)
    sw t4, 4(zero)		#  
    lw t4, 0(zero)		#  
    add t2, t6, t4		#  (ADD, $5, f6, f5)
    sw t4, 8(zero)		#  
    mv t4, t2		#  (MOV, f7, $5)
    add t2, t4, t6		#  (ADD, $6, f7, f6)
    sw t6, 12(zero)		#  
    mv t6, t2		#  (MOV, f8, $6)
    add t2, t6, t4		#  (ADD, $7, f8, f7)
    sw t4, 16(zero)		#  
    mv t4, t2		#  (MOV, f9, $7)
    add t2, t4, t6		#  (ADD, $8, f9, f8)
    sw t1, 20(zero)		#  
    mv t1, t2		#  (MOV, f10, $8)
    add t2, t1, t4		#  (ADD, $9, f10, f9)
    sw t5, 24(zero)		#  
    mv t5, t2		#  (MOV, f11, $9)
    add t2, t5, t1		#  (ADD, $10, f11, f10)
    sw t6, 28(zero)		#  
    mv t6, t2		#  (MOV, f12, $10)
    add t2, t6, t5		#  (ADD, $11, f12, f11)
    sw t4, 32(zero)		#  
    mv t4, t2		#  (MOV, f13, $11)
    add t2, t4, t6		#  (ADD, $12, f13, f12)
    sw t3, 36(zero)		#  
    mv t3, t2		#  (MOV, f14, $12)
    add t2, t3, t4		#  (ADD, $13, f14, f13)
    sw t3, 40(zero)		#  
    mv t3, t2		#  (MOV, f15, $13)
    sw t1, 44(zero)		#  
    lw t1, 40(zero)		#  
    add t2, t3, t1		#  (ADD, $14, f15, f14)
    sw t4, 48(zero)		#  
    mv t4, t2		#  (MOV, f16, $14)
    add t2, t4, t3		#  (ADD, $15, f16, f15)
    sw t5, 52(zero)		#  
    mv t5, t2		#  (MOV, f17, $15)
    add t2, t5, t4		#  (ADD, $16, f17, f16)
    sw t5, 56(zero)		#  
    mv t5, t2		#  (MOV, f18, $16)
    sw t1, 60(zero)		#  
    lw t1, 56(zero)		#  
    add t2, t5, t1		#  (ADD, $17, f18, f17)
    sw t5, 64(zero)		#  
    mv t5, t2		#  (MOV, f19, $17)
    mv t2, t0		#  (MOV, s0, f0)
    sw t6, 68(zero)		#  
    lw t6, 20(zero)		#  
    add t0, t2, t6		#  (ADD, $18, s0, f1)
    mv t2, t0		#  (MOV, s1, $18)
    lw t6, 36(zero)		#  
    add t0, t2, t6		#  (ADD, $19, s1, f2)
    mv t2, t0		#  (MOV, s2, $19)
    lw t6, 4(zero)		#  
    add t0, t2, t6		#  (ADD, $20, s2, f3)
    mv t2, t0		#  (MOV, s3, $20)
    lw t6, 24(zero)		#  
    add t0, t2, t6		#  (ADD, $21, s3, f4)
    mv t2, t0		#  (MOV, s4, $21)
    lw t6, 0(zero)		#  
    add t0, t2, t6		#  (ADD, $22, s4, f5)
    mv t2, t0		#  (MOV, s5, $22)
    lw t6, 12(zero)		#  
    add t0, t2, t6		#  (ADD, $23, s5, f6)
    mv t2, t0		#  (MOV, s6, $23)
    lw t6, 16(zero)		#  
    add t0, t2, t6		#  (ADD, $24, s6, f7)
    mv t2, t0		#  (MOV, s7, $24)
    lw t6, 28(zero)		#  
    add t0, t2, t6		#  (ADD, $25, s7, f8)
    mv t2, t0		#  (MOV, s8, $25)
    lw t6, 32(zero)		#  
    add t0, t2, t6		#  (ADD, $26, s8, f9)
    mv t2, t0		#  (MOV, s9, $26)
    lw t6, 44(zero)		#  
    add t0, t2, t6		#  (ADD, $27, s9, f10)
    mv t2, t0		#  (MOV, s10, $27)
    lw t6, 52(zero)		#  
    add t0, t2, t6		#  (ADD, $28, s10, f11)
    mv t2, t0		#  (MOV, s11, $28)
    lw t6, 68(zero)		#  
    add t0, t2, t6		#  (ADD, $29, s11, f12)
    mv t2, t0		#  (MOV, s12, $29)
    lw t6, 48(zero)		#  
    add t0, t2, t6		#  (ADD, $30, s12, f13)
    mv t2, t0		#  (MOV, s13, $30)
    lw t6, 40(zero)		#  
    add t0, t2, t6		#  (ADD, $31, s13, f14)
    mv t2, t0		#  (MOV, s14, $31)
    add t0, t2, t3		#  (ADD, $32, s14, f15)
    mv t2, t0		#  (MOV, s15, $32)
    add t0, t2, t4		#  (ADD, $33, s15, f16)
    mv t2, t0		#  (MOV, s16, $33)
    add t0, t2, t1		#  (ADD, $34, s16, f17)
    mv t1, t0		#  (MOV, s17, $34)
    lw t2, 64(zero)		#  
    add t0, t1, t2		#  (ADD, $35, s17, f18)
    mv t1, t0		#  (MOV, s18, $35)
    add t0, t1, t5		#  (ADD, $36, s18, f19)
    mv t1, t0		#  (MOV, s19, $36)
    mv a0, t1		#  (RET, , s19)
