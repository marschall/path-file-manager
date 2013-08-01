package com.github.marschall.pathjavafilemanager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

class OutputPathFileObject extends PathFileObject {

  OutputPathFileObject(Path path, Charset fileEncoding) {
    super(path, fileEncoding);
  }
  
  @Override
  public OutputStream openOutputStream() throws IOException {
    return Files.newOutputStream(this.path);
  }

  @Override
  public Writer openWriter() throws IOException {
    return new OutputStreamWriter(this.openOutputStream(), this.fileEncoding);
  }

  @Override
  public InputStream openInputStream() {
    throw new IllegalStateException("reading not supported");
  }

  @Override
  public Reader openReader(boolean ignoreEncodingErrors) {
    throw new IllegalStateException("reading not supported");
  }

  @Override
  public CharSequence getCharContent(boolean ignoreEncodingErrors) {
    throw new IllegalStateException("reading not supported");
  }

}
