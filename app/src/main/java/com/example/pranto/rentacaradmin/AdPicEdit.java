package com.example.pranto.rentacaradmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class AdPicEdit extends AppCompatActivity {
    private String id = "", url = "", title = "";
    LinearLayout upload;
    ImageView pic;
    EditText ad_title;
    TextView sub, ad_list;
    private Bitmap bitmap;
    private Uri filePath;
    //public static final String UPLOAD_URL = "http://192.168.1.133/PictureBlood/slider_img_edit.php";
    public static final String UPLOAD_URL = "http://bhromondata.banijyadarpan.com/rentacar/slider_img_edit.php";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ad_upload);

        id = getIntent().getStringExtra("id");
        url = getIntent().getStringExtra("url");
        title = getIntent().getStringExtra("title");
        pic = (ImageView) findViewById(R.id.pic);

        if (url.equals("")) {
            Uri uri = Uri.parse("android.resource://com.example.pranto.rentacaradmin/drawable/bhromon_logo");
            if (uri != null) {

                bitmap = decodeUri(uri, 100);
            }
        }
        if (!url.equals("")) {
            Picasso.with(AdPicEdit.this).load(url).into(pic);
            //Glide.with(MyProfile.this).load(picture).into(profile_pic);
            try {
                URL urls = new URL(url);
                HttpURLConnection connection = null;
                connection = (HttpURLConnection) urls.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(input);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 2);
            }
        });

        ad_title = (EditText) findViewById(R.id.ad_title);
        ad_title.setText(title);
        sub = (TextView) findViewById(R.id.sub);
        upload = (LinearLayout) findViewById(R.id.upload);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ad_tit = ad_title.getText().toString();
                if (ad_tit.equals("")) {
                    sub.setVisibility(View.VISIBLE);
                } else {
                    sub.setVisibility(View.GONE);
                    uploadPic();
                }
            }
        });

        ad_list = (TextView)findViewById(R.id.ad_list);
        LinearLayout ads = (LinearLayout) findViewById(R.id.ads);
        ads.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 2:
                if (resultCode == RESULT_OK) {
                    filePath = data.getData();

                    if (filePath != null) {

                        bitmap = decodeUri(filePath, 100);
                        /*try {
                            bp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), choosenImage);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }*/
                        pic.setImageBitmap(bitmap);
                    }
                    //super.onActivityResult(requestCode, resultCode, data);
                }
        }
    }

    protected Bitmap decodeUri(Uri selectedImage, int REQUIRED_SIZE) {

        try {

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);

            // The new size we want to scale to
            // final int REQUIRED_SIZE =  size;

            // Find the correct scale value. It should be the power of 2.
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE
                        || height_tmp / 2 < REQUIRED_SIZE) {
                    break;
                }
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 0, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public void uploadPic() {
        class UploadImage extends AsyncTask<Bitmap, Void, String> {

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(AdPicEdit.this, "Uploading (আপলোড হচ্ছে)...", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                //Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
                /*if (s.contains("Success") || s.contains("success") || s.contains("ok") || s.contains("OK") || s.contains("Ok")) {
                    sub.setVisibility(View.VISIBLE);
                    sub.setText("Successfully uploaded (সফলভাবে আপলোড হয়েছে)");
                    sub.setTextColor(Color.parseColor("#006400"));
                    Toast.makeText(AdUpload.this, "Successfully uploaded (সফলভাবে আপলোড করা হয়েছে)", Toast.LENGTH_LONG).show();
                    finish();
                }*/
                if (s.contains("সফলভাবে আপলোড করা হয়েছে") || s.contains("সফলভাবে আপডেট করা হয়েছে")) {
                    sub.setVisibility(View.VISIBLE);
                    sub.setText("Successfully uploaded (সফলভাবে আপলোড হয়েছে)\nIt may take few minutes to change the data. Please wait. (ডাটা বদল হতে কিছুসময় লাগতে পারে। অনুগ্রহকরে অপেক্ষা করুন");
                    sub.setTextColor(Color.parseColor("#006400"));
                    //Toast.makeText(AdUpload.this, "Successfully uploaded (সফলভাবে আপলোড করা হয়েছে)", Toast.LENGTH_LONG).show();
                } else {
                    sub.setVisibility(View.VISIBLE);
                    sub.setText(s + "");
                    sub.setTextColor(Color.parseColor("#fd0001"));
                }
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                Bitmap bitmap = params[0];
                String uploadImage = getStringImage(bitmap);
                //Toast.makeText(PicChange.this, uploadImage, Toast.LENGTH_SHORT).show();
                String ad_tit = ad_title.getText().toString();
                String ad_id = id;
                if(ad_tit.contains(" ")){
                    ad_tit = ad_tit.replace(" ", "%20");
                }
                HashMap<String, String> data = new HashMap<>();

                data.put("id", ad_id);
                data.put("images", uploadImage);
                data.put("title", ad_tit);
                //data.put("image", uploadImage);
                //data.put("userid", userid);
                String result = rh.sendPostRequest(UPLOAD_URL, data);

                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute(bitmap);
    }

}
