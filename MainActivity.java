package com.example.ine5424.smartufsc;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.R.attr.id;

public class MainActivity extends AppCompatActivity {
    private ListView mListView;
    private EditText mEditText;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        displayLinesList();
//
//        Thread t = new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                ApiInterface api = getAPI();
//                try {
//                    List<Line> body = api.getLines().execute().body();
//                    retrofit2.Response<LineStop> execute = api.getLineStop(1).execute();
//                    Stop body2 = api.getStop(2).execute().body();
//                    LineStop body1 = execute.body();
//                    System.out.println("");
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        });
//        t.start();
        mListView = (ListView) findViewById(R.id.bus_list_view);
        mEditText = (EditText) findViewById(R.id.editText);
        mListView.setTextFilterEnabled(true);
        final Context context = this;


    }

    public ApiInterface getAPI() {
        Gson gson = new GsonBuilder().create();
        Retrofit r = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8000/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        return r.create(ApiInterface.class);
    }

    public void displayLinesList() {
        final ApiInterface api = getAPI();
        List<Line> lines = new ArrayList<Line>();
        final List<String> lineNames = new ArrayList<String>();

        new AsyncTask<Object, Object, Object>() {
            @Override
            protected Object doInBackground(Object[] objects) {
                try {
                    return api.getLines().execute().body();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
            }
        }.execute();
    }


//
//        BusTrackerApi busApi = BusTrackerApi.getInstance(); // talks to the web service
//        busApi.setRequestQueue(this);
//        busApi.sendRequest("lines/"); // Request to get all the lines
//        final ArrayList<String> lines = busApi.getLinesList(); // bus lines string
//        final ArrayList<BusLine> busLines = busApi.getBusLines(); // bus lines object
//
//        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lines);
//        adapter.notifyDataSetChanged(); // waits for the list to be populated
//
//        mListView.setAdapter(adapter);
//
//        mEditText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) { }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                int textLength = mEditText.getText().length();
//                ArrayList<String> filteredLines = new ArrayList<String>();
//                for (int i = 0; i < lines.size(); i++)
//                {
//                    if (textLength <= lines.get(i).length())
//                    {
//                        if(lines.get(i).toString().contains(mEditText.getText().toString()))
//                        {
//                            filteredLines .add(lines.get(i));
//                        }
//                    }
//                }
//
//                mListView.setAdapter(new ArrayAdapter<String>
//                        (MainActivity.this,
//                                android.R.layout.simple_list_item_1, filteredLines ));
//            }
//        });

//        mListView.setFilterText(mEditText.getText().toString());
//
//
//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                int busCode = busLines.get(position).getCode();
//                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
//                intent.putExtra("busLine", busCode);
//                startActivity(intent);
//            }
//        });
//    }


    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

}
