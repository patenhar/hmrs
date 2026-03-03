package com.hrms.backend.events;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;

@Getter
@Setter
public class GameConfigUpdateEvent extends ApplicationEvent {
    private final UUID pkGameId;

    public GameConfigUpdateEvent(Object source, UUID pkGameId) {
        super(source);
        this.pkGameId = pkGameId;
    }
}
