package mortezamaghrebi.com.leitnerauto1100;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
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

public class ChangePasswordDialogClass extends Dialog  {

    public Activity c;
    public Dialog d;
    public Button yes, no;
    Context context;
    Controller controller;
    final String url = "http://kingsofleitner.ir/words1100/webservice.php";

    public ChangePasswordDialogClass(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.context= a;
        controller = new Controller(context,false);
    }
    EditText txtcurrent,txtpass,txtconfirm;
    RelativeLayout btnEnter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_changepassword);
        txtcurrent = (EditText) findViewById(R.id.txtcurrent);
        txtpass = (EditText) findViewById(R.id.txtpassword);
        txtconfirm = (EditText) findViewById(R.id.txtconfirm);
        btnEnter = (RelativeLayout)findViewById(R.id.lytenter);
        try {
            ((Dialog) ChangePasswordDialogClass.this).getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
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
                            if(txtpass.getText().length()<4 || txtconfirm.getText().length()<4 || txtcurrent.getText().length()<4) Toast.makeText(context,"Password must be 4 characters or more",Toast.LENGTH_SHORT).show();
                            else if((controller.getPassword().equals(txtcurrent.getText().toString())==false))Toast.makeText(context,"Current password is wrong",Toast.LENGTH_SHORT).show();
                            else if(!txtpass.getText().toString().equals(txtconfirm.getText().toString()))Toast.makeText(context,"Passord and Confirm not match",Toast.LENGTH_SHORT).show();
                            else {
                                ChangePassword(controller.getUser(),controller.getPassword(),txtpass.getText().toString());
                            }
                        } catch (UnsupportedEncodingException e) {
                        }
                        return true;
                }
                return false;
            }
        });

    }

    public  void  ChangePassword(final String user,final String current,final String newpass)  throws UnsupportedEncodingException
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
                        if(params[0].toLowerCase().equals("change_password: result=ok"))
                        {
                            if(params[1].toLowerCase().contains("message"))
                            {
                                String _message =  params[1].toLowerCase().replace(" message=","");
                                Toast.makeText(context,_message,Toast.LENGTH_SHORT).show();
                            }
                            controller.setPassword(newpass);
                        }
                        else if(params[0].toLowerCase().contains("result"))
                        {
                            if(params[1].toLowerCase().contains("message"))
                            {
                                String _message =  params[1].toLowerCase().replace(" message=","");
                                Toast.makeText(context,_message,Toast.LENGTH_SHORT).show();
                            }
                        }
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
                params.put("command", "changepass,"+user+"~"+current+"~"+newpass);
                //params.put("domain", "http://itsalif.info");
                return params;
            }
        };
        queue.add(postRequest);

    }

}