package com.github.marschall.pathjavafilemanager;

import java.nio.charset.Charset;
import java.nio.file.Path;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.NestingKind;
import javax.tools.JavaFileObject;

final class InputPathJavaFileObject extends InputPathFileObject implements JavaFileObject {

  private final Kind kind;

  InputPathJavaFileObject(Path path, Charset fileEncoding, Kind kind) {
    super(path, fileEncoding);
    this.kind = kind;
  }

  @Override
  public Kind getKind() {
    return this.kind;
  }

  @Override
  public boolean isNameCompatible(String simpleName, Kind kind) {
    return JavaFileObjects.isNameCompatible(this.path, simpleName, kind);
  }

  @Override
  public NestingKind getNestingKind() {
    return JavaFileObjects.getNestingKind(this.path, this.kind);
  }

  @Override
  public Modifier getAccessLevel() {
    return JavaFileObjects.getAccessLevel(this.path);
  }

}
