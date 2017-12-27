package com.pwang.kings.adapters.google;

/**
 * @author pwang on 12/26/17.
 */

import com.pwang.kings.adapters.Adapter;
import com.pwang.kings.objects.model.KingsObject;

import java.io.Serializable;

public interface GoogleAdapter<G extends Serializable, K extends KingsObject> extends Adapter<G, K> {

}
