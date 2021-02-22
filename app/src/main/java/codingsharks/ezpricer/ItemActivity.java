package codingsharks.ezpricer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class ItemActivity extends AppCompatActivity {
    private RecyclerView mRecylerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        ArrayList<Items> examplesList = new ArrayList<>();
        // test cases
        examplesList.add(new Items(R.drawable.ic_android_black,"ItemTitle", "$200.00"));
        examplesList.add(new Items(R.drawable.shark,"ItemTitle2", "$20.00"));

        examplesList.add(new Items(R.drawable.ic_android_black,"ItemTitle3", "$2000.00"));
        examplesList.add(new Items(R.drawable.shark,"ItemTitle4", "$30.00"));
        examplesList.add(new Items(R.drawable.ic_android_black,"ItemTitle5", "$50.00"));
        examplesList.add(new Items(R.drawable.shark,"ItemTitle6", "$40.00"));
        examplesList.add(new Items(R.drawable.ic_android_black,"ItemTitle7", "$200.00"));
        examplesList.add(new Items(R.drawable.shark,"ItemTitle8", "$2000.00"));


        mRecylerView = findViewById(R.id.itemRecyclerView);
        mRecylerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ItemAdapter(examplesList);

        mRecylerView.setLayoutManager(mLayoutManager);
        mRecylerView.setAdapter(mAdapter);
    }
}