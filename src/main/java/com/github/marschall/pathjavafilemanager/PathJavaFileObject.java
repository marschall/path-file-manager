package com.github.marschall.pathjavafilemanager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.NestingKind;
import javax.tools.JavaFileObject;

final class PathJavaFileObject implements JavaFileObject {
  
  final Path path;
  final Kind kind;
  
  PathJavaFileObject(Path path, Kind kind) {
    this.path = path;
    this.kind = kind;
  }

  @Override
  public URI toUri() {
    return this.path.toUri();
  }

  @Override
  public String getName() {
    return this.path.toString();
  }

  @Override
  public InputStream openInputStream() throws IOException {
    return Files.newInputStream(this.path);
  }

  @Override
  public OutputStream openOutputStream() throws IOException {
    return Files.newOutputStream(this.path);
  }

  @Override
  public Reader openReader(boolean ignoreEncodingErrors) throws IOException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Writer openWriter() throws IOException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public long getLastModified() {
    try {
      return Files.getLastModifiedTime(path).toMillis();
    } catch (IOException e) {
      // per contract
      return 0L;
    }
  }

  @Override
  public boolean delete() {
    try {
      Files.delete(path);
      return true;
    } catch (IOException e) {
      return false;
    }
  }

  @Override
  public Kind getKind() {
    return this.kind;
  }

  @Override
  public boolean isNameCompatible(String simpleName, Kind kind) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public NestingKind getNestingKind() {
    // TODO check for $ in name and $123
    return null;
  }

  @Override
  public Modifier getAccessLevel() {
    return null;
  }

}
