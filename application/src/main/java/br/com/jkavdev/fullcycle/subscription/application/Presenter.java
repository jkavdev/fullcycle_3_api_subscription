package br.com.jkavdev.fullcycle.subscription.application;

import java.util.function.Function;

public interface Presenter<USECASE_OUTPUT, PRESENTER_OUTPUT> extends Function<USECASE_OUTPUT, PRESENTER_OUTPUT> {
}
