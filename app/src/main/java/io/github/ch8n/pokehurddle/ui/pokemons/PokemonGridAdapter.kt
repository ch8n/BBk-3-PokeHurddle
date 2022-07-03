package io.github.ch8n.pokehurddle.ui.pokemons

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.github.ch8n.pokehurddle.data.models.Pokemon
import io.github.ch8n.pokehurddle.databinding.ListCellPokemonBinding

class PokemonGridAdapter() : RecyclerView.Adapter<PokemonVH>() {

    // list of pokemon
    private val currentList = mutableListOf<Pokemon>()

    // update pokemon list in recycler view
    fun submitList(pokemon: List<Pokemon>) {
        currentList.clear()
        currentList.addAll(pokemon)
        notifyDataSetChanged()
    }

    // inflate UI for pokemon and bind to Pokemon view holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonVH {
        val binding = ListCellPokemonBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return PokemonVH(binding)
    }

    // bind list data to view holder
    override fun onBindViewHolder(holder: PokemonVH, position: Int) {
        holder.onBind(currentList[position])
    }

    // items in recycler view
    override fun getItemCount(): Int = currentList.size
}

// Recycler View Holder for Pokemon
class PokemonVH(private val binding: ListCellPokemonBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun onBind(pokemon: Pokemon) = with(binding) {
        // set pokemon image
        Glide.with(root.context)
            .load(pokemon.imageUrl)
            .into(imgPokemon)
        // set Pokemon name
        labelPokemon.text = pokemon.name
    }
}