package com.itsoku.lambda;

public class IllegalPropertyException extends RuntimeException {
    private final String methodName;
    private final String className;

    public IllegalPropertyException(String className, String methodName) {
        super("class:" + className + " method:" + methodName + " has not a valid java bean");
        this.className = className;
        this.methodName = methodName;
    }
}
