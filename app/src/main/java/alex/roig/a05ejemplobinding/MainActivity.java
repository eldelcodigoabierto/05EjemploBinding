package alex.roig.a05ejemplobinding;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import alex.roig.a05ejemplobinding.databinding.ActivityMainBinding;
import alex.roig.a05ejemplobinding.modelos.Alumno;

public class MainActivity extends AppCompatActivity {


    private ActivityMainBinding binding;
    private ActivityResultLauncher<Intent> launcherAlumno;
    private ActivityResultLauncher<Intent> editAlumnoLauncher;

    private ArrayList<Alumno> listaAlumnos;

    private int posicion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        listaAlumnos = new ArrayList<>();
        inicializarlauncher();


        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               //LANZAR LA ACTIVIDAD ADDALUMNO
                launcherAlumno.launch(new Intent(MainActivity.this, AddAlumnoActivity.class));
            }
        });
    }

    private void inicializarlauncher() {
        launcherAlumno = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK) {
                    if (result.getData() != null && result.getData().getExtras() != null) {
                        Alumno alumno = (Alumno) result.getData().getExtras().getSerializable("ALUMNO");
                        listaAlumnos.add(alumno);
                        mostrarAlumnos();
                    }else{
                        Toast.makeText(MainActivity.this, "NO HAY EXTRAS", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(MainActivity.this, "ACCION CANCELADA", Toast.LENGTH_SHORT).show();
                }
            }

        }
        );
        editAlumnoLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
               //que ocurrira cuando se edita un alumno
                if (result.getResultCode() == RESULT_OK){
                    if(result.getData() != null && result.getData().getExtras() != null){
                        //pulsaron editar
                        Alumno alumno = (Alumno) result.getData().getExtras().getSerializable("ALUMNO");
                        listaAlumnos.set(posicion, alumno);
                        mostrarAlumnos();
                    }else{
                        //pulsaron borrar
                        listaAlumnos.remove(posicion);
                        mostrarAlumnos();
                    }
                }
            }
        });
    }

    private void mostrarAlumnos() {
        //eliminar lo que haya en el linearlayout
        binding.contentMain.contenedorMain.removeAllViews();

        for (Alumno alumno : listaAlumnos) {
            LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);

            View alumnoView = layoutInflater.inflate(R.layout.alumno_fila_view, null);
            TextView txtNombre = alumnoView.findViewById(R.id.lbNombreAlumnoView);
            TextView txtApellidos = alumnoView.findViewById(R.id.lbApellidosAlumnoView);
            TextView txtCiclo = alumnoView.findViewById(R.id.lbCicloAlumnoView);
            TextView txtGrupo = alumnoView.findViewById(R.id.lbGrupoAlumnoView);

            txtNombre.setText(alumno.getNombre());
            txtApellidos.setText(alumno.getApellidos());
            txtCiclo.setText(alumno.getCiclo());
            txtGrupo.setText(String.valueOf(alumno.getGrupo()));

  alumnoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //enviar el alumno
                    Intent intent = new Intent(MainActivity.this, EditAlumnoActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("ALUMNO", alumno);
                    intent.putExtras(bundle);

                    posicion = listaAlumnos.indexOf(alumno);

                    //recibir el alumno modificado o la orden de eliminar
                    editAlumnoLauncher.launch(intent);

                }
            });

            binding.contentMain.contenedorMain.addView(alumnoView);
        }
    }


}