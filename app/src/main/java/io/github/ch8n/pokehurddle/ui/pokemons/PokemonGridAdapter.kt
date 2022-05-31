package io.github.ch8n.pokehurddle.ui.pokemons

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.github.ch8n.pokehurddle.data.models.Pokemon
import io.github.ch8n.pokehurddle.databinding.ListCellPokemonBinding

class PokemonGridAdapter private constructor(
    diffUtil: DiffUtil.ItemCallback<Pokemon>
) : ListAdapter<Pokemon, PokemonVH>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonVH {
        val binding = ListCellPokemonBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return PokemonVH(binding)
    }

    override fun onBindViewHolder(holder: PokemonVH, position: Int) {
        holder.onBind(getItem(position))
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<Pokemon>() {
            override fun areItemsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean =
                oldItem == newItem
        }

        fun newInstance() = PokemonGridAdapter(diffUtil)
    }
}

class PokemonVH(private val binding: ListCellPokemonBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun onBind(pokemon: Pokemon) = with(binding) {
        Glide.with(root.context)
            .load(pokemon.imageUrl)
            .into(imgPokemon)
        labelPokemon.text = pokemon.name
    }
}