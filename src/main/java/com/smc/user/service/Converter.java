package com.smc.user.service;

@FunctionalInterface
public interface Converter<S, T> {
    T convert(S source);
}
