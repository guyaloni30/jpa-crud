package com.example.jpacrud.crud;

import com.example.jpacrud.users.User;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CrudTest extends HttpActions {
    @Test
    public void emptyUsers() {
        assertTrue(getSortedUsers().isEmpty());
    }

    @Test
    public void create() {
        User user = create("John", "Doe");
        assertNotNull(user);
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        user = create("Jane", "Doe");
        assertNotNull(user);
        assertEquals("Jane", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        List<User> users = getSortedUsers();
        assertEquals(2, users.size());
        User jane = users.get(0);
        User john = users.get(1);
        assertEquals("John", john.getFirstName());
        assertEquals("Doe", john.getLastName());
        assertEquals("Jane", jane.getFirstName());
        assertEquals("Doe", jane.getLastName());
    }

    @Test
    public void update() {
        User user = create("John", "Doe");
        User updated = update(user.getId(), "Jane", "Does");
        assertNotNull(updated);
        assertEquals(user.getId(), updated.getId());
        assertEquals("Jane", updated.getFirstName());
        assertEquals("Does", updated.getLastName());
    }

    @Test
    public void updateUnknown() {
        User user = create("John", "Doe");
        assertThrows(StatusErrorException.class, () -> update(user.getId() + 1, "Jane", "Does"));
    }

    @Test
    public void updateDuplicate() {
        create("Jane", "Exists");
        User user = create("John", "Doe");
        assertThrows(StatusErrorException.class, () -> update(user.getId() + 1, "Jane", "Exists"));
    }

    @Test
    public void createDouble() {
        create("Jane", "Exists");
        assertThrows(StatusErrorException.class, () -> create("Jane", "Exists"));
    }

    @Test
    public void delete() {
        assertTrue(getSortedUsers().isEmpty());
        User user = create("John", "Doe");
        assertEquals(1, getSortedUsers().size());
        assertEquals(user, delete(user.getId()));
        assertTrue(getSortedUsers().isEmpty());
    }

    @Test
    public void deleteUnknown() {
        assertThrows(StatusErrorException.class, () -> delete(123));
    }
}