#include <stdio.h>
#include <stdlib.h>
#include<time.h>
#include<math.h>

#define MAXLEN 4096
/**��Կ���ɹ���**/
//�������������

int prime[10]={2,3,5,7,11,13,17,19,23,29};//������������

int getprime(int *p,int *q){
    int temp1=0,temp2=1;
    int flag;
    srand((unsigned)time(NULL));
    for(int i=0;temp1<1000 || !flag;i++)
    {
        temp1 = rand();
        //temp1 = rand()%10000;
        flag = Miller_Rabin(temp1);
    }
    *p = temp1;
    //srand((unsigned)time(NULL));
    for(int j=0;temp2<1000 || !flag||abs(temp1-temp2)<200;j++){
        temp2 = rand();
        //temp2 = rand()%10000;
        flag = Miller_Rabin(temp2);
    }
    *q= temp2;
}


long long Quick_Multiply(int a,int b,int c)  //���ٻ����� ,aΪ������cΪȡģ������bΪ������a*b % c
{
    long long ans=0,res=a;
    while(b)
    {
        if(b&1){
         ans=(ans+res)%c;//��ǰλΪ1,��һ�δ��ڵ��������ϴ����������ȡ�࣬ģ�ļӷ�
        }
        res=(res+res)%c;//��һλ������
        b>>=1;
    }
    return ans;
}


long long Quick_Power(int a,int b,int c)     //���������࣬aΪ������cΪȡģ������bΪָ��
{
    long long ans=1,res=a;
    while(b)
    {
        if(b&1)
          ans=Quick_Multiply(ans,res,c);//��һ������˵�ǰ������ȡ�࣬ģ�ĳ˷�
        res=Quick_Multiply(res,res,c);//��һ�ε���
        b>>=1;
    }
    return ans;
}



int Miller_Rabin(int x){
   int i,j,k;
   int num=0;
   int t = x-1;
   if(x==2) return 1;//�ж�2
   if(x<2 || !(x&1)) return 0;//�ж�ż����1
   while(!(t&1)){//ͨ����λ���,Ҳ��ͨ��ȡ����
      num++;
      t >>=1;
   }
   for(i=0;i<10&& (prime[i]<x);i++){
      int temp = prime[i];
      int b = Quick_Power(temp,t,x);
      for(j =1 ;j<=num;j++){
        k=Quick_Multiply(b,b,x);
       // printf("M:%d,%d,%d\n",b,k,prime[i]);
         if(k==1 && b!=1 && b!=x-1){
            return 0;
         }
         b = k;//��������
      }

      if(b!=1)  return 0;
   }


 return 1;
}

/**ppt�ϵģ��õ���������ȷ���е��**/
int de_Miller_Rabin(int x){
   int i,j,k;
   int num=0;
   int t = x-1;
   if(x==2) return 1;//�ж�2
   if(x<2 || !(x&1)) return 0;//�ж�ż����1
   while(!(t&1)){//ͨ����λ���
      num++;
      t >>=1;
   }

   for(i=0;i<10&& (prime[i]<x);i++){
      int temp = prime[i];
      int b = Quick_Power(temp,t,x);
      if(b==1 || b==x-1){
        return 1;
      }
      for(j =1 ;j<=num;j++){
        k=Quick_Multiply(b,b,x);
         if(k==x-1){
            return 1;
         }
         b = k;//��������
      }

}


 return 0;
}

//��Կ˽Կ�Ļ��
//ŷ������㷨
int Euclid(int a,int b){
    if(b>a){
        if(b%a==0){
            return a;
        }
        else
            return Euclid(a,b%a);
        }
    else{
           if(a%b == 0){
            return b;
            }else{
             return Euclid(b,a%b);
           }
    }

}
//��չŷ������㷨

int EX_Euclid(int a,int b,int *d){//��aģb�ĳ˷���
    int x0=1,x1=0;
    int y0=0,y1=1;
    int x,y;
    int r = b%a;
    int q= (b-r)/a;
    int temp =b;

    while(r){
        x=x0 - q*x1; y = y0 - q*y1;
        x0 = x1;y0=y1;
        x1 = x ; y1 =y;
        b = a ; a = r ; r = b%a;
        q=(b-r)/a;

    }

    *d = (y+temp)%temp ;    // �˷��棬��֤Ϊ����
    return a;//aΪ����

}


int getEandD(int k,int *e,int *d){
    int m=*d;
    srand((unsigned)time(NULL));
    for(int i=2;i<k;){
        int gcd = EX_Euclid(i,k,&m);
        //printf("%d gcd:%d\n",i,gcd);
        if(gcd == 1 && i > 1000 && Miller_Rabin(i)){
            *e = i;
            *d = m;
            break;
        }
        //i= i + rand();
        i= i + rand()%7000;
    }



}




/***�ļ���д����*****/
/**
 * ���ַ���д���ļ�
 */
void writeStrToFile(char *str, int len, char *fileName)
{
    FILE *fp;
    fp = fopen(fileName, "wb");
    for(int i = 0; i < len; i++)
        putc(str[i], fp);
    fclose(fp);
}

/**
 * ���ļ��ж�ȡ�ַ���
 */
int readStrFromFile(char *fileName, char *str)
{
    FILE *fp = fopen(fileName, "rb");
    if(fp == NULL)
    {
        printf("���ļ�������ȷ���ļ����ڵ�ǰĿ¼�£�\n");
        exit(0);
    }

    int i;
    //for(i = 0; i < MAXLEN && (str[i] = getc(fp)) != EOF ; i++);
     for(i = 0; i < MAXLEN ; i++){
            str[i] = getc(fp);
           if(feof(fp)){
            break;
           }

          // printf("%c",str[i]);

     };

     rewind(fp);


    if(i >= MAXLEN)
    {
        printf("�����ļ�����\n");
        exit(0);
    }

    str[i] = '\0';


    fclose(fp);
    return i;
}



void Rsaread(int e,int n,int k)
{
    char fileName[64];
    char c[MAXLEN];
    int  M[MAXLEN];
    char A[MAXLEN];
    int  plen;
    printf("������Ҫ���ܵ��ļ��������ļ�����ͱ�������ͬһ��Ŀ¼\n");
    if(scanf("%s",fileName)==1)
    {
     int clen = readStrFromFile(fileName,c);

     if(clen %2 == 1){
        c[clen] = 0;
        clen ++ ;
     }
      //printf("%d,%c\n",clen,c[clen-1]);
     int mlen = RSA_Encrypt(clen,c,e,n,k,M,A);
     printf("���ܺ�����ĳ��ȣ�%d\n",mlen);
     //if(k=1){
     //print_ten(M,mlen);
     //}
     printf("\n");
     rsaStrToFile(M,A,mlen,k);


    }

}


/**���ܹ���**/
int deal_Encrypt(char c)
{
      int a = c;
      if(a>=100){
        return a % 100 + 1;
      }else{
         return a;

      }


}


int RSA_Encrypt(int clen,char *str,int e,int n,int k,int *con,char *con1)//�ǵ�ȥ��d
{
   printf("��ʼ����.........\n");
   int c;int a;int b;
   int temp;int res;
   int num = 0;
   for(int i = 0;i <clen ;i=i+2){//�޸ĵĵط�i<clen

           a = deal_Encrypt(str[i]);
           //printf("a:%d \n",a);
           b = deal_Encrypt(str[i+1]);
           //printf("b:%d \n",b);
           temp = a*100 + b;

       res = Quick_Power(temp,e,n);
//      printf("%d,%d,%d,%d,%d\n",e,n,temp,res,Quick_Power(res,d,n));
      if(k==1){
      for(int j= 0;j<3;j++){//�������Ϊ10λ��ÿ4λʮ����Ϊһ�飬����һ����Ϊ3��,ascii�ַ��洢��Ҫÿһ��ת��Ϊ16���ƣ�֮��ÿһ��ɱ�Ϊ�����ַ�����1
            con[num] = res%10000;
           // printf("%d\n",con[num]);
            num++;
            res = res/10000;
       }
      }
      else{
       for(int j= 0;j<5;j++){//�������Ϊ10λ��ÿ2λʮ����Ϊһ�飬����һ����Ϊ5��,֮��ÿһ��Ϊһ���ַ�������2
            //printf("%d\n",res);
            con1[num] = res%100;
            //printf("%d\n",con1[num]);
            num++;
            res = res/100;
       }
      }
   }

    return num;
}


void fillwithzero(int k){
    if(k<10){
        printf("000%d",k);
    }
    else if(k<100&&k>=10){
        printf("00%d",k);
    }
    else if(k<1000&&k>=100){
        printf("0%d",k);
    }else {
       printf("%d",k);
     }
}


void print_ten(int *c,int num){//��ӡ����
       //printf("%d\n",num);
      for(int i = 0;i<num; i++){
        if(i % 3 == 0) fillwithzero(c[i+2]);
        else if(i%3 == 1) fillwithzero(c[i]);
        else {fillwithzero(c[i-2]);printf(" ");}
        if(i%20 == 0 && i!=0){
            printf("\n");
        }

      }

}


/**���ܽ�����뵽�ļ��У���ӡ��ʮ���ƴ����ASCii�ַ��洢**/
void rsaStrToFile(int  *con,char *con1,int mlen,int k)
{
    char fileName[64],fileName2[64];
    FILE *fp;
    if(k==2){
     printf("����������Ҫд����Ϊ5��洢���ļ���������'test.txt':\n");
     if(scanf("%s", fileName) == 1)
    {//���Լ�����ԭ����read��ʽ
        writeStrToFile(con1, mlen, fileName);
        printf("�Ѿ����ַ�����д��%s����,���������иó���ĵ�ǰĿ¼���ҵ�����\n", fileName);
     }
    }
    else {
     printf("����������Ҫд����Ϊ3��洢���ļ���������'test.txt':\n");
     if(scanf("%s", fileName2) == 1)
    {
        fp = fopen (fileName2, "w+");
       // printf("changdu:%d \n",mlen);
        for(int i=0;i<mlen;i++){
            //printf("%d ",con[i]);
            fprintf(fp,"%d ",con[i]);
        }
        fclose(fp);
        printf("�Ѿ�����������д��%s����,���������иó���ĵ�ǰĿ¼���ҵ�����\n", fileName2);
    }
    }
}




/**���ܹ���**/

int readIntFromFile(char *fileName, int *M)
{//����Ϊ3���ר�Ŷ���
    FILE *fp = fopen(fileName, "rb");
    if(fp == NULL)
    {
        printf("���ļ�������ȷ���ļ����ڵ�ǰĿ¼�£�\n");
        exit(0);
    }

    int i;
    //for(i = 0; i < MAXLEN && (str[i] = getc(fp)) != EOF ; i++);
     for(i = 0; i < MAXLEN ; i++){
           fscanf(fp,"%d",&M[i]);
           if(feof(fp)){
            break;
           }

          // printf("%c",str[i]);

     };

     rewind(fp);


    if(i >= MAXLEN)
    {
        printf("�����ļ�����\n");
        exit(0);
    }

    fclose(fp);
    return i;
}


void deRsaread(int n,int d)
{//����2
    char fileName[64];
    char  c[MAXLEN];
    int  M[MAXLEN];
    char A[MAXLEN];
    int  plen;
    printf("������Ҫ���ܵ��ļ��������ļ�����ͱ�������ͬһ��Ŀ¼\n");
    if(scanf("%s",fileName)==1)
    {
     int clen = readStrFromFile(fileName,c);
      for(int i=0;i<clen;i++){
        M[i] =  c[i];
        //printf("%d\n",M[i]);
      }
     // printf("%d,%c\n",clen,c[clen-1]);
     plen=RSA_Decrypt(M,A,clen,d,n);
     print_ans(A,plen);
     //int mlen = RSA_Decrypt(clen,c,e,n,d,M,A,&plen);
     //printM(M,mlen);
     printf("\n");
     //AnsToFile(M,A,plen,mlen);

    }

}

void deRsaread_b(int n,int d)
{
   char fileName[64];
   int M[MAXLEN];
   char C[MAXLEN];
   printf("������Ҫ���ܵ��ļ��������ļ�����ͱ�������ͬһ��Ŀ¼\n");
   if(scanf("%s",fileName)==1)
    {
      int clen = readIntFromFile(fileName,M);
      //for(int i=0;i<clen;i++){
        //printf("%d\n",M[i]);
      //}
      //printf("%d\n",clen);
      int len = RSA_Decrypt_b(M,C,clen,d,n);
      print_ans(C,len);
     //int mlen = RSA_Decrypt(clen,c,e,n,d,M,A,&plen);
     //printM(M,mlen);
      printf("\n");
     //AnsToFile(M,A,plen,mlen);

    }
}


int deal_Decrypt(int c)
{
    if(c < 32 && c>0){
        return c+99;
    }else{
       return c;
    }

}


int RSA_Decrypt(int *M,char *str,int clen,int d,int n){//����ȥ��e
    printf("��ʼ����.........\n");

    int sum = 0;
    int temp = 0;
    int num =0;
    int a,b;
    for(int i=0;i<clen;i++)
    {
        sum = sum + M[i]*pow(100,i%5);

       // printf("���ؽ��%d\n",sum);
        if(i%5 == 4){
            temp = Quick_Power(sum,d,n);
            b = deal_Decrypt(temp % 100);
            a = deal_Decrypt(temp /100);
            //printf("%d,%c\n",a,a);
            str[num] = a;
            num ++;
            str[num] = b;
            num++;
            sum = 0;

        }
    }
  return num;


}


int RSA_Decrypt_b(int *M,char *str,int clen,int d,int n){
    printf("��ʼ����.........\n");

    int sum = 0;
    int temp = 0;
    int num =0;
    int a,b;

    for(int i=0;i<clen;i++)
    {
        sum = sum + M[i]*pow(10000,i%3);

        //printf("���ؽ��%d\n",sum);
        if(i%3 == 2){
            temp = Quick_Power(sum,d,n);
            b = deal_Decrypt(temp % 100);
            a = deal_Decrypt(temp /100);
            //printf("%d,%c\n",a,a);
            str[num] = a;
            num ++;
            str[num] = b;
            num++;
            sum = 0;

        }
    }
    return num;
}


void print_ans(char *c,int num)

{
    int j;
    char fileName[64];
    for(int i=0;i<num;i++){

        printf("%c",c[i]);
    }
    printf("\n��������\n");
    printf("�Ƿ���Ҫ�������ģ�1.Yes   2.No\n");
    scanf("%d",&j);
    if(j==1){
        printf("����������Ҫд�����ĵ��ļ���������'test.txt':\n");
        if(scanf("%s", fileName) == 1)
       {//���Լ�����ԭ����read��ʽ
        writeStrToFile(c, num, fileName);
        printf("�Ѿ����ַ�����д��%s����,���������иó���ĵ�ǰĿ¼���ҵ�����\n", fileName);
       }
    }
}


/**�������ܺ���**/
void dumpfile(int e,int d,int n){
    printf("��Կ˽Կ���Ѵ����ļ���\n");
    FILE * fp;

    fp = fopen ("miyao.txt", "w+");
    fprintf(fp, "%s %d %s %d", "(e,n):", e,"," ,n);
    fprintf(fp,"%s","\n");
    fprintf(fp, "%s %d %s %d", "(d,n):", d,"," ,n);

    fclose(fp);
}


//���ȵ�������
int main()
{
    int choose;
    int flag = 1;
    int p,q;//��������
    int e,d;//����˽Կ����
    int k;
    int cho1;
    int c;
    while(flag)
	{
		printf("��ѡ��\n");
		printf("1������Կ��\n");
		printf("2�������ļ�\n");
		printf("3�������ļ�\n");
		printf("4���˳�\n");
        scanf("%d",&choose);
		switch(choose)
		{
			case 1:
                getprime(&p,&q);//�õ�����
                k = (p-1)*(q-1);
                printf("��һ��������p��%d,�ڶ���������q��%d,���������ĳ˻�n��%d,n��ŷ��������%d\n",p,q,p*q,k);
                //EX_Euclid(15,49,&d);
                getEandD(k,&e,&d);
                printf("���ɵ�˽Կ��%d,��Կ��%d\n",d,e);
                dumpfile(e,d,p*q);
               // printf("%ld,%d,%ld\n",d*e,k,(d*e)%k);
				break;
			case 2:
			    printf("1.ʹ���Զ����ɵ���Կ\n2.�Լ�������Կ\n");
			    scanf("%d",&cho1);
			    switch(cho1){
			        case 1:
			            printf("��ѡ�����Ĵ洢���ı��ķ�ʽ���⽫�������ĵķ��鷽ʽ��1.���� 2.�����ַ�\n");
			            scanf("%d",&c);
                        Rsaread(e,p*q,c);
                        break;
                    case 2:
                        printf("������e:\n");
                        scanf("%d",&e);
                        printf("������n:\n");
                        scanf("%d",&k);
                        printf("��ѡ�����Ĵ洢���ı��ķ�ʽ���⽫�������ĵķ��鷽ʽ��1.���� 2.�����ַ�\n");
                        scanf("%d",&c);
                        Rsaread(e,k,c);
                        break;
                    default:
                          break;
			    }

				break;
            case 3:
                printf("1.ʹ���Զ����ɵ���Կ\n2.�Լ�������Կ\n");
			    scanf("%d",&cho1);
			    switch(cho1){
			        case 1:
			            printf("��ѡ�����Ĵ洢���ı��ķ�ʽ����ȡ�������ĵķ��鷽ʽ��1.���� 2.�����ַ�(���Դ��ĵ��鿴)\n");
			            scanf("%d",&c);
			            if(c==2){
                        deRsaread(p*q,d);
			            }
			            else{
                        deRsaread_b(p*q,d);
			            }
                        break;
                    case 2:
                        printf("��ѡ�����Ĵ洢���ı��ķ�ʽ����ȡ�������ĵķ��鷽ʽ��1.���� 2.�����ַ�(���Դ��ĵ��鿴)\n");
                        scanf("%d",&c);
                        printf("������d:\n");
                        scanf("%d",&d);
                        printf("������n:\n");
                        scanf("%d",&k);
                        if(c==2){
                        deRsaread(k,d);
			            }
			            else{
                        deRsaread_b(k,d);
			            }
                        break;
                    default:
                          break;
			    }
                 break;
            case 4:
                 flag = 0;
                 break;
		}
	}
}
