package com.github.appreciated.vortex_crud.core.service;

import com.vaadin.flow.shared.Registration;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

@Service
public class SignalService {
    public record Signal(String topic, Object payload) {}

    private final List<Consumer<Signal>> listeners = new CopyOnWriteArrayList<>();

    public void emit(String topic, Object payload) {
        emit(new Signal(topic, payload));
    }

    public void emit(Signal signal) {
        listeners.forEach(listener -> {
            try {
                listener.accept(signal);
            } catch (Exception e) {
                org.slf4j.LoggerFactory.getLogger(SignalService.class).error("Error in signal listener", e);
            }
        });
    }

    public Registration subscribe(Consumer<Signal> listener) {
        listeners.add(listener);
        return () -> listeners.remove(listener);
    }

    public Registration subscribe(String topic, Consumer<Signal> listener) {
        return subscribe(signal -> {
            if (signal.topic().equals(topic)) {
                listener.accept(signal);
            }
        });
    }
}
