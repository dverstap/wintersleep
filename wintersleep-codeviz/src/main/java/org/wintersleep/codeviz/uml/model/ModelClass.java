package org.wintersleep.codeviz.uml.model;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ModelClass {

    private Class clazz;
    private Set<String> stereoTypes;
    private Map<String, RelationEndpoint> relationMap = new HashMap<String, RelationEndpoint>();

    public ModelClass(Class clazz) {
        this.clazz = clazz;
    }

    public Class getClazz() {
        return clazz;
    }

    public Set<String> getStereoTypes() {
        return stereoTypes;
    }

    public Relation addRelationTo(Field field, ModelClass otherModelClass, int maxToCardinality) {
        RelationEndpoint from = new RelationEndpoint(this, field.getName());
        RelationEndpoint to = new RelationEndpoint(otherModelClass, null);
        to.setMaxCardinality(maxToCardinality);
        relationMap.put(field.getName(), from);
        return new Relation(from, to);
    }

    public Relation addRelationTo(RelationEndpoint from, RelationEndpoint to) {
        assert (from.getModelClass() == this);
        relationMap.put(from.getFieldName(), from);
        return new Relation(from, to);

    }

    public Collection<RelationEndpoint> getRelationEndpoints() {
        return relationMap.values();
    }
}
