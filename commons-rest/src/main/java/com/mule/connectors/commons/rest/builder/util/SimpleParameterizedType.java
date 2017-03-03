package com.mule.connectors.commons.rest.builder.util;

import com.google.common.base.Optional;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;

/**
 * Simple {@link ParameterizedType} implementation.
 */
public class SimpleParameterizedType implements ParameterizedType {

    private final Type rawType;
    private final Type[] actualTypeArguments;

    public SimpleParameterizedType(Type rawType, Type... actualTypeArguments) {
        this.rawType = Optional.fromNullable(rawType).or(Object.class);
        this.actualTypeArguments = actualTypeArguments;
    }

    @Override
    public Type[] getActualTypeArguments() {
        return actualTypeArguments;
    }

    @Override
    public Type getRawType() {
        return rawType;
    }

    @Override
    public Type getOwnerType() {
        return rawType;
    }

    @Override
    public boolean equals(Object other) {
        return (this == other) ||
                (other instanceof SimpleParameterizedType &&
                        getRawType().equals(SimpleParameterizedType.class.cast(other).getRawType()) &&
                        Arrays.equals(getActualTypeArguments(), SimpleParameterizedType.class.cast(other).getActualTypeArguments()));
    }

    @Override
    public int hashCode() {
        return 31 * getRawType().hashCode() + Arrays.hashCode(getActualTypeArguments());
    }
}
