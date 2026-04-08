package com.robinloom.jweaver.dictionary.java.nio;

import com.robinloom.jweaver.dictionary.TypeWeaver;
import com.robinloom.jweaver.dictionary.WeavingContext;
import com.robinloom.loom.Loom;
import org.jspecify.annotations.Nullable;

import java.nio.file.Path;

public class PathWeaver implements TypeWeaver {

    @Override
    public Class<?> targetType() {
        return java.nio.file.Path.class;
    }

    @Override
    public String weave(@Nullable Object object, WeavingContext context) {
        if (object == null) {
            return "";
        }

        Path path = (Path) object;
        return Loom.with(Path.class.getSimpleName())
                   .lbracket()
                   .append(path.toAbsolutePath())
                   .rbracket()
                   .toString();
    }
}
