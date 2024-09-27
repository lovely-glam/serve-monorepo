package com.lovelyglam.utils.general;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.util.Strings;

import jakarta.annotation.Nonnull;

public class UrlUtils {
    
    public static Collection<String> convertUrlStringToList(@Nonnull String urls, @Nonnull String split){
        if (urls.isEmpty()) return Collections.emptyList();
        return Arrays.asList(urls.split(urls));
    }

    public static String convertUrlListToString(List<String> urls, @Nonnull String split) {
        if (urls == null) return Strings.EMPTY;
        var builder = new StringBuilder();
        for (int i = 0; i < urls.size(); i++) {
            builder.append(urls.get(i));
            if (i < urls.size() - 1) {
                builder.append(split);
            }
        }
        return builder.toString();
    }
}
