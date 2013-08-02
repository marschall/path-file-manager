package com.github.marschall.pathjavafilemanager;

import static java.nio.file.StandardCopyOption.COPY_ATTRIBUTES;
import static javax.tools.JavaFileObject.Kind.SOURCE;
import static javax.tools.JavaFileObject.Kind.CLASS;
import static javax.tools.StandardLocation.SOURCE_PATH;
import static javax.tools.StandardLocation.CLASS_OUTPUT;
import static javax.lang.model.element.NestingKind.LOCAL;
import static javax.lang.model.element.NestingKind.MEMBER;
import static javax.lang.model.element.NestingKind.TOP_LEVEL;
import static javax.lang.model.element.NestingKind.ANONYMOUS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import javax.tools.DiagnosticListener;
import javax.tools.FileObject;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;

import org.junit.Ignore;
import org.junit.Test;

import com.github.marschall.memoryfilesystem.MemoryFileSystemBuilder;

public class PathJavaFileManagerTest {

  @Test
  public void compileFiles() throws IOException {
    try (FileSystem fileSystem = MemoryFileSystemBuilder.newEmpty().build("pathjavafilemanager")) {
      Path src = fileSystem.getPath("src");
      Path target = fileSystem.getPath("target");
      
      Files.createDirectory(src);
      Files.createDirectory(target);
      copySourceFilesTo(src);
      
      JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
      try (JavaFileManager fileManager = PathJavaFileManagerBuilder.onPaths(src, target).build()) {
        
        JavaFileObject helloWorldInvoker = fileManager.getJavaFileForInput(SOURCE_PATH, "com.github.marschall.pathjavafilemanager.HelloWorldInvoker", SOURCE);
        JavaFileObject helloWorld = fileManager.getJavaFileForInput(SOURCE_PATH, "com.github.marschall.pathjavafilemanager.HelloWorld", SOURCE);
        
        Writer out = new StringWriter();
        DiagnosticListener<? super JavaFileObject> diagnosticListener = null; // use the compiler's default method for reporting diagnostics
        Iterable<String> options = null; // no options
        Iterable<String> classes = null; // means no class names
        List<JavaFileObject> compilationUnits = Arrays.asList(helloWorldInvoker, helloWorld);
        
        CompilationTask task = compiler.getTask(out, fileManager, diagnosticListener, options, classes, compilationUnits);
        if (!task.call()) {
          fail(out.toString());
        }
      }
    }
  }
  
  @Test
  public void getNestingKind() throws IOException {
    try (FileSystem fileSystem = MemoryFileSystemBuilder.newEmpty().build("pathjavafilemanager")) {
      Path src = fileSystem.getPath("src");
      Path target = fileSystem.getPath("target");
      
      try (JavaFileManager fileManager = PathJavaFileManagerBuilder.onPaths(src, target).build()) {
        JavaFileObject javaFileObject = fileManager.getJavaFileForOutput(CLASS_OUTPUT, "com.github.marschall.pathjavafilemanager.NestingExamples", CLASS, null);
        assertEquals(TOP_LEVEL, javaFileObject.getNestingKind());
        
        javaFileObject = fileManager.getJavaFileForOutput(CLASS_OUTPUT, "com.github.marschall.pathjavafilemanager.NestingExamples$MemberClass1", CLASS, null);
        assertEquals(MEMBER, javaFileObject.getNestingKind());
        
        javaFileObject = fileManager.getJavaFileForOutput(CLASS_OUTPUT, "com.github.marschall.pathjavafilemanager.NestingExamples$1LocalClass", CLASS, null);
        assertEquals(LOCAL, javaFileObject.getNestingKind());
        
        javaFileObject = fileManager.getJavaFileForOutput(CLASS_OUTPUT, "com.github.marschall.pathjavafilemanager.NestingExamples$1", CLASS, null);
        assertEquals(ANONYMOUS, javaFileObject.getNestingKind());
      }
    }
  }
  
  @Test
  @Ignore
  public void getFileForInput() throws IOException {
    JavaFileManager fileManager = null;
    FileObject fileForInput = fileManager.getFileForInput(SOURCE_PATH, "com.sun.tools.javac", "resources/compiler.properties");
  }
  
  private void copySourceFilesTo(Path target) throws IOException {
    copySourceFileTo(Paths.get("com/github/marschall/pathjavafilemanager/HelloWorld.java"), target);
    copySourceFileTo(Paths.get("com/github/marschall/pathjavafilemanager/HelloWorldInvoker.java"), target);
  }
  
  private void copySourceFileTo(Path sourceFile, Path target) throws IOException {
    Path sourceFolder = Paths.get("src/test/java");
    Path targetFile = target.resolve(sourceFile.toString()); // different provider so we need #toString
    Files.createDirectories(targetFile.getParent());
    Files.copy(sourceFolder.resolve(sourceFile), targetFile, COPY_ATTRIBUTES);
  }

}
