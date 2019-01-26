package jmapps.fortressofthemuslim;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Objects;

import jmapps.fortressofthemuslim.Adapter.ListAppsAdapter;
import jmapps.fortressofthemuslim.Adapter.MainTabsPager;
import jmapps.fortressofthemuslim.DBSetup.DBAssetHelper;
import jmapps.fortressofthemuslim.Download.DownloadsAudio;
import jmapps.fortressofthemuslim.Fragment.BookmarkChapters;
import jmapps.fortressofthemuslim.Fragment.MainChapters;
import jmapps.fortressofthemuslim.Fragment.MainItemsAll;
import jmapps.fortressofthemuslim.Fragment.MainItemsBookmark;
import jmapps.fortressofthemuslim.Permission.GetPermission;

import static jmapps.fortressofthemuslim.MainApplication.keyNightMode;
import static jmapps.fortressofthemuslim.Model.ListAppM.listAppM;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener {

    public static final int PERMISSION_REQUEST_CODE = 1;

    private DBAssetHelper dbAssetHelper;

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    private DrawerLayout drawer;
    private MenuItem itemNightMode;
    private TabLayout mainTabLayout;

    private final int[] tabIcons = {
            R.drawable.state_tab_one_selected,
            R.drawable.state_tab_two_selected,
            R.drawable.state_tab_three_selected,
            R.drawable.state_tab_four_selected,
    };

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activateNightMode(MainApplication.getSingleton().isNightModeEnabled());
        setContentView(R.layout.activity_main);

        dbAssetHelper = new DBAssetHelper(this);
        dbAssetHelper.getReadableDatabase();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPreferences.edit();

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        ViewPager mainViewPager = findViewById(R.id.main_view_pager);
        mainViewPager.setOffscreenPageLimit(1);
        setupViewPager(mainViewPager);

        mainTabLayout = findViewById(R.id.main_tab_layout);
        mainTabLayout.setupWithViewPager(mainViewPager);
        setupTabIcon();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mainViewPager.addOnPageChangeListener(this);
        mainViewPager.setOffscreenPageLimit(4);

        new LockOrientation(this).lock();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        itemNightMode = menu.findItem(R.id.action_night_mode);
        boolean lastStateNightModeItem = mPreferences.getBoolean(keyNightMode, false);
        itemNightMode.setChecked(lastStateNightModeItem);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean currentNightModeState = !item.isChecked();
        switch (item.getItemId()) {
            case R.id.action_night_mode:
                if (currentNightModeState) {
                    setEnabledNightMode(true);
                } else {
                    setEnabledNightMode(false);
                }
                break;
            case R.id.action_exit:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_settings:
                break;
            case R.id.nav_list_apps:
                listApps();
                break;
            case R.id.nav_about_us:
                aboutUsDialog();
                break;
            case R.id.nav_download_all_audio:
                GetPermission getPermission = new GetPermission(this);
                if (Build.VERSION.SDK_INT >= 23) {
                    if (getPermission.checkedPermission()) {
                        downloadAllAudio();
                    } else {
                        getPermission.requestPermission();
                    }
                } else {
                    downloadAllAudio();
                }
                break;
            case R.id.nav_rate_app:
                rateApp();
                break;
            case R.id.nav_share:
                shareAppLink();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                this.setTitle(R.string.action_chapter_list);
                break;
            case 1:
                this.setTitle(R.string.action_bookmark_list);
                break;
            case 2:
                this.setTitle(R.string.action_dua_list);
                break;
            case 3:
                this.setTitle(R.string.action_bookmark_dua_list);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbAssetHelper.close();
    }

    private void activateNightMode(boolean nightModeState) {
        if (nightModeState) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    private void setEnabledNightMode(boolean enabled) {
        itemNightMode.setChecked(enabled);
        MainApplication.getSingleton().setNightModeEnabled(enabled);
        recreateActivity();
    }

    private void recreateActivity() {
        Intent recreate = new Intent(MainActivity.this, MainActivity.class);
        startActivity(recreate);
        finish();
    }

    private void setupViewPager(ViewPager mainViewPager) {
        MainTabsPager mainTabsPager = new MainTabsPager(getSupportFragmentManager());
        mainTabsPager.addFragment(new MainChapters());
        mainTabsPager.addFragment(new BookmarkChapters());
        mainTabsPager.addFragment(new MainItemsAll());
        mainTabsPager.addFragment(new MainItemsBookmark());
        mainViewPager.setAdapter(mainTabsPager);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setupTabIcon() {
        Objects.requireNonNull(mainTabLayout.getTabAt(0)).setIcon(tabIcons[0]);
        Objects.requireNonNull(mainTabLayout.getTabAt(1)).setIcon(tabIcons[1]);
        Objects.requireNonNull(mainTabLayout.getTabAt(2)).setIcon(tabIcons[2]);
        Objects.requireNonNull(mainTabLayout.getTabAt(3)).setIcon(tabIcons[3]);
    }

    @SuppressLint("InflateParams")
    private void listApps() {
        BottomSheetDialog dialogListApp = new BottomSheetDialog(this, R.style.BottomSheetDialog);
        View rootListApp = getLayoutInflater().inflate(R.layout.bottom_sheet_list_apps, null);
        dialogListApp.setContentView(rootListApp);

        RecyclerView rvListApps = rootListApp.findViewById(R.id.rv_list_apps);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvListApps.setLayoutManager(linearLayoutManager);
        rvListApps.setHasFixedSize(true);

        ListAppsAdapter listAppsAdapter = new ListAppsAdapter(Arrays.asList(listAppM));
        rvListApps.setAdapter(listAppsAdapter);

        dialogListApp.show();
    }

    private void aboutUsDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        @SuppressLint("InflateParams")
        View dialogAboutUs = inflater.inflate(R.layout.dialog_about_us, null);

        AlertDialog.Builder instructionDialog = new AlertDialog.Builder(this);

        instructionDialog.setView(dialogAboutUs);
        TextView tvAboutUsContent = dialogAboutUs.findViewById(R.id.tv_about_us_content);
        tvAboutUsContent.setMovementMethod(LinkMovementMethod.getInstance());

        instructionDialog.setPositiveButton("Ок", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        instructionDialog.create().show();
    }

    private void downloadAllAudio() {
        AlertDialog.Builder downloadAll = new AlertDialog.Builder(this);
        downloadAll.setIcon(R.drawable.ic_download_item)
                .setTitle("Предупреждение")
                .setMessage(R.string.download_all_instruction)
                .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Скачать", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DownloadsAudio downloadsAudio = new DownloadsAudio(MainActivity.this);
                        downloadsAudio.downloadAllAudios();
                    }
                });
        downloadAll.create().show();

    }

    private void rateApp() {
        Intent rateApp = new Intent(Intent.ACTION_VIEW);
        rateApp.setData(Uri.parse("https://play.google.com/store/apps/details?id=jmapps.fortressofthemuslim"));
        startActivity(rateApp);
    }

    private void shareAppLink() {
        String strAppLink = "https://play.google.com/store/apps/details?id=jmapps.fortressofthemuslim";
        Intent shareLink = new Intent(Intent.ACTION_SEND);
        shareLink.setType("text/plain");
        shareLink.putExtra(Intent.EXTRA_TEXT,
                "Серия бесплатных андроид-приложений для мусульман:\n" +
                        "Крепость мусульманина\n" + strAppLink);
        startActivity(shareLink);
    }
}