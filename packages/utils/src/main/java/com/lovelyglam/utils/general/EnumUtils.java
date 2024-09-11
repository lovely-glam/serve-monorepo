package com.lovelyglam.utils.general;

public class EnumUtils {
    public static <E extends Enum<E>> E convertStringToEnum (Class<E> enumType ,String value) {
        try {
            return Enum.valueOf(enumType, value.toUpperCase());
        }catch (Exception ex) {
            return null;
        }
    }
}
