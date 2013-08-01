package com.github.marschall.pathjavafilemanager;

final class ClosedCecker {
  
  private volatile boolean closed;

  ClosedCecker() {
    this.closed = false;
  }
  
  void close() {
    this.closed = true;
  }
  
  void check() {
    if (this.closed) {
      throw new IllegalStateException("file manager is closed");
    }
  }
  
  

}
