package com.example.upnbiblioteca.actividades;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.upnbiblioteca.R;
import com.example.upnbiblioteca.interfaces.Hash;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    //definimos los objetos
    private CheckBox chkRecordar;
    private EditText txtCorreo, txtPassword;
    private Button btnLogear;
    private ProgressDialog progressDialog;

    //Declaramos un objeto firebaseAuth
    private FirebaseAuth firebaseAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        //inicializamos el objeto firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();
        //Referenciamos los views
        txtCorreo = (EditText) findViewById(R.id.logTxtCorreo);
        txtPassword = (EditText) findViewById(R.id.logTxtClave);
        btnLogear = (Button) findViewById(R.id.LogBtnIniciar);

        //Verificamos si hay una sesion activa
        progressDialog = new ProgressDialog(this);
        //attaching listener to button
        btnLogear.setOnClickListener((View.OnClickListener) this);





        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void loginUsuario(String correo,String password) {

        Hash hash = new Hash();

        //Obtenemos el email y la contraseña desde las cajas del texto
        //String correo = txtCorreo.getText().toString().trim();
        //String password = txtPassword.getText().toString().trim();
            //Verificamos que las cajas de texto no esten vacias
            if (TextUtils.isEmpty(correo)) {
                Toast.makeText(this, "Se debe ingresar un email", Toast.LENGTH_LONG).show();
                return;
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Se debe ingresar una contraseña", Toast.LENGTH_LONG).show();
                return;
            }

            //loguear un nuevo usuario
            firebaseAuth.signInWithEmailAndPassword(correo, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //checking if success
                            if (task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Bienvenido: " + correo, Toast.LENGTH_LONG).show();
                                Intent iMenu = new Intent(LoginActivity.this, MenuActivity.class);
                                //Aqui creo una carga extra enviado como id: idboton
                                //y el numero 0 representa el fragmento con el que voy a arrancar la avivity Menu
                                iMenu.putExtra("idBoton", 0);
                                startActivity(iMenu);
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "Error de acceso", Toast.LENGTH_LONG).show();
                            }
                            progressDialog.dismiss();
                        }
                    });



    }





    @Override
    public void onClick(View v) {

        loginUsuario(txtCorreo.getText().toString(),txtPassword.getText().toString());
    }

}