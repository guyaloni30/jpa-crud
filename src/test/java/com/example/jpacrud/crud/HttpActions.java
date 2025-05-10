package com.example.jpacrud.crud;

import com.example.jpacrud.users.NewUser;
import com.example.jpacrud.users.UpdatedUser;
import com.example.jpacrud.users.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.BeforeEach;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class HttpActions {
    private static final HttpClient http = HttpClient.newHttpClient();
    private static final String BASE_URI = "http://localhost:8080/api/users";
    private static final JsonMapper JSON_MAPPER = new JsonMapper();
    private static final TypeReference<List<User>> usersTypeRef = new TypeReference<>() {
    };

    @BeforeEach
    public void deleteExistingUsers() {
        getSortedUsers().forEach(user -> delete(user.getId()));
        List<User> users = getSortedUsers();
        assertTrue(users.isEmpty(), users.toString());
    }

    protected final List<User> getSortedUsers() {
        HttpRequest get = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URI))
                .build();
        try {
            HttpResponse<String> response = http.send(get, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new StatusErrorException(response);
            }
            return JSON_MAPPER.readValue(response.body(), usersTypeRef).stream()
                    .sorted(Comparator.comparing(User::getFirstName).thenComparing(User::getLastName))
                    .toList();
        } catch (StatusErrorException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected final User create(String firstName, String lastName) {
        try {
            HttpRequest create = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(JSON_MAPPER.writeValueAsString(new NewUser(firstName, lastName))))
                    .uri(URI.create(BASE_URI))
                    .header("Content-Type", "application/json")
                    .build();
            HttpResponse<String> response = http.send(create, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new StatusErrorException(response);
            }
            return JSON_MAPPER.readValue(response.body(), User.class);
        } catch (StatusErrorException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected final User update(int id, String firstName, String lastName) {
        try {
            HttpRequest update = HttpRequest.newBuilder()
                    .PUT(HttpRequest.BodyPublishers.ofString(JSON_MAPPER.writeValueAsString(new UpdatedUser(firstName, lastName))))
                    .uri(URI.create(BASE_URI + "/" + id))
                    .header("Content-Type", "application/json")
                    .build();
            HttpResponse<String> response = http.send(update, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new StatusErrorException(response);
            }
            return JSON_MAPPER.readValue(response.body(), User.class);
        } catch (StatusErrorException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected final User delete(int id) {
        HttpRequest delete = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(BASE_URI + "/" + id))
                .build();
        try {
            HttpResponse<String> response = http.send(delete, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new StatusErrorException(response);
            }
            return JSON_MAPPER.readValue(response.body(), User.class);
        } catch (StatusErrorException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
