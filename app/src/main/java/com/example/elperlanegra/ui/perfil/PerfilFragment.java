package com.example.elperlanegra.ui.perfil;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.elperlanegra.R;
import com.example.elperlanegra.adaptadores.DatosActualesAdapter;
import com.example.elperlanegra.modelos.UserModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;


public class PerfilFragment extends Fragment {

    RecyclerView datosActuales_rec;

    FirebaseDatabase db;
    DatabaseReference usersRef;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseStorage storage;

    List<UserModel> userModelList;
    DatosActualesAdapter datosActualesAdapter;
    ShapeableImageView profileIMG, addfotoperfil;
    EditText nombreAp, direccion, telef, correo;
    Button actualizar;

    private int previousHeightDiffrence = 0;
    private final int KEYBOARD_HEIGHT_THRESHOLD = 150;
    private View rootView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_perfil, container, false);

        db = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        usersRef = db.getReference("Users").child(auth.getUid());
        storage = FirebaseStorage.getInstance();


        datosActuales_rec = root.findViewById(R.id.datosActuales_rec);
        datosActuales_rec.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));

        userModelList = new ArrayList<>();
        datosActualesAdapter = new DatosActualesAdapter(getActivity(), userModelList);
        datosActuales_rec.setAdapter(datosActualesAdapter);

        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                userModelList.clear();
                for (DataSnapshot snapshot :
                        datasnapshot.getChildren()) {
                    UserModel user = datasnapshot.getValue(UserModel.class);
                    userModelList.add(user);
                    String nombre = user.getNombreAp();
                    String direccion = user.getDireccion();
                    String correo = user.getEmail();
                    String telefono = user.getTelefono();


                    String uid = datasnapshot.getKey();
                }
                datosActualesAdapter.notifyDataSetChanged();
                ;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Falló la lectura: " + error.getCode(), Toast.LENGTH_SHORT).show();
            }
        });

        ////PARA ACTUALIZAR DATOS DEL PERFIL Y FOTO
        profileIMG = root.findViewById(R.id.profile_img);
        addfotoperfil = root.findViewById(R.id.addphoto_img);
        nombreAp = root.findViewById(R.id.perfil_nombre);
        direccion = root.findViewById(R.id.perfil_direccion);
        telef = root.findViewById(R.id.perfil_telefono);


        actualizar = root.findViewById(R.id.perfil_btn);

        db.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserModel userModel = snapshot.getValue(UserModel.class);

                        Glide.with(getContext()).load(userModel.getFotoPerfil()).into(profileIMG);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        addfotoperfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentIMG = new Intent();
                intentIMG.setAction(Intent.ACTION_GET_CONTENT);
                intentIMG.setType("image/*");
                startActivityForResult(intentIMG, 33);
            }
        });

        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserProfile();
            }
        });
        return root;
    }

    private void updateUserProfile() {

        String newNombreAp = nombreAp.getText().toString();
        String newDireccion = direccion.getText().toString();
        String newTelef = telef.getText().toString();

        if (newNombreAp.equals("") && newDireccion.equals("") && newTelef.equals("")) {
            Toast.makeText(getContext(), "Ingrese al menos un campo para actualizar", Toast.LENGTH_SHORT).show();
            return;
        }

        usersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String currentNombreAp = snapshot.child("nombreAp").getValue(String.class);
                String currentDireccion = snapshot.child("direccion").getValue(String.class);
                String currentTelef = snapshot.child("telefono").getValue(String.class);

                if (!newNombreAp.equals("") && !newNombreAp.equals(currentNombreAp)) {
                    usersRef.child("nombreAp").setValue(newNombreAp);
                }
                if (!newDireccion.equals("") && !newDireccion.equals(currentDireccion)) {
                    usersRef.child("direccion").setValue(newDireccion);
                }
                if (!newTelef.equals("") && !newTelef.equals(currentTelef)) {
                    usersRef.child("telefono").setValue(newTelef);
                }

                Toast.makeText(getContext(), "Dato/s actualizados correctamente", Toast.LENGTH_SHORT).show();
                nombreAp.setText("");
                direccion.setText("");
                telef.setText("");


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Manejar el error
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 33 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();

            //CORTAR IMAGEN
            // Iniciar actividad de recorte con uCrop


            // Aquí puedes actualizar la foto en Firebase Storage y obtener la URL de la imagen actualizada

            final StorageReference reference = storage.getReference().child("fotoPerfil")
                    .child(FirebaseAuth.getInstance().getUid());
            reference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getContext(), "FOTO DE PERFIL ACTUALIZADA", Toast.LENGTH_SHORT).show();

                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            db.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                                    .child("fotoPerfil").setValue(uri.toString());

                            Toast.makeText(getContext(), "FOTO DE PERFIL SUBIDA", Toast.LENGTH_SHORT).show();

                            Glide.with(getContext()).load(imageUri).into(profileIMG);
                        }
                    });


                }
            });
        }
    }
}


