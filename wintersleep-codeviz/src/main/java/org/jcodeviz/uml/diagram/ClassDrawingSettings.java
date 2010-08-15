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

public class ClassDrawingSettings implements Cloneable {

    private boolean drawingAttributes;
    private boolean drawingAttributeTypes;

    private boolean drawingOperations;
    private boolean drawingOperationReturnTypes;
    // TODO: parameter names are not possible based on reflection: Should look into ParaNamer: http://paranamer.codehaus.org/
    private boolean drawingOperationParameterNames;
    private boolean drawingOperationParameterTypes;

    public ClassDrawingSettings() {
    }

    public ClassDrawingSettings(boolean drawingAttributes, boolean drawingAttributeTypes, boolean drawingOperations, boolean drawingOperationReturnTypes, boolean drawingOperationParameterNames, boolean drawingOperationParameterTypes) {
        this.drawingAttributes = drawingAttributes;
        this.drawingAttributeTypes = drawingAttributeTypes;
        this.drawingOperations = drawingOperations;
        this.drawingOperationReturnTypes = drawingOperationReturnTypes;
        this.drawingOperationParameterNames = drawingOperationParameterNames;
        this.drawingOperationParameterTypes = drawingOperationParameterTypes;
    }

    public ClassDrawingSettings(ClassDrawingSettings settings) {
        this.drawingAttributes = settings.drawingAttributes;
        this.drawingAttributeTypes = settings.drawingAttributeTypes;
        this.drawingOperations = settings.drawingOperations;
        this.drawingOperationReturnTypes = settings.drawingOperationReturnTypes;
        this.drawingOperationParameterNames = settings.drawingOperationParameterNames;
        this.drawingOperationParameterTypes = settings.drawingOperationParameterTypes;

    }

    @Override
    protected ClassDrawingSettings clone() {
        return new ClassDrawingSettings(this);
    }

    public boolean isDrawingAttributes() {
        return drawingAttributes;
    }

    public void setDrawingAttributes(boolean drawingAttributes) {
        this.drawingAttributes = drawingAttributes;
    }

    public ClassDrawingSettings enableDrawingAttributes() {
        drawingAttributes = true;
        return this;
    }

    public ClassDrawingSettings disableDrawingAttributes() {
        drawingAttributes = false;
        return this;
    }

    public boolean isDrawingAttributeTypes() {
        return drawingAttributeTypes;
    }

    public void setDrawingAttributeTypes(boolean drawingAttributeTypes) {
        this.drawingAttributeTypes = drawingAttributeTypes;
    }

    public ClassDrawingSettings enableDrawingAttributeTypes() {
        drawingAttributeTypes = true;
        return this;
    }

    public ClassDrawingSettings disableDrawingAttributeTypes() {
        drawingAttributeTypes = false;
        return this;
    }

    public boolean isDrawingOperations() {
        return drawingOperations;
    }

    public void setDrawingOperations(boolean drawingOperations) {
        this.drawingOperations = drawingOperations;
    }

    public ClassDrawingSettings enableDrawingOperations() {
        drawingOperations = true;
        return this;
    }

    public ClassDrawingSettings disableDrawingOperations() {
        drawingOperations = false;
        return this;
    }

}
