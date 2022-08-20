package com.raidrin.graphqldemo.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@Entity
public class Person {
    @Id
    private int id;
    private String name;
    private String mobile;
    private String email;
    @SuppressWarnings("JpaAttributeTypeInspection")
    private String[] address;
}
