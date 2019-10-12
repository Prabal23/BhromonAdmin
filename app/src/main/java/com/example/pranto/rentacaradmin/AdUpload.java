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
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class AdUpload extends AppCompatActivity {
    LinearLayout upload;
    ImageView pic;
    EditText title;
    TextView sub, ad_list;
    private Bitmap bitmap;
    private Uri filePath;
    //public static final String UPLOAD_URL = "http://192.168.1.133/PictureBlood/slider_img_upload.php";
    public static final String UPLOAD_URL = "http://bhromondata.banijyadarpan.com/rentacar/slider_img_upload.php";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ad_upload);

        Uri uri = Uri.parse("android.resource://com.example.pranto.rentacaradmin/drawable/bhromon_logo");
        if (uri != null) {

            bitmap = decodeUri(uri, 100);
        }

        pic = (ImageView) findViewById(R.id.pic);
        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 2);
            }
        });

        title = (EditText) findViewById(R.id.ad_title);
        sub = (TextView) findViewById(R.id.sub);
        upload = (LinearLayout) findViewById(R.id.upload);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ad_title = title.getText().toString();
                if (ad_title.equals("")) {
                    sub.setVisibility(View.VISIBLE);
                } else {
                    sub.setVisibility(View.GONE);
                    uploadPic();
                }
            }
        });

        ad_list = (TextView)findViewById(R.id.ad_list);
        LinearLayout ads = (LinearLayout) findViewById(R.id.ads);
        ads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), AdList.class);
                startActivity(intent);
            }
        });
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
                loading = ProgressDialog.show(AdUpload.this, "Uploading (আপলোড হচ্ছে)...", null, true, true);
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
                    sub.setText("Successfully uploaded (সফলভাবে আপলোড হয়েছে)");
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
                String ad_title = title.getText().toString();
                if(ad_title.contains(" ")){
                    ad_title = ad_title.replace(" ", "%20");
                }
                HashMap<String, String> data = new HashMap<>();

                data.put("images", uploadImage);
                data.put("title", ad_title);
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
