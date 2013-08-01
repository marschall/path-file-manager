package com.github.marschall.pathjavafilemanager;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
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
      
      JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
      try (JavaFileManager fileManager = new PathJavaFileManager(source, target)) {
        
      }
      
      
      
    }
  }

}
