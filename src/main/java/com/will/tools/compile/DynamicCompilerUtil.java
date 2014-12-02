package com.will.tools.compile;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import javax.tools.JavaCompiler.CompilationTask;

import org.apache.commons.lang.StringUtils;

/**
 * @author zhengtian
 * 
 * @date 2012-4-17 下午07:24:24
 */
@SuppressWarnings("all")
public class DynamicCompilerUtil {

	/**
	 * 编译java文件
	 * 
	 * @param filePath
	 *            文件或者目录（若为目录，自动递归编译）
	 * @param sourceDir
	 *            java源文件存放目录
	 * @param targetDir
	 *            编译后class类文件存放目录
	 * @param diagnostics
	 *            存放编译过程中的错误信息
	 * @return
	 * @throws Exception
	 */
	public static boolean compiler(String filePath, String sourceDir, String targetDir, DiagnosticCollector<JavaFileObject> diagnostics)
			throws Exception {
		// 获取编译器实例
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		// 获取标准文件管理器实例
		// 该文件管理器实例的作用就是将我们需要动态编译的java源文件转换为getTask需要的编译单元。
		StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
		try {
			if (StringUtils.isEmpty(filePath) && StringUtils.isEmpty(sourceDir) && StringUtils.isEmpty(targetDir)) {
				return false;
			}
			// 得到filePath目录下的所有java源文件
			File sourceFile = new File(filePath);
			List<File> sourceFileList = new ArrayList<File>();
			getSourceFiles(sourceFile, sourceFileList);
			// 没有java文件，直接返回
			if (sourceFileList.size() == 0) {
				System.out.println(filePath + "目录下查找不到任何java文件");
				return false;
			}
			// 获取要编译的编译单元
			Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles(sourceFileList);
			/**
			 * 编译选项，在编译java文件时，编译程序会自动的去寻找java文件引用的其他的java源文件或者class。 -sourcepath选项就是定义java源文件的查找目录， -classpath选项就是定义class文件的查找目录。
			 */
			Iterable<String> options = Arrays.asList("-d", targetDir, "-sourcepath", sourceDir);
			// 获取一个编译任务对象
			CompilationTask compilationTask = compiler.getTask(null, fileManager, diagnostics, options, null, compilationUnits);
			// 运行编译任务
			return compilationTask.call();
		} finally {
			fileManager.close();
		}
	}

	/**
	 * 查找该目录下的所有的java文件
	 * 
	 * @param sourceFile
	 * @param sourceFileList
	 * @throws Exception
	 */
	private static void getSourceFiles(File sourceFile, List<File> sourceFileList) throws Exception {
		if (sourceFile.exists() && sourceFileList != null) {// 文件或者目录必须存在
			if (sourceFile.isDirectory()) {// 若file对象为目录
				// 得到该目录下以.java结尾的文件或者目录
				File[] childrenFiles = sourceFile.listFiles(new FileFilter() {
					public boolean accept(File pathname) {
						if (pathname.isDirectory()) {
							return true;
						} else {
							String name = pathname.getName();
							return name.endsWith(".java") ? true : false;
						}
					}
				});
				// 递归调用
				for (File childFile : childrenFiles) {
					getSourceFiles(childFile, sourceFileList);
				}
			} else {// 若file对象为文件
				sourceFileList.add(sourceFile);
			}
		}
	}

	public static void main(String[] args) {
		try {
			// 编译F:\\亚信工作\\SDL文件\\sdl\\src目录下的所有java文件
			String filePath = "F:\\亚信工作\\SDL文件\\sdl\\src";
			String sourceDir = "F:\\亚信工作\\SDL文件\\sdl\\src";
			String targetDir = "F:\\亚信工作\\SDL文件\\sdl\\classes";
			DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
			boolean compilerResult = compiler(filePath, sourceDir, targetDir, diagnostics);
			if (compilerResult) {
				System.out.println("编译成功");
			} else {
				System.out.println("编译失败");
				for (Diagnostic diagnostic : diagnostics.getDiagnostics()) {
					// System.out.format("%s[line %d column %d]-->%s%n", diagnostic.getKind(), diagnostic.getLineNumber(),
					// diagnostic.getColumnNumber(),
					// diagnostic.getMessage(null));
					System.out.println(diagnostic.getMessage(null));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

