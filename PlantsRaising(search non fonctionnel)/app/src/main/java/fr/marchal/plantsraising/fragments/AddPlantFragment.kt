package fr.marchal.plantsraising.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import androidx.fragment.app.Fragment
import fr.marchal.plantsraising.MainActivity
import fr.marchal.plantsraising.PlantModel
import fr.marchal.plantsraising.PlantRepository
import fr.marchal.plantsraising.PlantRepository.Singleton.downloadUri
import fr.marchal.plantsraising.R
import java.util.*

class AddPlantFragment(
        private val context: MainActivity
) : Fragment() {

    private var file: Uri? = null
    private var uploadedImage: ImageView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_add_plant, container, false)

        //recupere uploadImage
        uploadedImage = view.findViewById(R.id.preview_image)

        //recupere le bouton d'image
        val pickupImageButton = view.findViewById<Button>(R.id.upload_button)

        //ouvre la galerie photo du telephone
        pickupImageButton.setOnClickListener{ pickupImage() }

        //recupere le bouton de confirmation
        val confirmButton = view.findViewById<Button>(R.id.confirm_button)
        confirmButton.setOnClickListener { sendForm(view) }

        return view
    }

    private fun sendForm(view: View) {
        val repo = PlantRepository()
        repo.uploadImage(file!!) {
            val plantName = view.findViewById<EditText>(R.id.name_input).text.toString()
            val plantDescription = view.findViewById<EditText>(R.id.description_input).text.toString()
            val water = view.findViewById<Spinner>(R.id.water_spinner).selectedItem.toString()
            val brightness = view.findViewById<Spinner>(R.id.brightness_spinner).selectedItem.toString()
            val warmth = view.findViewById<Spinner>(R.id.warmth_spinner).selectedItem.toString()
            val downloadImageUrl = downloadUri

            //creer un nouvel objet PlantModel
            val plant = PlantModel(
                    UUID.randomUUID().toString(),
                    plantName,
                    plantDescription,
                    downloadImageUrl.toString(),
                    water,
                    brightness,
                    warmth
            )
            //envoie dans la BDD
            repo.insertPlant(plant)
        }
    }

    private fun pickupImage() {
        val intent = Intent()
        intent.type = "image/"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 47)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 47 && resultCode == Activity.RESULT_OK){
            //verifie si les données sont réceptionnées
            if(data == null || data.data == null) return
            //recupere l'image
            file = data.data

            //met à jour l'affichage
            uploadedImage?.setImageURI(file)


        }

    }
}