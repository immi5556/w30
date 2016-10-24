package within30.com;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import within30.com.sidemenu.interfaces.Resourceble;
import within30.com.sidemenu.interfaces.ScreenShotable;


public class AppMenuActivity extends BaseActivity implements View.OnClickListener {
    LinearLayout homeLayout;

    public void initialize(){

        homeLayout = (LinearLayout) inflater.inflate(R.layout.app_menu, null);
       llBody.addView(homeLayout);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        tvTitle.setText("Menu");
        intilizeControls();

    }


    @Override
    public void loadData() {

    }



    private  void intilizeControls() {

    }




    @Override
    public void onClick(View v) {


    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        menu.findItem(R.id.menu_reset).setVisible(false);
        menu.findItem(R.id.menu_home).setVisible(true);

        mSearchCheck = true;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                overridePendingTransition(R.anim.app_menu_out,0);
                return true;

            case R.id.menu_home:
                Intent homeIntent = new Intent(this,LandingActivity.class);
                startActivity(homeIntent);
                overridePendingTransition(R.anim.app_menu_out, 0);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return false;
    }

    @Override
    public ScreenShotable onSwitch(Resourceble slideMenuItem, ScreenShotable screenShotable, int position) {
        return null;
    }

    @Override
    public void disableHomeButton() {

    }

    @Override
    public void enableHomeButton() {

    }

    @Override
    public void addViewToContainer(View view) {

    }
}
