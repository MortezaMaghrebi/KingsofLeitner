package mortezamaghrebi.com.leitnerauto1100;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class UserMenuDialogClass extends Dialog  {

    public Activity c;
    public Dialog d;
    public Button yes, no;
    Context context;
    Controller controller;
    final String url = "http://kingsofleitner.ir/words1100/webservice.php";

    public UserMenuDialogClass(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.context= a;
        controller = new Controller(context,true);
    }
     RelativeLayout btnchangepass,btnsave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_menuuser);
        btnchangepass = (RelativeLayout)findViewById(R.id.lytpassword);
        btnsave = (RelativeLayout)findViewById(R.id.lytsave);
        try {
            ((Dialog) UserMenuDialogClass.this).getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }catch (Exception e){}
        btnchangepass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btnchangepass.setBackgroundResource(R.drawable.outline_buttonusermenub);
                        return true;
                    case MotionEvent.ACTION_CANCEL:
                        btnchangepass.setBackgroundResource(R.drawable.outline_buttonusermenu);
                        return true;
                    case MotionEvent.ACTION_UP:
                        btnchangepass.setBackgroundResource(R.drawable.outline_buttonusermenu);
                        ChangePasswordDialogClass cdd=new ChangePasswordDialogClass(c);
                        cdd.show();
                        return true;
                }
                return false;
            }
        });
        btnsave.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btnsave.setBackgroundResource(R.drawable.outline_buttonusermenub);
                        return true;
                    case MotionEvent.ACTION_CANCEL:
                        btnsave.setBackgroundResource(R.drawable.outline_buttonusermenu);
                        return true;
                    case MotionEvent.ACTION_UP:
                        btnsave.setBackgroundResource(R.drawable.outline_buttonusermenu);
                        try {
                            Save();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        return true;
                }
                return false;
            }
        });
    }


    public  void  Save()  throws UnsupportedEncodingException
    {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        String resp ="";
                        try {
                            resp = response.substring(response.indexOf("{")+1,response.indexOf("}"));
                        }catch (Exception e){}
                        String[] params = resp.split(",");
                        if(params[0].toLowerCase().contains("save: result"))
                        {
                            if(params[0].toLowerCase().contains("ok"))controller.setLastHeartIdSaved(controller.getHeartId());
                            if(params.length>1) {
                                if (params[1].toLowerCase().contains("message")) {
                                    String msg = params[1].replace(" Message=", "");
                                    Toast.makeText(c, msg, Toast.LENGTH_SHORT).show();
                                    UserMenuDialogClass.this.dismiss();
                                }
                            }

                            else Toast.makeText(c,resp,Toast.LENGTH_SHORT).show();
                        }else Toast.makeText(c,resp,Toast.LENGTH_SHORT).show();
                        Log.d("Response", resp);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Toast.makeText(context,"Could not connect to server",Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("command", "save,"+controller.getUser()+"~"+controller.getLevel()+"~"+controller.getProgress()+"~"+(controller.gethearts()+controller.getheartsBought())+"~"+controller.getHeartId()+"~"+controller.getExir()+"~"+controller.getPercent10()+"~"+controller.getNumberOfPlayings()+"~"+controller.Version+"|"+controller.getDBVersion());
                params.put("data", controller.getSaveString());
                return params;
            }
        };
        queue.add(postRequest);

    }
}