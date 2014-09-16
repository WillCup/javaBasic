package com.will.tooljars.javassist.v1;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;

/**
 * 参考：http://www.iteye.com/topic/53740。
 * 想统计一下A.method()的执行时间
 * 
 * javassist和 asm都是动态创建、修改字节码的类库。我感觉主要区别是： 
        asm的性能要好一些。
        javassist的主要优势是学习成本低，可以根据java源代码生成字节码，而不必直接和字节码打交道。
 * 
 * @author Will
 * @created at 2014-8-28 下午1:35:05
 */
public class Main {
    public static void main(String[] args) throws Exception {
        // 用于取得字节码类，必须在当前的classpath中，使用全称
        CtClass ctClass = ClassPool.getDefault().get("com.will.tooljars.javassist.v1.A");
        // 需要修改的方法名称
        String mname = "method";
        CtMethod mold = ctClass.getDeclaredMethod(mname);
        // 修改原有的方法名称
        String nname = mname + "$impl";
        mold.setName(nname);
        // 创建新的方法，复制原来的方法
        CtMethod mnew = CtNewMethod.copy(mold, mname, ctClass, null);
        // 主要的注入代码
        StringBuffer body = new StringBuffer();
        body.append("{\nlong start = System.currentTimeMillis();\n");
        // 调用原有代码，类似于method();($$)表示所有的参数
        body.append(nname + "($$);\n");
        body.append("System.out.println(\"Call to method " + mname
                + " took \" +\n (System.currentTimeMillis()-start) + "
                + "\" ms.\");\n");
        body.append("System.out.println(\"This coms from will test.\");");

        body.append("}");
        // 替换新方法
        mnew.setBody(body.toString());
        // 增加新方法
        ctClass.addMethod(mnew);
        // 类已经更改，注意不能使用A a=new A();，因为在同一个classloader中，不允许装载同一个类两次
        A a = (A) ctClass.toClass().newInstance();
        a.method();
    }
}
