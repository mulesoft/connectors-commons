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
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof SimpleParameterizedType))
            return false;

        SimpleParameterizedType that = (SimpleParameterizedType) o;

        if (!getRawType().equals(that.getRawType()))
            return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(getActualTypeArguments(), that.getActualTypeArguments());

    }

    @Override
    public int hashCode() {
        int result = getRawType().hashCode();
        result = 31 * result + Arrays.hashCode(getActualTypeArguments());
        return result;
    }
}
