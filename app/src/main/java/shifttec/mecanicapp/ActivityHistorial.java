package shifttec.mecanicapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import shifttec.mecanicapp.adapter.VehAdapter;
import shifttec.mecanicapp.model.Veh;

public class ActivityHistorial extends AppCompatActivity {

    private Button btn_agregarveh, btn_agregar_frag;
    RecyclerView mRecycler;
    private VehAdapter mAdapter;
    private FirebaseFirestore mFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        mFirestore = FirebaseFirestore.getInstance();
        mRecycler = findViewById(R.id.RecyclerView_1);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        Query query = mFirestore.collection("veh");
        FirestoreRecyclerOptions<Veh> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Veh>().setQuery(query, Veh.class).build();

        mAdapter = new VehAdapter(firestoreRecyclerOptions, this, getSupportFragmentManager());
        mAdapter.notifyDataSetChanged();
        mRecycler.setAdapter(mAdapter);

        btn_agregarveh=(Button)findViewById(R.id.btn_registrar_history);
        btn_agregar_frag=(Button)findViewById(R.id.btn_agregar_fg);

        btn_agregarveh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityHistorial.this, ActivityRegistro.class));
            }
        });
        btn_agregar_frag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateVehFragment fm = new CreateVehFragment();
                fm.show(getSupportFragmentManager(),"Navegar a Fragment");
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        mAdapter.startListening();
    }
    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }
}