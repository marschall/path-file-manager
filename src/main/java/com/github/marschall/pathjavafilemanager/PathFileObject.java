package com.github.marschall.pathjavafilemanager;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.tools.FileObject;

abstract class PathFileObject implements FileObject {
  
  final Path path;
  final Charset fileEncoding;
  
  PathFileObject(Path path, Charset fileEncoding) {
    this.path = path;
    this.fileEncoding = fileEncoding;
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
  public String toString() {
    return this.getName();
  }

}
