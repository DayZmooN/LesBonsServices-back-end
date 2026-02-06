package com.example.lesbonsservices.exception;

import java.time.Instant;

public record ApiError(Instant timestamp, String status, String error, String message, String path, String requestId) {
}
