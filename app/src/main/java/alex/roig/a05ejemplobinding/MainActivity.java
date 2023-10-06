package alex.roig.a05ejemplobinding;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import alex.roig.a05ejemplobinding.databinding.ActivityMainBinding;
import alex.roig.a05ejemplobinding.modelos.Alumno;

public class MainActivity extends AppCompatActivity {


    private ActivityMainBinding binding;
    private ActivityResultLauncher<Intent> launcherAlumno;
    private ArrayList<Alumno> listaAlumnos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
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
                    }else{
                        Toast.makeText(MainActivity.this, "NO HAY EXTRAS", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(MainActivity.this, "ACCION CANCELADA", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }


}