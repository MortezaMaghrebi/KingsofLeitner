package mortezamaghrebi.com.leitnerauto1100;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
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

public class CustomDialogClass extends Dialog  {

    public Activity c;
    public Button yes, no;
    Context context;
    Controller controller;
    final String url = "http://kingsofleitner.ir/words1100/webservice.php";

    public CustomDialogClass(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.context= (Context) a;
        controller = new Controller(context,false);
    }

    EditText txtuser;
    EditText txtpass;
    RelativeLayout btnEnter;
    LinearLayout lytrel;
    TextView txtmessage;
    Boolean ready=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_login);
        txtuser = (EditText) findViewById(R.id.txtuser);
        txtpass = (EditText) findViewById(R.id.txtpassword);
        btnEnter = (RelativeLayout)findViewById(R.id.lytenter);
        lytrel = (LinearLayout)findViewById(R.id.lytrel);
        txtmessage = (TextView)findViewById(R.id.txtmessage);
        try {
            ((Dialog) CustomDialogClass.this).getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }catch (Exception e){}
        btnEnter.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btnEnter.setBackgroundResource(R.drawable.outline_button1b);
                        return true;
                    case MotionEvent.ACTION_CANCEL:
                        btnEnter.setBackgroundResource(R.drawable.outline_button1);
                        return true;
                    case MotionEvent.ACTION_UP:
                        btnEnter.setBackgroundResource(R.drawable.outline_button1);
                        try {
                            if(txtuser.getText().length()<2) Toast.makeText(context,"Username must be 2 characters at least",Toast.LENGTH_SHORT).show();
                            else if(txtpass.getText().length()<4) Toast.makeText(context,"Password must be 4 characters or more",Toast.LENGTH_SHORT).show();
                            else if((controller.getPassword().length()>1)&&(txtpass.getText().toString().equals(controller.getPassword())&&txtuser.getText().toString().equals(controller.getUser())))
                            {
                                if(ready) {
                                    try {
                                        loadGetLoading();
                                    }catch (Exception e){}

                                }else Toast.makeText(context,"Please wait and try again after 3 seconds",Toast.LENGTH_SHORT).show();
                            }
                            else GetByUser(txtuser.getText().toString());
                        } catch (UnsupportedEncodingException e) {
                        }
                        return true;
                }
                return false;
            }
        });

    }

    public void hideSoftKeyboard(Activity activity) {
        try {
            InputMethodManager imm = (InputMethodManager)activity.getSystemService(activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(txtpass.getWindowToken(), 0);
        }catch (Exception e){}
    }

    public  void  loadGetLoading()  throws UnsupportedEncodingException
    {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        if(response.contains("getLoading")) {
                            String resp = response.substring(response.indexOf("{") + 1, response.indexOf("}"));
                            controller.setMessages(resp);
                        }
                        Intent secondact = new Intent(context, SecondActivity.class);
                        context.startActivity(secondact);
                        CustomDialogClass.this.dismiss();
                        c.finish();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Intent secondact = new Intent(context, SecondActivity.class);
                        context.startActivity(secondact);
                        CustomDialogClass.this.dismiss();
                        c.finish();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("command", "getLoading"+","+controller.getUser()+"~"+controller.getPassword());
                //params.put("domain", "http://itsalif.info");
                return params;
            }
        };
        queue.add(postRequest);

    }
    public  void  GetByUser(final String user)  throws UnsupportedEncodingException
    {
        ready=false;
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        String resp = response.substring(response.indexOf("{")+1,response.indexOf("}"));
                        String[] params = resp.split(",");
                        if(params[0].toLowerCase().equals("check_existance: result=ok"))
                        {
                            if(params[1].toLowerCase().contains("count"))
                            {
                                String _count =  params[1].toLowerCase().replace(" count=","");
                                int count = Integer.parseInt(_count);
                                if(count==1)
                                {
                                    hideSoftKeyboard(c);
                                    if(params[2].toLowerCase().contains("items")&&params[2].contains("]"))
                                    {
                                        String _param2=params[2].toLowerCase().replace(" items: [","").replace("]","").replace("\"","");
                                        if(_param2.contains("~")) {
                                            String[] data = _param2.split("~");
                                            // Toast.makeText(context,_param2,Toast.LENGTH_LONG).show();
                                            try {
                                                if (data[2].equals(txtpass.getText().toString())) {
                                                    controller.setUser(txtuser.getText().toString());
                                                    controller.setPassword(txtpass.getText().toString());
                                                    int level = Integer.parseInt(data[3]);
                                                    int progress = Integer.parseInt(data[4]);
                                                    try {
                                                        double percent = Double.parseDouble(data[5]);
                                                        controller.setPercent(percent/10);
                                                    }catch (Exception e){}
                                                    int numgames = Integer.parseInt(data[6]);
                                                    int hearts = Integer.parseInt(data[7]);
                                                    int heartid = Integer.parseInt(data[8]);
                                                    int potions = Integer.parseInt(data[9]);
                                                    controller.setLastHeartIdSaved(heartid);
                                                    controller.initialHeartAndExir(hearts,heartid,potions,level,progress);
                                                    controller.setIsLogin(true);
                                                    controller.setNumberOfPlayings(numgames);
                                                    try {
                                                        GetInitialValues(false);
                                                    } catch (UnsupportedEncodingException e) {
                                                        e.printStackTrace();
                                                    }
                                                } else
                                                    Toast.makeText(context, "Wrong password", Toast.LENGTH_LONG).show();

                                            } catch (Exception e) {
                                            }
                                        }
                                    }
                                }
                                else if(count==0)
                                {
                                    // Toast.makeText(context,"user not exist",Toast.LENGTH_LONG).show();
                                    try {
                                        hideSoftKeyboard(c);
                                        SignUp(txtuser.getText().toString(),txtpass.getText().toString());
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                        }
                        Log.d("Response", resp);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Toast.makeText(context,"Error\n"+"Could not connect to server",Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("command", "check,"+user);
                //params.put("domain", "http://itsalif.info");
                return params;
            }
        };
        queue.add(postRequest);

    }

    public  void  GetInitialValues(final Boolean newAccount)  throws UnsupportedEncodingException
    {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        String resp = response.substring(response.indexOf("{")+1,response.indexOf("}"));
                        String[] params = resp.split(":");
                        if(params[0].toLowerCase().equals("initial_values"))
                        {
                            String valStrings[]  = params[1].split(",");
                            int hearts=10,maxheart=controller.HeartsMaximum,potions=controller.InitialExir,level=1,progress=0,timeheart=controller.HeartIncreasTime,timepotion=controller.DecreasExirEachMin;
                            for(int i=0;i<valStrings.length;i++)
                            {
                                String name=valStrings[i].split("=")[0];
                                String value=valStrings[i].split("=")[1];
                                switch (name.toLowerCase()) {
                                    case "hearts":
                                        hearts = Integer.parseInt(value);
                                        break;
                                    case "maxheart":
                                        maxheart = Integer.parseInt(value);
                                        break;
                                    case "potions":
                                        potions = Integer.parseInt(value);
                                        break;
                                    case "level":
                                        level = Integer.parseInt(value);
                                        break;
                                    case "progress":
                                        progress = Integer.parseInt(value);
                                        break;
                                    case "timeheart":
                                        timeheart = Integer.parseInt(value);
                                        break;
                                    case "timepotion":
                                        timepotion = Integer.parseInt(value);
                                        break;
                                }
                            }
                            if(newAccount) controller.initialHeartAndExir(hearts,0,potions,level,progress);
                            controller.initialHeartAndExirSettings(true,timeheart,timepotion,maxheart);
                            //Toast.makeText(context,"Initial settings set",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            int hearts=10,maxheart=controller.HeartsMaximum,potions=controller.InitialExir,level=1,progress=0,timeheart=controller.HeartIncreasTime,timepotion=controller.DecreasExirEachMin;
                            if(newAccount)controller.initialHeartAndExir(hearts,0,potions,level,progress);
                            controller.initialHeartAndExirSettings(false,timeheart,timepotion,maxheart);
                            Toast.makeText(context,"Review initial settings: error",Toast.LENGTH_SHORT).show();
                        }
                        Enter(newAccount);
                        Log.d("Response", resp);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Toast.makeText(context,"Error\n"+"Could not connect to server",Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("command", "getinitial");
                //params.put("domain", "http://itsalif.info");
                return params;
            }
        };
        queue.add(postRequest);

    }

    public  void  SignUp(final String user, final String pass)  throws UnsupportedEncodingException
    {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        String resp = response.substring(response.indexOf("{")+1,response.indexOf("}"));
                        String[] params = resp.split(",");
                        if (params[0].toLowerCase().contains("result")){
                            String _status=params[0].substring(params[0].indexOf("=")+1,params[0].length());
                            if(params[1].toLowerCase().contains("message"))
                            {
                                String _message=params[1].substring(params[1].indexOf("=")+1,params[1].length());
                                if(_status.toLowerCase().equals("ok"))
                                {
                                    Toast.makeText(context,_message,Toast.LENGTH_LONG).show();
                                    controller.setUser(txtuser.getText().toString());
                                    controller.setPassword(txtpass.getText().toString());
                                    try {
                                        GetInitialValues(true);
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }

                                }
                                else {
                                    Toast.makeText(context,_message,Toast.LENGTH_LONG).show();
                                }
                            }
                            else {Toast.makeText(context,"Unable to recieve message from the server. please try again",Toast.LENGTH_LONG).show();}
                        }
                        else {
                            Toast.makeText(context,"Invalid response from the server. please contact us",Toast.LENGTH_LONG).show();}
                    }

                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Toast.makeText(context,"ERROR\n"+error.getMessage(),Toast.LENGTH_LONG).show();
                        Log.d("Error.Response", "eeeerrrroooorrrr");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("command", "insert,"+user+"~"+pass);
                //params.put("domain", "http://itsalif.info");
                return params;
            }
        };
        queue.add(postRequest);

    }

    void Enter(Boolean newAccount) {
        ready=true;
        hideSoftKeyboard(c);
        Intent secondact = new Intent(context, SecondActivity.class);
        Boolean notfinished=false;
        lytrel.getLayoutParams().height=0;
        lytrel.setVisibility(View.INVISIBLE);
        lytrel.requestLayout();
        if(newAccount) txtmessage.setText("Your account created successfully. Click to enter.");
        else txtmessage.setText("You logged in successfully. Click to enter.");
        try {
            ((Dialog)CustomDialogClass.this).setCancelable(false);
        }catch (Exception e){}
        if(controller.getDBVersion()==0)
        {
            notfinished=true;
            try {
                controller.checkVersionAndUpdate();
            } catch (UnsupportedEncodingException e) {
                Toast.makeText(context,"Could not connect to internet\nWe need to get words online",Toast.LENGTH_LONG).show();
            }
        }
        if(!newAccount) {
            if (!controller.getIsUserWordsDownloaded()) {
                notfinished=true;
                try {
                    controller.getSavedWordsOnline();
                } catch (Exception e) {
                    Toast.makeText(context, "Could not connect to internet\nWe need to get your progress online", Toast.LENGTH_LONG).show();
                }
            }
        }
        if(!notfinished)
        {
            context.startActivity(secondact);
            c.overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            this.dismiss();
            c.finish();
        }
    }



}