package com.example.upnbiblioteca.actividades;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.upnbiblioteca.R;
import com.example.upnbiblioteca.fragmentos.BuscarFragment;
import com.example.upnbiblioteca.fragmentos.EstanteriaFragment;
import com.example.upnbiblioteca.fragmentos.InicioFragment;
import com.example.upnbiblioteca.fragmentos.LugarFragment;
import com.example.upnbiblioteca.fragmentos.MasFragment;
import com.example.upnbiblioteca.interfaces.Menu;


public class MenuActivity extends AppCompatActivity implements Menu {


    private ViewPager viewPager;
    private MyPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        viewPager = findViewById(R.id.viewPager);
        pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        // Recuperar el índice del fragmento de inicio desde el intent
        int idBoton = getIntent().getIntExtra("idBoton", 0); // 0 como valor predeterminado
        viewPager.setCurrentItem(idBoton);
    }

    @Override
    public void onClickMenu(int idBoton) {
        viewPager.setCurrentItem(idBoton, true);
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new InicioFragment();
                case 1:
                    return new BuscarFragment();
                case 2:
                    return new EstanteriaFragment();
                case 3:
                    return new LugarFragment();
                case 4:
                    return new MasFragment();

                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 5; // Número total de fragmentos
        }
    }
}


/*package com.example.upnbiblioteca.actividades;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.upnbiblioteca.R;
import com.example.upnbiblioteca.fragmentos.BuscarFragment;
import com.example.upnbiblioteca.fragmentos.EstanteriaFragment;
import com.example.upnbiblioteca.fragmentos.InicioFragment;
import com.example.upnbiblioteca.fragmentos.ListasFragment;
import com.example.upnbiblioteca.fragmentos.MasFragment;
import com.example.upnbiblioteca.interfaces.Menu;

public class MenuActivity extends AppCompatActivity implements Menu {

    Fragment[] fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });
        //defino el array de fragmentos en este caso 5
        fragments = new Fragment[5];
        fragments[0]=new InicioFragment();
        fragments[1]=new BuscarFragment();
        fragments[2]=new EstanteriaFragment();
        fragments[3]=new ListasFragment();
        fragments[4]=new MasFragment();
        //Recupero la carga del Intent de la activity anterior con la idBoton
        //en este caso el valor que se llamara debe ser 0 y segun el array el fragmento 0 es Inicio
        //Por lo tanto debe arrancar en el fragmento inicio
        int idBoton = getIntent().getIntExtra("idBoton",-1);
        onClickMenu(idBoton);
    }

    @Override
    public void onClickMenu(int idBoton) {

        //con el replace alternamos segun el idbton asignado poor el array

        FragmentManager fm =getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.menRelArea,fragments[idBoton]);
        ft.commit();

    }
}*/