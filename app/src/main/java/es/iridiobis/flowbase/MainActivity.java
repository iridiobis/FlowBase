package es.iridiobis.flowbase;

import com.google.gson.Gson;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.InjectView;
import flow.Flow;
import flow.FlowDelegate;
import flow.History;
import flow.path.Path;
import flow.path.PathContainerView;
import hugo.weaving.DebugLog;

import static flow.Flow.Direction.FORWARD;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener, Flow.Dispatcher {

    private FlowDelegate flowSupport;

    @InjectView(R.id.navigation) NavigationView navigationDrawer;
    @InjectView(R.id.container) FramePathContainerView container;
    @InjectView(R.id.toolbar) Toolbar toolbar;
    ActionBarDrawerToggle mDrawerToggle;
    private int selected;
    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        if ( savedInstanceState != null) {
            selected = savedInstanceState.getInt("Selected");
            navigationDrawer.getMenu().findItem(selected).setChecked(true);
        }
        mTitle = getTitle();
        setSupportActionBar(toolbar);
        setTitle(mTitle);
        selected = navigationDrawer.getMenu().getItem(0).getItemId();
        navigationDrawer.setNavigationItemSelectedListener(this);
        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,  mDrawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );
        mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override public void onClick(final View v) {
                onBackPressed();
            }
        });
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerToggle.syncState();

        @SuppressWarnings("deprecation") FlowDelegate.NonConfigurationInstance nonConfig =
                (FlowDelegate.NonConfigurationInstance) getLastCustomNonConfigurationInstance();
        GsonParceler parceler = new GsonParceler(new Gson());
        flowSupport = FlowDelegate.onCreate(nonConfig, getIntent(), savedInstanceState, parceler,
                History.single(new SectionScreen(getString(R.string.title_section1))), this);
    }

    @Override protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        flowSupport.onNewIntent(intent);
    }

    @Override protected void onResume() {
        super.onResume();
        flowSupport.onResume();
    }

    @Override protected void onPause() {
        flowSupport.onPause();
        super.onPause();
    }

    @DebugLog @Override public Object onRetainCustomNonConfigurationInstance () {
        return flowSupport.onRetainNonConfigurationInstance();
    }

    @Override public Object getSystemService(String name) {
        Object service = null;
        if (flowSupport != null) {
            service = flowSupport.getSystemService(name);
        }
        return service != null ? service : super.getSystemService(name);
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("Selected", selected);
        super.onSaveInstanceState(outState);
        flowSupport.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override public boolean onNavigationItemSelected(final MenuItem menuItem) {
        Flow.get(this).setHistory(History.emptyBuilder()
                .push(new SectionScreen(menuItem.getTitle().toString()))
                .build(), FORWARD);
        menuItem.setChecked(true);
        selected = menuItem.getItemId();
        return true;
    }

    @Override public void onBackPressed() {
        if (container.onBackPressed()) return;
        if (flowSupport.onBackPressed()) return;
        super.onBackPressed();
    }

    @Override
    public void dispatch(final Flow.Traversal traversal, final Flow.TraversalCallback callback) {
        Path path = traversal.destination.top();
        setTitle(path.getClass().getSimpleName());
        boolean canGoBack = traversal.destination.size() > 1;
        mDrawerToggle.setHomeAsUpIndicator(canGoBack ?R.drawable.abc_ic_ab_back_mtrl_am_alpha:0);
        mDrawerToggle.setDrawerIndicatorEnabled(!canGoBack);
        container.dispatch(traversal, new Flow.TraversalCallback() {
            @Override public void onTraversalCompleted() {
                invalidateOptionsMenu();
                callback.onTraversalCompleted();
            }
        });
    }
}
