package com.programmazione_avanzata.final_project.chef_ideas.server.exception;

import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public record ApiException(String message, HttpStatus status, ZonedDateTime timestamp) {
}
