package cn.edu.hitsz.compiler.lexer;

import cn.edu.hitsz.compiler.NotImplementedException;
import cn.edu.hitsz.compiler.symtab.SymbolTable;
import cn.edu.hitsz.compiler.symtab.SymbolTableEntry;
import cn.edu.hitsz.compiler.utils.FileUtils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.StreamSupport;

/**
 * TODO: 实验一: 实现词法分析
 * <br>
 * 你可能需要参考的框架代码如下:
 *
 * @see Token 词法单元的实现
 * @see TokenKind 词法单元类型的实现
 */
public class LexicalAnalyzer {
    private final SymbolTable symbolTable;
    private BufferedReader bufferedReader;
    private StringBuffer stringBuffer ;
    private char [] SignList = {'/',';','+',',','(',')','-','=','*'};
    private ArrayList<Token> tokens =new ArrayList<>();
    public LexicalAnalyzer(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }
    public static enum  STATE {ERROR,START,DONE,ID,NUM};
    /**
     * 从给予的路径中读取并加载文件内容
     *
     * @param path 路径
     */

    public void loadFile(String path) {
        // TODO: 词法分析前的缓冲区实现
        // 可自由实现各类缓冲区
        // 或直接采用完整读入方法
        try {
            bufferedReader = new BufferedReader(new FileReader(path));
            stringBuffer = new StringBuffer();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
        //throw new NotImplementedException();
    }

    /**
     * 执行词法分析, 准备好用于返回的 token 列表 <br>
     * 需要维护实验一所需的符号表条目, 而得在语法分析中才能确定的符号表条目的成员可以先设置为 null
     */
    public void run() {
        // TODO: 自动机实现的词法分析过程
        int linenum = 0;
        String currentline = new String();
        try {
            while ((currentline = bufferedReader.readLine()) != null) {//逐行处理
               //  System.out.println(currentline);
                 linenum ++;
                 char [] charsArray = currentline.toCharArray();
                // System.out.println(SignList.length);
                 //遍历字符数组
                 int i=0;
               //  System.out.println("Line Length:"+ currentline.length());

                while(i<currentline.length()){
                          STATE state = STATE.START;
                          while(!state.equals(STATE.DONE)) {

                             //System.out.println(i);
                            // System.out.println((charsArray[i]==' '));
                              switch (state) {

                                  case ERROR://错误状态
                                      System.out.println("###Error###" + '\n' + "##错误出项在第" + linenum + "行！快去修改吧。##");
                                      state = STATE.DONE;
                                      throw new NotImplementedException();

                                  case START://开始状态

                                      if (i < currentline.length()) {
                                          if ((charsArray[i] == ' ') || (charsArray[i] == '\t') || (charsArray[i] == '\r') ||
                                                  (charsArray[i] == '\n')) {
                                              state = STATE.START;
                                          }else if(isLetter(charsArray[i])||(charsArray[i] == '_')){
                                              stringBuffer.append(charsArray[i]);
                                              state = STATE.ID;
                                          }else if(isNum(charsArray[i])){//不用isNum_without_zero是因为有0这种情况0也是数字
                                              stringBuffer.append(charsArray[i]);
                                              state =state.NUM;
                                          }else if(isSign(charsArray[i])){
                                              if(TokenKind.isAllowed(String.valueOf(charsArray[i]))) {
                                                 // System.out.println(TokenKind.fromString(String.valueOf(charsArray[i])));
                                                  tokens.add(Token.simple(TokenKind.fromString(String.valueOf(charsArray[i]))));
                                              }else{
                                                  tokens.add(Token.simple("Semicolon"));
                                              }
                                              state = STATE.DONE;
                                          }else {
                                              state =STATE.ERROR;
                                          }
                                      }
                                      break;
                                  case ID:
                                      if (i<currentline.length()){
                                          if(isNum(charsArray[i])||isLetter(charsArray[i])||(charsArray[i] == '_')){
                                              stringBuffer.append(charsArray[i]);
                                              state = STATE.ID;
                                          } else {//读到其他字符
                                              if(TokenKind.isAllowed(String.valueOf(stringBuffer))) {
                                                  //System.out.println(TokenKind.fromString(String.valueOf(stringBuffer)));
                                                 tokens.add(Token.simple(TokenKind.fromString(String.valueOf(stringBuffer))));
                                              }else {//自定义字符
                                                 tokens.add(Token.normal("id",String.valueOf(stringBuffer)));
                                                 //System.out.println(symbolTable.has(String.valueOf(stringBuffer)));
                                                  //symbolTable.add(String.valueOf(stringBuffer));
                                                 if(!symbolTable.has(String.valueOf(stringBuffer))){
                                                       symbolTable.add(String.valueOf(stringBuffer));
                                                 }
                                              }
                                              //System.out.println(String.valueOf(stringBuffer));
                                              //System.out.println(TokenKind.isAllowed(String.valueOf(stringBuffer)));
                                              state =STATE.DONE;
                                             i--; //回溯
                                          }

                                      }
                                      break;

                                  case NUM:
                                       if (i<currentline.length()){
                                           if(isNum(charsArray[i]) && (charsArray[i-1] != '0')){//排除08等命名情况
                                               stringBuffer.append(charsArray[i]);
                                               state =STATE.NUM;
                                           }else if ((isNum(charsArray[i])||isLetter(charsArray[i])) && (charsArray[i-1] == '0')){
                                               state = STATE.ERROR;
                                           }
                                           else {
                                               //System.out.println(String.valueOf(stringBuffer));
                                               if(TokenKind.isAllowed(String.valueOf(stringBuffer))) {
                                                   //System.out.println(TokenKind.fromString(String.valueOf(stringBuffer)));
                                                   tokens.add(Token.simple(TokenKind.fromString(String.valueOf(stringBuffer))));
                                               }else {
                                                   tokens.add(Token.normal("IntConst",String.valueOf(stringBuffer)));
                                               }
                                               state = STATE.DONE;
                                                i--;
                                           }

                                       }
                                       break;
                              }
                              i++;
                              if(state.equals(STATE.DONE)){
                                  stringBuffer = new StringBuffer();
                              }
                          }

                 }
            }
            tokens.add(Token.eof());//结束后加入eof
        }catch (IOException e){
            e.printStackTrace();
        }


        //throw new NotImplementedException();
    }



    /**
     * 获得词法分析的结果, 保证在调用了 run 方法之后调用
     *
     * @return Token 列表
     */
    public Iterable<Token> getTokens() {

        // TODO: 从词法分析过程中获取 Token 列表
        // 词法分析过程可以使用 Stream 或 Iterator 实现按需分析
        // 亦可以直接分析完整个文件
        // 总之实现过程能转化为一列表即可
       return tokens;
       //throw new NotImplementedException();
    }

    public void dumpTokens(String path) {
        FileUtils.writeLines(
            path,
            StreamSupport.stream(getTokens().spliterator(), false).map(Token::toString).toList()
        );
    }

   private boolean isNum(char ch){
        return (ch >= '0' && ch <= '9');
   }

   private boolean isLetter (char ch){
        return ((ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z'));
   }

  // private boolean isNum_without_zero(char ch){
       //return (ch >= '1' && ch <= '9');
   //}

    private boolean isSign (char ch){
        boolean flag = false;
        for(int i=0;i< SignList.length;i++){
            if(ch == SignList[i]){
                flag = true;
                break;
            }
        }
        return flag;
    }


}
