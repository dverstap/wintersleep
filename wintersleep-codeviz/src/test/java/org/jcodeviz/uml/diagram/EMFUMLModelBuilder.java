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

package org.jcodeviz.uml.diagram;

public class EMFUMLModelBuilder {

/*
    private final UMLFactory factory;
    private final List<Class> classes;
    private final Map<java.lang.Package, Package> packageMap = new HashMap<java.lang.Package, Package>();

    public EMFUMLModelBuilder(UMLFactory factory, List<Class> classes) {
        this.factory = factory;
        this.classes = classes;
    }

    protected static void registerResourceFactories() {
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(
			UMLResource.FILE_EXTENSION, UMLResource.Factory.INSTANCE);
	}

    public Model build() throws IOException {
        Model model = factory.createModel();
        model.setName("mymodel");

        for (Class aClass : classes) {
            Package aPackage = buildPackage(model, aClass.getPackage());
            aPackage.createOwnedClass(aClass.getSimpleName(), isAbstract(aClass));
        }

        URI uri = URI.createURI("file:///tmp/")
                .appendSegment(model.getName())
                .appendFileExtension(UMLResource.FILE_EXTENSION);
        System.out.println(uri);
        ResourceSet resourceSet = new ResourceSetImpl();
        resourceSet.setResourceFactoryRegistry(Resource.Factory.Registry.INSTANCE);
        Resource resource = resourceSet.createResource(uri);
        if (resource == null) {
            throw new IllegalArgumentException();
        }
        if (resource.getContents() == null) {
            throw new IllegalArgumentException();
        }
        resource.getContents().add(model);
        resource.save(null);

        return model;
    }

    private boolean isAbstract(Class aClass) {
        return (aClass.getModifiers() & Modifier.ABSTRACT) != 0;
    }

    private Package buildPackage(Model model, java.lang.Package aPackage) {

        Package result = packageMap.get(aPackage);
        if (result == null) {
            result = factory.createPackage();
            result.setName(aPackage.getName());
            packageMap.put(aPackage, result);
            result.setNestingPackage(model);
        }
        return result;
    }


    public static void main(String[] args) {
        try {
            registerResourceFactories();

            List<Class> classes = new ArrayList<Class>();
            classes.add(EMFUMLModelBuilder.class);
            classes.add(DiGraph.class);

            new EMFUMLModelBuilder(UMLFactory.eINSTANCE, classes).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/
}
