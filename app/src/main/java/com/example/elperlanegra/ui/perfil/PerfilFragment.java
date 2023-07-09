package com.example.elperlanegra.ui.perfil;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
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


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


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

    EditText nombreAp, direccion, telef;
    Button actualizar;

    private int rotation;

    private static final int STORAGE_PERMISSION_REQUEST_CODE = 1;

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
                if (checkStoragePermission()) {
                    // Permiso concedido, abrir la galería para seleccionar la imagen
                    Intent intentIMG = new Intent();
                    intentIMG.setAction(Intent.ACTION_GET_CONTENT);
                    intentIMG.setType("image/*");
                    startActivityForResult(intentIMG, 33);
                }
                /*Intent intentIMG = new Intent();
                intentIMG.setAction(Intent.ACTION_GET_CONTENT);
                intentIMG.setType("image/*");
                startActivityForResult(intentIMG, 33);*/
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

        if (requestCode == 33 && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();

            Log.d("IMAGE_URI", "Image URI: " + imageUri.toString());

            //CORTAR IMAGEN
            // Obtener el path absoluto de la imagen
            String imagePath = getImagePath(imageUri);
            Log.d("IMAGE_PATH", "Image Path: " + imagePath);

            // Recortar la imagen y obtener el bitmap recortado
            Bitmap croppedBitmap = cropImage(imageUri);

            // Obtener la orientación de la imagen
            int rotation = getOrientationFromExif(imagePath);

            // Aplicar la rotación si es necesario
            if (rotation != 0) {
                Matrix matrix = new Matrix();
                matrix.postRotate(rotation);

                Bitmap rotatedBitmap = Bitmap.createBitmap(croppedBitmap, 0, 0, croppedBitmap.getWidth(), croppedBitmap.getHeight(), matrix, true);
                croppedBitmap.recycle(); // Liberar memoria del bitmap original
                croppedBitmap = rotatedBitmap; // Utilizar el bitmap rotado
            }

            // Subir el bitmap recortado a Firebase Storage
            uploadCroppedImage(croppedBitmap);

            // Cargar la imagen recortada en el ImageView
            profileIMG.setImageBitmap(croppedBitmap);
        }
    }

    private int getImageRotation(Uri imageUri) {
        String[] projection = { MediaStore.Images.ImageColumns.ORIENTATION };
        Cursor cursor = getActivity().getContentResolver().query(imageUri, projection, null, null, null);

        int rotation = 0;

        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(projection[0]);
            rotation = cursor.getInt(columnIndex);
            cursor.close();
        }

        return rotation;
    }

    private void uploadCroppedImage(Bitmap croppedBitmap) {
        final Bitmap finalCroppedBitmap = croppedBitmap;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        croppedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageData = baos.toByteArray();

        // Aplicar la rotación si es necesario
        if (rotation != 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(rotation);

            Bitmap rotatedBitmap = Bitmap.createBitmap(croppedBitmap, 0, 0, croppedBitmap.getWidth(), croppedBitmap.getHeight(), matrix, true);
            croppedBitmap.recycle(); // Liberar memoria del bitmap original
            croppedBitmap = rotatedBitmap; // Utilizar el bitmap rotado
        }

        // Subir el byte array de la imagen a Firebase Storage
        final StorageReference reference = storage.getReference().child("fotoPerfil")
                .child(FirebaseAuth.getInstance().getUid());
        reference.putBytes(imageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getContext(), "FOTO DE PERFIL ACTUALIZADA", Toast.LENGTH_SHORT).show();

                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        db.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                                .child("fotoPerfil").setValue(uri.toString());

                        Toast.makeText(getContext(), "FOTO DE PERFIL SUBIDA", Toast.LENGTH_SHORT).show();

                        // Cargar la imagen recortada en el ImageView
                        profileIMG.setImageBitmap(finalCroppedBitmap);
                    }
                });
            }
        });
    }

    private Bitmap cropImage(Uri imagePath) {
        // Configurar el tamaño deseado para el recorte
        int targetWidth = 500;
        int targetHeight = 500;

        try {
            InputStream inputStream = getActivity().getContentResolver().openInputStream(imagePath);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(inputStream, null, options);
            inputStream.close();

            // Verificar que el ancho sea válido
            if (options.outWidth <= 0) {
                Toast.makeText(getContext(), "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
                return null;
            }

            inputStream = getActivity().getContentResolver().openInputStream(imagePath);
            options.inJustDecodeBounds = false;
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
            inputStream.close();

            // Calcular las dimensiones y la escala para el recorte
            float scaleX = (float) targetWidth / bitmap.getWidth();
            float scaleY = (float) targetHeight / bitmap.getHeight();
            float scale = Math.max(scaleX, scaleY);
            float scaledWidth = scale * bitmap.getWidth();
            float scaledHeight = scale * bitmap.getHeight();

            // Calcular las coordenadas para el recorte
            float translateX = (targetWidth - scaledWidth) / 2;
            float translateY = (targetHeight - scaledHeight) / 2;

            // Crear la matriz de transformación
            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);
            matrix.postTranslate(translateX, translateY);

            // Aplicar la matriz de transformación al bitmap
            Bitmap croppedBitmap = Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(croppedBitmap);
            canvas.drawBitmap(bitmap, matrix, null);

            return croppedBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
            return null;
        }
    }


    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int width = options.outWidth;
        final int height = options.outHeight;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    private String getImagePath(Uri imageUri) {
        if (imageUri == null) {
            return null;
        }

        if (DocumentsContract.isDocumentUri(getActivity(), imageUri)) {
            String documentId = DocumentsContract.getDocumentId(imageUri);
            if (isExternalStorageDocument(imageUri)) {
                String[] parts = documentId.split(":");
                if (parts.length >= 2) {
                    String storageId = parts[0];
                    String path = parts[1];
                    return Environment.getExternalStorageDirectory() + "/" + path;
                }
            } else if (isDownloadsDocument(imageUri)) {
                Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.parseLong(documentId));
                return getDataColumn(getActivity(), contentUri, null, null);
            } else if (isMediaDocument(imageUri)) {
                String[] split = documentId.split(":");
                if (split.length >= 2) {
                    String type = split[0];
                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }
                    String selection = "_id=?";
                    String[] selectionArgs = new String[]{split[1]};
                    return getDataColumn(getActivity(), contentUri, selection, selectionArgs);
                }
            }
        } else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
            return getDataColumn(getActivity(), imageUri, null, null);
        } else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
            return imageUri.getPath();
        }

        return null;
    }

    private boolean isMediaDocument(Uri imageUri) {
        return "com.android.providers.media.documents".equals(imageUri.getAuthority());
    }

    private boolean isDownloadsDocument(Uri imageUri) {
        return "com.android.providers.downloads.documents".equals(imageUri.getAuthority());
    }

    private boolean isExternalStorageDocument(Uri imageUri) {
        return "com.android.externalstorage.documents".equals(imageUri.getAuthority());
    }

    private String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(projection[0]);
                return cursor.getString(columnIndex);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    ///////PERMISOS
    private boolean checkStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int readPermission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
            int writePermission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (readPermission != PackageManager.PERMISSION_GRANTED || writePermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        STORAGE_PERMISSION_REQUEST_CODE);
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido, puedes realizar las operaciones de almacenamiento aquí
            } else {
                // Permiso denegado, muestra un mensaje o realiza alguna acción alternativa
            }
        }
    }

    private int getOrientationFromExif(String imagePath) {
        try {
            ExifInterface exifInterface = new ExifInterface(imagePath);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return 90;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return 180;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return 270;
                default:
                    return 0;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

}


