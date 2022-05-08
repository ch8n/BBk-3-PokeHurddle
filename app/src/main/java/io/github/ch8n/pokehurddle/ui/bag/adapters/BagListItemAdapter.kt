package io.github.ch8n.pokehurddle.ui.bag.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.github.ch8n.pokehurddle.data.models.Berries
import io.github.ch8n.pokehurddle.data.models.Berries.*
import io.github.ch8n.pokehurddle.data.models.Player
import io.github.ch8n.pokehurddle.data.models.Pokeball
import io.github.ch8n.pokehurddle.data.models.Pokeball.*
import io.github.ch8n.pokehurddle.databinding.ListCellBagItemBinding

enum class BagListType {
    POKE_BERRY, POKE_BALL
}

class BagListItemAdapter(
    private val type: BagListType,
    private val onBerryClicked: (berry: Berries) -> Unit = {},
    private val onBallClicked: (pokeball: Pokeball) -> Unit = {},
) : RecyclerView.Adapter<BagItemVH>() {

    private var playerStats: Player? = null

    private val berries = listOf(
        PomegBerry,
        KelpsyBerry,
        QualotBerry,
        HondewBerry,
        GrepaBerry
    )

    private val pokeballs = listOf(
        MasterBall,
        UltraBall,
        GreatBall,
        LuxuryBall,
        NormalBall
    )

    fun setPlayerStats(playerStats: Player) {
        this.playerStats = playerStats
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BagItemVH {
        val binding = ListCellBagItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return BagItemVH(binding)
    }

    override fun onBindViewHolder(holder: BagItemVH, position: Int) {
        val stats = playerStats ?: return
        holder.onBind(type, stats, position, pokeballs, berries, onBallClicked, onBerryClicked)
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
        berries: List<Berries>,
        onBallClicked: (pokeball: Pokeball) -> Unit,
        onBerryClicked: (berry: Berries) -> Unit
    ) = with(binding) {

        when (type) {
            BagListType.POKE_BERRY -> {
                val berry = berries.get(position)
                val playerQty = playerStats.berries.get(berry) ?: 0

                Glide.with(root.context)
                    .load(berry.sprite)
                    .into(imgItem)

                labelItem.text = berry.name
                labelQty.text = "x$playerQty"
                binding.root.setOnClickListener {
                    onBerryClicked.invoke(berry)
                }
            }
            BagListType.POKE_BALL -> {
                val pokeball = pokeballs.get(position)
                val playerQty = playerStats.pokeball.get(pokeball) ?: 0

                Glide.with(root.context)
                    .load(pokeball.sprite)
                    .into(imgItem)

                labelItem.text = pokeball.name
                labelQty.text = "x$playerQty"
                binding.root.setOnClickListener {
                    onBallClicked.invoke(pokeball)
                }
            }
        }
    }
}