package com.mulesoft.extensions.request.builder.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Optional;

import static java.util.Arrays.copyOf;

/**
 * Simple {@link ParameterizedType} implementation.
 */
public class SimpleParameterizedType implements ParameterizedType {

    private final Type rawType;
    private final Type[] actualTypeArguments;

    public SimpleParameterizedType(Class<?> rawType, Type... actualTypeArguments) {
        this.rawType = Optional.<Class<?>>ofNullable(rawType).orElse(Object.class);
        this.actualTypeArguments = actualTypeArguments;
    }

    @Override
    public Type[] getActualTypeArguments() {
        return copyOf(actualTypeArguments, actualTypeArguments.length, actualTypeArguments.getClass());
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
