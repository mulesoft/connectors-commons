package com.mule.connectors.commons.rest.builder.util;

import com.google.common.base.Optional;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

public class SimpleParameterizedTypeTest {

    @Test
    public void simpleClassEqualsTest() {
        assertEquals(new SimpleParameterizedType(List.class), new SimpleParameterizedType(List.class));
    }

    @Test
    public void simpleClassDifferentTest() {
        assertDifferent(new SimpleParameterizedType(List.class), new SimpleParameterizedType(Set.class));
    }

    @Test
    public void parameterizedTypeEqualsTest() {
        assertEquals(new SimpleParameterizedType(List.class, String.class), new SimpleParameterizedType(List.class, String.class));
    }

    @Test
    public void parameterizedTypeDifferentRawTest() {
        assertDifferent(new SimpleParameterizedType(List.class, String.class), new SimpleParameterizedType(Set.class, String.class));
    }

    @Test
    public void parameterizedTypeDifferentArgumentTest() {
        assertDifferent(new SimpleParameterizedType(List.class, String.class), new SimpleParameterizedType(List.class, Integer.class));
    }

    @Test
    public void parameterizedTypeDifferentArgumentSizeTest() {
        assertDifferent(new SimpleParameterizedType(List.class, String.class), new SimpleParameterizedType(List.class, String.class, String.class));
    }

    @Test
    public void parameterizedTypeWithinParameterizedTypeArgumentEqualTest() {
        assertEquals(new SimpleParameterizedType(List.class, new SimpleParameterizedType(List.class, String.class)),
                new SimpleParameterizedType(List.class, new SimpleParameterizedType(List.class, String.class)));
    }

    @Test
    public void parameterizedTypeWithinParameterizedTypeArgumentDifferentTest() {
        assertDifferent(new SimpleParameterizedType(List.class, new SimpleParameterizedType(List.class, String.class)),
                new SimpleParameterizedType(List.class, new SimpleParameterizedType(List.class, Integer.class)));
    }

    @Test
    public void parameterizedTypeNotEqualToNullTest() {
        assertDifferent(new SimpleParameterizedType(List.class), null);
    }

    @Test
    public void selfEqualTest() {
        Type type = new SimpleParameterizedType(List.class);
        assertEquals(type, type);
    }

    @Test
    public void nullRawTypeEqualTest() {
        assertEquals(new SimpleParameterizedType(null), new SimpleParameterizedType(null));
    }

    @Test
    public void getOwnerTest() {
        assertThat(new SimpleParameterizedType(List.class).getOwnerType(), CoreMatchers.<Type>equalTo(List.class));
    }

    private void assertEquals(Type type, Type otherType) {
        assertThat(type, equalTo(otherType));
        assertThat(type.hashCode(), equalTo(otherType.hashCode()));
    }

    private void assertDifferent(Type type, Type otherType) {
        assertThat(type, not(equalTo(otherType)));
        assertThat(type.hashCode(), not(equalTo(Optional.<Object>fromNullable(otherType).or(new Object()).hashCode())));
    }
}
