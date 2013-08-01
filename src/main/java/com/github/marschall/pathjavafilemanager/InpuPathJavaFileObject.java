package com.github.marschall.pathjavafilemanager;

import static java.lang.Math.min;
import static java.nio.charset.CodingErrorAction.REPORT;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.file.Files;
import java.nio.file.Path;

class InpuPathJavaFileObject extends PathJavaFileObject {

  InpuPathJavaFileObject(Path path, Kind kind, Charset fileEncoding) {
    super(path, kind, fileEncoding);
  }
  
  @Override
  public InputStream openInputStream() throws IOException {
    return Files.newInputStream(this.path);
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
  public OutputStream openOutputStream() {
    throw new IllegalStateException("writing not supported");
  }

  @Override
  public Writer openWriter() {
    throw new IllegalStateException("writing not supported");
  }

}
