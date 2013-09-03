package com.github.marschall.pathjavafilemanager;

import java.nio.file.Path;
import java.util.Locale;
import java.util.Locale.Category;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.StandardJavaFileManager;

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
  private StandardJavaFileManager delegate;
  
  public static PathJavaFileManagerBuilder onPath(Path folder, JavaCompiler compiler) {
    return onPaths(folder, folder, compiler);
  }
  
  public static PathJavaFileManagerBuilder onPaths(Path input, Path output, JavaCompiler compiler) {
    PathJavaFileManagerBuilder builder = new PathJavaFileManagerBuilder();
    builder.source = input;
    builder.classOutput = output;
    builder.sourceOutput = output;
    ClassLoader classLoader = new EmptyClassLoader();
    builder.classPath = classLoader;
    builder.annotationProcessorPath = classLoader;
    builder.forCompiler(compiler);
    return builder;
  }
  
  public PathJavaFileManagerBuilder forCompiler(JavaCompiler compiler) {
    return this.delegateTo(compiler.getStandardFileManager(null, null, null));
  }
  
  public PathJavaFileManagerBuilder delegateTo(StandardJavaFileManager delegate) {
    this.delegate = delegate;
    return this;
  }
  
  JavaFileManager build() {
    return new PathJavaFileManager(this.source, this.classOutput, this.sourceOutput, this.classPath, this.annotationProcessorPath, this.delegate);
  }

}
