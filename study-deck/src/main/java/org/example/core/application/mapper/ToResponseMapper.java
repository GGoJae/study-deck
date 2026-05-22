package org.example.core.application.mapper;

public interface ToResponseMapper <D, R> {
    R toResponse(D domain);
}
