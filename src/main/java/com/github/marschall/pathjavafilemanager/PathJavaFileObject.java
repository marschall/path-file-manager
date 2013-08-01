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

final class PathJavaFileObject implements JavaFileObject {
  
  final Path path;
  final Kind kind;
  private final Charset fileEncoding;
  
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
  public InputStream openInputStream() throws IOException {
    return Files.newInputStream(this.path);
  }

  @Override
  public OutputStream openOutputStream() throws IOException {
    return Files.newOutputStream(this.path);
  }

  @Override
  public Reader openReader(boolean ignoreEncodingErrors) throws IOException {
    if (ignoreEncodingErrors) {
      return new InputStreamReader(this.openInputStream(), this.fileEncoding);
    } else {
      CharsetDecoder decoder = this.fileEncoding.newDecoder()
          .onMalformedInput(REPORT)
          .onUnmappableCharacter(REPORT);
      return new InputStreamReader(this.openInputStream(), decoder);
    }
  }

  @Override
  public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
    // REVIEW will fail for files larger than 2GB
    long size = (int) Files.size(this.path);
    if (size > Integer.MAX_VALUE) {
      throw new IllegalStateException("file " + this.getName() + " is larger than: " + Integer.MAX_VALUE);
    }
    int fileSize = (int) size;
    if (fileSize == 0) {
      // REVIEW can this ever be negative?
      return "";
    }
    
    try (Reader reader = this.openReader(ignoreEncodingErrors)) {
      char[] buffer = new char[min(fileSize, 8192)]; // avoid allocating buffer that is larger than the file
      StringBuilder stringBuilder = new StringBuilder(fileSize);
      int read;
      while ((read = reader.read(buffer)) != -1) {
        stringBuilder.append(buffer, 0, read);
      }
      return stringBuilder.toString();
    }
  }

  @Override
  public Writer openWriter() throws IOException {
    return new OutputStreamWriter(this.openOutputStream(), this.fileEncoding);
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
