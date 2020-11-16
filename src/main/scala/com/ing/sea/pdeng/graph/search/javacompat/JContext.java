package com.ing.sea.pdeng.graph.search.javacompat;

import com.ing.sea.pdeng.graph.Type;

import java.util.Set;

public class JContext {
    public final Type target;
    public final Set<Type> given;

    public JContext(Type target, Set<Type> given) {
        this.target = target;
        this.given = given;
    }
}
