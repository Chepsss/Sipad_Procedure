package it.almaviva.difesa.cessazione.procedure.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.Collection;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonUtilsMethod {

    public static boolean isNotBlankString(String string) {
        return string != null && !string.trim().isEmpty();
    }

    public static <T> boolean isNotCollectionEmpty(Collection<T> collection) {
        return !CollectionUtils.isEmpty(collection);
    }

    public static String getClassName(Class<?> clazz) {
        String genericClassTypeName = clazz.getTypeName();
        String[] directoriesAndClass = genericClassTypeName.split("\\.");
        return directoriesAndClass[directoriesAndClass.length - 1].toLowerCase();
    }

}