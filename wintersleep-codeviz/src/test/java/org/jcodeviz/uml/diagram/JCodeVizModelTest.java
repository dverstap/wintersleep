package org.jcodeviz.uml.diagram;

import org.wintersleep.test.util.FileTestUtil;
import static org.wintersleep.test.util.FileTestUtil.assertCreated;
import org.jcodeviz.uml.model.CodeModel;
import org.jcodeviz.uml.model.ModelClass;
import org.jcodeviz.uml.model.Relation;
import org.jcodeviz.uml.model.RelationEndpoint;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class JCodeVizModelTest {

    private final File outputDir = FileTestUtil.makeOutputDir(JCodeVizModelTest.class);

    @Test
    public void test() throws IOException, InterruptedException {
        CodeModel model = new CodeModel("JCodeVizModel");
        model.add(CodeModel.class);
        model.add(ModelClass.class);
        model.add(Relation.class);
        model.add(RelationEndpoint.class);
        model.addFieldRelations();

        ClassDiagram d = new ClassDiagram("JCodeVizDiagram", model);
        d.getSettings()
                .enableDrawingAttributes()
                .disableDrawingOperations();
        d.addAllModelClasses();
        d.add(CodeModel.class).getSettings()
                .enableDrawingAttributes()
                .enableDrawingAttributeTypes();
        //.enableDrawingOperations();
        assertCreated(d.createGraph().makeImageFile(outputDir, "png", true));

    }


}
