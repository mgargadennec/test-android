package fr.billetel.bolotusandroid.modules.home;

import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;

import fr.billetel.bolotusandroid.R;
import fr.billetel.bolotusandroid.api.HttpGetTask;
import fr.billetel.bolotusandroid.api.request.ApiFollowLinkRequest;
import fr.billetel.bolotusandroid.api.request.ApiRootRequest;

public class MainActivity extends AppCompatActivity
  implements NavigationView.OnNavigationItemSelectedListener
  , HomeFragment.OnFragmentInteractionListener,
  HttpGetTask.HttpGetTaskDelegate<Resource> {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Toolbar toolbar = configureToolbar();
    DrawerLayout drawerLayout = configureDrawer(toolbar);
    NavigationView navigationView = configureNavigationView();

//    HttpGetTask<Void, Resource> task = new HttpGetTask<Void, Resource>(this, this, Resource.class);
//    task.execute(new ApiRootRequest());
  }

  private Toolbar configureToolbar() {
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    return toolbar;
  }

  private DrawerLayout configureDrawer(Toolbar toolbar) {

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
      this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    drawer.setDrawerListener(toggle);
    toggle.syncState();

    return drawer;

  }

  private NavigationView configureNavigationView() {
    NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
    navigationView.setNavigationItemSelectedListener(this);

    Menu menu = navigationView.getMenu();
    menu.findItem(R.id.nav_home).setIcon(
      new IconDrawable(this, FontAwesomeIcons.fa_home)
        .colorRes(R.color.colorPrimary)
        .actionBarSize());

    //Administration
    menu.findItem(R.id.nav_user).setIcon(
      new IconDrawable(this, FontAwesomeIcons.fa_user)
        .colorRes(R.color.colorPrimary)
        .actionBarSize());
    menu.findItem(R.id.nav_groups).setIcon(
      new IconDrawable(this, FontAwesomeIcons.fa_group)
        .colorRes(R.color.colorPrimary)
        .actionBarSize());
    menu.findItem(R.id.nav_roles).setIcon(
      new IconDrawable(this, FontAwesomeIcons.fa_key)
        .colorRes(R.color.colorPrimary)
        .actionBarSize());

    //Webmastering
    menu.findItem(R.id.nav_templates).setIcon(
      new IconDrawable(this, FontAwesomeIcons.fa_th)
        .colorRes(R.color.colorPrimary)
        .actionBarSize());
    menu.findItem(R.id.nav_themes).setIcon(
      new IconDrawable(this, FontAwesomeIcons.fa_paint_brush)
        .colorRes(R.color.colorPrimary)
        .actionBarSize());

    //Multisite
    menu.findItem(R.id.nav_websites).setIcon(
      new IconDrawable(this, FontAwesomeIcons.fa_sitemap)
        .colorRes(R.color.colorPrimary)
        .actionBarSize());
    menu.findItem(R.id.nav_pages).setIcon(
      new IconDrawable(this, FontAwesomeIcons.fa_file)
        .colorRes(R.color.colorPrimary)
        .actionBarSize());

    //Content
    menu.findItem(R.id.nav_widgets).setIcon(
      new IconDrawable(this, FontAwesomeIcons.fa_th_large)
        .colorRes(R.color.colorPrimary)
        .actionBarSize());
    menu.findItem(R.id.nav_datasources).setIcon(
      new IconDrawable(this, FontAwesomeIcons.fa_database)
        .colorRes(R.color.colorPrimary)
        .actionBarSize());

    return navigationView;
  }

  @Override
  public void onBackPressed() {
    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    if (drawer.isDrawerOpen(GravityCompat.START)) {
      drawer.closeDrawer(GravityCompat.START);
    } else {
      super.onBackPressed();
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

  @SuppressWarnings("StatementWithEmptyBody")
  @Override
  public boolean onNavigationItemSelected(MenuItem item) {
    int id = item.getItemId();

    if (id == R.id.nav_home) {
      FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
      fragmentTransaction.add(R.id.fragment_container, new HomeFragment());
      fragmentTransaction.commit();
    } else if (id == R.id.nav_user) {
    }

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    drawer.closeDrawer(GravityCompat.START);
    return true;
  }

  @Override
  public void onFragmentInteraction(Uri uri) {
    Log.i(this.getClass().toString(), uri.toString());
  }

  @Override
  public void onHttpTaskSuccess(Resource resource) {
    Log.i(this.getClass().toString(), "Resource success !" + resource.toString()+" / " +resource.getContent()+" / "+resource.getId());

    HttpGetTask<Void,PagedResources> userTask = new HttpGetTask<Void, PagedResources>(this, this, PagedResources.class);
    userTask.execute(new ApiFollowLinkRequest(resource,"pagedUsers"));
  }

  @Override
  public void onHttpTaskError() {
    Log.i(this.getClass().toString(), "Resource error !");

  }

  @Override
  public void onHttpTaskCancelled() {
    Log.i(this.getClass().toString(), "Resource cancelled !");

  }
}
