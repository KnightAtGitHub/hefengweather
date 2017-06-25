package com.knight.hefengweather.util;

import android.util.Log;

/**
 * Created by kexsh on 2017/3/17.
 */

public class L {
    public static String packageName = "com.knight.hefengweather";
    public static boolean isDebugMode = true;
    public static boolean isSingleLineLog = false;
    private static int logNum = 0;
    private static int maxTagLength = 0;

    private static final int VERBOSE = 2;
    private static final int DEBUG = 3;
    private static final int INFO = 4;
    private static final int WARN = 5;
    private static final int ERROR = 6;
    private static final int ASSERT = 7;




    private L(){
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }
    public static void v(String msg){
        logBuilder(VERBOSE,msg);
    }
    public static void v(String message, Object... args){
        String msg = (args == null || args.length == 0 ? message : String.format(message, args));
        logBuilder(VERBOSE,msg);
    }
    public static void d(String msg){
        logBuilder(DEBUG,msg);
    }
    public static void d(String message, Object... args){
        String msg = (args == null || args.length == 0 ? message : String.format(message, args));
        logBuilder(DEBUG,msg);
    }
    public static void i(String msg){
        logBuilder(INFO,msg);
    }
    public static void i(String message, Object... args){
        String msg = (args == null || args.length == 0 ? message : String.format(message, args));
        logBuilder(INFO,msg);
    }
    public static void w(String msg){
        logBuilder(WARN,msg);
    }
    public static void w(String message, Object... args){
        String msg = (args == null || args.length == 0 ? message : String.format(message, args));
        logBuilder(WARN,msg);
    }
    public static void e(String msg){
        logBuilder(ERROR,msg);
    }
    public static void e(String message, Object... args){
        String msg = (args == null || args.length == 0 ? message : String.format(message, args));
        logBuilder(ERROR,msg);
    }

    public static void logBuilder(int logLevel,String msg) {
        if (!isDebugMode) {
            return;
        }
        //分隔符
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0 ; i < maxTagLength ; i++ ){
            stringBuilder.append("-");
        }
        String separator = stringBuilder.toString();
        switch (logLevel){
            case VERBOSE:
                Log.v(separator ,separator);
                break;
            case DEBUG:
                Log.d(separator ,separator);
                break;
            case INFO:
                Log.i(separator ,separator);
                break;
            case WARN:
                Log.w(separator ,separator);
                break;
            case ERROR:
                Log.e(separator ,separator);
                break;
            case ASSERT:
                break;
                default:
                    break;
        }
        //日志输出

        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        for (int i = 0 ; i < trace.length-1; i++ ) {
            if (trace[i].getClassName().startsWith(packageName)){
                if ((trace[i].getClassName().endsWith("." + L.class.getSimpleName()))
                        && !(trace[i+1].getClassName().endsWith("." + L.class.getSimpleName())))
                {
                    i += 1;

                    String tag = tagBuilder(trace[i],i);
                    msg = msgBuilder(msg);
                    switch (logLevel){
                        case VERBOSE:
                            Log.v(tag,msg);break;
                        case DEBUG:
                            Log.d(tag,msg);break;
                        case INFO:
                            Log.i(tag,msg);break;
                        case WARN:
                            Log.w(tag,msg);break;
                        case ERROR:
                            Log.e(tag,msg);break;
                        case ASSERT:
                            default:break;
                    }
                    if (isSingleLineLog){
                        return;
                    }else {
                        continue;
                    }
                }else if((trace[i].getClassName().endsWith("." + L.class.getSimpleName()))
                        && (trace[i+1].getClassName().endsWith("." + L.class.getSimpleName())))
                {
                    continue;
                }
                else{
                    String tag = tagBuilder(trace[i],i);
                    switch (logLevel){
                        case VERBOSE:
                            Log.v(tag,logNum + "#");break;
                        case DEBUG:
                            Log.d(tag,logNum + "#");break;
                        case INFO:
                            Log.i(tag,logNum + "#");break;
                        case WARN:
                            Log.w(tag,logNum + "#");break;
                        case ERROR:
                            Log.e(tag,logNum + "#");break;
                        case ASSERT:
                        default:break;
                    }
                }
            }
        }


    }


    private static String tagBuilder(StackTraceElement stackTraceElement, int traceIndex){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                //获取文件名.行数
                .append("(")
                .append(stackTraceElement.getFileName())
                .append(":")
                .append(stackTraceElement.getLineNumber())
                .append(")");
        int tagHeadLength = stringBuilder.length();
                //获取 类名.方法名
        stringBuilder
                .append("(")
                .append(getSimpleClassName(stackTraceElement.getClassName()))
                .append(".")
                .append(stackTraceElement.getMethodName())
                .append(")");
        if( stringBuilder.length() > maxTagLength){
            maxTagLength = stringBuilder.length();
        }else {
            int length = maxTagLength - stringBuilder.length();
            for(int i = 0 ; i< length; i++){
                stringBuilder.append("-");
            //    stringBuilder.insert(tagHeadLength,"-");
            }
        }
  //      stringBuilder.append(traceIndex + "*");
        return stringBuilder.toString();
    }
    private static String msgBuilder(String msg){
        StringBuilder stringBuilder = new StringBuilder();
        logNum++;
        stringBuilder
                .append(logNum)
                .append("#")
                .append(":")
                .append(msg)
                .append(" ");

        return stringBuilder.toString();
    }
    private static String getSimpleClassName(String name) {
        int lastIndex = name.lastIndexOf(".");
        return name.substring(lastIndex + 1);
    }

}
