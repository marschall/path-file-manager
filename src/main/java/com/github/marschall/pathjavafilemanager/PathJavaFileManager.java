package com.github.marschall.pathjavafilemanager;

import static java.nio.file.FileVisitResult.CONTINUE;
import static java.nio.file.FileVisitResult.SKIP_SUBTREE;
import static javax.tools.StandardLocation.ANNOTATION_PROCESSOR_PATH;
import static javax.tools.StandardLocation.CLASS_OUTPUT;
import static javax.tools.StandardLocation.CLASS_PATH;
import static javax.tools.StandardLocation.PLATFORM_CLASS_PATH;
import static javax.tools.StandardLocation.SOURCE_OUTPUT;
import static javax.tools.StandardLocation.SOURCE_PATH;

import static javax.tools.JavaFileObject.Kind.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.tools.FileObject;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;

/**
 * A JSR-199 JavaFileManager that uses {@link java.nio.Path} instead
 * of {@link java.io.File}
 */
public final class PathJavaFileManager implements JavaFileManager {
  
  private static final String FILE_ENCODING = "file.encoding";

  private final Path source;
  private final Path classOutput;

  private final ClosedCecker checker;

  private volatile Charset fileEncoding;

  public PathJavaFileManager(Path source, Path classOutput) {
    this.source = source;
    this.classOutput = classOutput;
    this.checker = new ClosedCecker();
    // not need to catch exception, system default charset is always supported
    this.fileEncoding = Charset.forName(System.getProperty(FILE_ENCODING));
  }

  @Override
  public ClassLoader getClassLoader(Location location) {
    this.checker.check();
    if (location == CLASS_OUTPUT) {
      
    } else if (location == SOURCE_OUTPUT) {
    } else if (location == CLASS_PATH) {
    } else if (location == SOURCE_PATH) {
      // can't load classes from source
      return null;
    } else if (location == PLATFORM_CLASS_PATH) {
      return ClassLoader.getSystemClassLoader();
    } else if (location == ANNOTATION_PROCESSOR_PATH) {
    }
    // unknown location
    return null;
  }

  @Override
  public Iterable<JavaFileObject> list(final Location location, String packageName, final Set<Kind> kinds, final boolean recurse) throws IOException {
    this.checker.check();
    final Path path = this.getPath(location);
    if (path == null) {
      return Collections.emptyList();
    }
    
    final List<JavaFileObject> files = new ArrayList<>();
    Files.walkFileTree(resolvePackage(path, packageName), new SimpleFileVisitor<Path>() {

      @Override
      public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        if (Files.isSameFile(path, dir)) {
          return CONTINUE;
        }
        return recurse ? CONTINUE : SKIP_SUBTREE;
      }

      @Override
      public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        Kind kind = getKind(path);
        if (kinds.contains(kind)) {
          JavaFileObject fileObject;
          if (location.isOutputLocation()) {
            fileObject = new OutputPathJavaFileObject(file, kind, fileEncoding);
          } else {
            fileObject = new InputPathJavaFileObject(file, kind, fileEncoding);
          }
          files.add(fileObject);
        }
        return CONTINUE;
      }

    });
    return files;
  }
  
  private static Kind getKind(Path path) {
    String fileName = path.getFileName().toString();
    for (Kind kind : Kind.values()) {
      String extension = kind.extension;
      if (!extension.isEmpty() && fileName.endsWith(extension)) {
        return kind;
      }
    }
    return OTHER;
  }
  
  private Path getPath(Location location) {
    if (location == CLASS_OUTPUT) {
      return this.classOutput;
    } else if (location == SOURCE_OUTPUT) {
    } else if (location == CLASS_PATH) {
    } else if (location == SOURCE_PATH) {
      // can't load classes from source
      return this.source;
    } else if (location == PLATFORM_CLASS_PATH) {
    } else if (location == ANNOTATION_PROCESSOR_PATH) {
    }
    return null;
  }
  
  private Path resolvePackage(Path base, String packageName) {
    Path path = base;
    // TODO optimize
    for (String element : packageName.split("\\.")) {
      path = path.resolve(element);
    }
    return path;
  }

  @Override
  public String inferBinaryName(Location location, JavaFileObject file) {
    this.checker.check();
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean isSameFile(FileObject a, FileObject b) {
    if (!(a instanceof PathJavaFileObject)) {
      throw new IllegalArgumentException("unsupported file object: " + a + " must have been created with this file manager");
    }
    if (!(b instanceof PathJavaFileObject)) {
      throw new IllegalArgumentException("unsupported file object: " + a + " must have been created with this file manager");
    }
    PathJavaFileObject first = (PathJavaFileObject) a;
    PathJavaFileObject second = (PathJavaFileObject) b;
    try {
      return Files.isSameFile(first.path, second.path);
    } catch (IOException e) {
      // REVIEW unsure
      throw new RuntimeException("could not compare " + a + " with " + b, e);
    }
  }

  @Override
  public boolean handleOption(String current, Iterator<String> remaining) {
    if (current.equals(FILE_ENCODING)) {
      if (remaining.hasNext()) {
        try {
          this.fileEncoding = Charset.forName(remaining.next());
        } catch (IllegalArgumentException e) {
          throw new IllegalArgumentException("unsuported value for " + FILE_ENCODING + " supported");
        }
        if (remaining.hasNext()) {
          throw new IllegalArgumentException("only one value for " + FILE_ENCODING + " supported");
        }
      } else {
        throw new IllegalArgumentException("option value for " + FILE_ENCODING + " missing");
      }
      return false;
    } else {
      return false;
    }
  }
  
  @Override
  public int isSupportedOption(String option) {
    if (option.equals(FILE_ENCODING)) {
      return 1;
    }
    return -1;
  }

  @Override
  public boolean hasLocation(Location location) {
    if (location == CLASS_OUTPUT) {
      return true;
    } else if (location == SOURCE_OUTPUT) {
    } else if (location == CLASS_PATH) {
    } else if (location == SOURCE_PATH) {
      return true;
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
    this.checker.check();
    if (location.isOutputLocation()) {
      throw new IllegalArgumentException(location + " is an output location");
    }
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public JavaFileObject getJavaFileForOutput(Location location, String className, Kind kind, FileObject sibling) throws IOException {
    this.checker.check();
    if (!location.isOutputLocation()) {
      throw new IllegalArgumentException(location + " is an not output location");
    }
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public FileObject getFileForInput(Location location, String packageName, String relativeName) throws IOException {
    this.checker.check();
    if (location.isOutputLocation()) {
      throw new IllegalArgumentException(location + " is an output location");
    }
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public FileObject getFileForOutput(Location location, String packageName, String relativeName, FileObject sibling) throws IOException {
    this.checker.check();
    if (!location.isOutputLocation()) {
      throw new IllegalArgumentException(location + " is an not output location");
    }
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void flush() throws IOException {
    // nothing
  }

  @Override
  public void close() throws IOException {
    this.checker.close();
  }

}
