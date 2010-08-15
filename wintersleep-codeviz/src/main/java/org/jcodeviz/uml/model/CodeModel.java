package org.jcodeviz.uml.model;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CodeModel {

    private final String name;
    private final Map<Class, ModelClass> classModelMap = new HashMap<Class, ModelClass>();

    public CodeModel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ModelClass findModelClass(Class clazz) {
        return classModelMap.get(clazz);
    }

    public Collection<ModelClass> getModelClasses() {
        return classModelMap.values();
    }

    public void add(Class... classes) {
        for (Class clazz : classes) {
            if (classModelMap.containsKey(clazz)) {
                throw new IllegalArgumentException(clazz + " is already added");
            }
            classModelMap.put(clazz, new ModelClass(clazz));
        }
    }

    public void addFieldRelations() {
        for (ModelClass modelClass : classModelMap.values()) {
            for (Field field : modelClass.getClazz().getDeclaredFields()) {
                if ((field.getModifiers() & Modifier.STATIC) == 0) {
                    Class<?> aClass = field.getType();
                    ModelClass otherModelClass = classModelMap.get(aClass);
                    if (otherModelClass != null) {
                        modelClass.addRelationTo(field, otherModelClass, 1);
                    } else if (Collection.class.isAssignableFrom(aClass)) {
                        addCollectionFieldRelations(modelClass, field);
                    } else if (Map.class.isAssignableFrom(aClass)) {
                        addMapFieldRelations(modelClass, field);
                    }
                }
            }
        }
    }

    private void addCollectionFieldRelations(ModelClass modelClass, Field field) {
        ModelClass otherModelClass;
        Type type = field.getGenericType();
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            for (Type actualType : parameterizedType.getActualTypeArguments()) {
                if (actualType instanceof ParameterizedType) {
                    ParameterizedType actualParameterizedType = (ParameterizedType) actualType;
                    Type rawType = actualParameterizedType.getRawType();
                    Class rawClass = (Class) rawType;
                    if (classModelMap.containsKey(rawClass)) {
                        otherModelClass = classModelMap.get(rawClass);
                        modelClass.addRelationTo(field, otherModelClass, RelationEndpoint.MANY_CARDINALITY);
                    }
                }
            }
        }
    }

    private void addMapFieldRelations(ModelClass modelClass, Field field) {
        ModelClass otherModelClass;
        Type type = field.getGenericType();
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            for (Type actualType : parameterizedType.getActualTypeArguments()) {
                if (actualType instanceof ParameterizedType) {
                    ParameterizedType actualParameterizedType = (ParameterizedType) actualType;
                    Type rawType = actualParameterizedType.getRawType();
                    Class rawClass = (Class) rawType;
                    if (classModelMap.containsKey(rawClass)) {
                        otherModelClass = classModelMap.get(rawClass);
                        modelClass.addRelationTo(field, otherModelClass, RelationEndpoint.MANY_CARDINALITY);
                    }
                } else if (actualType instanceof Class) {
                    Class rawClass = (Class) actualType;
                    if (classModelMap.containsKey(rawClass)) {
                        otherModelClass = classModelMap.get(rawClass);
                        modelClass.addRelationTo(field, otherModelClass, RelationEndpoint.MANY_CARDINALITY);
                    }
                }
            }
        }
    }


}