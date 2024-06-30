package com.example.upnbiblioteca.fragmentos;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.upnbiblioteca.R;
import com.example.upnbiblioteca.adaptadores.LibroAdapter;
import com.example.upnbiblioteca.interfaces.Libro;
import com.example.upnbiblioteca.sqLite.BdUpnBiblioteca;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class BuscarFragment extends Fragment {

    final static String urlBuscarLibros = "https://bibliotecaupn2024.000webhostapp.com/Servicios/ander.php";

    private AutoCompleteTextView autoCompleteSearch;
    private Button btnSearch;
    private RecyclerView recLibros;
    private ArrayList<Libro> listaLibro;
    private LibroAdapter adapter;

    private TextView tvNoResults;
    private BdUpnBiblioteca historial;
    private ArrayAdapter<String> autoCompleteAdapter;

    public BuscarFragment() {
        // Constructor público requerido
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        historial = new BdUpnBiblioteca(getContext());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Creamos un ArrayAdapter para el AutoCompleteTextView
        autoCompleteAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line);
        autoCompleteSearch.setAdapter(autoCompleteAdapter);

        // Agregar un TextChangedListener al AutoCompleteTextView
        autoCompleteSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Obtener sugerencias basadas en el texto ingresado
                obtenerSugerencias(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Obtener los datos de la base de datos y agregarlos al ArrayAdapter
        obtenerLibrosGuardados();
    }

    private void obtenerLibrosGuardados() {
        // Obtener los títulos de la base de datos
        SQLiteDatabase db = historial.getReadableDatabase();
        Cursor cursor = db.query(BdUpnBiblioteca.TABLE_HISTORIAL,
                new String[]{BdUpnBiblioteca.COLUMN_PALABRA},
                null,
                null,
                null,
                null,
                null);

        // Agregar los títulos al ArrayAdapter
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String titulo = cursor.getString(cursor.getColumnIndexOrThrow(BdUpnBiblioteca.COLUMN_PALABRA));
                autoCompleteAdapter.add(titulo);
            }
            cursor.close();
        }
    }

    private void obtenerSugerencias(String texto) {
        // Limpiar el adaptador antes de agregar nuevas sugerencias
        autoCompleteAdapter.clear();

        // Obtener sugerencias de la base de datos
        SQLiteDatabase db = historial.getReadableDatabase();
        Cursor cursor = db.query(BdUpnBiblioteca.TABLE_HISTORIAL,
                new String[]{BdUpnBiblioteca.COLUMN_PALABRA},
                BdUpnBiblioteca.COLUMN_PALABRA + " LIKE ?",
                new String[]{"%" + texto + "%"},
                null,
                null,
                null);

        // Agregar las sugerencias al adaptador
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String titulo = cursor.getString(cursor.getColumnIndexOrThrow(BdUpnBiblioteca.COLUMN_PALABRA));
                autoCompleteAdapter.add(titulo);
            }
            cursor.close();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buscar, container, false);
        recLibros = view.findViewById(R.id.frgBusRecBusqueda);
        autoCompleteSearch = view.findViewById(R.id.frgBusAutoCompleteSearch);
        btnSearch = view.findViewById(R.id.btnSearch);
        tvNoResults = view.findViewById(R.id.frgBusAutoCompleteSearch); // TextView para el mensaje de no hay resultados
        listaLibro = new ArrayList<>();
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recLibros.setLayoutManager(manager);
        adapter = new LibroAdapter(listaLibro);
        recLibros.setAdapter(adapter);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titulo = autoCompleteSearch.getText().toString().trim();
                if (!TextUtils.isEmpty(titulo)) {

                    buscarLibros(titulo);
                    historial.insertPalabra(titulo);
                } else {
                    Toast.makeText(getContext(), "Ingrese un título para buscar", Toast.LENGTH_SHORT).show();
                }
                autoCompleteSearch.setText("");
            }
        });

        return view;
    }

    private void buscarLibros(String titulo) {
        AsyncHttpClient ahcMostrarLibros = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("titulo", titulo);
        ahcMostrarLibros.get(urlBuscarLibros, params, new BaseJsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Object response) {
                if (statusCode == 200) {
                    try {
                        JSONArray jsonArray = new JSONArray(rawJsonResponse);
                        listaLibro.clear();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            listaLibro.add(new Libro(
                                    jsonArray.getJSONObject(i).getInt("id"),
                                    jsonArray.getJSONObject(i).getString("portada"),
                                    jsonArray.getJSONObject(i).getString("titulo"),
                                    jsonArray.getJSONObject(i).getString("autor"),
                                    jsonArray.getJSONObject(i).getInt("year"),
                                    jsonArray.getJSONObject(i).getString("genero"),
                                    jsonArray.getJSONObject(i).getString("idioma")
                            ));
                        }
                        adapter.notifyDataSetChanged();


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "No se encontraron coincidencias", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonResponse, Object errorResponse) {
                Toast.makeText(getContext(), "Error en la solicitud: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            protected Object parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return null; // No es necesario realizar un análisis adicional aquí
            }
        });
    }
}