package com.pwang.kings.objects.api.kings;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pwang.kings.objects.action.Bout;
import org.immutables.value.Value;

import java.util.List;

/**
 * @author pwang on 1/7/18.
 */
@JsonDeserialize(as = com.pwang.kings.objects.api.kings.ImmutableBoutHistoryResponse.class)
@JsonSerialize(as = com.pwang.kings.objects.api.kings.ImmutableBoutHistoryResponse.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Value.Immutable
public interface BoutHistoryResponse {

    List<Bout> bouts();
}
