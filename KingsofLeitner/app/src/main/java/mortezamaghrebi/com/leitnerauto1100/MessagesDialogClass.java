package mortezamaghrebi.com.leitnerauto1100;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.RelativeLayout;
import java.util.ArrayList;
import java.util.List;

public class MessagesDialogClass extends Dialog  {

    public SecondActivity c;
    Context context;
    Controller controller;

    public MessagesDialogClass(SecondActivity c) {
        super(c);
        // TODO Auto-generated constructor stub
        this.c = c;
        this.context= c;
    }
     RelativeLayout btnok;
    ListView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_messages);
        try {
            ((Dialog) MessagesDialogClass.this).getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }catch (Exception e){}
        btnok = (RelativeLayout)findViewById(R.id.lytok);
        list = (ListView) findViewById(R.id.listmessages);
        final Controller controller = new Controller(context,false);
        String datastr = controller.getMessages();
        String[] data = datastr.split("#");
        List<String> slist;
        slist = new ArrayList<String>();
        if(datastr.length()>1) {
            for (int i = 0; i < data.length; i++)
                slist.add(data[i]);
        }
        final ListAdapterMessages customAdapter = new ListAdapterMessages(c, R.layout.message_item, slist);
        list.setAdapter(customAdapter);
        controller.setMessagesShownCount(controller.getMessagesCount());
        try {
            list.post(new Runnable() {
                @Override
                public void run() {
                    // Select the last row so it will scroll into view...
                    int a= controller.getMessagesShownCount();
                    if (a==0) a=1;
                    if(a>controller.getMessagesCount())a=controller.getMessagesCount();
                    if(customAdapter.getCount()>1) list.setSelection(controller.getMessagesCount() - a);
                }
            });
        }catch (Exception e){}
        btnok.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btnok.setBackgroundResource(R.drawable.outline_button1b);
                        return true;
                    case MotionEvent.ACTION_CANCEL:
                        btnok.setBackgroundResource(R.drawable.outline_button1);
                        return true;
                    case MotionEvent.ACTION_UP:
                        btnok.setBackgroundResource(R.drawable.outline_button1);
                        controller.setMessagesShownCount(controller.getMessagesCount());
                        c.setMessagesUI();
                        MessagesDialogClass.this.dismiss();
                        return true;
                }
                return false;
            }
        });
    }

}