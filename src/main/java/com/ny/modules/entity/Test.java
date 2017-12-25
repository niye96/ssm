package com.ny.modules.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

@Data
public class Test implements Serializable {

    private static final long serialVersionUID = -1401209233021437538L;

    @Id
    public String id;
    public String name;
}
