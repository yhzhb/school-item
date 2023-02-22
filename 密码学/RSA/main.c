#include <stdio.h>
#include <stdlib.h>
#include<time.h>
#include<math.h>

#define MAXLEN 4096
/**密钥生成过程**/
//随机素数的生成

int prime[10]={2,3,5,7,11,13,17,19,23,29};//素数测试数组

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


long long Quick_Multiply(int a,int b,int c)  //快速积求余 ,a为乘数，c为取模的数，b为乘数；a*b % c
{
    long long ans=0,res=a;
    while(b)
    {
        if(b&1){
         ans=(ans+res)%c;//当前位为1,上一次存在的余数加上此项的余数再取余，模的加法
        }
        res=(res+res)%c;//下一位的余数
        b>>=1;
    }
    return ans;
}


long long Quick_Power(int a,int b,int c)     //快速幂求余，a为底数，c为取模的数，b为指数
{
    long long ans=1,res=a;
    while(b)
    {
        if(b&1)
          ans=Quick_Multiply(ans,res,c);//上一步的余乘当前的余在取余，模的乘法
        res=Quick_Multiply(res,res,c);//下一次的余
        b>>=1;
    }
    return ans;
}



int Miller_Rabin(int x){
   int i,j,k;
   int num=0;
   int t = x-1;
   if(x==2) return 1;//判断2
   if(x<2 || !(x&1)) return 0;//判断偶数或1
   while(!(t&1)){//通过移位获得,也可通过取余获得
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
         b = k;//继续二次
      }

      if(b!=1)  return 0;
   }


 return 1;
}

/**ppt上的，得到素数的正确率有点低**/
int de_Miller_Rabin(int x){
   int i,j,k;
   int num=0;
   int t = x-1;
   if(x==2) return 1;//判断2
   if(x<2 || !(x&1)) return 0;//判断偶数或1
   while(!(t&1)){//通过移位获得
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
         b = k;//继续二次
      }

}


 return 0;
}

//公钥私钥的获得
//欧几里得算法
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
//扩展欧几里得算法

int EX_Euclid(int a,int b,int *d){//求a模b的乘法逆
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

    *d = (y+temp)%temp ;    // 乘法逆，保证为正数
    return a;//a为余数

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




/***文件读写处理*****/
/**
 * 把字符串写进文件
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
 * 从文件中读取字符串
 */
int readStrFromFile(char *fileName, char *str)
{
    FILE *fp = fopen(fileName, "rb");
    if(fp == NULL)
    {
        printf("打开文件出错，请确认文件存在当前目录下！\n");
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
        printf("解密文件过大！\n");
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
    printf("请输入要加密的文件名，该文件必须和本程序在同一个目录\n");
    if(scanf("%s",fileName)==1)
    {
     int clen = readStrFromFile(fileName,c);

     if(clen %2 == 1){
        c[clen] = 0;
        clen ++ ;
     }
      //printf("%d,%c\n",clen,c[clen-1]);
     int mlen = RSA_Encrypt(clen,c,e,n,k,M,A);
     printf("加密后的密文长度：%d\n",mlen);
     //if(k=1){
     //print_ten(M,mlen);
     //}
     printf("\n");
     rsaStrToFile(M,A,mlen,k);


    }

}


/**加密过程**/
int deal_Encrypt(char c)
{
      int a = c;
      if(a>=100){
        return a % 100 + 1;
      }else{
         return a;

      }


}


int RSA_Encrypt(int clen,char *str,int e,int n,int k,int *con,char *con1)//记得去掉d
{
   printf("开始加密.........\n");
   int c;int a;int b;
   int temp;int res;
   int num = 0;
   for(int i = 0;i <clen ;i=i+2){//修改的地方i<clen

           a = deal_Encrypt(str[i]);
           //printf("a:%d \n",a);
           b = deal_Encrypt(str[i+1]);
           //printf("b:%d \n",b);
           temp = a*100 + b;

       res = Quick_Power(temp,e,n);
//      printf("%d,%d,%d,%d,%d\n",e,n,temp,res,Quick_Power(res,d,n));
      if(k==1){
      for(int j= 0;j<3;j++){//余数最大为10位，每4位十进制为一组，密文一个分为3组,ascii字符存储需要每一个转化为16进制，之后每一组可变为两个字符分组1
            con[num] = res%10000;
           // printf("%d\n",con[num]);
            num++;
            res = res/10000;
       }
      }
      else{
       for(int j= 0;j<5;j++){//余数最大为10位，每2位十进制为一组，密文一个分为5组,之后每一组为一个字符，分组2
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


void print_ten(int *c,int num){//打印密文
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


/**加密结果输入到文件中，打印出十进制储存和ASCii字符存储**/
void rsaStrToFile(int  *con,char *con1,int mlen,int k)
{
    char fileName[64],fileName2[64];
    FILE *fp;
    if(k==2){
     printf("请输入你想要写进分为5组存储的文件名，比如'test.txt':\n");
     if(scanf("%s", fileName) == 1)
    {//可以继续用原来的read方式
        writeStrToFile(con1, mlen, fileName);
        printf("已经将字符密文写进%s中了,可以在运行该程序的当前目录中找到它。\n", fileName);
     }
    }
    else {
     printf("请输入你想要写进分为3组存储的文件名，比如'test.txt':\n");
     if(scanf("%s", fileName2) == 1)
    {
        fp = fopen (fileName2, "w+");
       // printf("changdu:%d \n",mlen);
        for(int i=0;i<mlen;i++){
            //printf("%d ",con[i]);
            fprintf(fp,"%d ",con[i]);
        }
        fclose(fp);
        printf("已经将数字密文写进%s中了,可以在运行该程序的当前目录中找到它。\n", fileName2);
    }
    }
}




/**解密过程**/

int readIntFromFile(char *fileName, int *M)
{//给分为3组的专门读法
    FILE *fp = fopen(fileName, "rb");
    if(fp == NULL)
    {
        printf("打开文件出错，请确认文件存在当前目录下！\n");
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
        printf("解密文件过大！\n");
        exit(0);
    }

    fclose(fp);
    return i;
}


void deRsaread(int n,int d)
{//分组2
    char fileName[64];
    char  c[MAXLEN];
    int  M[MAXLEN];
    char A[MAXLEN];
    int  plen;
    printf("请输入要解密的文件名，该文件必须和本程序在同一个目录\n");
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
   printf("请输入要解密的文件名，该文件必须和本程序在同一个目录\n");
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


int RSA_Decrypt(int *M,char *str,int clen,int d,int n){//可以去掉e
    printf("开始解密.........\n");

    int sum = 0;
    int temp = 0;
    int num =0;
    int a,b;
    for(int i=0;i<clen;i++)
    {
        sum = sum + M[i]*pow(100,i%5);

       // printf("揭秘结果%d\n",sum);
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
    printf("开始解密.........\n");

    int sum = 0;
    int temp = 0;
    int num =0;
    int a,b;

    for(int i=0;i<clen;i++)
    {
        sum = sum + M[i]*pow(10000,i%3);

        //printf("揭秘结果%d\n",sum);
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
    printf("\n明文如上\n");
    printf("是否需要导出明文：1.Yes   2.No\n");
    scanf("%d",&j);
    if(j==1){
        printf("请输入你想要写进明文的文件名，比如'test.txt':\n");
        if(scanf("%s", fileName) == 1)
       {//可以继续用原来的read方式
        writeStrToFile(c, num, fileName);
        printf("已经将字符明文写进%s中了,可以在运行该程序的当前目录中找到它。\n", fileName);
       }
    }
}


/**辅助功能函数**/
void dumpfile(int e,int d,int n){
    printf("公钥私钥对已存入文件中\n");
    FILE * fp;

    fp = fopen ("miyao.txt", "w+");
    fprintf(fp, "%s %d %s %d", "(e,n):", e,"," ,n);
    fprintf(fp,"%s","\n");
    fprintf(fp, "%s %d %s %d", "(d,n):", d,"," ,n);

    fclose(fp);
}


//调度的主程序
int main()
{
    int choose;
    int flag = 1;
    int p,q;//素数定义
    int e,d;//公，私钥定义
    int k;
    int cho1;
    int c;
    while(flag)
	{
		printf("请选择：\n");
		printf("1、生成钥匙\n");
		printf("2、加密文件\n");
		printf("3、解密文件\n");
		printf("4、退出\n");
        scanf("%d",&choose);
		switch(choose)
		{
			case 1:
                getprime(&p,&q);//得到素数
                k = (p-1)*(q-1);
                printf("第一个大素数p：%d,第二个大素数q：%d,两个素数的乘积n：%d,n的欧拉函数：%d\n",p,q,p*q,k);
                //EX_Euclid(15,49,&d);
                getEandD(k,&e,&d);
                printf("生成的私钥：%d,公钥：%d\n",d,e);
                dumpfile(e,d,p*q);
               // printf("%ld,%d,%ld\n",d*e,k,(d*e)%k);
				break;
			case 2:
			    printf("1.使用自动生成的密钥\n2.自己输入密钥\n");
			    scanf("%d",&cho1);
			    switch(cho1){
			        case 1:
			            printf("请选择密文存储于文本的方式，这将决定密文的分组方式：1.数字 2.乱码字符\n");
			            scanf("%d",&c);
                        Rsaread(e,p*q,c);
                        break;
                    case 2:
                        printf("请输入e:\n");
                        scanf("%d",&e);
                        printf("请输入n:\n");
                        scanf("%d",&k);
                        printf("请选择密文存储于文本的方式，这将决定密文的分组方式：1.数字 2.乱码字符\n");
                        scanf("%d",&c);
                        Rsaread(e,k,c);
                        break;
                    default:
                          break;
			    }

				break;
            case 3:
                printf("1.使用自动生成的密钥\n2.自己输入密钥\n");
			    scanf("%d",&cho1);
			    switch(cho1){
			        case 1:
			            printf("请选择密文存储于文本的方式，这取决于密文的分组方式：1.数字 2.乱码字符(可以打开文档查看)\n");
			            scanf("%d",&c);
			            if(c==2){
                        deRsaread(p*q,d);
			            }
			            else{
                        deRsaread_b(p*q,d);
			            }
                        break;
                    case 2:
                        printf("请选择密文存储于文本的方式，这取决于密文的分组方式：1.数字 2.乱码字符(可以打开文档查看)\n");
                        scanf("%d",&c);
                        printf("请输入d:\n");
                        scanf("%d",&d);
                        printf("请输入n:\n");
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
