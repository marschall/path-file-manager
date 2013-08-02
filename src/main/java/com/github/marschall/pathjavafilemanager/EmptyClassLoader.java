package com.github.marschall.pathjavafilemanager;

final class EmptyClassLoader extends ClassLoader {

  EmptyClassLoader() {
    super(getSystemClassLoader());
  }

}
