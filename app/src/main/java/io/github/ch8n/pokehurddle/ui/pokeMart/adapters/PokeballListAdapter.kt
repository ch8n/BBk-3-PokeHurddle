package io.github.ch8n.pokehurddle.ui.pokeMart.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.github.ch8n.pokehurddle.data.models.Pokeball
import io.github.ch8n.pokehurddle.data.models.Pokeball.*
import io.github.ch8n.pokehurddle.databinding.ListCellMartItemBinding

class PokeballListAdapter(
    private val onPokeballClicked: (ball: Pokeball) -> Unit,
) : RecyclerView.Adapter<PokeballItemVH>() {

    private val pokeballs = listOf(MasterBall, UltraBall, GreatBall, LuxuryBall, NormalBall)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokeballItemVH =
        ListCellMartItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
            .let { PokeballItemVH(it, onPokeballClicked) }

    override fun onBindViewHolder(holder: PokeballItemVH, position: Int) = holder
        .onBind(pokeballs.get(position))

    override fun getItemCount(): Int = pokeballs.size
}

class PokeballItemVH(
    private val binding: ListCellMartItemBinding,
    private val onPokeballClicked: (ball: Pokeball) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    fun onBind(
        pokeball: Pokeball
    ) = with(binding) {
        Glide.with(root.context)
            .load(pokeball.imageUrl)
            .into(imgMartItem)
        labelMartItem.text = pokeball.name
        labelMartPrice.text = "${pokeball.price} Poke-coins"
        root.setOnClickListener {
            onPokeballClicked.invoke(pokeball)
        }
    }
}
