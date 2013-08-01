PathJavaFileManager
===================
A JSR-199 JavaFileManager that uses `java.nio.Path` instead of `java.io.File`.

JSR-199
-------
Requires a JDK or JDT
org.eclipse.jdt.compiler.tool
https://bugs.eclipse.org/bugs/show_bug.cgi?id=154111

Supported
---------

Not Supported
-------------
* Option emulation
* `javax.tools.JavaFileObject.getNestingKind()`