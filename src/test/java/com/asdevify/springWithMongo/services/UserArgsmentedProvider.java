package com.asdevify.springWithMongo.services;

import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import com.asdevify.springWithMongo.entities.UserEntity;

public class UserArgsmentedProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
      return Stream.of(
        Arguments.of(UserEntity.builder().username("test1").password("test1").build()),
        Arguments.of(UserEntity.builder().username("test2").password("test2").build())
      );
    }
    
}
