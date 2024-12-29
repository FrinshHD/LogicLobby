package de.frinshhd.logiclobby.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Command {

    @SerializedName("name")
    private String name;

    @SerializedName("aliases")
    private List<String> aliases = new ArrayList<>();

    public String getName() {
        return name;
    }

    public List<String> getAliases() {
        return aliases;
    }

}