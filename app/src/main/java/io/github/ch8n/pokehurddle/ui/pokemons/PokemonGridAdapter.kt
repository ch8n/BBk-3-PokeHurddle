package io.github.ch8n.pokehurddle.ui.pokemons

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.github.ch8n.pokehurddle.data.models.PokemonDTO
import io.github.ch8n.pokehurddle.databinding.ListCellPokemonBinding

class PokemonGridAdapter private constructor(
    diffUtil: DiffUtil.ItemCallback<PokemonDTO>
) : ListAdapter<PokemonDTO, PokemonVH>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonVH {
        val binding = ListCellPokemonBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return PokemonVH(binding)
    }

    override fun onBindViewHolder(holder: PokemonVH, position: Int) {
        holder.onBind(getItem(position))
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<PokemonDTO>() {
            override fun areItemsTheSame(oldItem: PokemonDTO, newItem: PokemonDTO): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: PokemonDTO, newItem: PokemonDTO): Boolean =
                oldItem == newItem
        }

        fun newInstance() = PokemonGridAdapter(diffUtil)
    }
}

class PokemonVH(private val binding: ListCellPokemonBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun onBind(pokemonDTO: PokemonDTO) = with(binding) {
        Glide.with(root.context)
            .load(pokemonDTO.sprites.front_default)
            .into(imgPokemon)
        labelPokemon.text = pokemonDTO.name
    }
}