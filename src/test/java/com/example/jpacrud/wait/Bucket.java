package com.example.jpacrud.wait;

import java.util.List;

public record Bucket(String now, long created, List<WaitingResponse> responses) {
}