package com.pwang.clashbash.objects.model;

import org.immutables.value.Value;

/**
 * @author pwang on 12/26/17.
 */
@Value.Immutable
public interface PlaceContestant extends Contestant {

    String placeId();
}
