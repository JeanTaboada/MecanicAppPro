package shifttec.mecanicapp;

import android.adservices.topics.GetTopicsRequest;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateVehFragment extends DialogFragment {
    String id_veh;
    private EditText edt_placa, edt_nombredelconductor, edt_descripcion;
    private Button boton_registro, btn_go_hist;
    private FirebaseFirestore mfirestore;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         if(getArguments()!=null){
           id_veh = getArguments().getString("id_veh");
         }
        }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_create_veh, container, false);
        mfirestore = FirebaseFirestore.getInstance();

        edt_placa = (EditText)v.findViewById(R.id.txt_placa);
        edt_nombredelconductor = (EditText)v.findViewById(R.id.txt_nombre_conductor);
        edt_descripcion = (EditText)v.findViewById(R.id.txt_descripcion_registro);
        boton_registro = (Button)v.findViewById(R.id.btn_agregar_registro);
        btn_go_hist = (Button)v.findViewById(R.id.btn_go_history);

        if(id_veh==null||id_veh==""){
            boton_registro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String Placa_veh = edt_placa.getText().toString().trim();
                    String NombreConductor = edt_nombredelconductor.getText().toString().trim();
                    String DescripcionVeh = edt_descripcion.getText().toString().trim();

                    if(Placa_veh.isEmpty() && NombreConductor.isEmpty() && DescripcionVeh.isEmpty()){
                        Toast.makeText(getContext(),"Ingresar los datos",Toast.LENGTH_SHORT).show();
                    }else{
                        postVeh(Placa_veh, NombreConductor, DescripcionVeh);
                    }
                }
            });
        }else{
            getVeh();
            boton_registro.setText("update");
            boton_registro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String Placa_veh = edt_placa.getText().toString().trim();
                    String NombreConductor = edt_nombredelconductor.getText().toString().trim();
                    String DescripcionVeh = edt_descripcion.getText().toString().trim();

                    if(Placa_veh.isEmpty() && NombreConductor.isEmpty() && DescripcionVeh.isEmpty()){
                        Toast.makeText(getContext(),"Ingresar los datos",Toast.LENGTH_SHORT).show();
                    }else{
                        updateVeh(Placa_veh, NombreConductor, DescripcionVeh, id_veh);
                    }
                }
            });
        }



        return v;
    }

    private void updateVeh(String placaVeh, String nombreConductor, String descripcionVeh, String idVeh) {
        Map<String, Object> map = new HashMap<>();
        map.put("placa", placaVeh);
        map.put("nombre", nombreConductor);
        map.put("descripcion", descripcionVeh);

        mfirestore.collection("vehiculos").document(id_veh).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getContext(), "Actualizado exitosamente", Toast.LENGTH_SHORT).show();
                getDialog().dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error al actualizar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void postVeh(String PlacaVeh, String NombreConductor, String DescripcionVeh) {
        Map<String, Object> map = new HashMap<>();
        map.put("placa", PlacaVeh);
        map.put("nombre", NombreConductor);
        map.put("descripcion", DescripcionVeh);

        mfirestore.collection("vehiculos").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(getContext(), "Creado exitosamente", Toast.LENGTH_SHORT).show();
                getDialog().dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error al ingresar", Toast.LENGTH_SHORT).show();
            }
        });}
        private void getVeh() {
            mfirestore.collection("vehiculos").document(id_veh).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    String placaVeh = documentSnapshot.getString("placa");
                    String conductorVeh = documentSnapshot.getString("nombre");
                    String descripcionVeh = documentSnapshot.getString("descripcion");

                    edt_placa.setText(placaVeh);
                    edt_nombredelconductor.setText(conductorVeh);
                    edt_descripcion.setText(descripcionVeh);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Error al obtener los datos", Toast.LENGTH_SHORT).show();
                }
            });
        }}