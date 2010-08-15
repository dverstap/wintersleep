/*
 * Copyright 2009 Davy Verstappen.
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
package org.jcodeviz.uml.model.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.dialect.MySQL5InnoDBDialect;
import org.hibernate.auction.AuctionItem;
import org.hibernate.auction.Bid;
import org.hibernate.auction.User;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.wintersleep.test.util.FileTestUtil;
import static org.wintersleep.test.util.FileTestUtil.assertCreated;
import org.jcodeviz.uml.diagram.ClassDiagram;
import org.jcodeviz.uml.model.CodeModel;
import org.jcodeviz.uml.model.HibernateModelFactory;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class HibernateAuctionTutorialTest {

    private final File outputDir = FileTestUtil.makeOutputDir(HibernateAuctionTutorialTest.class);

    @Test
    public void test() throws IOException {
        Configuration cfg = new Configuration()
            .addClass(AuctionItem.class)
            .addClass(Bid.class)
            .addClass(User.class)
            .setProperty(Environment.HBM2DDL_AUTO, "create")
                .setProperty(Environment.DIALECT, MySQL5InnoDBDialect.class.getName());
        //cfg.setProperty("hibernate.show_sql", "true");

        SessionFactory factory = cfg.buildSessionFactory();

        CodeModel model = new HibernateModelFactory(cfg, "Auction Tutorial").create();


        ClassDiagram diagram = new ClassDiagram("AuctionDiagram", model);
        diagram.getSettings()
                .enableDrawingAttributes()
                //.enableDrawingOperations()
                ;
        diagram.addAllModelClasses();

        assertCreated(diagram.createGraph().makeImageFile(outputDir, "png", true));

    }
}
