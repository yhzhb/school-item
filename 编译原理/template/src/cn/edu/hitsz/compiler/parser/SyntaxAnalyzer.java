package cn.edu.hitsz.compiler.parser;

import cn.edu.hitsz.compiler.NotImplementedException;
import cn.edu.hitsz.compiler.lexer.Token;
import cn.edu.hitsz.compiler.parser.table.*;
import cn.edu.hitsz.compiler.symtab.SymbolTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

//TODO: 实验二: 实现 LR 语法分析驱动程序

/**
 * LR 语法分析驱动程序
 * <br>
 * 该程序接受词法单元串与 LR 分析表 (action 和 goto 表), 按表对词法单元流进行分析, 执行对应动作, 并在执行动作时通知各注册的观察者.
 * <br>
 * 你应当按照被挖空的方法的文档实现对应方法, 你可以随意为该类添加你需要的私有成员对象, 但不应该再为此类添加公有接口, 也不应该改动未被挖空的方法,
 * 除非你已经同助教充分沟通, 并能证明你的修改的合理性, 且令助教确定可能被改动的评测方法. 随意修改该类的其它部分有可能导致自动评测出错而被扣分.
 */
public class SyntaxAnalyzer {
    private final SymbolTable symbolTable;
    private final List<ActionObserver> observers = new ArrayList<>();
    private ArrayList<Token> tokens =new ArrayList<>();
    private LRTable lrTable;

    private final Stack<Status> status = new Stack<>();//状态栈
    private final Stack<Symbol> symbols = new Stack<>();//符号栈


    public SyntaxAnalyzer(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }

    /**
     * 注册新的观察者
     *
     * @param observer 观察者
     */
    public void registerObserver(ActionObserver observer) {
        observers.add(observer);
        observer.setSymbolTable(symbolTable);
    }

    /**
     * 在执行 shift 动作时通知各个观察者
     *
     * @param currentStatus 当前状态
     * @param currentToken  当前词法单元
     */
    public void callWhenInShift(Status currentStatus, Token currentToken) {
        for (final var listener : observers) {
            listener.whenShift(currentStatus, currentToken);
        }
    }

    /**
     * 在执行 reduce 动作时通知各个观察者
     *
     * @param currentStatus 当前状态
     * @param production    待规约的产生式
     */
    public void callWhenInReduce(Status currentStatus, Production production) {
        for (final var listener : observers) {
            listener.whenReduce(currentStatus, production);
        }
    }

    /**
     * 在执行 accept 动作时通知各个观察者
     *
     * @param currentStatus 当前状态
     */
    public void callWhenInAccept(Status currentStatus) {
        for (final var listener : observers) {
            listener.whenAccept(currentStatus);
        }
    }

    public void loadTokens(Iterable<Token> tokens) {
        // TODO: 加载词法单元
        // 你可以自行选择要如何存储词法单元, 譬如使用迭代器, 或是栈, 或是干脆使用一个 list 全存起来
        // 需要注意的是, 在实现驱动程序的过程中, 你会需要面对只读取一个 token 而不能消耗它的情况,
        // 在自行设计的时候请加以考虑此种情况
        this.tokens = (ArrayList<Token>) tokens;
       // throw new NotImplementedException();
    }

    public void loadLRTable(LRTable table) {
        // TODO: 加载 LR 分析表
        // 你可以自行选择要如何使用该表格:
        // 是直接对 LRTable 调用 getAction/getGoto, 抑或是直接将 initStatus 存起来使用

        lrTable = table;
       // throw new NotImplementedException();
    }

    public void run() {
        // TODO: 实现驱动程序
        // 你需要根据上面的输入来实现 LR 语法分析的驱动程序
        // 请分别在遇到 Shift, Reduce, Accept 的时候调用上面的 callWhenInShift, callWhenInReduce, callWhenInAccept
        // 否则用于为实验二打分的产生式输出可能不会正常工作
        status.push(lrTable.getInit());//初始状态入栈
        //System.out.println(lrTable.getInit());
        Action cur_action;
        int end_flag = 0;
        for(int i=0;end_flag==0;){
            //System.out.println(tokens.get(i));
            cur_action=lrTable.getAction(status.peek(),tokens.get(i));//获得当前的动作
            //status.push(cur_action.getStatus());
            switch (cur_action.getKind()) {
                case Shift -> {//移进,状态放入栈中，字符移入栈中
                    final var shiftTo = cur_action.getStatus();
                    callWhenInShift(shiftTo,tokens.get(i));
                    status.push(cur_action.getStatus());//状态入栈，

                    symbols.push(makeSymbol(tokens.get(i)));//字符入栈
                    //System.out.println(makeSymbol(tokens.get(i)));


                    //System.out.println(tokens.size());

                    i++;//读下一个字符
                    // ...
                    break;
                }

                case Reduce -> {//规约，获得规约式，弹出字符栈和状态栈中的元素，之后压入非终结符，用goto转移状态
                    final var production = cur_action.getProduction();
                    callWhenInReduce(status.peek(),production);
                    // ...


                    for(int j =0 ;j<production.body().size();j++){//弹栈
                        symbols.pop();
                        status.pop();
                    }

                    symbols.push(makeSymbol(production.head()));//规约后的非终结符入栈

                    if(symbols.peek().isNonterminal()) {
                        status.push(lrTable.getGoto(status.peek(), symbols.peek().nonTerminal));
                    }else {
                        throw  new RuntimeException("字符出错");
                    }



                    break;
                }

                case Accept -> {//接受
                    // ...

                    callWhenInAccept(status.peek());
                    //System.out.println(symbols.peek().nonTerminal);
                    end_flag=1;
                    break;
                }

                case Error -> {//出错
                    // ...

                   throw new RuntimeException("转移状态动作出错");
                }
            }

        }
       // throw new NotImplementedException();
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

        private Symbol(Token token, NonTerminal nonTerminal){
            this.token = token;
            this.nonTerminal = nonTerminal;
        }



        public boolean isToken(){
            return this.token != null;
        }

        public boolean isNonterminal(){
            return this.nonTerminal != null;
        }
    }



}

