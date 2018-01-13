package com.pwang.kings.objects.api.kings;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pwang.kings.objects.action.Bout;
import org.immutables.value.Value;

/**
 * @author pwang on 1/11/18.
 */
@JsonDeserialize(as = com.pwang.kings.objects.api.kings.ImmutableGetBoutResponse.class)
@JsonSerialize(as = com.pwang.kings.objects.api.kings.ImmutableGetBoutResponse.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Value.Immutable
public interface GetBoutResponse {

    Bout bout();
}
