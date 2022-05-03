package io.github.ch8n.pokehurddle.ui.bag.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.github.ch8n.pokehurddle.data.models.Berries
import io.github.ch8n.pokehurddle.data.models.Player
import io.github.ch8n.pokehurddle.data.models.Pokeball
import io.github.ch8n.pokehurddle.databinding.ListCellBagItemBinding

enum class BagListType {
    POKE_BERRY, POKE_BALL
}

class BagListItemAdapter(
    private val type: BagListType
) : RecyclerView.Adapter<BagItemVH>() {

    private var playerStats: Player? = null

    private val berries = listOf<Berries>(
        Berries.PomegBerry,
        Berries.KelpsyBerry,
        Berries.QualotBerry,
        Berries.HondewBerry,
        Berries.GrepaBerry
    )

    private val pokeballs = listOf<Pokeball>(
        Pokeball.MasterBall,
        Pokeball.UltraBall,
        Pokeball.GreatBall,
        Pokeball.LuxuryBall,
        Pokeball.PokeBall
    )

    fun setPlayerStats(playerStats: Player) {
        this.playerStats = playerStats
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BagItemVH {
        val binding =
            ListCellBagItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BagItemVH(binding)
    }

    override fun onBindViewHolder(holder: BagItemVH, position: Int) {
        val stats = playerStats ?: return
        holder.onBind(type, stats, position, pokeballs, berries)
    }

    override fun getItemCount(): Int = when (type) {
        BagListType.POKE_BERRY -> berries.size
        BagListType.POKE_BALL -> pokeballs.size
    }

}

class BagItemVH(private val binding: ListCellBagItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun onBind(
        type: BagListType,
        playerStats: Player,
        position: Int,
        pokeballs: List<Pokeball>,
        berries: List<Berries>
    ) = with(binding) {
        when (type) {
            BagListType.POKE_BERRY -> {
                val berry = berries.get(position)
                val playerQty = playerStats.berries.get(berry) ?: 0
                Glide.with(root.context)
                    .load(berry.sprite)
                    .into(imgItem)
                labelItem.setText(berry.name)
                labelQty.setText("x$playerQty")
            }
            BagListType.POKE_BALL -> {
                val pokeball = pokeballs.get(position)
                val playerQty = playerStats.pokeball.get(pokeball) ?: 0
                Glide.with(root.context)
                    .load(pokeball.sprite)
                    .into(imgItem)
                labelItem.setText(pokeball.name)
                labelQty.setText("x$playerQty")
            }
        }
    }
}