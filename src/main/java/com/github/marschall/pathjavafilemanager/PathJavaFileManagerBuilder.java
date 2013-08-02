package com.github.marschall.pathjavafilemanager;

import java.nio.file.Path;

import javax.tools.JavaFileManager;

/**
 * Builds a JSR-199 JavaFileManager that uses {@link java.nio.Path} instead
 * of {@link java.io.File}
 */
public final class PathJavaFileManagerBuilder {
  
  private Path source;
  private Path classOutput;
  private Path sourceOutput;
  private ClassLoader classPath;
  private ClassLoader annotationProcessorPath;
  
  public static PathJavaFileManagerBuilder onPath(Path folder) {
    return onPaths(folder, folder);
  }
  
  public static PathJavaFileManagerBuilder onPaths(Path input, Path output) {
    PathJavaFileManagerBuilder builder = new PathJavaFileManagerBuilder();
    builder.source = input;
    builder.classOutput = output;
    builder.sourceOutput = output;
    ClassLoader classLoader = new EmptyClassLoader();
    builder.classPath = classLoader;
    builder.annotationProcessorPath = classLoader;
    return builder;
  }
  
  JavaFileManager build() {
    return new PathJavaFileManager(this.source, this.classOutput, this.sourceOutput, this.classPath, this.annotationProcessorPath);
  }

}
