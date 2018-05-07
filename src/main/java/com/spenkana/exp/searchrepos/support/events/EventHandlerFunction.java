package com.spenkana.exp.searchrepos.support.events;

import com.spenkana.exp.searchrepos.support.stateMachine.StateMachine;
import com.spenkana.exp.searchrepos.support.result.Result;

import java.util.function.BiFunction;

public interface EventHandlerFunction<T> extends
    BiFunction<T, StateMachine.VisitorContext, Result<Void>> {}
