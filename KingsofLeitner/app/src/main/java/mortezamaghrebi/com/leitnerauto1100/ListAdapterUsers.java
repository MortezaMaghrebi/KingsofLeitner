package mortezamaghrebi.com.leitnerauto1100;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class ListAdapterUsers extends ArrayAdapter<String> {

    private int resourceLayout;
    private Context mContext;
    final String uri_getimage = "http://kingsofleitner.ir/words1100/webservice.php?get_command=getimage,";

    public ListAdapterUsers(Context context, List<String> items) {
        super(context, R.layout.user_item, items);
        this.resourceLayout = R.layout.user_item;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            v = vi.inflate(resourceLayout, null);
        }
        try {
            String[] s = getItem(position).split("~");
            TextView txtuser = (TextView) v.findViewById(R.id.userit4);
            TextView txtlevel = (TextView) v.findViewById(R.id.userit3);
            TextView txtplays = (TextView) v.findViewById(R.id.userit2);
            TextView txtpercent = (TextView) v.findViewById(R.id.userit1);
            RelativeLayout lytuser = (RelativeLayout) v.findViewById(R.id.lytuseritem);
            txtuser.setText(s[0]);
            txtlevel.setText(s[1]);
            txtplays.setText(s[2]);
            txtpercent.setText(""+(Integer.parseInt(s[3])/10)+"%");
        }catch (Exception e){}
        return v;
    }

}