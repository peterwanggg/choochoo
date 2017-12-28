package com.pwang.kings.objects.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import javax.annotation.Nullable;
import java.net.URL;

/**
 * @author pwang on 12/26/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(as = com.pwang.kings.objects.model.ImmutableContestant.class)
@JsonSerialize(as = com.pwang.kings.objects.model.ImmutableContestant.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Value.Immutable
public interface Contestant extends KingsObject {

    @Nullable
    @JsonProperty
    Long getContestantId();

    @Nullable
    @JsonProperty
    Long getCategoryId();

    @JsonProperty
    String getContestantName();

    @JsonProperty
    URL getImageUrl();
}
