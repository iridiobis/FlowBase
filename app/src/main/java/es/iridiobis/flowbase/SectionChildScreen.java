package es.iridiobis.flowbase;

import flow.path.Path;

/**
 * TODO: Add a class header comment!
 *
 * @author jlbe
 * @since ${VERSION}
 */
@Layout(R.layout.section_child_view)
public class SectionChildScreen extends Path{
    public final String childName;
    public SectionChildScreen(final String name) {
        this.childName = String.format("Child of %s",name);
    }
}
