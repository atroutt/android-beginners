package com.audreytroutt.androidbeginners.firstapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final String TAG = "MainActivity";

    private DrawerLayout mDrawer;
    private Uri androidBeginnerImageUri;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);

        if (haveAndroidBeginnerImageLocally()) {
            updateMainImageFromFile();
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (haveAndroidBeginnerImageLocally()) {
                    shareAction();
                } else {
                    // create Intent to take a picture and return control to the calling application
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, getAndroidBeginnerImageUri()); // set the image file name that the camera will save to
                    MainActivity.this.startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                }
            }
        });

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setUserInfoInDrawer();
    }

    private void setUserInfoInDrawer() {
        // TODO Project 3: Get user info and display it at the top of the drawer
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_sign_out) {
            // TODO Project 3: implement sign out
            return true;
        } else if (id == R.id.action_delete_photo) {
            File savedImage = getAndroidBeginnerImageFile();
            if (savedImage.exists()) {
                if (!isStoragePermissionGranted()) {
                    requestWriteExternalStoragePermission();
                }
                savedImage.delete();
                Toast.makeText(this, "Photo deleted", Toast.LENGTH_LONG).show();

                resetMainImageToInitialState();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // TODO Project 2: create an intent for the MediaStore.ACTION_IMAGE_CAPTURE
        } else if (id == R.id.nav_list) {
            // TODO Project 2: create an intent for the PaintingListActivity
        } else if (id == R.id.nav_grid) {
            // TODO Project 2: create an intent for the PaintingGridActivity
        } else if (id == R.id.nav_web) {
            // TODO Project 2: create an intent to open a url
        } else if (id == R.id.nav_share) {
            // TODO Project 2: create an intent to social share about this app
            shareAction();
        } else if (id == R.id.nav_send) {
            // TODO Project 2: create an intent to send an email
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:")); // only email apps should handle this
            intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "gdiandroidbeginners@mailinator.com"});
            intent.putExtra(Intent.EXTRA_SUBJECT, "Testing out my Email Intent -- Success!");
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }

        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void shareAction() {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "I just made my first Android app! #androidbeginner #gdiphilly");
        shareIntent.setType("text/plain");
        if (haveAndroidBeginnerImageLocally()) {
            shareIntent.putExtra(Intent.EXTRA_STREAM, getAndroidBeginnerImageUri());
            shareIntent.setType("*/*");
        }
        startActivity(shareIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            // A picture was just taken, let's display that in our image view
            editImage();
            updateMainImageFromFile();
        }
    }

    // ----------------------------------
    // Image-related methods
    // ----------------------------------

    private Uri getAndroidBeginnerImageUri() {
        if (androidBeginnerImageUri == null) {
            androidBeginnerImageUri = Uri.fromFile(getAndroidBeginnerImageFile());
        }
        return androidBeginnerImageUri;
    }

    private boolean haveAndroidBeginnerImageLocally() {
        return getAndroidBeginnerImageFile().exists();
    }

    /** Create a File for saving an image or video */
    private File getAndroidBeginnerImageFile() {
        File mediaStorageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return new File(mediaStorageDir.getPath(), "androidBeginnersImage.jpg");
    }

    private void updateMainImageFromFile() {
        ImageView imageView = (ImageView)findViewById(R.id.camera_image);
        Bitmap bitmap = BitmapFactory.decodeFile(getAndroidBeginnerImageUri().getPath(), null);
        imageView.setImageBitmap(bitmap);

        ((TextView)findViewById(R.id.welcome_message)).setText(R.string.main_screen_welcom_message_if_image_set);

        // Hide the instructions for taking a photo
        findViewById(R.id.initial_arrow_image).setVisibility(View.INVISIBLE);
        findViewById(R.id.initial_instructions).setVisibility(View.INVISIBLE);

        // Switch the icon on the FAB to share
        fab.setImageResource(R.drawable.ic_share);
    }

    // this is the opposite of updateMainImageFromFile
    private void resetMainImageToInitialState() {
        ImageView imageView = (ImageView)findViewById(R.id.camera_image);
        imageView.setImageBitmap(null);

        ((TextView)findViewById(R.id.welcome_message)).setText(R.string.main_screen_welcome_message_if_no_image);

        // Show the instructions for taking a photo
        findViewById(R.id.initial_arrow_image).setVisibility(View.VISIBLE);
        findViewById(R.id.initial_instructions).setVisibility(View.VISIBLE);

        // Switch the icon on the FAB to camera
        fab.setImageResource(R.drawable.ic_camera_alt);
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {
                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }

    private void requestWriteExternalStoragePermission() {
        int requestCodeIgnoredForNow = 0;
        ActivityCompat.requestPermissions(this, new String[]{ WRITE_EXTERNAL_STORAGE }, requestCodeIgnoredForNow);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // continue editing image now that we have permission
        }
    }

    private void editImage() {
        if (!isStoragePermissionGranted()) {
            requestWriteExternalStoragePermission();
        }

        // Load the image into memory from the file
        Bitmap bmp = BitmapFactory.decodeFile(getAndroidBeginnerImageUri().getPath(), null);

        // Square up the image from the camera
        int croppedImageSize = (int)Math.min(bmp.getWidth(), bmp.getHeight());
        Bitmap cropped = centerCropBitmapToSquareSize(bmp, croppedImageSize);

        // TODO Project 4: Draw text on the cropped image


        // Finally, save the edited image back to the file
        saveBitmapToFile(cropped);
    }

    private Bitmap centerCropBitmapToSquareSize(Bitmap bmp, int cropSize) {
        int cropStartX = (int)Math.max(0, (int)(bmp.getWidth() / 2) - (int)(cropSize / 2));
        int cropStartY = (int)Math.max(0, (int)(bmp.getHeight() / 2) - (int)(cropSize / 2));
        return Bitmap.createBitmap(bmp, cropStartX, cropStartY, cropSize, cropSize);
    }

    private void saveBitmapToFile(Bitmap bitmap) {
        FileOutputStream out = null;
        try {
            // overwrite the file
            out = new FileOutputStream(getAndroidBeginnerImageFile());
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
        } catch (Exception e) {
            Log.e(TAG, "save edited image failed", e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                Log.e(TAG, "close stream failed", e);
            }
        }
    }
}
