package onlyw.example.com.onlyw;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.Window;

import org.apache.http.HttpClientConnection;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class Login extends AppCompatActivity {

    private String Tag = "http";
    private EditText editTextaccount = null;
    private EditText editTextpassword = null;
    private ImageView imageViewLogin = null;

    private String BaseUrl="http://10.80.104.60:8080/user/login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i(Tag,"OnCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();

        editTextaccount=(EditText)findViewById(R.id.account2);     //account
        editTextpassword=(EditText)findViewById(R.id.password2);   //password

        TextView textViewRegister=(TextView)findViewById(R.id.register);    //register
        TextView textViewFindPassword=(TextView)findViewById(R.id.findpassword);    //findpassword

        imageViewLogin=(ImageView) findViewById(R.id.login);      // login


        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentLoginToRegister=new Intent(Login.this,Register.class);
                startActivity(intentLoginToRegister);
            }
        });

        textViewFindPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentLoginToMain=new Intent(Login.this,MainActivity.class);
                startActivity(intentLoginToMain);
            }
        });
        imageViewLogin.setOnClickListener(LoginClickListener);
    }


    private View.OnClickListener LoginClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String strAccount=editTextaccount.getText().toString();
            String strPassword=editTextpassword.getText().toString();
            //sendRequest(strAccount,strPassword);
            show(view);
        }
    };

    public void sendRequest(final String account,final String password) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet("http://10.80.104.60:8080/user/login?name="+account+"&password="+password);
                try{
                    HttpResponse response = httpClient.execute(httpGet);
                    HttpEntity entity = response.getEntity();
                    String responseStr = EntityUtils.toString(entity);
                    Message message = new Message();
                    message.what = 1;
                    message.obj = responseStr;
                    handler.sendMessage(message);
                }catch (Exception e) {
                    System.out.print("==================>"+e);
                }
            }
        }).start();
    }

    public void showPicture() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet("http://192.168.2.12:8080/getProductions?cameristId=1");
                try{
                    HttpResponse response = httpClient.execute(httpGet);
                    HttpEntity entity = response.getEntity();
                    String responseStr = EntityUtils.toString(entity);
                    Message message = new Message();
                    message.what = 1;
                    message.obj = responseStr;
                    handler.sendMessage(message);
                }catch (Exception e) {
                    System.out.print("==================>"+e);
                }
            }
        }).start();
    }

    public void show(View view) {
        new Thread(new Runnable() {
            public void run() {
                Message message = new Message();
                message.obj = getPicture();
                message.what = 3;
                handler.sendMessage(message);
            }
        }).start();

    }

    private Bitmap getPicture() {
        URL imgUrl = null;
        Bitmap bitmap = null;
        try {
            imgUrl = new URL("http://192.168.2.12:8080/temp2.jpg");
            HttpURLConnection conn = (HttpURLConnection) imgUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public Handler handler = new Handler() {
        public void handleMessage(Message message) {
            try {
                if(message.what == 1) {
                    JSONObject obj = new JSONObject(message.obj.toString());
                    Boolean data = (Boolean) obj.get("data");
                    if(data != null && data == true) {
                        System.out.println("登陆成功");
                    } else {
                        System.out.println("用户名或密码错误");
                    }
                }
                if(message.what == 2) {
                    JSONObject obj = new JSONObject(message.obj.toString());
                    JSONArray data = obj.getJSONArray("data");
                    for (int i = 0; i <= data.length() - 1; i++) {
                        JSONObject object = (JSONObject) data.get(i);
                        System.out.println("name=========>" + object.get("name"));
                        System.out.println("price========>" + object.get("price"));
                    }
                }
                if(message.what == 3) {
                    ImageView view1 = (ImageView) findViewById(R.id.production);
                    view1.setImageBitmap((Bitmap) message.obj);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
}


