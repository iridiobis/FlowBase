package es.iridiobis.flowbase;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import flow.Flow;
import flow.path.Path;

/**
 * TODO: Add a class header comment!
 *
 * @author jlbe
 * @since ${VERSION}
 */
public class SectionChildView extends RelativeLayout {

    @InjectView(R.id.section_child_name) TextView childName;
    private String name;
    public SectionChildView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        SectionChildScreen screen = Path.get(context);
        name = screen.childName;
    }

    @Override protected void onFinishInflate() {
        super.onFinishInflate();

        ButterKnife.inject(this);

        childName.setText(name);
    }
}
