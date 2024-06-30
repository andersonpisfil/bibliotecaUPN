package com.example.upnbiblioteca.fragmentos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.upnbiblioteca.R;
import com.example.upnbiblioteca.adaptadores.LibroAdapter;
import com.example.upnbiblioteca.interfaces.Libro;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InicioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InicioFragment extends Fragment {
    final static String urlMostrarLibros = "https://bibliotecaupn2024.000webhostapp.com/Servicios/mostrarLibros.php";
    RecyclerView recLibros;
    ArrayList<Libro>lista;
    LibroAdapter adapter = null;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public InicioFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InicioFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InicioFragment newInstance(String param1, String param2) {
        InicioFragment fragment = new InicioFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inicio, container, false);
        recLibros = view.findViewById(R.id.frgIniRecInicio);
        lista = new ArrayList<>();
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recLibros.setLayoutManager(manager);
        adapter = new LibroAdapter(lista);
        recLibros.setAdapter(adapter);
        mostrarLibros();
        return view;
    }

    private void mostrarLibros() {
        AsyncHttpClient ahcMostrarLibros = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("id","-1");
        ahcMostrarLibros.get(urlMostrarLibros, params, new BaseJsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Object response) {
                if(statusCode == 200){
                    try {
                        JSONArray jsonArray =new JSONArray(rawJsonResponse);
                        lista.clear();
                        for(int i =0;i<jsonArray.length();i++){
                            lista.add(new Libro(jsonArray.getJSONObject(i).getInt("id"),
                                    jsonArray.getJSONObject(i).getString("portada"),
                                    jsonArray.getJSONObject(i).getString("titulo"),
                                    jsonArray.getJSONObject(i).getString("autor"),
                                    jsonArray.getJSONObject(i).getInt("year"),
                                    jsonArray.getJSONObject(i).getString("genero"),
                                    jsonArray.getJSONObject(i).getString("idioma")));
                            adapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }

            }

            @Override
            public void onFailure(int i, Header[] headers, Throwable throwable, String s, Object o) {

            }

            @Override
            protected Object parseResponse(String s, boolean b) throws Throwable {
                return null;
            }
        });


    }
}