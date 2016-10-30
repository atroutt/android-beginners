package com.audreytroutt.androidbeginners.firstapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.res.ResourcesCompat;
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

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final String TAG = "MainActivity";

    private DrawerLayout mDrawer;
    private Uri androidBeginnerImageUri;
    private FloatingActionButton fab;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up Google Auth and request basic user profile data and email
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

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
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            View headerView = navigationView.getHeaderView(0);
            TextView currentUserEmail = (TextView) headerView.findViewById(R.id.current_user_email);
            currentUserEmail.setText(user.getEmail());
            TextView currentUserName = (TextView) headerView.findViewById(R.id.current_user_name);
            currentUserName.setText(user.getDisplayName());
            ImageView currentUserImage = (ImageView) headerView.findViewById(R.id.current_user_photo);
            Picasso.with(this).load(user.getPhotoUrl()).into(currentUserImage);
        }
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
            // TODO Implement sign out
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            if (status.isSuccess()) {
                                Intent loginScreenIntent = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(loginScreenIntent);
                            }
                        }
                    });
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
            // TODO create an intent for the MediaStore.ACTION_IMAGE_CAPTURE
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, getAndroidBeginnerImageUri()); // set the image file name
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        } else if (id == R.id.nav_list) {
            // TODO create an intent for the PaintingListActivity
            Intent listIntent = new Intent(this, PaintingListActivity.class);
            startActivity(listIntent);
        } else if (id == R.id.nav_grid) {
            // TODO create an intent for the PaintingGridActivity
            Intent listIntent = new Intent(this, PaintingGridActivity.class);
            startActivity(listIntent);
        } else if (id == R.id.nav_web) {
            // TODO create an intent to open a url
            Uri webpage = Uri.parse("http://audreytroutt.com/android-beginners/");
            Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        } else if (id == R.id.nav_share) {
            // TODO create an intent to social share about this app
            shareAction();
        } else if (id == R.id.nav_send) {
            // TODO create an intent to send an email
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult.getErrorMessage());
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
        int minDimension = (int)Math.min(bmp.getWidth(), bmp.getHeight());
        int cropWidthX = (int)Math.max(0, (int)(bmp.getWidth() / 2) - (int)(minDimension / 2));
        int cropHeightY = (int)Math.max(0, (int)(bmp.getHeight() / 2) - (int)(minDimension / 2));
        Bitmap cropped = Bitmap.createBitmap(bmp, cropWidthX, cropHeightY, minDimension, minDimension);

        // TODO Draw text on the cropped image
        Canvas canvas = new Canvas(cropped);
        Paint paint = new Paint();
        paint.setColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimaryDark, null));
        final int textSize = minDimension / 10; // I want the text to be about 1/10 as tall as the image
        paint.setTextSize(textSize);
        // X is the horizontal position of the text, relative to the left side
        final int textXPosition = textSize; // it works out to start the text about 1/10 of the way into the image
        // Y is the vertical position of the text, measured as how far the BOTTOM of the text is from the top of the image.
        final int textYPosition = minDimension - (textSize / 2); // I want the text to be a little above the bottom of the image
        canvas.drawText(getString(R.string.android_developer_image_label), textXPosition, textYPosition, paint);

        // Save the edited image back to the file
        saveBitmapToFile(cropped);
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
