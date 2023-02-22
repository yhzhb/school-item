package cn.edu.hitsz.compiler.parser;

import cn.edu.hitsz.compiler.NotImplementedException;
import cn.edu.hitsz.compiler.ir.IRImmediate;
import cn.edu.hitsz.compiler.ir.IRValue;
import cn.edu.hitsz.compiler.ir.IRVariable;
import cn.edu.hitsz.compiler.ir.Instruction;
import cn.edu.hitsz.compiler.lexer.Token;
import cn.edu.hitsz.compiler.parser.table.NonTerminal;
import cn.edu.hitsz.compiler.parser.table.Production;
import cn.edu.hitsz.compiler.parser.table.Status;
import cn.edu.hitsz.compiler.symtab.SourceCodeType;
import cn.edu.hitsz.compiler.symtab.SymbolTable;
import cn.edu.hitsz.compiler.utils.FileUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

// TODO: 实验三: 实现 IR 生成

/**
 *
 */
public class IRGenerator implements ActionObserver {

    private  SymbolTable symbolTable =new SymbolTable();
    private Stack<Status> status = new Stack<>();
   // private Stack<Token>   tokens =new Stack<>();
    //private Stack<NonTerminal> nonTerminals = new Stack<>();
    private List<Instruction> IR = new ArrayList<>();
    private Stack<Symbol> symbols = new Stack<>();

    @Override
    public void whenShift(Status currentStatus, Token currentToken) {
        // TODO
        //throw new NotImplementedException();
        status.push(currentStatus);//保存之前的规约信息的状态
        symbols.push(makeSymbol(currentToken));
    }

    @Override
    public void whenReduce(Status currentStatus, Production production) {
        // TODO
        switch (production.index()) {
            // 我们推荐在 case 后面使用注释标明产生式
            // 这样能比较清楚地看出产生式索引与产生式的对应关系
            case 2, 3, 4, 5 -> {// S_list -> S Semicolon S_list;
                for(int j =0 ;j<production.body().size();j++){//弹栈
                    symbols.pop();
                }
                symbols.push(new Symbol(null,production.head()));
                break;
            }
            // S_list -> S Semicolon;

            // S -> D id

            // D -> int
            /* ... */
            //System.out.println(symbols.peek().nonTerminal);

            case 6 ->{// S -> id = E;
                 IRValue form = symbols.peek().from;
                 symbols.pop();
                 symbols.pop();
                 //System.out.println(symbols.peek().val);
                 IR.add(Instruction.createMov(IRVariable.named(symbols.peek().token.getText()),form));
                 symbols.pop();
                symbols.push(new Symbol(null,production.head()));
                 break;
            }

            case 7 ->{//S -> return E;
                IR.add(Instruction.createRet(symbols.peek().from));
                for(int j =0 ;j<production.body().size();j++){//弹栈
                    symbols.pop();
                }
                symbols.push(new Symbol(null,production.head()));
                //System.out.println(symbols.peek().nonTerminal);
                break;
            }
            case 8 ->{//E -> E + A;
                IRValue from1 = symbols.peek().from;
                symbols.pop();
                symbols.pop();
                IRValue from2 = symbols.peek().from;
                symbols.pop();

                IRVariable from = IRVariable.temp();
                symbols.push(makeSymbol(production.head(),from));
                IR.add(Instruction.createAdd(from,from2,from1));
                break;

            }
            case 9 ->{//E -> E - A;
                IRValue from1 = symbols.peek().from;
                symbols.pop();
                symbols.pop();
                IRValue from2 = symbols.peek().from;
                symbols.pop();

                IRVariable from = IRVariable.temp();
                symbols.push(makeSymbol(production.head(),from));
                IR.add(Instruction.createSub(from,from2,from1));
                break;
            }
            case 10, 12 ->{ // E -> A
                IRValue from = symbols.peek().from;
                symbols.pop();
                symbols.push(makeSymbol(production.head(),from));
               // symbols.peek().setFrom(from);
                break;
            }
            case 11 ->{ // A -> A*B
                IRValue from1 = symbols.peek().from;
                symbols.pop();
                symbols.pop();
                IRValue from2 = symbols.peek().from;
                symbols.pop();

                IRVariable from = IRVariable.temp();
                symbols.push(makeSymbol(production.head(),from));
                IR.add(Instruction.createMul(from,from2,from1));
                break;
            }
            // A -> B
            case 13 ->{//B -> ( E );
                symbols.pop();
                IRValue from = symbols.peek().from;
                symbols.pop();
                symbols.pop();
                symbols.push(makeSymbol(production.head(),from));
                //symbols.peek().setFrom(from);
                break;

            }
            case 14 ->{//B -> id;
                String val = symbols.peek().token.getText();
                //System.out.println(val);
                symbols.pop();
                symbols.push(makeSymbol(production.head(),IRVariable.named(val)));
                //symbols.peek().setFrom(IRVariable.named(val));
                  break;
            }
            case 15 ->{// B -> IntConst

                int val = Integer.parseInt(symbols.peek().token.getText());
                //System.out.println(val);
                for(int j =0 ;j<production.body().size();j++){//弹栈
                    symbols.pop();
                }
                symbols.push(makeSymbol(production.head(),IRImmediate.of(val)));
                //symbols.peek().setFrom(IRImmediate.of(val));
                //System.out.println(symbols.peek().nonTerminal);
                break;
            }
            // ...
            default -> //
                    // 或者任何默认行为
                    throw new RuntimeException("Unknown production index");
        }
       // throw new NotImplementedException();
    }


    @Override
    public void whenAccept(Status currentStatus) {
        // TODO

        if(IR.isEmpty()){
            throw  new RuntimeException("没有产生中间表达式");
        }
        //throw new NotImplementedException();
    }

    @Override
    public void setSymbolTable(SymbolTable table) {
        // TODO
        if(table == null){
            throw  new RuntimeException("符号表为空！");
        }
        // symbolTable = table;
        //throw new NotImplementedException();
    }

    public List<Instruction> getIR() {
        // TODO
        return  IR;
        //throw new NotImplementedException();
    }

    public void dumpIR(String path) {
        FileUtils.writeLines(path, getIR().stream().map(Instruction::toString).toList());
    }



    public Symbol makeSymbol(Token token){
        return new Symbol(token, null);
    }

    public Symbol makeSymbol(NonTerminal nonTerminal,IRValue val){
        Symbol symbol = new Symbol(null, nonTerminal);
        symbol.setFrom(val);
        return symbol;
    }


    class Symbol{
        Token token;
        NonTerminal nonTerminal;
        private IRValue from;

        private Symbol(Token token, NonTerminal nonTerminal){
            this.token = token;
            this.nonTerminal = nonTerminal;
           // this.val = Integer.parseInt(null);
            this.from = null;
        }



        public boolean isToken(){
            return this.token != null;
        }

        public boolean isNonterminal(){
            return this.nonTerminal != null;
        }

        public void setFrom(IRValue from) {
            this.from = from;
        }
    }
}

