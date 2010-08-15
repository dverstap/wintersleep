package org.jcodeviz.uml.diagram;

import org.jcodeviz.graphviz.*;
import org.jcodeviz.uml.model.CodeModel;
import org.jcodeviz.uml.model.ModelClass;
import org.jcodeviz.uml.model.Relation;
import org.jcodeviz.uml.model.RelationEndpoint;

import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ClassDiagram {

    private final String name;
    private final CodeModel codeModel;
    private final Map<Class, ClassShape> shapeMap = new HashMap<Class, ClassShape>();

    private final ClassDrawingSettings settings = new ClassDrawingSettings();

    public ClassDiagram(String name, CodeModel codeModel) {
        this.name = name;
        this.codeModel = codeModel;
    }

    public String getName() {
        return name;
    }

    public CodeModel getCodeModel() {
        return codeModel;
    }

    public ClassDrawingSettings getSettings() {
        return settings;
    }

    public DiGraph createGraph() {
        DiGraph g = new DiGraph(name);
        g.setMessage("line 1", "line 2");
        g.addEdgeAttributeListList().addAttributeList()
                .setFontName("Helvetica")
                .setFontSize(10)
                .setLabelFontName("Helvetica")
                .setLabelFontSize(10);
        g.addNodeAttributeListList().addAttributeList()
                .setFontName("Helvetica")
                .setFontSize(10)
                .setShape("plaintext");
        boolean horizontal = true;
        if (horizontal) {
            g.getAttributeList()
                    .setRankDir(RankDir.LR)
                    .setRankSep(1);
        }
        Color bgColor = Color.blue;
        if (bgColor != null) {
            //g.getAttributeList().setBgColor(bgColor);
            g.getAttributeList().setBgColor("red");
        }
        Map<Class, Node> nodeMap = drawClasses(g);
        drawRelations(g, nodeMap);
        return g;
    }

    private Map<Class, Node> drawClasses(DiGraph g) {
        Map<Class, Node> result = new HashMap<Class, Node>();
        for (ClassShape classShape : shapeMap.values()) {
            result.put(classShape.getModelClass().getClazz(), classShape.draw(g));
        }
        return result;
    }

    private void drawRelations(DiGraph g, Map<Class, Node> nodeMap) {
        Set<Relation> relations = new HashSet<Relation>();
        for (ClassShape classShape : shapeMap.values()) {
            Node myNode = nodeMap.get(classShape.getModelClass().getClazz());
            for (RelationEndpoint relationEndpoint : classShape.getModelClass().getRelationEndpoints()) {
                Relation relation = relationEndpoint.getRelation();
                if (!relations.contains(relation)) {
                    relations.add(relation);
                    RelationEndpoint otherEndpoint = relationEndpoint.getOtherEndpoint();
                    Node otherNode = nodeMap.get(otherEndpoint.getModelClass().getClazz());
                    if (otherNode != null) {
                        EdgeAttributeList edgeAttributeList = g.addEdge(myNode, "p", otherNode, "p").addAttributeList()
                                //.setDir(Direction.BACK)
                                .setStyle(EdgeStyle.SOLID)
                                        //.setArrowTail(Arrow.DIAMOND)
                                .setTailLabel(relation.getTo().getCardinalityStr())
                                .setLabel(relation.getFrom().getFieldName())
                                        //.setArrowHead(Arrow.NONE)
                                .setHeadLabel(relation.getFrom().getCardinalityStr())
                                        //.setFontName("helvetica")
                                .setFontColor(Color.RED)
                                .setFontSize(10.0)
                                        //.setLabelAngle(30)
                                .setLabelDistance(2.0);

                    }
                }
            }
        }
    }

    public ClassShape add(Class clazz) {
        ClassShape result = shapeMap.get(clazz);
        if (result == null) {
            ModelClass modelClass = codeModel.findModelClass(clazz);
            result = new ClassShape(modelClass, settings.clone());
            shapeMap.put(clazz, result);
        }
        return result;
    }

    public void addAllModelClasses
            () {
        for (ModelClass modelClass : codeModel.getModelClasses()) {
            add(modelClass.getClazz());
        }
    }

}
