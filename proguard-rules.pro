-adaptclassstrings
-adaptresourcefilecontents

-repackageclasses ''
-allowaccessmodification

-obfuscationdictionary dictionary.txt

-renamesourcefileattribute SourceFile
-keepattributes Exceptions, InnerClasses, Signature

-keep public class com.hytaletop.hytaletopvote.Vote {
    public *;
}

-keep class com.hytale.** { *; }