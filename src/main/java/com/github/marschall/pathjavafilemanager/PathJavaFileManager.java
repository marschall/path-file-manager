package com.github.marschall.pathjavafilemanager;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Set;

import javax.tools.FileObject;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.StandardLocation;
import static javax.tools.StandardLocation.*;

/**
 * A JSR-199 JavaFileManager that uses {@link java.nio.Path} instead
 * of {@link java.io.File}
 */
public final class PathJavaFileManager implements JavaFileManager {

  private final Path source;
  private final Path classOutput;

  public PathJavaFileManager(Path source, Path classOutput) {
    this.source = source;
    this.classOutput = classOutput;
  }

  @Override
  public ClassLoader getClassLoader(Location location) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Iterable<JavaFileObject> list(Location location, String packageName,
      Set<Kind> kinds, boolean recurse) throws IOException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String inferBinaryName(Location location, JavaFileObject file) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean isSameFile(FileObject a, FileObject b) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean handleOption(String current, Iterator<String> remaining) {
    // REVIEW missing option support
    return false;
  }
  
  @Override
  public int isSupportedOption(String option) {
    // REVIEW missing option support
    return -1;
  }

  @Override
  public boolean hasLocation(Location location) {
    if (location == CLASS_OUTPUT) {
      
    } else if (location == SOURCE_OUTPUT) {
    } else if (location == CLASS_PATH) {
    } else if (location == SOURCE_PATH) {
    } else if (location == PLATFORM_CLASS_PATH) {
    } else if (location == ANNOTATION_PROCESSOR_PATH) {
    }
      
      // TODO java 8
//    } else if (location == javax.tools.DocumentationTool.Location.DOCUMENTATION_OUTPUT) {
//    } else if (location == javax.tools.DocumentationTool.Location.DOCLET_PATH) {
//    } else if (location == javax.tools.DocumentationTool.Location.TAGLET_PATH) {
    // TODO java 8
//    } else if (location == StandardLocation.NATIVE_HEADER_OUTPUT) {

    // unknown option
    return false;
  }

  @Override
  public JavaFileObject getJavaFileForInput(Location location, String className, Kind kind) throws IOException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public JavaFileObject getJavaFileForOutput(Location location, String className, Kind kind, FileObject sibling) throws IOException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public FileObject getFileForInput(Location location, String packageName, String relativeName) throws IOException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public FileObject getFileForOutput(Location location, String packageName, String relativeName, FileObject sibling) throws IOException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void flush() throws IOException {
    // TODO Auto-generated method stub

  }

  @Override
  public void close() throws IOException {
    // TODO Auto-generated method stub
  }

}
