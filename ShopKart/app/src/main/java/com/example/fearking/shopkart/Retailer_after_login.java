package com.example.fearking.shopkart;

import android.app.ActionBar;
import android.app.ListActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class Retailer_after_login extends ListActivity{
    ListView listView;
    Button button;
    String[] orders={
            "Shirt","Jeans","T-Shirt","Mobiles","Camera"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.retailer_after_login);
        listView=getListView();
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setTextFilterEnabled(true  );
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked,orders);
        listView.setAdapter(adapter);
        button=(Button)findViewById(R.id.confirm_order_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showList();
            }
        });
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar2);
       // ActionBar mActionBar;
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_menu_share));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        String msg=l.isItemChecked(position)?"You selected:"+l.getItemAtPosition(position):"You deselected: "+l.getItemAtPosition(position);
        Message.message(getApplicationContext(),msg);

    }
    public void showList(){
        int j=0;
        String msg="Selected:-\n";
        for (int i=0;i<listView.getCount();i++){
            if(listView.isItemChecked(i))
                msg += listView.getItemAtPosition(i) + "\n";
        }
        Message.message(getApplicationContext(),msg);

}
}
