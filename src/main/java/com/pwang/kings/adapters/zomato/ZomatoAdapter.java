package com.pwang.kings.adapters.zomato;

import com.pwang.kings.adapters.Adapter;
import com.pwang.kings.objects.api.zomato.ZomatoObject;
import com.pwang.kings.objects.model.KingsObject;

/**
 * @author pwang on 12/27/17.
 */
public interface ZomatoAdapter<Z extends ZomatoObject, T extends KingsObject> extends Adapter<Z, T> {
}
