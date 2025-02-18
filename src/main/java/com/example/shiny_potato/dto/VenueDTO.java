package com.example.shiny_potato.dto;

import org.hibernate.sql.ast.tree.expression.Over;

import java.util.Set;

public class VenueDTO {

    private Long id;
    private String name;
    private String address;
    private String description;
    private Set<Long> eventIds;
    private Integer version;

    public VenueDTO() {}

    public VenueDTO(Long id, String name, String address, String description, Set<Long> eventIds, Integer version) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.description = description;
        this.eventIds = eventIds;
        this.version = version;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Long> getEventIds() {
        return eventIds;
    }

    public void setEventIds(Set<Long> eventIds) {
        this.eventIds = eventIds;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
