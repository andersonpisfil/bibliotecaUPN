package com.example.upnbiblioteca.adaptadores;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.upnbiblioteca.R;
import com.example.upnbiblioteca.interfaces.Libro;
import com.loopj.android.http.Base64;

import java.util.List;

public class LibroAdapter extends RecyclerView.Adapter<LibroAdapter.ViewHolder> {
    private List<Libro> listaLibros;

    public LibroAdapter(List<Libro> listaLibros) {
        this.listaLibros = listaLibros;
    }

    @NonNull
    @Override
    public LibroAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_libro,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Libro libro = listaLibros.get(position);
        String portada = libro.getPortada();
        byte[] imageByte = Base64.decode(portada, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageByte,0, imageByte.length);
        holder.imgPortada.setImageBitmap(bitmap);
        holder.lblTitulo.setText(libro.getTitulo());
        holder.lblAutor.setText(libro.getAutor());
        holder.lblYear.setText(String.valueOf(libro.getYear()));
        holder.lblGenero.setText(libro.getGenero());
        holder.lblIdioma.setText(libro.getIdioma());
    }

    @Override
    public int getItemCount() {

        return listaLibros.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView carItemLibro;
        ImageView imgPortada;
        TextView lblYear,lblGenero, lblIdioma,lblTitulo,lblAutor;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            carItemLibro = itemView.findViewById(R.id.itmCarItemLibro);
            imgPortada = itemView.findViewById(R.id.itcImgPortada);
            lblTitulo = itemView.findViewById(R.id.itcLblTitulo);
            lblAutor = itemView.findViewById(R.id.itcLblAutor);
            lblYear = itemView.findViewById(R.id.itcLblYear);
            lblGenero = itemView.findViewById(R.id.itcLblGenero);
            lblIdioma = itemView.findViewById(R.id.itcLblIdioma);

        }
    }
}
