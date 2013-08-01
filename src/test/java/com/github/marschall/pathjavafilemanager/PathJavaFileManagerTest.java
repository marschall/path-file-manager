package com.github.marschall.pathjavafilemanager;

import static javax.tools.JavaFileObject.Kind.SOURCE;
import static javax.tools.StandardLocation.SOURCE_PATH;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import javax.tools.DiagnosticListener;
import javax.tools.FileObject;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;

import org.junit.Test;

import com.github.marschall.memoryfilesystem.MemoryFileSystemBuilder;

public class PathJavaFileManagerTest {

  @Test
  public void compileFiles() throws IOException {
    try (FileSystem fileSystem = MemoryFileSystemBuilder.newEmpty().build("pathjavafilemanager")) {
      Path source = fileSystem.getPath("src");
      Path target = fileSystem.getPath("target");
      
      Files.createDirectory(source);
      Files.createDirectory(target);
      
      // TODO copy files to source
      
      JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
      try (JavaFileManager fileManager = new PathJavaFileManager(source, target)) {
        
        FileObject sibling = null;
        JavaFileObject helloWorldInvoker = fileManager.getJavaFileForOutput(SOURCE_PATH, "com.github.marschall.pathjavafilemanager.HelloWorldInvoker", SOURCE, sibling);
        JavaFileObject helloWorld = fileManager.getJavaFileForOutput(SOURCE_PATH, "com.github.marschall.pathjavafilemanager.HelloWorld", SOURCE, sibling);
        
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

}
