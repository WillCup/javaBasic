java在1.5引入java.lang.instrument，你可以由此实现一个java agent,通过此agent来修改类的字节码即改变一个类。本文中，会通过java instrument 实现一个简单的profiler。当然instrument并不限于profiler，instrument可以做很多事情，

它类似一种更低级，更松耦合的AOP，可以从底层来改变一个类的行为，你可以由此产生无限的遐想。

接下来要做的事情，就是计算一个方法所花的时间，通常我们会在代码这么写： 

在方法开始开头加入long stime = System.nanoTime();

在方法结尾通过System.nanoTime()-stime得出方法所花时间，你不得不在你想监控的每个方法中写入重复的代码，

好一点的情况，你可以用AOP来干这事，但总是感觉有点别扭，这种profiler的代码还是打包在你的项目中，

java instrument使得这更干净。

1） 写agent类

package org.toy;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.ClassFileTransformer;
public class PerfMonAgent {
    static private Instrumentation inst = null;
    /**
     * This method is called before the application’s main-method is called,
     * when this agent is specified to the Java VM.
     **/
    public static void premain(String agentArgs, Instrumentation _inst) {
        System.out.println("PerfMonAgent.premain() was called.");
        // Initialize the static variables we use to track information.
        inst = _inst;
        // Set up the class-file transformer.
        ClassFileTransformer trans = new PerfMonXformer();
        System.out.println("Adding a PerfMonXformer instance to the JVM.");
        inst.addTransformer(trans);
    }
}
2）写ClassFileTransformer类

package org.toy;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
public class PerfMonXformer implements ClassFileTransformer {
    public byte[] transform(ClassLoader loader, String className,
            Class<?> classBeingRedefined, ProtectionDomain protectionDomain,
            byte[] classfileBuffer) throws IllegalClassFormatException {
        byte[] transformed = null;
        System.out.println("Transforming " + className);
        ClassPool pool = ClassPool.getDefault();
        CtClass cl = null;
        try {
            cl = pool.makeClass(new java.io.ByteArrayInputStream(
                    classfileBuffer));
            if (cl.isInterface() == false) {
                CtBehavior[] methods = cl.getDeclaredBehaviors();
                for (int i = 0; i < methods.length; i++) {
                    if (methods[i].isEmpty() == false) {
                        doMethod(methods[i]);
                    }
                }
                transformed = cl.toBytecode();
            }
        } catch (Exception e) {
            System.err.println("Could not instrument  " + className
                    + ",  exception : " + e.getMessage());
        } finally {
            if (cl != null) {
                cl.detach();
            }
        }
        return transformed;
    }
    private void doMethod(CtBehavior method) throws NotFoundException,
            CannotCompileException {
        // method.insertBefore("long stime = System.nanoTime();");
        // method.insertAfter("System.out.println(\"leave "+method.getName()+" and time:\"+(System.nanoTime()-stime));");
        method.instrument(new ExprEditor() {
            public void edit(MethodCall m) throws CannotCompileException {
                m
                        .replace("{ long stime = System.nanoTime(); $_ = $proceed($$); System.out.println(\""
                                + m.getClassName()+"."+m.getMethodName()
                                + ":\"+(System.nanoTime()-stime));}");
            }
        });
    }
}
上面两个类就是agent的核心了，jvm启动时并会在应用加载前会调用 PerfMonAgent.premain, 然后PerfMonAgent.premain中实例化了一个定制的ClassFileTransforme即 PerfMonXformer

并通过inst.addTransformer(trans);把PerfMonXformer的实例加入Instrumentation实例（由jvm传入），这就使得应用中的类加载的时候， PerfMonXformer.transform都会被调用，你在此方法中

可以改变加载的类，真的有点神奇，为了改变类的字节码，我使用了jboss的javassist,虽然你不一定要这么用，但jboss的javassist真的很强大，让你很容易的改变类的字节码。在上面的方法中

我通过改变类的字节码，在每个类的方法入口中加入了long stime = System.nanoTime();，在方法的出口加入了System.out.println("methodClassName.methodName:"+(System.nanoTime()-stime));

 

3） 打包agent

对于agent的打包，有点讲究，

3.1）

jar的META-INF/MANIFEST.MF加入Premain-Class: xx, xx在此语境中就是我们的agent类，即org.toy.PerfMonAgent

3.2）

如果你的agent类引入别的包，需使用Boot-Class-Path: xx,xx在此语境中就是上面提到的jboss javassit 即 /home/pwlazy/.m2/repository/javassist/javassist/3.8.0 .GA/javassist-3.8.0.GA.jar

 

下面附上maven 的pom

<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <groupId>org.toy</groupId>
  <artifactId>toy-inst</artifactId>
  <packaging>jar</packaging>
  <version>1.0-SNAPSHOT</version>
  <name>toy-inst</name>
  <url>http://maven.apache.org</url>
  <dependencies>
     <dependency>
      <groupId>javassist</groupId>
      <artifactId>javassist</artifactId>
      <version>3.8.0.GA</version>
    </dependency>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>3.8.1</version>
      <scope>test</scope>
    </dependency>
  </dependencies>
 
   <build>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-jar-plugin</artifactId>
        <version>2.2</version>
        <configuration>
          <archive>
            <manifestEntries>
              <Premain-Class>org.toy.PerfMonAgent</Premain-Class>
              <Boot-Class-Path>/home/pwlazy/.m2/repository/javassist/javassist/3.8.0.GA/javassist-3.8.0.GA.jar</Boot-Class-Path>
          
            </manifestEntries>
          </archive>