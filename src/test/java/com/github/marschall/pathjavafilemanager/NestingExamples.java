package com.github.marschall.pathjavafilemanager;

import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static javax.lang.model.element.NestingKind.LOCAL;
import static javax.lang.model.element.NestingKind.MEMBER;
import static javax.lang.model.element.NestingKind.TOP_LEVEL;
import static javax.lang.model.element.NestingKind.ANONYMOUS;

import java.lang.annotation.Retention;

import javax.lang.model.element.NestingKind;


@Nesting(TOP_LEVEL)
public class NestingExamples {
  
  @Nesting(MEMBER)
  static class MemberClass1{}

  @Nesting(MEMBER)
  class MemberClass2{}

  public static void main(String[] argv) {
    @Nesting(LOCAL)
    class LocalClass{};
    
    @Nesting(ANONYMOUS)
    Runnable anonymous = new Runnable() {

      @Override
      public void run() {
      }
      
    };

    Class<?>[] classes = {
        NestingExamples.class,
        MemberClass1.class,
        MemberClass2.class,
        LocalClass.class,
        anonymous.getClass()
    };

    for(Class<?> clazz : classes) {
      Nesting nesting = clazz.getAnnotation(Nesting.class);
      NestingKind nestingKind;
      if (nesting == null) {
        nestingKind = ANONYMOUS;
      } else {
        nestingKind = nesting.value();
      }
      System.out.format("%s is %s%n", clazz.getName(), nestingKind);
    }
  }
}

@Retention(RUNTIME)
@interface Nesting {
  NestingKind value();
}
