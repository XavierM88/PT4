package fr.marchal.plantsraising.fragments

import android.os.Bundle
import android.view.View
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import fr.marchal.plantsraising.MainActivity
import fr.marchal.plantsraising.PlantModel
import fr.marchal.plantsraising.PlantRepository.Singleton.plantList
import fr.marchal.plantsraising.R
import fr.marchal.plantsraising.adapter.PlantAdapter
import fr.marchal.plantsraising.adapter.PlantItemDecoration

class HomeFragment(
    private val context: MainActivity,
) : Fragment() {
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_home, container, false)


        //recuperation du recyclerview
        val horizontalRecyclerView = view.findViewById<RecyclerView>(R.id.horizontal_recycler_view)
        horizontalRecyclerView.adapter = PlantAdapter(context, plantList.filter{ !it.liked }, R.layout.item_horizontal_plant)
        
        //recuperation du second recyclerview
        val verticalRecyclerView = view.findViewById<RecyclerView>(R.id.vertical_recycler_view)
        verticalRecyclerView.adapter = PlantAdapter(context, plantList, R.layout.item_vertical_plant)
        verticalRecyclerView.addItemDecoration(PlantItemDecoration())

        return view
    }

}