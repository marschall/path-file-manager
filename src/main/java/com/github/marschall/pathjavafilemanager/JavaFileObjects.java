package com.github.marschall.pathjavafilemanager;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.NestingKind;
import javax.tools.JavaFileObject.Kind;

final class JavaFileObjects {

  private JavaFileObjects() {
    throw new AssertionError("not instantiable");
  }
  
  static boolean isNameCompatible(String simpleName, Kind kind) {
    // TODO Auto-generated method stub
    return false;
  }

  static NestingKind getNestingKind() {
    // TODO check for $ in name and $123
    return null;
  }

  static Modifier getAccessLevel() {
    return null;
  }

}
