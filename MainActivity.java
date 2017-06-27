package com.example.ine5424.smartufsc;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.paperdb.Paper;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {
    private ListView mListView;
    private EditText mEditText;
    private ApiInterface api = getAPI();
    private Timer timer;
    /* Initiates the timer to get information from the server periodically */
    private TimerTask timerTask = new TimerTask() {

        @Override
        public void run() {
            if (MapsActivity.isPopulated()) {
                    MapsActivity.moveBuses();
            }
        }
    };

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Context context = this;
        Paper.init(context);
        displayLinesList(); // display list with bus lines
        manipulateView();
        start(); // start the itmer
    }

    /* Start the timer */
    public void start() {
        if(timer != null) {
            return;
        }
        timer = new Timer();
        timer.scheduleAtFixedRate(timerTask, 0, 2000);

    }

    /* Stop the timer */
    public void stop() {
        timer.cancel();
        timer = null;
    }


    public static ApiInterface getAPI() {
        Gson gson = new GsonBuilder().create();
        Retrofit r = new Retrofit.Builder()
                .baseUrl("http://192.168.43.184:8000/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        return r.create(ApiInterface.class);
    }

    /* Retrieve bus lines from the server and display them */
    public void displayLinesList() {
        new AsyncTask<Object, Object, Object>() {
            @Override
            protected List<BusLine> doInBackground(Object[] objects) {
                try {
                    return getAPI().getLines().execute().body();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                ArrayList<BusLine> linesObject = (ArrayList<BusLine>)o;
                super.onPostExecute(o);
                BusLineAdapter adapter = new BusLineAdapter(MainActivity.this, linesObject );
                mListView.setAdapter(adapter);
                filterLines(linesObject);
            }

        }.execute();
    }

    /* Filter lines as user types */
    public void filterLines(final ArrayList<BusLine> lines) {
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                int textLength = mEditText.getText().length();
                ArrayList<BusLine> filteredLines = new ArrayList <BusLine>();
                String busName = "";
                BusLine l;
                for (int i = 0; i < lines.size(); i++) {
                    l = lines.get(i);
                    busName = l.getCode() + " " + l.getName();
                    if (textLength <= busName.length()) {
                        if(busName.contains(mEditText.getText().toString())) {
                            filteredLines.add(l);
                        }
                    }
                }

                mListView.setAdapter(new BusLineAdapter(MainActivity.this,
                                filteredLines));
            }
        });

        mListView.setFilterText(mEditText.getText().toString());
    }

    /* Opens map based on the selected bus line */
    public void manipulateView() {
        mListView = (ListView) findViewById(R.id.bus_list_view);
        mEditText = (EditText) findViewById(R.id.editText);
        mListView.setTextFilterEnabled(true);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BusLine selected = (BusLine)mListView.getAdapter().getItem(position);
                int code = selected.getCode();
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                intent.putExtra("lineCode", code);
                startActivity(intent);
            }
        });
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

}
