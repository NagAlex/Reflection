package org.nag.json.adapters;

import org.nag.json.JsonSerializer;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.Collection;

/**
 * Converts all objects that extends java.util.Collections to JSONArray.
 */
public class CollectionAdapter implements JsonDataAdapter<Collection> {
    @Override
    public Object toJson(Collection c) throws JSONException{
        JSONArray jCol = new JSONArray();
        //JSONArray jCol = new JSONArray(c);
        for(Object obj: c) {
            jCol.put(JsonSerializer.serialize(obj));
        }
        return jCol;
    }
}
