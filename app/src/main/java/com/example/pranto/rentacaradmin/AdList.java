package com.example.pranto.rentacaradmin;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import pl.droidsonroids.gif.GifImageView;

public class AdList extends AppCompatActivity {
    ListView listView;
    GifImageView loader;
    private String JSON_STRING;
    String name, price;
    Dialog dialog;
    ImageView calling;
    TextView packnum, packnumbd;
    ArrayList<Ads> arrayList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ad_list);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        packnum = (TextView) findViewById(R.id.packnum);
        packnumbd = (TextView) findViewById(R.id.packnumbd);
        listView = (ListView) findViewById(R.id.listView);
        loader = (GifImageView) findViewById(R.id.loader);
        loader.setVisibility(View.VISIBLE);

        arrayList = new ArrayList<>();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new ReadJSON().execute("http://bhromondata.banijyadarpan.com/rentacar/slider.php");
                //new ReadJSON().execute("http://192.168.1.7/pathshala/getstd.php");
            }
        });
    }

    class ReadJSON extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            return readURL(params[0]);
        }

        @Override
        protected void onPostExecute(String content) {
            try {
                loader.setVisibility(View.GONE);
                JSONObject jsonObject = new JSONObject(content);
                JSONArray jsonArray = jsonObject.getJSONArray("image");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject productObject = jsonArray.getJSONObject(i);
                    arrayList.add(new Ads(
                            productObject.getString("id"),
                            productObject.getString("url"),
                            productObject.getString("title")
                    ));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            CustomListAdapter adapter = new CustomListAdapter(
                    getApplicationContext(), R.layout.ad_list_item, arrayList
            );
            listView.setAdapter(adapter);
            int tot = listView.getAdapter().getCount();
            String s = String.valueOf(tot);
            s = s.replace("0", "০").replace("1", "১").replace("2", "২").replace("3", "৩").replace("4", "৪").replace("5", "৫").replace("6", "৬").replace("7", "৭").replace("8", "৮").replace("9", "৯");
            packnum.setText(tot + " Advertisements");
            packnumbd.setText(" (" + s + "টি বিজ্ঞাপণ)");

            /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    //String quete = arrayList.get(i).getQuete();
                    String id = arrayList.get(i).getId();
                    String url = arrayList.get(i).getUrl();
                    String title = arrayList.get(i).getTitle();

                    Intent intent = new Intent(getBaseContext(), AdPicEdit.class);
                    //intent.putExtra("QUETE", quete);
                    intent.putExtra("id", id);
                    intent.putExtra("url", url);
                    intent.putExtra("title", title);
                    startActivity(intent);
                }
            });*/
        }
    }

    private String readURL(String theUrl) {
        StringBuilder content = new StringBuilder();
        try {
            // create a url object
            URL url = new URL(theUrl);
            // create a urlconnection object
            URLConnection urlConnection = url.openConnection();
            // wrap the urlconnection in a bufferedreader
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            // read from the urlconnection via the bufferedreader
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line + "\n");
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    public class CustomListAdapter extends ArrayAdapter<Ads> {

        ArrayList<Ads> products;
        Context context;
        int resource;
        Typeface fontAwesomeFont;

        public CustomListAdapter(Context context, int resource, ArrayList<Ads> products) {
            super(context, resource, products);
            this.products = products;
            this.context = context;
            this.resource = resource;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                LayoutInflater layoutInflater = (LayoutInflater) getContext()
                        .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.ad_list_item, null, true);

            }
            Ads product = getItem(position);

            String pic = product.getUrl();
            final ImageView image = (ImageView) convertView.findViewById(R.id.pic);
            Picasso.with(context).load(pic).into(image);

            try {
                TextView ad_title = (TextView) convertView.findViewById(R.id.title);
                String title = product.getTitle();
                byte[] b1 = title.getBytes("UTF-8");
                String ad_tit = new String(b1, "UTF-8");
                if (ad_tit.contains("%20")) {
                    ad_tit = ad_tit.replace("%20", " ");
                }
                ad_title.setText(ad_tit);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            TextView edit = (TextView) convertView.findViewById(R.id.edit);
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String id = arrayList.get(position).getId();
                    String url = arrayList.get(position).getUrl();
                    String title = arrayList.get(position).getTitle();
                    if (title.contains("%20")) {
                        title = title.replace("%20", " ");
                    }

                    Intent intent = new Intent(getBaseContext(), AdPicEdit.class);
                    intent.putExtra("id", id);
                    intent.putExtra("url", url);
                    intent.putExtra("title", title);
                    startActivity(intent);
                }
            });

            TextView delete = (TextView) convertView.findViewById(R.id.delete);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String id = arrayList.get(position).getId();
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(AdList.this);
                    builder1.setIcon(R.drawable.bhromon_logo);
                    builder1.setTitle("Delete");
                    builder1.setMessage("Want to delete Ad?");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id1) {
                                    InputStream is = null;

                                    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                                    nameValuePairs.add(new BasicNameValuePair("id", id));

                                    try{
                                        HttpClient httpClient = new DefaultHttpClient();
                                        HttpPost httpPost = new HttpPost("http://bhromondata.banijyadarpan.com/rentacar/ad_delete.php");
                                        //httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                                        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
                                        HttpResponse response = httpClient.execute(httpPost);
                                        HttpEntity entity = response.getEntity();
                                        is = entity.getContent();
                                        String msg = "Advertisement Deleted Successfully";
                                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                                        finish();
                                        Intent intent = new Intent(getBaseContext(), AdList.class);
                                        startActivity(intent);
                                    }
                                    catch(ClientProtocolException e)
                                    {
                                        Log.e("ClientProtocol", "Log_tag");
                                        e.printStackTrace();
                                    }  catch (IOException e) {
                                        Log.e("Log_tag", "IOException");
                                        e.printStackTrace();
                                    }
                                }
                            });

                    builder1.setNegativeButton(
                            "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id1) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
            });
            return convertView;
        }
    }
}
