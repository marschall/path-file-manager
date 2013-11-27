PathJavaFileManager [![Build Status](https://travis-ci.org/marschall/path-file-manager.png?branch=master)](https://travis-ci.org/marschall/path-file-manager)
===================
A JSR-199 JavaFileManager that uses `java.nio.Path` instead of `java.io.File`.

The encoding used will be the value of the "file.encoding" option.


Maven
-----

```xml
<dependency>
  <groupId>com.github.marschall</groupId>
  <artifactId>path-file-manager</artifactId>
  <version>0.1.0</version>
</dependency>
```

JSR-199
-------
Requires a JDK or JDT
org.eclipse.jdt.compiler.tool
https://bugs.eclipse.org/bugs/show_bug.cgi?id=154111

Supported
---------
* reporting encoding errors

Not Supported
-------------
* Option emulation
* compiling files larger than 2 GB
