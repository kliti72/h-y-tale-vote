package com.hytaletop.hytaletopvote.utils;


public class CodeUtils {
    
    public static final String genUniqueCode() {
        int rand = (int) (Math.random() * 2982);
        return new StringBuilder("server-").append(rand).toString();    
    }

}
