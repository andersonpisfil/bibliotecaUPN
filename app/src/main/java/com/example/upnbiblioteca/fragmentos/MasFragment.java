package com.example.upnbiblioteca.fragmentos;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.upnbiblioteca.R;
import com.example.upnbiblioteca.actividades.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class MasFragment extends Fragment implements View.OnClickListener {
    CheckBox cbxNotificaaciones, cbxSonido;
    Spinner cboIdiomas;
    Button btnGuardar, btnSalir, btnAcercaDe;
    private FirebaseAuth firebaseAuth;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public MasFragment() {
        // Required empty public constructor
    }

    public static MasFragment newInstance(String param1, String param2) {
        MasFragment fragment = new MasFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_mas, container, false);

        cbxNotificaaciones = vista.findViewById(R.id.frgMasNotificaciones);
        cbxSonido = vista.findViewById(R.id.frgMasSonido);
        cboIdiomas = vista.findViewById(R.id.frgMasIdioma);
        btnGuardar = vista.findViewById(R.id.frgMasBtnGuardar);
        btnSalir = vista.findViewById(R.id.frgMasBtnCerrarSesion);
        btnAcercaDe = vista.findViewById(R.id.btn_acerca_de);

        btnGuardar.setOnClickListener(this);
        btnSalir.setOnClickListener(this);
        btnAcercaDe.setOnClickListener(this);

        llenarIdiomas();
        cargarPreferencias();
        return vista;
    }

    private void cargarPreferencias() {
        SharedPreferences preferences = getActivity().getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
        boolean notificaciones = preferences.getBoolean("Notificaciones", false);
        boolean sonido = preferences.getBoolean("Sonido", false);
        int idioma = preferences.getInt("Idioma", 0);
        cbxNotificaaciones.setChecked(notificaciones);
        cbxSonido.setChecked(sonido);
        cboIdiomas.setSelection(idioma);
    }

    private void llenarIdiomas() {
        String idiomas[] = {"Español", "Ingles", "Quechua"};
        cboIdiomas.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, idiomas));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.frgMasBtnGuardar)
            guardar();
        else if (v.getId() == R.id.frgMasBtnCerrarSesion)
            salir();
        else if (v.getId() == R.id.btn_acerca_de)
            mostrarAcercaDeDialog();
    }

    private void salir() {
        firebaseAuth.signOut();
        getActivity().finish();
        Intent inicioSesion = new Intent(getContext(), LoginActivity.class);
        startActivity(inicioSesion);
    }

    private void guardar() {
        SharedPreferences preferences = getActivity().getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("Notificaciones", cbxNotificaaciones.isChecked());
        editor.putBoolean("Sonido", cbxSonido.isChecked());
        editor.putInt("Idioma", cboIdiomas.getSelectedItemPosition());
        editor.commit();
        Toast.makeText(getContext(), "Preferencias Guardadas", Toast.LENGTH_SHORT).show();
    }

    private void mostrarAcercaDeDialog() {
        AcercaDeDialogFragment dialogFragment = new AcercaDeDialogFragment();
        dialogFragment.show(getChildFragmentManager(), "AcercaDeDialogFragment");
    }
}
