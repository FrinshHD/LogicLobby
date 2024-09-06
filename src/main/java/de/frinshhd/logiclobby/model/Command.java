package de.frinshhd.logiclobby.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Command {

    @JsonProperty
    private String name;

    @JsonProperty
    private List<String> aliases = new ArrayList<>();

    public String getName() {
        return name;
    }

    public List<String> getAliases() {
        return aliases;
    }

}
