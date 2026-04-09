package com.robinloom.jweaver.dictionary.java.nio;

import com.robinloom.jweaver.TypeWeaver;
import com.robinloom.jweaver.WeavingContext;
import com.robinloom.loom.Loom;
import org.jspecify.annotations.NonNull;

import java.nio.file.Path;

public class PathWeaver extends TypeWeaver {

    @Override
    public Class<?> targetType() {
        return java.nio.file.Path.class;
    }

    @Override
    public String weave(@NonNull Object object, WeavingContext ctx) {
        Path path = (Path) object;
        return Loom.with(Path.class.getSimpleName())
                   .lbracket()
                   .append(path.toAbsolutePath())
                   .rbracket()
                   .toString();
    }
}
