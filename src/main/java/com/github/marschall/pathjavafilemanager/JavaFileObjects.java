package com.github.marschall.pathjavafilemanager;

import java.nio.file.Path;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.NestingKind;
import javax.tools.JavaFileObject.Kind;
import static javax.tools.JavaFileObject.Kind.CLASS;
import static javax.lang.model.element.NestingKind.LOCAL;
import static javax.lang.model.element.NestingKind.MEMBER;
import static javax.lang.model.element.NestingKind.TOP_LEVEL;
import static javax.lang.model.element.NestingKind.ANONYMOUS;

final class JavaFileObjects {

  private JavaFileObjects() {
    throw new AssertionError("not instantiable");
  }
  
  static boolean isNameCompatible(Path path, Kind fileKind, String simpleName, Kind kind) {
    if (fileKind != kind) {
      return false;
    }
    String fileName = path.getFileName().toString();
    return fileName.equals(simpleName + kind.extension);
  }

  static NestingKind getNestingKind(Path path, Kind kind) {
    if (kind != CLASS) {
      return null;
    }
    String fileName = path.getFileName().toString();
    int dollarIndex = fileName.lastIndexOf('$');
    if (dollarIndex == -1) {
      return TOP_LEVEL;
    }
    int dotIndex = fileName.indexOf('.', dollarIndex + 1);
    for (int i = dollarIndex + 1; i < dotIndex; ++i) {
      char c = fileName.charAt(i);
      if (c < '0' || c > '9') {
        // not a number
        if (i == dollarIndex + 1) {
          // never had a number after the $
          return MEMBER;
        } else {
          return LOCAL;
        }
      }
    }
    return ANONYMOUS;
  }

  static Modifier getAccessLevel(Path path) {
    return null;
  }

}
