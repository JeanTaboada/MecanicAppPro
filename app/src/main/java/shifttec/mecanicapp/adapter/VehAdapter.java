package shifttec.mecanicapp.adapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import shifttec.mecanicapp.ActivityRegistro;
import shifttec.mecanicapp.CreateVehFragment;
import shifttec.mecanicapp.R;
import shifttec.mecanicapp.model.Veh;

public class VehAdapter extends FirestoreRecyclerAdapter<Veh, VehAdapter.ViewHolder> {
     private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
     Activity activity;
     FragmentManager fm;
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public VehAdapter(@NonNull FirestoreRecyclerOptions<Veh> options, Activity activity, FragmentManager fm) {

        super(options);
        this.activity = activity;
        this.fm = fm;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull Veh Veh) {
        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(viewHolder.getAbsoluteAdapterPosition());

        final String id = documentSnapshot.getId();
        viewHolder.txt_placa_v.setText(Veh.getPlaca());
        viewHolder.txt_conductor_v.setText(Veh.getNombre());
        viewHolder.txt_descripcion_v.setText(Veh.getDescripcion());
        String photoVeh = Veh.getPhoto();
        try{
            if(!photoVeh.equals("")){
                Picasso.get()
                        .load(photoVeh)
                        .resize(150,150)
                        .into(viewHolder.photo_Veh);
            }
        }catch(Exception e){
            Log.d("Exception","e: "+e);
        }

        viewHolder.btn_editar_da.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, ActivityRegistro.class);
                i.putExtra( "id_veh",id);
               // activity.startActivity(i);
               //* CreateVehFragment createVehFragment = new CreateVehFragment();
                //Bundle bundle = new Bundle();
                //bundle.putString("id_veh", id);
                //createVehFragment.setArguments(bundle);
                //createVehFragment.show(fm, "open fragment");*//
            }
        });

        viewHolder.btn_eliminar_da.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteVeh(id);
            }
        });
    }
    private void deleteVeh(String id) {
        mFirestore.collection("veh").document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(activity, "Eliminado Correctamente", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activity,"Error al Eliminar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_veh_single, parent, false);
    return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_placa_v, txt_conductor_v,txt_descripcion_v;

        ImageView btn_eliminar_da, btn_editar_da, photo_Veh;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_placa_v = itemView.findViewById(R.id.txt_placa_wiew);
            txt_conductor_v = itemView.findViewById(R.id.txt_conductor_view);
            txt_descripcion_v = itemView.findViewById(R.id.txt_descripcion_view);
            btn_eliminar_da = itemView.findViewById(R.id.btn_eliminar_dato);
            btn_editar_da = itemView.findViewById(R.id.btn_editar_dato);
            photo_Veh = itemView.findViewById(R.id.photo_V);
        }
    }
}
