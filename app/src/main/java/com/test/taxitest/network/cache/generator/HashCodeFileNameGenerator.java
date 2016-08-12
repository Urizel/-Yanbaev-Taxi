package com.test.taxitest.network.cache.generator;

public class HashCodeFileNameGenerator implements FileNameGenerator {
    public String generate(String imageUri) {
        return String.valueOf(imageUri.hashCode());
    }
}