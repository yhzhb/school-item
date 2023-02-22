package cn.edu.hitsz.compiler.asm;

import cn.edu.hitsz.compiler.NotImplementedException;
import cn.edu.hitsz.compiler.ir.IRImmediate;
import cn.edu.hitsz.compiler.ir.IRValue;
import cn.edu.hitsz.compiler.ir.IRVariable;
import cn.edu.hitsz.compiler.ir.Instruction;
import cn.edu.hitsz.compiler.lexer.Token;
import cn.edu.hitsz.compiler.parser.table.NonTerminal;
import cn.edu.hitsz.compiler.parser.table.Production;
import cn.edu.hitsz.compiler.symtab.Reg;
import cn.edu.hitsz.compiler.utils.FileUtils;

import java.util.*;


/**
 * TODO: 实验四: 实现汇编生成
 * TIPS:由于做了预处理，实际上deal各个操作语句的函数可以有所简化，预处理和存活变量处理其实可以在实验三中进行.
 * <br>
 * 在编译器的整体框架中, 代码生成可以称作后端, 而前面的所有工作都可称为前端.
 * <br>
 * 在前端完成的所有工作中, 都是与目标平台无关的, 而后端的工作为将前端生成的目标平台无关信息
 * 根据目标平台生成汇编代码. 前后端的分离有利于实现编译器面向不同平台生成汇编代码. 由于前后
 * 端分离的原因, 有可能前端生成的中间代码并不符合目标平台的汇编代码特点. 具体到本项目你可以
 * 尝试加入一个方法将中间代码调整为更接近 risc-v 汇编的形式, 这样会有利于汇编代码的生成.
 * <br>
 * 为保证实现上的自由, 框架中并未对后端提供基建, 在具体实现时可自行设计相关数据结构.
 *
 * @see AssemblyGenerator#run() 代码生成与寄存器分配
 */
public class AssemblyGenerator {
    private  int MAX = 512;

    private enum op {li,mv,add,addi,mul,sub,sw,lw};
    private List<Instruction> instructions = new ArrayList<>();
    private BMap<IRValue,Reg> bMap = new BMap<>();
    private List<Symbol> symbols = new ArrayList<>();
    private List<Active> actives = new Stack<>();
    private List<String> ASM = new ArrayList<>();
    int    count =0 ;
    //private List<Reg> stack_reg = new ArrayList<>();
    /**
     * 加载前端提供的中间代码
     * <br>
     * 视具体实现而定, 在加载中或加载后会生成一些在代码生成中会用到的信息. 如变量的引用
     * 信息. 这些信息可以通过简单的映射维护, 或者自行增加记录信息的数据结构.
     *
     * @param originInstructions 前端提供的中间代码
     */
    public void loadIR(List<Instruction> originInstructions) {
        // TODO: 读入前端提供的中间代码并生成所需要的信息
        //instructions = originInstructions;
        //throw new NotImplementedException();

        Instruction instruction;
        for(int i = 0;i< originInstructions.size();i++) {
            instruction = originInstructions.get(i);
            // System.out.println(cur_instruction.getLHS());
            switch (instruction.getKind()) {
                case MOV, RET -> {
                    instructions.add(instruction);
                    break;
                }
                case SUB -> {
                    //System.out.println(cur_instruction.getRHS()+"((");
                    if(instruction.getLHS().isImmediate() && instruction.getRHS().isImmediate()){


                        instructions.add(Instruction.createMov(IRVariable.temp(),
                                IRImmediate.of(Integer.parseInt(instruction.getLHS().toString())-Integer.parseInt(instruction.getRHS().toString()))));
                    }else {
                        IRValue op2 = deal_with(instruction.getLHS());
                        IRValue op3 = deal_with(instruction.getRHS());
                        instructions.add(Instruction.createSub(instruction.getResult(), op2, op3));
                    }

                    break;
                }
                case ADD -> {
                    if (instruction.getLHS().isImmediate()){
                        instructions.add(Instruction.createAdd(instruction.getResult(),instruction.getRHS(),instruction.getLHS()));
                    }else {
                        instructions.add(instruction);
                    }
                    break;
                }

                case MUL -> {
                    if(instruction.getLHS().isImmediate() && instruction.getRHS().isImmediate()){
                        instructions.add(Instruction.createMov(IRVariable.temp(),
                                IRImmediate.of(Integer.parseInt(instruction.getLHS().toString())-Integer.parseInt(instruction.getRHS().toString()))));
                    }else {
                        IRValue op2 = deal_with(instruction.getLHS());
                        IRValue op3 = deal_with(instruction.getRHS());
                        instructions.add(Instruction.createMul(instruction.getResult(), op2, op3));
                    }
                    break;
                }
                default -> {
                    throw new RuntimeException("No Such Kind!");
                }


            }
        }

        //System.out.println(instructions.size());
        //instructions = originInstructions;
        makeList(instructions);
        //System.out.println(actives.size());

    }


    /**
     * 执行代码生成.
     * <br>
     * 根据理论课的做法, 在代码生成时同时完成寄存器分配的工作. 若你觉得这样的做法不好,
     * 也可以将寄存器分配和代码生成分开进行.
     * <br>
     * 提示: 寄存器分配中需要的信息较多, 关于全局的与代码生成过程无关的信息建议在代码生
     * 成前完成建立, 与代码生成的过程相关的信息可自行设计数据结构进行记录并动态维护.
     */
    public void run() {
        // TODO: 执行寄存器分配与代码生成
        //System.out.println(instructions.size());
        Instruction cur_instruction;
        ASM.add(".text");
        for (int i = 0;i< instructions.size();i++){
             cur_instruction=instructions.get(i);
            // System.out.println(cur_instruction.getLHS());
             switch (cur_instruction.getKind()){
                 case MOV -> {
                    // System.out.println(cur_instruction.getFrom().isImmediate());
                     //System.out.println(cur_instruction.getResult());
                     dealMOV(cur_instruction);
                     break;
                 }
                 case SUB -> {
                     //System.out.println(cur_instruction.getRHS()+"((");
                     dealSUB(cur_instruction);
                     break;
                 }
                 case ADD -> {
                     dealADD(cur_instruction);
                     break;
                 }

                 case MUL -> {
                     dealMUL(cur_instruction);
                     break;
                 }
                 case RET -> {
                     dealRET(cur_instruction);
                     break;
                 }
                 default -> {
                     throw new RuntimeException("No Such Kind!");
                 }
             }
             release(cur_instruction);
        }
        System.out.println("指令长度："+ASM.size());
       // System.out.println(ASM);
        //System.out.println(count);

               // ASM.add(1,"    %s %s, %s, %s\t\t%s ".formatted(op.addi,Reg.sp,
                    //    Reg.sp,0 - count*4,"#"));从zero开始不需要该语句


       // throw new NotImplementedException();
    }


    /**
     * 输出汇编代码到文件
     *
     * @param path 输出文件路径
     */
    public void dump(String path) {
        // TODO: 输出汇编代码到文件
       FileUtils.writeLines(path, ASM);
        //throw new NotImplementedException();
    }


    private  void dealMOV (Instruction instruction){
        if(instruction.getFrom().isImmediate()){//语法构造为li
            //System.out.println(bMap.containsKey(instruction.getResult()));
            if(!bMap.containsKey(instruction.getResult())) {//新的变量
                bMap.replace(instruction.getResult(), empty_reg(Reg.zero,Reg.sp));
                //System.out.println(bMap.getByKey(instruction.getResult()));
                //System.out.println(bMap.getByValue(bMap.getByKey(instruction.getResult())));
            }
            System.out.println("    %s %s, %s\t\t%s  %s".formatted(op.li,bMap.getByKey(instruction.getResult()),instruction.getFrom(),"#",instruction.toString()));
            ASM.add("    %s %s, %s\t\t%s  %s".formatted(op.li,bMap.getByKey(instruction.getResult()),instruction.getFrom(),"#",instruction.toString()));

        }else if(instruction.getFrom().isIRVariable()){//asm语法构造为mv
          //System.out.println(bMap.getByKey(instruction.getFrom()));
            if(!bMap.containsKey(instruction.getResult())){
                bMap.replace(instruction.getResult(),empty_reg(Reg.zero,Reg.sp));
            }
            IRValue op_from = lw_back(instruction.getFrom(),bMap.getByKey(instruction.getResult()),Reg.zero);
            System.out.println("    %s %s, %s\t\t%s  %s".formatted(op.mv,bMap.getByKey(instruction.getResult()),bMap.getByKey(instruction.getFrom()),"#",instruction.toString()));
            ASM.add("    %s %s, %s\t\t%s  %s".formatted(op.mv,bMap.getByKey(instruction.getResult()),bMap.getByKey(op_from),"#",instruction.toString()));
        }

    }


    private void dealSUB (Instruction instruction){

        IRValue op1 = lw_back(instruction.getResult(),Reg.sp,Reg.zero);
        IRValue op2 = lw_back(instruction.getLHS(),bMap.getByKey(op1),Reg.zero);
        IRValue op3 = lw_back(instruction.getRHS(),bMap.getByKey(op2),bMap.getByKey(op1));

        System.out.println("    %s %s, %s, %s\t\t%s  %s".formatted(op.sub,bMap.getByKey(op1),
                bMap.getByKey(op2),bMap.getByKey(op3),"#",
                Instruction.createSub(instruction.getResult(),op2,op3)));

        ASM.add("    %s %s, %s, %s\t\t%s  %s".formatted(op.sub,bMap.getByKey(op1),
                bMap.getByKey(op2),bMap.getByKey(op3),"#",
                Instruction.createSub(instruction.getResult(),op2,op3)));


    }




    private void dealADD (Instruction instruction){
        IRValue op1 =lw_back(instruction.getResult(),Reg.sp,Reg.zero);
        IRValue op2;
       if(instruction.getRHS().isImmediate()){
           op2 =lw_back(instruction.getLHS(),bMap.getByKey(op1),Reg.zero);
           System.out.println("    %s %s, %s, %s\t\t%s  %s".formatted(op.addi,bMap.getByKey(op1),
                   bMap.getByKey(op2),instruction.getRHS(),"#",instruction.toString()));
           ASM.add("    %s %s, %s, %s\t\t%s  %s".formatted(op.addi,bMap.getByKey(op1),
                   bMap.getByKey(op2),instruction.getRHS(),"#",instruction.toString()));
       }else if (instruction.getLHS().isImmediate()){
           dealADD(Instruction.createAdd(instruction.getResult(),instruction.getRHS(),instruction.getLHS()));
       }
       else{
           op2 =lw_back(instruction.getLHS(),bMap.getByKey(op1),Reg.zero);
           IRValue op3 = lw_back(instruction.getRHS(),bMap.getByKey(op2),bMap.getByKey(op1));
           System.out.println("    %s %s, %s, %s\t\t%s  %s".formatted(op.add,bMap.getByKey(op1),
                   bMap.getByKey(instruction.getLHS()),bMap.getByKey(instruction.getRHS()),"#",instruction.toString()));
           ASM.add("    %s %s, %s, %s\t\t%s  %s".formatted(op.add,bMap.getByKey(op1),
                   bMap.getByKey(op2),bMap.getByKey(op3),"#",instruction.toString()));
        }
    }


    private void dealMUL(Instruction instruction){
        IRValue op1 = lw_back(instruction.getResult(),Reg.sp,Reg.zero);
        IRValue op2 = lw_back(instruction.getLHS(),bMap.getByKey(op1),Reg.zero);
        IRValue op3 = lw_back(instruction.getRHS(),bMap.getByKey(op2),bMap.getByKey(op1));

        System.out.println("    %s %s, %s, %s\t\t%s  %s".formatted(op.mul,bMap.getByKey(op1),bMap.getByKey(op2),
                bMap.getByKey(op3),"#",Instruction.createMul(instruction.getResult(),op2,op3)));

        ASM.add("    %s %s, %s, %s\t\t%s  %s".formatted(op.mul,bMap.getByKey(op1),bMap.getByKey(op2),
                bMap.getByKey(op3),"#",Instruction.createMul(instruction.getResult(),op2,op3)));

    }


    private void dealRET(Instruction instruction){
        if(bMap.containsValue(Reg.a0)){
            IRValue irValue = bMap.getByValue(Reg.a0);
            if(irValue.equals(instruction.getReturnValue())){
                return;
            }else {
                bMap.removeByValue(Reg.a0);
            }
        }
        System.out.println("    %s %s, %s\t\t%s  %s".formatted(op.mv,Reg.a0,bMap.getByKey(instruction.getReturnValue()),"#",instruction.toString()));
        ASM.add("    %s %s, %s\t\t%s  %s".formatted(op.mv,Reg.a0,bMap.getByKey(instruction.getReturnValue()),"#",instruction.toString()));
    }


    private IRValue deal_with(IRValue irValue){
        if (irValue.isImmediate()){
            IRVariable value = IRVariable.temp();
            //bMap.replace(value, empty_reg());
            //System.out.println("    %s %s, %s\t\t%s  %s".formatted(op.li,bMap.getByKey(value),irValue,"#",Instruction.createMov(value,irValue).toString()));
            instructions.add(Instruction.createMov(value,irValue));
            return value;
        }
        return irValue;
    }


    //lw取回实现
    private IRValue lw_back(IRValue irValue,Reg reg,Reg reg2){
        if(!bMap.containsKey(irValue) && irValue.isIRVariable()){
            if(have(irValue)!=null) {
                Reg need =empty_reg(reg,reg2);
                System.out.println("    %s %s, %s(%s)\t\t%s 2000 ".formatted(op.lw, need, have(irValue).num,have(irValue).reg, "#"));
                ASM.add("    %s %s, %s(%s)\t\t%s  ".formatted(op.lw, need, have(irValue).num,have(irValue).reg, "#"));

                bMap.replace(irValue,need);
                //down(arr,have(irValue).reg);
                //symbols.remove(have(irValue));

            }else {
                Reg reg_ = empty_reg(Reg.zero,Reg.sp);
                //System.out.println(reg);
                bMap.replace(irValue,reg_);
            }
        }else if(irValue.isImmediate()){
            IRVariable value = IRVariable.temp();
            bMap.replace(value, empty_reg(reg,reg2));//立即数li导入时不能与前面目的寄存器和另一个操作数寄存器重复
            System.out.println("    %s %s, %s\t\t%s  %s".formatted(op.li,bMap.getByKey(value),irValue,"#",Instruction.createMov(value,irValue).toString()));
            ASM.add("    %s %s, %s\t\t%s  %s".formatted(op.li,bMap.getByKey(value),irValue,"#",Instruction.createMov(value,irValue).toString()));
            return value;
        }
        return irValue;
    }



    //判断是否在内存中
    private Symbol have(IRValue irValue){
        if (irValue.isIRVariable()){
            for (int i=0;i<symbols.size();i++){
                if(symbols.get(i).irValue.equals(irValue)){
                    return symbols.get(i);
                }
            }
        }
        return null;
    }

    //判断变量是否存活
    private Active have_(IRValue irValue){
        if (irValue.isIRVariable()){
            for (int i=0;i<actives.size();i++){
                if(actives.get(i).irValue.equals(irValue)){
                    return actives.get(i);
                }
            }
        }
        return null;
    }

    //分配寄存器
    private  Reg empty_reg(Reg reg1,Reg reg2){

        for(Reg myReg : Reg.values()){
            if(myReg.ordinal() > 4 && myReg.ordinal() < 12){
               if(!bMap.containsValue(myReg)){
                   return myReg;
               }
            }
        }
        return dealwithreg(reg1,reg2);
    }




    private Reg dealwithreg(Reg reg1,Reg reg2){
        Random r = new Random();
        while (true) {
            for (Reg myReg : Reg.values()) {
                if ((myReg.ordinal() == (r.nextInt(30) + 1)) ) {
                    if (bMap.containsValue(myReg) && !myReg.equals(reg1) && !myReg.equals(reg2) ) {
                        sw_in(myReg);
                        bMap.removeByValue(myReg);
                        return myReg;
                    }
                }
            }
        }
    }


    private void sw_in(Reg reg){
        Symbol position = null;
        for(int j=0;j<=count;j++){
              if(Have_unuse_index(j)){
                  position = new Symbol(bMap.getByValue(reg),Reg.zero,j*4);
                  break;
            }
        }
        //System.out.println(j);
        if(position == null) {
            count = count+1;
            position = free_stack(bMap.getByValue(reg));
        }
        symbols.add(position);
        System.out.println("    %s %s, %s(%s)\t\t%s  ".formatted(op.sw,reg,position.num,position.reg,"#"));
        ASM.add("    %s %s, %s(%s)\t\t%s  ".formatted(op.sw,reg,position.num,position.reg,"#"));
    }
    
    private boolean Have_unuse_index(int index){
        for(int i = 0;i<symbols.size();i++ ){
            if(symbols.get(i).num == (index * 4))
                return false;
        }
        return true;
        
    }


    private Symbol free_stack(IRValue irValue){
        int position = count*4 ;
        return new Symbol(irValue, Reg.zero, position);
    }


    /**
     * 创建活跃元素列表
     * @param instructions
     */
    private void makeList(List<Instruction> instructions){
        Instruction instruction;
        for(int i = 0;i< instructions.size();i++) {
            instruction = instructions.get(i);
            // System.out.println(cur_instruction.getLHS());
            switch (instruction.getKind()) {
                case MOV -> {
                    add_(instruction.getResult());
                    add_(instruction.getFrom());
                    break;
                }
                case SUB, MUL, ADD -> {
                    add_(instruction.getResult());
                    add_(instruction.getRHS());
                    add_(instruction.getLHS());

                    break;
                }

                case RET ->  {
                    add_(instruction.getReturnValue());
                }
                default -> {
                    throw new RuntimeException("No Such Kind!");
                }


            }
        }

    }

    private void  add_(IRValue irValue){
        if(irValue.isIRVariable()) {
            for (int i = 0; i < actives.size(); i++) {
                if (actives.get(i).irValue.equals(irValue)) {
                    actives.get(i).up();
                    return;
                }
            }
            actives.add(new Active(irValue));
        }

    }


    private void release(IRValue irValue){
        if(irValue.isIRVariable()) {
            for (int i = 0; i < actives.size(); i++) {
                if (actives.get(i).irValue.equals(irValue)) {
                    if(actives.get(i).num > 0) {
                        actives.get(i).down();
                        return;
                    }else {
                        if(bMap.containsKey(irValue)) {
                            bMap.removeByKey(irValue);
                        }
                        symbols.remove(have(irValue));
                        actives.remove(have_(irValue));
                    }

                }
            }

        }
    }

    private void release(Instruction instruction){
        switch (instruction.getKind()) {
            case MOV -> {
                release(instruction.getResult());
                release(instruction.getFrom());
                break;
            }
            case SUB, MUL, ADD -> {
                release(instruction.getResult());
                release(instruction.getRHS());
                release(instruction.getLHS());

                break;
            }

            case RET ->  {
                release(instruction.getReturnValue());
            }
            default -> {
                throw new RuntimeException("No Such Kind!");
            }


        }
    }

    public class BMap<K, V> {
        private final Map<K, V> KVmap = new HashMap<>();
        private final Map<V, K> VKmap = new HashMap<>();

        public void removeByKey(K key) {
            VKmap.remove(KVmap.remove(key));
        }

        public void removeByValue(V value) {
            KVmap.remove(VKmap.remove(value));

        }

        public boolean containsKey(K key) {
            return KVmap.containsKey(key);
        }

        public boolean containsValue(V value) {
            return VKmap.containsKey(value);
        }

        public void replace(K key, V value) {
            // 对于双射关系, 将会删除交叉项
            removeByKey(key);
            removeByValue(value);
            KVmap.put(key, value);
            VKmap.put(value, key);
        }

        public V getByKey(K key) {
            return KVmap.get(key);
        }

        public K getByValue(V value) {
            return VKmap.get(value);
        }
    }

    class Symbol{
        IRValue irValue;
        Reg reg;
        int num;

        private Symbol(IRValue irValue,Reg reg,int num){
            this.irValue = irValue;
            this.reg = reg;
            this.num = num;
        }

    }

    class Active {
        IRValue irValue;
        int num;

        private Active(IRValue irValue){
            this.irValue = irValue;
            this.num = 0;
        }

        public void up(){
            num++;
        }

        public void down(){
            num--;
        }

    }
}

