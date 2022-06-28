package io.github.ch8n.pokehurddle.ui.bag.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.github.ch8n.pokehurddle.data.models.Berries
import io.github.ch8n.pokehurddle.data.models.Pokeball
import io.github.ch8n.pokehurddle.databinding.ListCellBagItemBinding

// Recycler view list item view holder for Bag list item
class BagItemCommonViewHolder(
    private val binding: ListCellBagItemBinding,
    // pokeball clicked callback
    private val onPokeballClicked: (pokeball: Pokeball) -> Unit = {},
    // berry clicked callback
    private val onBerryClicked: (berry: Berries) -> Unit = {},
) : RecyclerView.ViewHolder(binding.root) {

    // we are re-using same holder for pokeball and berries
    // thus we have two different bind function

    // called to bind pokeball details
    fun onBindPokeball(pokeball: Pokeball, qty: Int) = with(binding) {
        Glide.with(root.context)
            .load(pokeball.imageUrl)
            .into(imgItem)
        labelItem.text = pokeball.name
        labelQty.text = "x$qty"
        root.setOnClickListener { onPokeballClicked.invoke(pokeball) }
    }

    // called to bind berries detail
    fun onBindBerry(berries: Berries, qty: Int) = with(binding) {
        Glide.with(root.context)
            .load(berries.imageUrl)
            .into(imgItem)
        labelItem.text = berries.name
        labelQty.text = "x$qty"
        root.setOnClickListener { onBerryClicked.invoke(berries) }
    }
}


// Recycler view adapter to bind Pokeball for Player Bag UI
class BagPokeBallAdapter(
    // pokeball click listener callback
    private val onPokeballClicked: (pokeball: Pokeball) -> Unit = {},
) : RecyclerView.Adapter<BagItemCommonViewHolder>() {

    // list of pairs containing information about Pokeball and player owned quantity
    private val pokeballs = mutableListOf<Pair<Pokeball, Int>>()

    // setter function that pass pokeball details of the player
    fun setPlayerPokeball(playerPokeballs: Map<String, Int>) {
        // from player pokeball map
        playerPokeballs
            .map { (key, value) ->
                // get the pokeball details from enum
                val pokeball = Pokeball.valueOf(key)
                // pair it with quantity of player
                pokeball to value
            }.let { balls ->
                // reset list
                pokeballs.clear()
                // add data to list
                pokeballs.addAll(balls)
            }
        // refresh recycler view to reflect list changes
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BagItemCommonViewHolder {
        return ListCellBagItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
            .let {
                // create view holder and pass pokeball click callback
                BagItemCommonViewHolder(it, onPokeballClicked = onPokeballClicked)
            }
    }

    override fun onBindViewHolder(holder: BagItemCommonViewHolder, position: Int) {
        // get pokeball and qauntity from the list
        val (pokeball, qty) = pokeballs.get(position)
        // bind pokeball details on holder
        holder.onBindPokeball(
            pokeball = pokeball,
            qty = qty
        )
    }

    // pokeball list size would be item count
    override fun getItemCount(): Int = pokeballs.size
}

class BagBerriesAdapter(
    private val onBerryClicked: (berry: Berries) -> Unit = {},
) : RecyclerView.Adapter<BagItemCommonViewHolder>() {

    private val berries = mutableListOf<Pair<Berries, Int>>()
    fun setPlayerBerries(berries: Map<String, Int>) {
        berries
            .map { (key, value) ->
                val berry = Berries.valueOf(key)
                berry to value
            }.let { berries ->
                this.berries.clear()
                this.berries.addAll(berries)
            }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BagItemCommonViewHolder {
        return ListCellBagItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
            .let {
                BagItemCommonViewHolder(it, onBerryClicked = onBerryClicked)
            }
    }

    override fun onBindViewHolder(holder: BagItemCommonViewHolder, position: Int) {
        val (berries, qty) = berries.get(position)
        holder.onBindBerry(
            berries = berries,
            qty = qty
        )
    }

    override fun getItemCount(): Int = berries.size
}


