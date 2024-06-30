package com.example.upnbiblioteca.actividades;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.upnbiblioteca.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CargaActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carga);
        firebaseAuth = FirebaseAuth.getInstance();

        SharedPreferences preferences = getSharedPreferences("my_preferences", MODE_PRIVATE);
        boolean termsAccepted = preferences.getBoolean("terms_accepted", false);

        if (!termsAccepted) {
            showTermsAndConditionsDialog();
        } else {
            startNextActivity();
        }
    }

    private void showTermsAndConditionsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Términos y Condiciones");

        // Inflar el diseño personalizado
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_terms_conditions, null);
        builder.setView(dialogView);

        CheckBox checkBox = dialogView.findViewById(R.id.checkbox_terms);

        builder.setPositiveButton("Aceptar", (dialog, which) -> {
            if (checkBox.isChecked()) {
                // Guardar en SharedPreferences que los términos han sido aceptados
                SharedPreferences preferences = getSharedPreferences("my_preferences", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("terms_accepted", true);
                editor.apply();

                // Continuar con la lógica de tu actividad
                startNextActivity();
            } else {
                // Mostrar mensaje de que deben aceptar los términos
                Toast.makeText(this, "Debe aceptar los términos y condiciones para continuar", Toast.LENGTH_SHORT).show();
                showTermsAndConditionsDialog(); // Mostrar el diálogo nuevamente
            }
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> {
            // Manejar la acción de cancelar si es necesario
        });

        builder.setCancelable(false); // Para que no puedan cancelar el diálogo
        builder.show();
    }

    private void startNextActivity() {
        Thread tCarga = new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    FirebaseUser usuarioActual = firebaseAuth.getCurrentUser();
                    if (usuarioActual != null) {
                        // Si el usuario está autenticado, redirige a MenuActivity
                        Intent iMenu = new Intent(CargaActivity.this, MenuActivity.class); // Asegúrate de que MenuActivity exista
                        iMenu.putExtra("idBoton", 0);
                        startActivity(iMenu);
                    } else {
                        // Si no hay usuario autenticado, redirige a LoginActivity
                        Intent iLogin = new Intent(CargaActivity.this, LoginActivity.class);
                        startActivity(iLogin);
                    }
                    finish();
                }
            }
        };
        tCarga.start();
    }
}
