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
public class SectionView extends RelativeLayout {

    @InjectView(R.id.section_name) TextView sectionName;
    private String name;
    public SectionView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        SectionScreen screen = Path.get(context);
        name = screen.sectionName;
    }

    @Override protected void onFinishInflate() {
        super.onFinishInflate();

        ButterKnife.inject(this);

        sectionName.setText(name);
    }

    @OnClick(R.id.section_to_child)
    public void goToChild() {
        Flow.get(this).set(new SectionChildScreen(name));
    }
}
