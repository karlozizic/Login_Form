package com.spring.security.formlogin.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Attempts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private int attempts;

    /**
     * @param id the id to set
     */

    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return the username
     */

    @Id
    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }
}
