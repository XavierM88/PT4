package fr.marchal.plantsraising

class PlantModel (
        val id: String ="plant0",
        val name: String ="Nom d'une plante",
        val description: String = "petite desc",
        val imageUrl: String = "https://cdn.pixabay.com/photo/2016/01/13/14/41/cosmos-flowers-1138041__340.jpg",
        val water: String = "Faible",
        val brightness: String = "Beaucoup",
        val warmth: String = "Chaud",
        var liked: Boolean = false,

        )
