package shifttec.mecanicapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText edt_correo, edt_password;
    private Button btn_ingresar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        edt_correo = (EditText)findViewById(R.id.edt_correo_login);
        edt_password = (EditText)findViewById(R.id.edt_password_login);
        btn_ingresar = (Button)findViewById(R.id.btn_login_ingresar);

        btn_ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String EmailUser = edt_correo.getText().toString().trim();
                String PasswordUser = edt_password.getText().toString().trim();

                if(EmailUser.isEmpty() && PasswordUser.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Ingresar los datos", Toast.LENGTH_SHORT).show();
                }else{
                    loginUser(EmailUser, PasswordUser);
                }
            }
        });
    }

    private void loginUser(String emailUser, String passwordUser) {
        mAuth.signInWithEmailAndPassword(emailUser, passwordUser).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
            if(task.isSuccessful()){
                finish();
                startActivity(new Intent(LoginActivity.this, ActivityHistorial.class));
                Toast.makeText(LoginActivity.this,"Bievenido", Toast.LENGTH_SHORT).show();
            }else{
                 Toast.makeText(LoginActivity.this, "Error, Verfique su correo y/o contraseña",Toast.LENGTH_SHORT).show();
             }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            Toast.makeText(LoginActivity.this,"Error al iniciar sesión",Toast.LENGTH_SHORT).show();
            }
        });
    }
}