package com.pwang.kings.objects.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.security.Principal;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(as = com.pwang.kings.objects.model.ImmutableKingsUser.class)
@JsonSerialize(as = com.pwang.kings.objects.model.ImmutableKingsUser.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Value.Immutable
public interface KingsUser extends Principal {

    @JsonProperty
    Long getKingsUserId();

    @Override
    @JsonProperty
    String getName();

    @JsonProperty
    Set<String> getRoles();
}


/**
 * @author pwang on 12/26/17.
 */
//public class KingsUser implements Principal {
//    private final String name;
//
//    private final Set<String> roles;
//
//    public KingsUser(String name) {
//        this.name = name;
//        this.roles = null;
//    }
//
//    public KingsUser(String name, Set<String> roles) {
//        this.name = name;
//        this.roles = roles;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public int getId() {
//        return (int) (Math.random() * 100);
//    }
//
//    public Set<String> getRoles() {
//        return roles;
//    }
//}

