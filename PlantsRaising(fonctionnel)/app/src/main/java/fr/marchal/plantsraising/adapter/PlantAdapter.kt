package fr.marchal.plantsraising.adapter;

import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.view.LayoutInflater
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import fr.marchal.plantsraising.*

class PlantAdapter(
        val context: MainActivity,
        private val plantList: List<PlantModel>,
        private val layoutId: Int
        ) : RecyclerView.Adapter<PlantAdapter.ViewHolder>() {

    //boite de rangement des composants controlés
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val plantImage = view.findViewById<ImageView>(R.id.image_item)
        val plantName:TextView? = view.findViewById(R.id.name_item)
        val plantDescription:TextView? = view.findViewById(R.id.description_item)
        val starIcon = view.findViewById<ImageView>(R.id.star_icon)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
                .from(parent.context)
                .inflate(layoutId, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //recupere les informations de la plante
        val currentPlant = plantList[position]

        //recupere le repository
        val repo = PlantRepository()

        //recuperer l'image a partir du lien
        Glide.with(context).load(Uri.parse(currentPlant.imageUrl)).into(holder.plantImage)

        //met à jour le nom de la plante
        holder.plantName?.text = currentPlant.name

        //met a jout la description de la plante
        holder.plantDescription?.text = currentPlant.description
        
        //verifie si la plante est dans les favoris
        if(currentPlant.liked){
            holder.starIcon.setImageResource(R.drawable.ic_star)
        }
        else{
            holder.starIcon.setImageResource(R.drawable.ic_unstar)
        }

        //rajout de l'action like/unlike
        holder.starIcon.setOnClickListener{
            currentPlant.liked = !currentPlant.liked
            repo.updatePlant(currentPlant)
        }

        //interaction affichage popup
        holder.itemView.setOnClickListener{
            PlantPopup(this, currentPlant).show()
        }

    }

    override fun getItemCount(): Int = plantList.size

}