package org.wintersleep.ganglia.doc;

import org.junit.Test;
import org.wintersleep.codeviz.uml.diagram.ClassDiagram;
import org.wintersleep.codeviz.uml.model.CodeModel;
import org.wintersleep.codeviz.uml.model.ModelClass;
import org.wintersleep.codeviz.uml.model.Stereotypes;
import org.wintersleep.ganglia.protocol.Ganglia_25metric;
import org.wintersleep.ganglia.protocol.Ganglia_extra_data;
import org.wintersleep.ganglia.protocol.Ganglia_gmetric_double;
import org.wintersleep.ganglia.protocol.Ganglia_gmetric_float;
import org.wintersleep.ganglia.protocol.Ganglia_gmetric_int;
import org.wintersleep.ganglia.protocol.Ganglia_gmetric_short;
import org.wintersleep.ganglia.protocol.Ganglia_gmetric_string;
import org.wintersleep.ganglia.protocol.Ganglia_gmetric_uint;
import org.wintersleep.ganglia.protocol.Ganglia_gmetric_ushort;
import org.wintersleep.ganglia.protocol.Ganglia_metadata_message;
import org.wintersleep.ganglia.protocol.Ganglia_metadata_msg;
import org.wintersleep.ganglia.protocol.Ganglia_metadatadef;
import org.wintersleep.ganglia.protocol.Ganglia_metadatareq;
import org.wintersleep.ganglia.protocol.Ganglia_metric_id;
import org.wintersleep.ganglia.protocol.Ganglia_msg_formats;
import org.wintersleep.ganglia.protocol.Ganglia_value_msg;
import org.wintersleep.ganglia.protocol.Ganglia_value_types;
import org.wintersleep.graphviz.DiGraph;
import org.wintersleep.graphviz.RankDir;
import org.wintersleep.test.util.FileTestUtil;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertTrue;
import static org.wintersleep.test.util.FileTestUtil.assertCreated;

public class GenerateGangliaMessageModelDiagramsTest {

    private final File outputDir = FileTestUtil.makeOutputDir(GenerateGangliaMessageModelDiagramsTest.class);

    private CodeModel model;

    @Test
    public void generateGangliaAllMessagesDiagram() throws IOException {
        generateDiagram("GangliaAllMessages", RankDir.LR,
                Ganglia_25metric.class,
                Ganglia_extra_data.class,
                Ganglia_gmetric_double.class,
                Ganglia_gmetric_float.class,
                Ganglia_gmetric_int.class,
                Ganglia_gmetric_short.class,
                Ganglia_gmetric_string.class,
                Ganglia_gmetric_uint.class,
                Ganglia_gmetric_ushort.class,
                Ganglia_metadata_message.class,
                Ganglia_metadata_msg.class,
                Ganglia_msg_formats.class,
                Ganglia_metadatadef.class,
                Ganglia_metadatareq.class,
                Ganglia_metric_id.class,
                Ganglia_value_msg.class,
                Ganglia_value_types.class
        );
    }


    @Test
    public void generateLegacyGangliaMessagesDiagram() throws IOException {
        generateDiagram("Ganglia25Messages", RankDir.LR,
                Ganglia_25metric.class,
                Ganglia_value_types.class);
        // TODO there are definitely more message types in ganglia 3.0 as well
        // TODO the Ganglia_value_types enum, is it used in 3.1?
    }

    @Test
    public void generateGanglia31MetadataMessagesDiagram() throws IOException {
        generateDiagram("Ganglia31MetadataMessages", RankDir.TB,
                Ganglia_extra_data.class,
                Ganglia_metadata_message.class,
                Ganglia_metadata_msg.class,
                Ganglia_metadatadef.class,
                Ganglia_metadatareq.class,
                Ganglia_msg_formats.class,
                Ganglia_metric_id.class);
    }

    @Test
    public void generateGanglia31ValueMessagesDiagram() throws IOException {
        generateDiagram("Ganglia31ValueMessages", RankDir.LR,
                Ganglia_gmetric_double.class,
                Ganglia_gmetric_float.class,
                Ganglia_gmetric_int.class,
                Ganglia_gmetric_short.class,
                Ganglia_gmetric_string.class,
                Ganglia_gmetric_uint.class,
                Ganglia_gmetric_ushort.class,
                Ganglia_metric_id.class,
                Ganglia_msg_formats.class,
                Ganglia_value_msg.class);
    }

    private void generateDiagram(String name, RankDir rankDir, Class... classes) throws IOException {
        model = new CodeModel(name);
        model.addClasses(classes);
        addStereoType(Ganglia_metadata_msg.class, Stereotypes.UNION);
        addStereoType(Ganglia_msg_formats.class, Stereotypes.ENUM);
        addStereoType(Ganglia_value_msg.class, Stereotypes.UNION);
        addStereoType(Ganglia_value_types.class, Stereotypes.ENUM);
        model.addFieldRelations();

        ClassDiagram d = new ClassDiagram(model.getName(), model);
        d.getSettings()
                .enableDrawingAttributes()
                .enableDrawingAttributeTypes()
                .disableDrawingOperations();

        d.addAllModelClasses();
//        d.addClass(CodeModel.class).getSettings()
//                .enableDrawingAttributes()
//                .enableDrawingAttributeTypes();
        //.enableDrawingOperations();
        DiGraph diGraph = d.createGraph();
        //diGraph.getAttributeList().setOrientation(Orientation.LANDSCAPE);
        diGraph.getAttributeList().setRankDir(rankDir);
        assertCreated(diGraph.makeImageFile(outputDir, "png", true));
        //assertCreated(d.createGraph().makeImageFile(outputDir, "pdf", true));
        File epsFile = diGraph.makeImageFile(outputDir, "eps", true);
        assertCreated(epsFile);
        assertTrue(epsFile.renameTo(FileTestUtil.changeExtension(epsFile, "ps")));
    }

    private void addStereoType(Class<?> aClass, String stereotype) {
        ModelClass modelClass = model.findModelClass(aClass);
        if (modelClass != null) {
            modelClass.addStereoType(stereotype);
        }
    }

}
