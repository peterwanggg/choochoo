package com.pwang.kings.adapters;

import com.pwang.kings.objects.model.KingsObject;

/**
 * @author pwang on 12/27/17.
 */
public interface Adapter<A, K extends KingsObject> {

    K adapt(A apiObject);
}
