package com.github.marschall.pathjavafilemanager;

import static java.lang.Math.min;
import static java.nio.charset.CodingErrorAction.REPORT;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.NestingKind;
import javax.tools.JavaFileObject;

abstract class PathJavaFileObject implements JavaFileObject {
  // TODO read only
  // TODO write only
  
  final Path path;
  final Kind kind;
  final Charset fileEncoding;
  
  PathJavaFileObject(Path path, Kind kind, Charset fileEncoding) {
    this.path = path;
    this.kind = kind;
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
