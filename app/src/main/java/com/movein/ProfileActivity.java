package com.movein;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import adapter.ProfileViewPagerAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import model.User;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rest.ApiClient;
import rest.services.UserInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity implements DialogInterface.OnDismissListener {

    @BindView(R.id.profile_cover)
    ImageView profileCover;
    @BindView(R.id.profile_image)
    CircleImageView profileImage;
    @BindView(R.id.profile_option_btn)
    Button profileOptionBtn;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.ViewPager_profile)
    ViewPager ViewPagerProfile;

    ProfileViewPagerAdapter profileViewPagerAdapter;

      /*

    0 = profile is still loading
    1=  two people are friends ( unfriend )
    2 = this person has sent friend request to another friend ( cancel sent requeset )
    3 = this person has received friend request from another friend  (  reject or accept request )
    4 = people are unkown ( you can send requeset )
    5 = own profile
     */

    int current_state = 0;
    String profileUrl = "", coverUrl = "";

    ProgressDialog progressDialog;
    int imageUploadType = 0;

    String uid = "0";
    File compressedImageFile;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //For hiding status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_profile);

        uid = getIntent().getStringExtra("uid");


        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Se incarca...");
        progressDialog.show();


        ButterKnife.bind(this);



        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.arrow_back_white);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
            }
        });


        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equalsIgnoreCase(uid)) {
            // UID is matched , we are going to load our own profile
            current_state = 5;
            profileOptionBtn.setText("Editeaza profilul");
            loadProfile();
        } else {

            otherOthersProfile();
            // load others profile here
        }
        profileOptionBtn.setOnClickListener(v -> {
            profileOptionBtn.setEnabled(false);
            if (current_state == 5) {
                CharSequence[] options = new CharSequence[]{"Schimba poza de coperta", "Schimba poza de profil", "Vezi poza de coperta", "Vezi poza de profil"};
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                builder.setOnDismissListener(ProfileActivity.this);
                builder.setTitle("Alege o optiune");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        if (position == 0) {
                            imageUploadType = 1;
                            ImagePicker.create(ProfileActivity.this)
                                    .folderMode(true)
                                    .single()
                                    .toolbarFolderTitle("Alege un fisier")
                                    .toolbarImageTitle("Selecteaza o imagine")
                                    .start();
                            //Change cover part
                        } else if (position == 1) {
                            imageUploadType = 0;
                            ImagePicker.create(ProfileActivity.this)
                                    .folderMode(true)
                                    .single().toolbarFolderTitle("Alege un fisier").toolbarImageTitle("Selecteaza o imagine")
                                    .start();
                            //Change  profile part
                        } else if (position == 2) {
                            viewFullImage(profileCover, coverUrl);
                            //view cover proifle
                        } else {
                            viewFullImage(profileImage, profileUrl);
                            //view profile picture
                        }
                    }
                });
                builder.show();
            } else if (current_state == 4) {
                profileOptionBtn.setText("Se incarca...");
                CharSequence[] options = new CharSequence[]{"Trimite cerere"};
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                builder.setOnDismissListener(ProfileActivity.this);
                builder.setTitle("Alege o optiune");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        if (position == 0) {
                           performAction(current_state);
                        }
                    }
                });
                builder.show();
            }else if(current_state==2){
                profileOptionBtn.setText("Se incarca...");
                CharSequence[] options = new CharSequence[]{"Anuleaza cerere"};
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                builder.setOnDismissListener(ProfileActivity.this);
                builder.setTitle("Alege o optiune");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        if (position == 0) {
                            performAction(current_state);
                        }
                    }
                });
                builder.show();
            }else if(current_state==3){
                profileOptionBtn.setText("Se incarca...");
                CharSequence[] options = new CharSequence[]{"Accepta cererea"};
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                builder.setOnDismissListener(ProfileActivity.this);
                builder.setTitle("Alege o optiune");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        if (position == 0) {
                            performAction(current_state);
                        }
                    }
                });
                builder.show();
            }else if(current_state ==1){
                profileOptionBtn.setText("Se incarca...");
                CharSequence[] options = new CharSequence[]{"Sterge conctact"};
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                builder.setOnDismissListener(ProfileActivity.this);
                builder.setTitle("Alege o optiune");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        if (position == 0) {
                            performAction(current_state);
                        }
                    }
                });
                builder.show();
            }
        });
    }

    private void performAction(final int i) {
        UserInterface userInterface = ApiClient.getApiClient().create(UserInterface.class);
        Call<Integer> call = userInterface.performAction(new PerformAction(i + "", FirebaseAuth.getInstance().getCurrentUser().getUid(), uid));
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                profileOptionBtn.setEnabled(true);
                if (response.body() == 1) {
                    if (i == 4) {
                        current_state = 2;
                        profileOptionBtn.setText("Request Sent");
                        Toast.makeText(ProfileActivity.this, "Cererea a fost trimisa!", Toast.LENGTH_SHORT).show();
                    }else if(i==2){
                        current_state = 4;
                        profileOptionBtn.setText("Send Request");
                        Toast.makeText(ProfileActivity.this, "Cererea a fost anulata!", Toast.LENGTH_SHORT).show();
                    }else if(i==3){
                        current_state = 1;
                        profileOptionBtn.setText("Friends");
                        Toast.makeText(ProfileActivity.this, "Persoana a fost adaugata la contacte!", Toast.LENGTH_SHORT).show();
                    }else if(i==1){
                        current_state =4;
                        profileOptionBtn.setText("Send Request");
                        Toast.makeText(ProfileActivity.this, "Persoana nu se mai afla in lista de contacte!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    profileOptionBtn.setEnabled(false);
                    profileOptionBtn.setText("Eroare...");
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });

    }


    private void otherOthersProfile() {
        UserInterface userInterface = ApiClient.getApiClient().create(UserInterface.class);

        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", FirebaseAuth.getInstance().getCurrentUser().getUid());
        params.put("profileId", uid);

        Call<User> call = userInterface.loadOtherProfile(params);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull final Response<User> response) {
                progressDialog.dismiss();
                if (response.body() != null) {
                    showUserData(response.body());

                    if (response.body().getState().equalsIgnoreCase("1")) {
                        profileOptionBtn.setText("Contacte");
                        current_state = 1;
                    } else if (response.body().getState().equalsIgnoreCase("2")) {
                        profileOptionBtn.setText("Anuleaza cerere");
                        current_state = 2;
                    } else if (response.body().getState().equalsIgnoreCase("3")) {
                        current_state = 3;
                        profileOptionBtn.setText("Accepta cerere");
                    } else if (response.body().getState().equalsIgnoreCase("4")) {
                        current_state = 4;
                        profileOptionBtn.setText("Trimite cerere");
                    } else {
                        current_state = 0;
                        profileOptionBtn.setText("Eroare");
                    }

                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ProfileActivity.this, "A aparut o eroare! Incearca din nou", Toast.LENGTH_SHORT).show();
            }
        });
        // Toast.makeText(Pro
    }

    private void showUserData(User user) {
        profileViewPagerAdapter = new ProfileViewPagerAdapter(getSupportFragmentManager(), 1, user.getUid(), user.getState());
        ViewPagerProfile.setAdapter(profileViewPagerAdapter);

        profileUrl = user.getProfileUrl();
        coverUrl = user.getCoverUrl();
        collapsingToolbar.setTitle(user.getName());
        if (!profileUrl.isEmpty()) {
            Picasso.with(ProfileActivity.this).load(profileUrl).into(profileImage, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(ProfileActivity.this).load(profileUrl).into(profileImage);
                }
            });

            if (!coverUrl.isEmpty()) {
                Picasso.with(ProfileActivity.this).load(coverUrl).into(profileCover, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(ProfileActivity.this).load(coverUrl).into(profileCover);
                    }
                });
            }

            addImageCoverClick();
        }
    }

    private void viewFullImage(View view, String link) {
        Intent intent = new Intent(ProfileActivity.this, FullImageActivity.class);
        intent.putExtra("imageUrl", link);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Pair[] pairs = new Pair[1];
            pairs[0] = new Pair<View, String>(view, "shared");
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(ProfileActivity.this, pairs);
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }

    }


    private void loadProfile() {
        UserInterface userInterface = ApiClient.getApiClient().create(UserInterface.class);
        Map<String, String> params = new HashMap<>();
        params.put("userId", FirebaseAuth.getInstance().getCurrentUser().getUid());
        Call<User> call = userInterface.loadownProfile(params);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                progressDialog.dismiss();
                if (response.body() != null) {
                    showUserData(response.body());
                } else {
                    Toast.makeText(ProfileActivity.this, "A aparut o eroare! Incearca din nou", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ProfileActivity.this, "A aparut o eroare! Incearca din nou", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void addImageCoverClick() {
        profileCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewFullImage(profileCover, coverUrl);
            }
        });
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewFullImage(profileImage, profileUrl);
            }
        });
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        profileOptionBtn.setEnabled(true);

    }

    @Override
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            Image selectedImage = ImagePicker.getFirstImageOrNull(data);

            try {
                compressedImageFile = new Compressor(this)
                        .setQuality(75)
                        .compressToFile(new File(selectedImage.getPath()));


                uploadFile(compressedImageFile);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadFile(final File compressedImageFile) {

        progressDialog.setTitle("Se incarca...");
        progressDialog.show();

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("postUserId", FirebaseAuth.getInstance().getCurrentUser().getUid());
        builder.addFormDataPart("imageUploadType", imageUploadType + "");
        builder.addFormDataPart("file", compressedImageFile.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), compressedImageFile));

        MultipartBody multipartBody = builder.build();

        UserInterface userInterface = ApiClient.getApiClient().create(UserInterface.class);
        Call<Integer> call = userInterface.uploadImage(multipartBody);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                progressDialog.dismiss();
                if (response.body() != null && response.body() == 1) {
                    if (imageUploadType == 0) {
                        Picasso.with(ProfileActivity.this).load(compressedImageFile).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.default_image_placeholder).into(profileImage, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
                                Picasso.with(ProfileActivity.this).load(compressedImageFile).placeholder(R.drawable.default_image_placeholder).into(profileImage);
                            }
                        });
                        Toast.makeText(ProfileActivity.this, "Poza de profil a fost schimbata!", Toast.LENGTH_LONG).show();
                    } else {
                        Picasso.with(ProfileActivity.this).load(compressedImageFile).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.default_image_placeholder).into(profileCover, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
                                Picasso.with(ProfileActivity.this).load(compressedImageFile).placeholder(R.drawable.default_image_placeholder).into(profileCover);
                            }
                        });
                        Toast.makeText(ProfileActivity.this, "Poza de coperta a fost schimbata", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(ProfileActivity.this, "A aparut o eroare! Incearca din nou.", Toast.LENGTH_SHORT).show();

                }


            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "A aparut o eroare! Incearca din nou.", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    public static class   PerformAction {
        String operationType, userId, profileid;

        public PerformAction(String operationType, String userId, String profileid) {
            this.operationType = operationType;
            this.userId = userId;
            this.profileid = profileid;
        }
    }
}






