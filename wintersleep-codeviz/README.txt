
Various aspects of automatically generating diagrams:

Automatically generating the UML Model of the code
- using javadoc
- javax.lang.model (annotation processing tool)
- from a hibernate Configuration
- from reflection

Creating diagrams (where diagram elements contain references to the model)
- using javadoc tags
- annotations?
- a file format (xml, ...)
- active code (just create the diagram objects in code)

Layout and Visualize Diagrams
- graphviz
- theoretically, layouts using other graphing tools are possible
- visualizing could be done using other UML tools (argouml, netbeans, eclipse)
- or even using diagramming libraries such as those from eclipse (GEF) and netbeans


Compile-time vs Runtime Approaches: TODO


Graphviz: correct approach would be to use jdigraph and to have a
visitor that at runtime creates the list of attributes that must be
printed.
