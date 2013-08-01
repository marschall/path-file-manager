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

import org.junit.Test;

import com.github.marschall.memoryfilesystem.MemoryFileSystemBuilder;

import static java.nio.file.StandardCopyOption.COPY_ATTRIBUTES;

public class PathJavaFileManagerTest {

  @Test
  public void compileFiles() throws IOException {
    try (FileSystem fileSystem = MemoryFileSystemBuilder.newEmpty().build("pathjavafilemanager")) {
      Path src = fileSystem.getPath("src");
      Path target = fileSystem.getPath("target");
      
      Files.createDirectory(src);
      Files.createDirectory(target);
      copySourceFilesTo(target);
      
      JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
      try (JavaFileManager fileManager = new PathJavaFileManager(src, target)) {
        
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
