package mortezamaghrebi.com.leitnerauto1100;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserActivity extends AppCompatActivity {

    ImageView imgback,imguser,imgpercent,imgtap;
    TextView txtuser,txtlevel,txtpercent,txtnumofgames;
    ListView lstwords;
    Controller controller;
    final String uri_sendimage = "http://kingsofleitner.ir/words1100/webservice.php?get_command=sendimage,";
    final String uri_getimage = "http://kingsofleitner.ir/words1100/webservice.php?get_command=getimage,";

    int RESULT_LOAD_IMG=110;
    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);


        if (resultCode == RESULT_OK && reqCode==RESULT_LOAD_IMG){
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap si = BitmapFactory.decodeStream(imageStream);
                int width = si.getWidth();
                int height= si.getHeight();
                int min= Math.min(width,height);
                int max=Math.max(width,height);
                if(min>=150) {
                    int w = Math.min(min, 220);
                    double r = 1.0 * min / w;
                    int dh = (height - min) / 2;
                    int dw = (width - min) / 2;
                    Bitmap bit = Bitmap.createBitmap(w, w, Bitmap.Config.RGB_565);
                    try {
                        for (int i = 0; i < w; i++) {
                            for (int j = 0; j < w; j++) {
                                int x = (int) (i * r + dw);
                                if (x < 0) x = 0;
                                else if (x >= width) x = width - 1;
                                int y = (int) (j * r + dh);
                                if (y < 0) y = 0;
                                else if (y >= height) y = height - 1;
                                bit.setPixel(i, j, si.getPixel(x, y));
                            }
                        }
                        SendImage(bit);
                    } catch (Exception e) {
                        Toast.makeText(UserActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                else Toast.makeText(UserActivity.this,"The image is too small, please select another one",Toast.LENGTH_LONG).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(UserActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(UserActivity.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }
    MediaPlayer mpbutton;

    @Override
    protected void onDestroy() {
        mpbutton.stop();
        mpbutton.release();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mpbutton = MediaPlayer.create(UserActivity.this, R.raw.clicksound
        );
        setContentView(R.layout.activity_user);
        Handler timerHandler = new Handler(Looper.getMainLooper());
        Runnable timerRunnable = new Runnable() {
            @Override
            public void run() {
                imgback = (ImageView)findViewById(R.id.imgback);
                imguser = (ImageView)findViewById(R.id.imgavatar);
                imgpercent = (ImageView)findViewById(R.id.imgpercent);
                imgtap = (ImageView)findViewById(R.id.imgtap);
                txtuser = (TextView)findViewById(R.id.txtuser);
                txtlevel = (TextView)findViewById(R.id.txtlevel);
                txtpercent = (TextView)findViewById(R.id.txtpercent);
                txtnumofgames = (TextView)findViewById(R.id.txtnumofgames);
                lstwords = (ListView)findViewById(R.id.lstwords);
                controller = new Controller(UserActivity.this,true);
                imguser.setImageBitmap(controller.getAvatar());
                txtlevel.setText(""+controller.getLevel());
                txtuser.setText(controller.getUser());
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int height = displayMetrics.heightPixels;
                int width = displayMetrics.widthPixels;
                List<Integer> slist;
                slist = new ArrayList<Integer>();
                for (int i = 0; i < controller.wordItems.length; i++)
                    slist.add(i);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        imgpercent.setImageBitmap(createProgressBitmap(controller.getPercent()));
                        txtnumofgames.setText("Number of games played: "+controller.getNumberOfPlayings());
                    }
                },700);
                txtpercent.setText(""+((int)controller.getPercent())+"%");
                ListAdapterWordsProgress customAdapter = new ListAdapterWordsProgress(UserActivity.this, controller, slist,width);
                lstwords.setAdapter(customAdapter);
                lstwords.post(new Runnable() {
                    @Override
                    public void run() {
                        // Select the last row so it will scroll into view...
                        int sel=controller.getPreviousWordIdReleased()-1;
                        if(sel<0)sel=0;
                        try {
                            lstwords.setSelection(sel);
                        }catch (Exception e){}
                    }
                });
                imguser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mpbutton.seekTo(0);mpbutton.start();
                        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                        photoPickerIntent.setType("image/*");
                        startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
                    }
                });
                imgback.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mpbutton.seekTo(0);mpbutton.start();
                        UserActivity.this.finish();
                    }
                });  // get the data
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(controller.getTapShown()%5==0) {
                            Animation connectingAnimation = AnimationUtils.loadAnimation(UserActivity.this, R.anim.tap);
                            imgtap.setVisibility(View.VISIBLE);
                            imgtap.startAnimation(connectingAnimation);
                            imgtap.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    imgtap.setVisibility(View.INVISIBLE);
                                }
                            });
                            (new Handler()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    imgtap.setVisibility(View.INVISIBLE);
                                }
                            }, 1500);
                        }
                        controller.setTapShown();
                    }
                },700);
            }
        };
        timerHandler.postDelayed(timerRunnable, 0);
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


    Bitmap createProgressBitmap(double percent)
    {
        int r=150;
        int r1=0,r2=0,r3=130,r4=151;
        int r12=r1*r1,r22=r2*r2,r32=r3*r3,r42=r4*r4;
        Bitmap bit = Bitmap.createBitmap(301, 301, Bitmap.Config.ARGB_8888);
        for(int i=0;i<301;i++)
        {
            for(int j=0;j<301;j++) {
                double radius2 = (i - 151) * (i - 151) + (j - 151) * (j - 151);
                if(radius2>r32&&radius2<=r42) {
                    bit.setPixel(i,j, Color.argb(180, 90, 43, 45));
                }
                else if(radius2>r22&&radius2<r32) {
                    int dx = i - 151;
                    int dy = j - 151;
                    double per = getAngle(dx, dy);
                    if (per < percent * 36 / 10) {
                        bit.setPixel(i, j, Color.argb(255, 33, 98, 34));
                    } else bit.setPixel(i, j, Color.argb(100, 187, 176, 140));
                }
            }
        }
        return bit;
    }
    double getAngle(int dx,int dy) {
        double angle = 0;
        if (dy < 0 && dx == 0) angle = 0;
        else if (dx > 0 && dy == 0) angle = 90;
        else if (dy > 0 && dx == 0) angle = 180;
        else if (dx < 0 && dy == 0) angle = 270;
        else {
            angle = Math.atan(Math.abs(dx*1.0/dy)) * 180 / Math.PI;
            if (dy < 0 && dx > 0) angle = angle;
            else if (dy > 0 && dx > 0) angle = -angle + 180;
            else if (dy > 0 && dx < 0) angle = angle + 180;
            else if (dy < 0 && dx < 0) angle = -angle + 360;
        }
        return angle;
    }
    void  SendImage(final Bitmap bitmap)  throws UnsupportedEncodingException
    {
        controller.AvatarChanged();
        RequestQueue queue = Volley.newRequestQueue(UserActivity.this);
        final Bitmap icon = BitmapFactory.decodeResource(UserActivity.this.getResources(),
                R.drawable.usericon);
        StringRequest postRequest = new StringRequest(Request.Method.POST, uri_sendimage+controller.getUser(),
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // response
                            String resp = response.substring(response.indexOf("{") + 1, response.indexOf("}"));
                            String[] params = resp.split(",");
                            if (params[0].toLowerCase().contains("avatar")) {
                                if (params[1].toLowerCase().contains("message")) {
                                    String message = params[1].toLowerCase().replace(" message=", "");
                                    if(!message.contains("success"))Toast.makeText(UserActivity.this,message,Toast.LENGTH_SHORT).show();
                                }
                            }

                            getImage();

                        }catch (Exception e){
                            int a=1;
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Toast.makeText(UserActivity.this,"Could not connect to server",Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("image", getStringImage(bitmap));
                //params.put("domain", "http://itsalif.info");
                return params;
            }
        };
        postRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        queue.add(postRequest);

    }
    void  getImage()  throws UnsupportedEncodingException
    {

        RequestQueue queue = Volley.newRequestQueue(UserActivity.this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, uri_getimage+controller.getUser(),
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if(response.length()>200) {
                                byte[] decodedString = Base64.decode(response, Base64.DEFAULT);
                                Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                int width = Math.min(bitmap.getWidth(), bitmap.getHeight());
                                double w = bitmap.getWidth() / 2.0;
                                double h = bitmap.getHeight() / 2.0;
                                double radius = width / 2.0;
                                Bitmap bit = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888);
                                try {
                                    for (int i = 0; i < width; i++) {
                                        for (int j = 0; j < width; j++) {
                                            if (Math.pow(i - w, 2) + Math.pow(j - h, 2) > (radius * radius)) {
                                                bit.setPixel(i, j, Color.argb(0, 0, 0, 0));
                                            } else if (Math.pow(i - w, 2) + Math.pow(j - h, 2) > (radius * 0.81 * radius)) {
                                                bit.setPixel(i, j, Color.argb(255, 0xf7, 0xaa, 0x3e));
                                            }
                                            else if (Math.pow(i - w, 2) + Math.pow(j - h, 2) > (radius * 0.73 * radius)) {
                                                bit.setPixel(i, j, Color.argb(255, 0xff, 0xff, 0xff));
                                            }else bit.setPixel(i, j, bitmap.getPixel(i, j));
                                        }
                                    }
                                } catch (Exception e) {
                                    Toast.makeText(UserActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                controller.setAvatar(getStringImage(bit));
                                imguser.setImageBitmap(bit);
                            }
                        }catch (Exception e){
                            int a=1;
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Toast.makeText(UserActivity.this,"Could not connect to server",Toast.LENGTH_LONG).show();
                    }
                }
        ) ;
        queue.add(postRequest);

    }
}
