package cn.edu.hitsz.compiler.parser;

import cn.edu.hitsz.compiler.NotImplementedException;
import cn.edu.hitsz.compiler.lexer.Token;
import cn.edu.hitsz.compiler.parser.table.GrammarInfo;
import cn.edu.hitsz.compiler.parser.table.NonTerminal;
import cn.edu.hitsz.compiler.parser.table.Production;
import cn.edu.hitsz.compiler.parser.table.Status;
import cn.edu.hitsz.compiler.symtab.SourceCodeType;
import cn.edu.hitsz.compiler.symtab.SymbolTable;

import javax.xml.transform.Source;
import java.util.Stack;

// TODO: 实验三: 实现语义分析
public class SemanticAnalyzer implements ActionObserver {

    private  SymbolTable symbolTable =new SymbolTable();
    private Stack<Status>  status = new Stack<>();
   // private Stack<Token>   tokens =new Stack<>();
   // private Stack<NonTerminal> nonTerminals = new Stack<>();
   // private Stack<SourceCodeType> sourceCodeTypes = new Stack<>();
    private Stack<Symbol> symbols = new Stack<>();

    @Override
    public void whenAccept(Status currentStatus) {
        // TODO: 该过程在遇到 Accept 时要采取的代码动作
       // throw new NotImplementedException();
        //GrammarInfo.getBeginProduction()
        System.out.println("语义分析完成");
    }

    @Override
    public void whenReduce(Status currentStatus, Production production) {
        // TODO: 该过程在遇到 reduce production 时要采取的代码动作
        switch (production.index()) {
            // 我们推荐在 case 后面使用注释标明产生式
            // 这样能比较清楚地看出产生式索引与产生式的对应关系


            case 4 -> { // S -> D id
                /* ... */
                //System.out.println(tokens.peek());
                //System.out.println(nonTerminals.peek());
                //System.out.println(production.body().get(0));
               // System.out.println(symbols.peek().isToken());
                if(symbolTable.has(String.valueOf(symbols.peek().token.getText()))){
                    Token token = symbols.pop().token;
                    symbolTable.get(String.valueOf(token.getText())).setType(symbols.peek().sourceCodeType);
                }else {
                   // Token token = symbols.pop().token;
                   //  symbolTable.add(token.getText());
                   // symbolTable.get(token.getText()).setType(symbols.peek().sourceCodeType);
                    throw new RuntimeException("符号表不含该标志变量！");
                }
                symbols.pop();
                symbols.push(new Symbol(null,production.head()));
                break;
            }

            case 5 -> { // D -> int
                /* ... */
                for(int j =0 ;j<production.body().size();j++){//弹栈
                  symbols.pop();
                }
                symbols.push(new Symbol(null,production.head()));
                symbols.peek().setSourceCodeType(SourceCodeType.Int);
               // System.out.println(symbols.peek().nonTerminal);
                //System.out.println("good");
                break;
            }



            case 2,3,6,7, 8, 9, 10, 11, 12, 13, 14, 15 ->{
                //System.out.println(symbols.size() - production.body().size());
                for(int j =0 ;j<production.body().size();j++){//弹栈
                    symbols.pop();
                }
                symbols.push(new Symbol(null,production.head()));
                //System.out.println(symbols.peek().nonTerminal);
                break;
            }
            // ...
            default -> { //
               // throw new RuntimeException("Unknown production index");
                // 或者任何默认行为
                break;
            }
        }
        //throw new NotImplementedException();
    }

    @Override
    public void whenShift(Status currentStatus, Token currentToken) {
        // TODO: 该过程在遇到 shift 时要采取的代码动作
        //throw new NotImplementedException();
        status.push(currentStatus);//保存之前的规约信息的状态
        //tokens.push(currentToken);
        symbols.push(new Symbol(currentToken,null));
    }

    @Override
    public void setSymbolTable(SymbolTable table) {
        // TODO: 设计你可能需要的符号表存储结构
        // 如果需要使用符号表的话, 可以将它或者它的一部分信息存起来, 比如使用一个成员变量存储
        symbolTable = table;
        //throw new NotImplementedException();
    }



    public Symbol makeSymbol(Token token){
        return new Symbol(token, null);
    }

    public Symbol makeSymbol(NonTerminal nonTerminal){
        return new Symbol(null, nonTerminal);
    }


    class Symbol{
        Token token;
        NonTerminal nonTerminal;
        SourceCodeType sourceCodeType;

        private Symbol(Token token, NonTerminal nonTerminal){
            this.token = token;
            this.nonTerminal = nonTerminal;
            this.sourceCodeType = null;
        }



        public boolean isToken(){
            return this.token != null;
        }

        public boolean isNonterminal(){
            return this.nonTerminal != null;
        }

        public void setSourceCodeType(SourceCodeType sourceCodeType) {
            this.sourceCodeType = sourceCodeType;
        }
    }
}

