package br.com.jkavdev.fullcycle.subscription.domain;

public interface Identifier<T> extends ValueObject {

    T value();

}
