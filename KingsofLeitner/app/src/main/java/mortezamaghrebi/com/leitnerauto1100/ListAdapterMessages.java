package mortezamaghrebi.com.leitnerauto1100;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.List;

public class ListAdapterMessages extends ArrayAdapter<String> {

    private int resourceLayout;
    private Activity mContext;
    Controller controller;

    public ListAdapterMessages(Activity context, int resource, List<String> items) {
        super(context, resource, items);
        this.resourceLayout = resource;
        this.mContext = context;
        controller = new Controller(context,true);
    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            v = vi.inflate(resourceLayout, null);
        }
        final String[] s=getItem(position).split("~");
            TextView tt1 = (TextView) v.findViewById(R.id.txtuser);
            TextView tt2 = (TextView) v.findViewById(R.id.txtmessage);
            TextView tt3 = (TextView) v.findViewById(R.id.txtdate);
            TextView tt4 = (TextView) v.findViewById(R.id.txtmessagebutton);
            RelativeLayout ttr= (RelativeLayout) v.findViewById(R.id.lytmessagebutton);
            try {
                if(s.length>0) tt1.setText(s[0]);
                if(s.length>1) tt2.setText(s[1]);
                if(s.length>2) tt3.setText(s[2]);

            }catch (Exception e){}
            try {
                if (s.length > 4) {
                    if (s[3].length() > 0) {
                        tt4.setText(s[3].replace("%bc%","").replace("%u%","").replace("%be%","").replace("%bd%",""));
                        ttr.getLayoutParams().height = dpToPx(75);
                        ttr.requestLayout();
                        ttr.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                try {
                                    String url = s[4];
                                    s[4].replace("*user*",controller.getUser());
                                    s[4].replace("*pass*",controller.getPassword().substring(0,2));
                                    Boolean web=true;
                                    if(s[3].contains("*"))
                                    {
                                        webDialogClass cdd = new webDialogClass(mContext,s[4]);
                                        cdd.show();
                                        web=false;
                                    }
                                    if(s[3].contains("%bc%"))
                                    {
                                        Intent intent = new Intent(Intent.ACTION_EDIT);
                                        intent.setData(Uri.parse(s[4]));
                                        intent.setPackage("com.farsitel.bazaar");
                                        mContext.startActivity(intent);
                                        web=false;
                                    }
                                    if(s[3].contains("%be%"))
                                    {
                                        controller.setBuyEnabled(true);
                                        web=false;
                                    }
                                    if(s[3].contains("%bd%"))
                                    {
                                        controller.setBuyEnabled(false);
                                        web=false;
                                    }
                                    if(s[3].contains("%u%"))
                                    {
                                       controller.checkVersionAndUpdate();
                                       web=false;
                                    }
                                    if(web) {
                                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                        mContext.startActivity(browserIntent);
                                    }
                                }catch (Exception e){}
                            }
                        });
                    } else {
                        ttr.getLayoutParams().height = 0;
                        ttr.requestLayout();
                    }
                }else {
                    ttr.getLayoutParams().height = 0;
                    ttr.requestLayout();
                }
            }catch (Exception e){}
        return v;
    }
    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}