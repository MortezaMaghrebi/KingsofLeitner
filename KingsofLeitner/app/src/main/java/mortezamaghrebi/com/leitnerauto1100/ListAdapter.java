package mortezamaghrebi.com.leitnerauto1100;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

public class ListAdapter extends ArrayAdapter<String> {

    private int resourceLayout;
    private Context mContext;
    final String uri_getimage = "http://kingsofleitner.ir/words1100/webservice.php?get_command=getimage,";

    public ListAdapter(Context context, int resource, List<String> items) {
        super(context, resource, items);
        this.resourceLayout = resource;
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

        String[] s=getItem(position).split("~");


            TextView tt1 = (TextView) v.findViewById(R.id.txtuser);
            TextView tt2 = (TextView) v.findViewById(R.id.txtmessage);
            TextView tt3 = (TextView) v.findViewById(R.id.txtdate);
            tt1.setText(s[0]);
            tt2.setText(s[1]);
            tt3.setText(s[2]);
        return v;
    }

}