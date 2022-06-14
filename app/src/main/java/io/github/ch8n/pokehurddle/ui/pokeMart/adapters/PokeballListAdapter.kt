package io.github.ch8n.pokehurddle.ui.pokeMart.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.github.ch8n.pokehurddle.data.models.Pokeball
import io.github.ch8n.pokehurddle.databinding.ListCellMartItemBinding

class PokeballListAdapter(
    //👇 on click callback for pokeball item on recycler view
    private val onPokeballClicked: (ball: Pokeball) -> Unit,
) : RecyclerView.Adapter<PokeballItemVH>() {

    //👇 list of all pokeballs in our system
    private val pokeballs = Pokeball.values().toList()

    //👇 create Berry View Holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : PokeballItemVH = ListCellMartItemBinding
        //👇 create binding and view for viewholder item
        .inflate(LayoutInflater.from(parent.context), parent, false)
        //👇 create and pass binding and click callback in view holder
        .let { PokeballItemVH(it, onPokeballClicked) }

    //👇 bind viewholder with pokeball data
    override fun onBindViewHolder(holder: PokeballItemVH, position: Int) =
        holder.onBind(pokeballs.get(position))

    //👇 applying size of pokeballs
    override fun getItemCount(): Int = pokeballs.size
}

class PokeballItemVH(
    private val binding: ListCellMartItemBinding,
    private val onPokeballClicked: (ball: Pokeball) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    fun onBind(
        pokeball: Pokeball
    ) = with(binding) {
        //👇 load image url into image view
        Glide.with(root.context)
            .load(pokeball.imageUrl)
            .into(imgMartItem)
        //👇 apply pokeball name
        labelMartItem.text = pokeball.name
        //👇 apply pokeball price
        labelMartPrice.text = "${pokeball.price} Poke-coins"
        //👇 on click of viewholder root trigger click callback
        imgMartItem.setOnClickListener {
            onPokeballClicked.invoke(pokeball)
        }
    }
}
