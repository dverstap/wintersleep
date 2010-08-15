/*
 * Copyright 2008 Davy Verstappen.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jcodeviz.uml.model;

public class RelationEndpoint {

    public static final int UNKNOWN_CARDINALITY = -1;
    public static final int MANY_CARDINALITY = Integer.MAX_VALUE;

    private ModelClass modelClass;
    private String fieldName;
    private Relation relation;
    private int minCardinality = UNKNOWN_CARDINALITY;
    private int maxCardinality = UNKNOWN_CARDINALITY;

    public RelationEndpoint(ModelClass modelClass, String fieldName) {
        this.modelClass = modelClass;
        this.fieldName = fieldName;
    }

    void setRelation(Relation relation) {
        this.relation = relation;
    }

    public ModelClass getModelClass() {
        return modelClass;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Relation getRelation() {
        return relation;
    }

    public int getMinCardinality() {
        return minCardinality;
    }

    public void setMinCardinality(int minCardinality) {
        this.minCardinality = minCardinality;
    }

    public int getMaxCardinality() {
        return maxCardinality;
    }

    public void setMaxCardinality(int maxCardinality) {
        this.maxCardinality = maxCardinality;
    }

    public String toString() {
        return "RelationEndpoint{" +
                "modelClass=" + modelClass +
                ", fieldName='" + fieldName + '\'' +
                ", minCardinality=" + minCardinality +
                ", maxCardinality=" + maxCardinality +
                '}';
    }

    public RelationEndpoint getOtherEndpoint() {
        if (this == relation.getTo()) {
            return relation.getFrom();
        } else {
            return relation.getTo();
        }
    }

    public String getCardinalityStr() {
        if (minCardinality == maxCardinality) {
            return toString(minCardinality);
        } else {
            return toString(minCardinality) + ".." + toString(maxCardinality);
        }
    }

    private static String toString(int cardinality) {
        if (cardinality == UNKNOWN_CARDINALITY) {
            return "?";
        } else if (cardinality == MANY_CARDINALITY) {
            return "*";
        } else {
            return Integer.toString(cardinality);
        }
    }
}
