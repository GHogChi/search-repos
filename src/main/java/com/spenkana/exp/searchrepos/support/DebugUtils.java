package com.spenkana.exp.searchrepos.support;

import java.text.MessageFormat;

public class DebugUtils {

    public static void dump(String message){
        System.out.println(message);
    }

    public static void dump(String format, Object... args){
        String msg = MessageFormat.format(format, args);
        dump(msg);
    }

    public static String formatStackTrace(Exception e){
        e.printStackTrace();
        StackTraceElement[] elements = e.getStackTrace();
        StringBuilder sb = new StringBuilder();
        for(StackTraceElement element: elements){
            sb.append(element.getClassName())
                .append(".")
                .append(element.getMethodName()).append(" Line:")
                .append(element.getLineNumber())
                .append("\n");
        }
        return sb.toString();
    }
}
