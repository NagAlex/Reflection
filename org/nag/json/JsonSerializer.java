package org.nag.json;

import org.nag.json.adapters.*;
//import org.nag.json.adapters.JsonDataAdapter;
//import org.nag.json.adapters.UseDataAdapter;
import org.nag.test.*;
//import org.nag.test.Cat;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;


/**
 * JsonSerializer converts Java objects to JSON representation.
 *
 */
public class JsonSerializer {

    /**
     * simpleTypes contains java classes for which we should not make any deeper serialization and we should return object as is
     * and use toString() method to get it serialized representation
     */
    private static Set<Class> simpleTypes = new HashSet<Class>(Arrays.asList(
            JSONObject.class,
            JSONArray.class,
            String.class,
            Integer.class,
            Short.class,
            Long.class,
            Byte.class,
            Double.class,
            Float.class,
            Character.class,
            Boolean.class,
            int.class,
            short.class,
            long.class,
            byte.class,
            double.class,
            float.class,
            char.class,
            boolean.class
    ));

    /**
     * Main method to convert Java object to JSON. If type of the object is part of the simpleTypes object itself will be returned.
     * If object is null String value "null" will be returned.
     * @param o object to serialize.
     * @return JSON representation of the object.
     */
    public static Object serialize(Object o) {
        if (null == o) {
            return "null";
        }
        if (simpleTypes.contains(o.getClass())) {
            return o;
        } else {
            try {
                return toJsonObject(o);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    /**
     * Converts Java object to JSON. Uses reflection to access object fields.
     * Uses JsonDataAdapter to serialize complex values. Ignores @Ignore annotated fields.
     * @param o object to serialize to JSON
     * @return JSON object.
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private static JSONObject toJsonObject(Object o) throws Exception {
        JSONObject jsonObject = new JSONObject();
        Class<?> cObj = o.getClass();
        Field fields[] = cObj.getDeclaredFields();

        for(Field f: fields) {
            f.setAccessible(true);

            Ignore ignoreAnn = f.getAnnotation(Ignore.class);
            if(ignoreAnn != null) continue;

            UseDataAdapter ann = f.getAnnotation(UseDataAdapter.class);
            if(ann == null) {
                jsonObject.put(f.getName(), serialize(f.get(o)));
            }
            else {
                //Object adapterObject = "";
                Class<? extends JsonDataAdapter> adapter = ann.value();

                Object adapterObject = adapter.newInstance().toJson(f.get(o));
                /*if(adapter.isInstance(new DateAdapter()))
                    adapterObject = new DateAdapter().toJson((Date)f.get(o));
                else if(adapter.isInstance(new ColorAdapter()))
                    adapterObject = new ColorAdapter().toJson((Color)f.get(o));
                else if(adapter.isInstance(new MapAdapter()))
                    adapterObject = new MapAdapter().toJson((Map)f.get(o));
                else if(adapter.isInstance(new CollectionAdapter()))
                    adapterObject = new CollectionAdapter().toJson((Collection)f.get(o));*/
                jsonObject.put(f.getName(), adapterObject);
            }
        }
        return jsonObject;
    }
}
