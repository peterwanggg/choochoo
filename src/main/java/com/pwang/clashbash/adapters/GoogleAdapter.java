package com.pwang.clashbash.adapters;

/**
 * @author pwang on 12/26/17.
 */

import java.io.Serializable;

public interface GoogleAdapter<G extends Serializable, T> {

    T adapt(G googleObject);
}
