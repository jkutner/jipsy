import me.qmx.jitescript.CodeBlock;
import me.qmx.jitescript.JiteClass;
import org.objectweb.asm.tree.LabelNode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Stack;

import static me.qmx.jitescript.util.CodegenUtils.*;

public class Compiler {

    public JiteClass compile(final String[] tokens) {
        return new JiteClass("Main") {{
            defineMethod("main", ACC_PUBLIC | ACC_STATIC, sig(void.class, String[].class), new JipsyCodeBlock() {{
                Stack<LabelNode[]> loopStack = new Stack<LabelNode[]>();
                iconst_0();
                for (String arg : tokens) {
                    if ("p".equals(arg)) {
                        jPrintInt();
                    } else if ("+".equals(arg)) {
                        jInc();
                    } else if ("-".equals(arg)) {
                        jDec();
                    } else if (">".equals(arg)) {
                        iconst_0();
                    } else if ("<".equals(arg)) {
                        pop();
                    } else if ("s".equals(arg)) {
                        swap();
                    } else if ("[".equals(arg)) {
                        LabelNode[] labelNodes = jBeingLoop();
                        loopStack.push(labelNodes);
                    } else if ("]".equals(arg)) {
                        LabelNode[] labelNodes = loopStack.pop();
                        jEndLoop(labelNodes);
                    }
                }
                ldc("");
                aprintln();
                voidreturn();
            }});
        }};
    }

    public static void main(String[] args) throws Exception {
        JiteClass jiteClass = new Compiler().compile(args);
        FileOutputStream output = new FileOutputStream(new File("Main.class"));
        output.write(jiteClass.toBytes());
    }

    public static class JipsyCodeBlock extends CodeBlock {
        public void jPrintInt() {
            dup();
            invokestatic(p(String.class), "valueOf", sig(String.class, int.class));
            getstatic(p(System.class), "out", ci(PrintStream.class));
            swap();
            invokevirtual(p(PrintStream.class), "print", sig(void.class, Object.class));
        }

        public void jInc() {
            iconst_1();
            iadd();
        }

        public void jDec() {
            iconst_1();
            isub();
        }

        public LabelNode[] jBeingLoop() {
            LabelNode begin = new LabelNode();
            LabelNode end = new LabelNode();
            label(begin);
            dup();
            ifeq(end);
            return new LabelNode[] {begin, end};
        }

        public void jEndLoop(LabelNode[] beginAndEnd) {
            LabelNode begin = beginAndEnd[0];
            LabelNode end = beginAndEnd[1];
            go_to(begin);
            label(end);
        }
    }
}
