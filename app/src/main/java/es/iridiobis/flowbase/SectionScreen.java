package es.iridiobis.flowbase;

import flow.path.Path;

/**
 * TODO: Add a class header comment!
 *
 * @author jlbe
 * @since ${VERSION}
 */
@Layout(R.layout.section_view)
public class SectionScreen extends Path{
    public final String sectionName;
    public SectionScreen(final String name) {
        this.sectionName = name;
    }
}
