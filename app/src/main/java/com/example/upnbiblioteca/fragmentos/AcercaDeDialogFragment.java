package com.example.upnbiblioteca.fragmentos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.upnbiblioteca.R;

public class AcercaDeDialogFragment extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_acerca_de_dialog, container, false);

        Button btnClose = view.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Obtener parámetros de la ventana del diálogo
        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        // Establecer ancho y alto del diálogo
        params.width = WindowManager.LayoutParams.MATCH_PARENT; // Ancho se ajusta a MATCH_PARENT
        params.height = WindowManager.LayoutParams.WRAP_CONTENT; // Alto se ajusta automáticamente según el contenido
        // Aplicar los parámetros a la ventana del diálogo
        getDialog().getWindow().setAttributes(params);
    }
}
