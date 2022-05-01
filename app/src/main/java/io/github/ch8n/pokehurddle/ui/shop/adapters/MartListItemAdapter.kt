package io.github.ch8n.pokehurddle.ui.shop.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.github.ch8n.pokehurddle.data.models.Berries
import io.github.ch8n.pokehurddle.data.models.Pokeballs
import io.github.ch8n.pokehurddle.databinding.ListCellMartItemBinding

enum class MartItemType {
    POKE_BERRY, POKE_BALL
}

class MartListAdapter(
    private val type: MartItemType,
    private val onPokeballClicked: (ball: Pokeballs) -> Unit = {},
    private val onBerryClicked: (berry: Berries) -> Unit = {}
) : RecyclerView.Adapter<MartItemVH>() {

    private val berries = listOf<Berries>(
        Berries.PomegBerry,
        Berries.KelpsyBerry,
        Berries.QualotBerry,
        Berries.HondewBerry,
        Berries.GrepaBerry
    )

    private val pokeballs = listOf<Pokeballs>(
        Pokeballs.MasterBall,
        Pokeballs.UltraBall,
        Pokeballs.GreatBall,
        Pokeballs.LuxuryBall,
        Pokeballs.PokeBall
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MartItemVH {
        val binding =
            ListCellMartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MartItemVH(binding, onPokeballClicked, onBerryClicked)
    }

    override fun onBindViewHolder(holder: MartItemVH, position: Int) {
        holder.onBind(type, position, pokeballs, berries)
    }

    override fun getItemCount(): Int = when (type) {
        MartItemType.POKE_BERRY -> berries.size
        MartItemType.POKE_BALL -> pokeballs.size
    }

}

class MartItemVH(
    private val binding: ListCellMartItemBinding,
    private val onPokeballClicked: (ball: Pokeballs) -> Unit = {},
    private val onBerryClicked: (berry: Berries) -> Unit = {}
) : RecyclerView.ViewHolder(binding.root) {

    fun onBind(
        type: MartItemType,
        position: Int,
        pokeballs: List<Pokeballs>,
        berries: List<Berries>
    ) = with(binding) {
        when (type) {
            MartItemType.POKE_BERRY -> {
                val berry = berries.get(position)
                Glide.with(root.context)
                    .load(berry.sprite)
                    .into(imgMartItem)
                labelMartItem.setText(berry.name)
                labelMartPrice.setText("${berry.attractionRate} Poke-coins")
                root.setOnClickListener {
                    onBerryClicked.invoke(berry)
                }
            }
            MartItemType.POKE_BALL -> {
                val pokeball = pokeballs.get(position)
                Glide.with(root.context)
                    .load(pokeball.sprite)
                    .into(imgMartItem)
                labelMartItem.setText(pokeball.name)
                labelMartPrice.setText("${pokeball.successRate} Poke-coins")
                root.setOnClickListener {
                    onPokeballClicked.invoke(pokeball)
                }
            }
        }
    }
}