package org.jcodeviz.uml.model;

/**
 * Created by IntelliJ IDEA.
 * User: davy
 * Date: Jan 20, 2008
 * Time: 1:15:01 PM
 */
public class Relation {

    private RelationEndpoint from;
    private RelationEndpoint to;

    public Relation(RelationEndpoint from, RelationEndpoint to) {
        this.from = from;
        this.to = to;

        from.setRelation(this);
        to.setRelation(this);
    }

    public RelationEndpoint getFrom() {
        return from;
    }

    public RelationEndpoint getTo() {
        return to;
    }


    public String toString() {
        return "Relation{" +
                "from=" + from +
                ", to=" + to +
                '}';
    }
}
