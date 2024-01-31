package shifttec.mecanicapp;

import static android.app.ProgressDialog.show;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class ActivityRegistro extends AppCompatActivity {
    private ImageView photo_V;
    private EditText edt_placa, edt_nombredelconductor, edt_descripcion;
    private Button boton_registro, btn_go_hist, btn_eliminar_fot, btn_subir_fot;
    private FirebaseFirestore mfirestore;
    private FirebaseAuth mAuth;

    StorageReference storageReference;
    String storage_path = "veh/*";

    private static final int COD_SEL_STORAGE = 200;
    private static final int COD_SEL_IMAGE = 300;
    private Uri image_url;
    String photo = "photo";
    String idd;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        String id = getIntent().getStringExtra("id_veh");
        mfirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        edt_placa = (EditText) findViewById(R.id.txt_placa);
        edt_nombredelconductor = (EditText) findViewById(R.id.txt_nombre_conductor);
        edt_descripcion = (EditText) findViewById(R.id.txt_descripcion_registro);
        boton_registro = (Button) findViewById(R.id.btn_agregar_registro);
        btn_go_hist = (Button) findViewById(R.id.btn_go_history);
        photo_V = (ImageView) findViewById(R.id.photo_veh);
        btn_subir_fot = (Button) findViewById(R.id.btn_foto_registro);
        btn_eliminar_fot = (Button) findViewById(R.id.btn_eliminar_foto_registro);

        btn_subir_fot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPhoto();
            }
        });
        btn_eliminar_fot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("photo", "");
                mfirestore.collection("veh").document(idd).update(map);
                Toast.makeText(ActivityRegistro.this, "Foto eliminada", Toast.LENGTH_SHORT).show();
            }
        });
        if(id == null || id == ""){
            boton_registro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String Placa_veh = edt_placa.getText().toString().trim();
                    String NombreConductor = edt_nombredelconductor.getText().toString().trim();
                    String DescripcionVeh = edt_descripcion.getText().toString().trim();

                    if(Placa_veh.isEmpty() && NombreConductor.isEmpty() && DescripcionVeh.isEmpty()){
                        Toast.makeText(getApplicationContext(),"Ingresar los datos",Toast.LENGTH_SHORT).show();
                    }else{
                        postVeh(Placa_veh, NombreConductor, DescripcionVeh);
                    }
                }
            });
        }else{
            idd = id;
            boton_registro.setText("Update");
            getVeh(id);
            boton_registro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String Placa_veh = edt_placa.getText().toString().trim();
                    String NombreConductor = edt_nombredelconductor.getText().toString().trim();
                    String DescripcionVeh = edt_descripcion.getText().toString().trim();
                    if(Placa_veh.isEmpty() && NombreConductor.isEmpty() && DescripcionVeh.isEmpty()){
                        Toast.makeText(getApplicationContext(),"Ingresar los datos",Toast.LENGTH_SHORT).show();
                    }else{
                        updateVeh(Placa_veh, NombreConductor, DescripcionVeh, id);
                    }
                }
            });
        }
        btn_go_hist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityRegistro.this, ActivityHistorial.class));
            }
        });

    }

    private void uploadPhoto() {
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(i, COD_SEL_IMAGE);
    }
        @Override
        public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            if(resultCode == RESULT_OK){
                if(requestCode == COD_SEL_IMAGE){
                    image_url = data.getData();
                    subirPhoto(image_url);
        }
    }super.onActivityResult(requestCode, resultCode, data);}

    private void subirPhoto(Uri image_url) {
        progressDialog.setMessage("Actualizando foto");
        progressDialog.show();
        String rute_storage_photo = storage_path+ "" + photo + "" + mAuth.getUid() +""+ idd;
        StorageReference reference = storageReference.child(rute_storage_photo);
        reference.putFile(image_url).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful());
                if(uriTask.isSuccessful()){
                    uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String download_uri = uri.toString();
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("photo", download_uri);
                            mfirestore.collection("veh").document(idd).update(map);
                            Toast.makeText(ActivityRegistro.this, "Foto Actualizada", Toast.LENGTH_SHORT);
                            progressDialog.dismiss();
                        }
                    });

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ActivityRegistro.this, "Error al cargar la foto", Toast.LENGTH_SHORT);
            }
        });
    }

    private void updateVeh(String placaVeh, String nombreConductor, String descripcionVeh, String id) {
        Map<String, Object> map = new HashMap<>();
        map.put("placa", placaVeh);
        map.put("nombre", nombreConductor);
        map.put("descripcion", descripcionVeh);

        mfirestore.collection("veh").document(id).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(), "Actualizado exitosamente", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error al actualizar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void postVeh(String PlacaVeh, String NombreConductor, String DescripcionVeh) {
        Map<String, Object> map = new HashMap<>();
        map.put("placa", PlacaVeh);
        map.put("nombre", NombreConductor);
        map.put("descripcion", DescripcionVeh);

        mfirestore.collection("veh").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(getApplicationContext(), "Creado exitosamente", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error al ingresar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getVeh(String id){
        mfirestore.collection("veh").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
            String placaVeh = documentSnapshot.getString("placa");
            String conductorVeh = documentSnapshot.getString("nombre");
            String descripcionVeh = documentSnapshot.getString("descripcion");
            String photoVeh = documentSnapshot.getString("photo");

            edt_placa.setText(placaVeh);
            edt_nombredelconductor.setText(conductorVeh);
            edt_descripcion.setText(descripcionVeh);

            try{
                if(!photoVeh.equals("")){
                    Toast toast = Toast.makeText(getApplicationContext(),"Cargando foto", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 0, 200);
                    toast.show();
                    Picasso.get()
                            .load(photoVeh)
                            .resize(150,150)
                            .into(photo_V);
                }
            }catch (Exception e){
                Log.v("Error","e: "+e);
            }}
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Error al obtener los datos",Toast.LENGTH_SHORT).show();
            }
        });
    }
}