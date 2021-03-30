package fr.marchal.plantsraising

import android.net.Uri
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import fr.marchal.plantsraising.PlantRepository.Singleton.databaseRef
import fr.marchal.plantsraising.PlantRepository.Singleton.downloadUri
import fr.marchal.plantsraising.PlantRepository.Singleton.plantList
import fr.marchal.plantsraising.PlantRepository.Singleton.storageReference
import java.util.*
import javax.security.auth.callback.Callback

class PlantRepository {

    object Singleton{
        //donne le lien de connexion au bucket
        private val BUCKET_URL: String = "gs://plantsraising.appspot.com"

        //connexion au stockage Firebase
        val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(BUCKET_URL)

        //connexion a la ref "plants"
        val databaseRef = FirebaseDatabase.getInstance().getReference("plants")

        //créer une liste qui contient les plantes
        val plantList = arrayListOf<PlantModel>()

        //contient le lien de l'image
        var downloadUri: Uri? = null
    }

    fun updateData(callback: () -> Unit) {
        databaseRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                //reinitialise la liste
                plantList.clear()
                //recolte la liste de plante
                for(ds in snapshot.children){
                    //construction d'un objet plante
                    val plant = ds.getValue(PlantModel::class.java)

                    //verifie si la plante à été chargé
                    if(plant != null){
                        //ajoute la plante à la liste
                        plantList.add(plant)
                    }
                }
                callback()
            }

            override fun onCancelled(error: DatabaseError) {}

        })
    }
    //fonction pour envoyer des fichier sur le storage de Firebase
    fun uploadImage(file: Uri, callback: () -> Unit){
        //verifie si le fichier est valide
        if (file != null) {
            val fileName =  UUID.randomUUID().toString() + ".jpg"
            val ref = storageReference.child(fileName)
            val uploadTask = ref.putFile(file)

            //demarre la tache d'envoie
            uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>>{ task ->

                //verifie si il y a eu un problème
                if(!task.isSuccessful){
                    task.exception?.let { throw it}
                }

                return@Continuation ref.downloadUrl

            }).addOnCompleteListener { task ->
                //verifie si tout à fonctionner
                if(task.isSuccessful){
                    //recupere l'image
                    downloadUri = task.result
                    callback()
                }
            }
        }
    }

    //met en jour un objet en plante dans la BDD
    fun updatePlant(plant: PlantModel) = databaseRef.child(plant.id).setValue(plant)

    //insere une nouvelle plante
    fun insertPlant(plant: PlantModel) = databaseRef.child(plant.id).setValue(plant)

    //supprime une plante de la BDD
    fun deletePlant(plant: PlantModel) = databaseRef.child(plant.id).removeValue()
}