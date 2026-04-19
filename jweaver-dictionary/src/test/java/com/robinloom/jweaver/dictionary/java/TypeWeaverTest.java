package com.robinloom.jweaver.dictionary.java;

import com.robinloom.jweaver.*;
import com.robinloom.jweaver.dictionary.Dictionary;

public class TypeWeaverTest {

    protected WeavingContext ctx
            = new WeavingContext(Mode.INLINE, Dictionary.getInstance(), _ -> (object, _) -> object.toString(), true);
}
