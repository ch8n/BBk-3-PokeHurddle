package io.github.ch8n.pokehurddle.ui.pokeMart.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.github.ch8n.pokehurddle.data.models.Berries
import io.github.ch8n.pokehurddle.data.models.Berries.*
import io.github.ch8n.pokehurddle.databinding.ListCellMartItemBinding

class BerriesListAdapter(
    private val onBerryClicked: (berry: Berries) -> Unit,
) : RecyclerView.Adapter<BerryItemVH>() {

    private val berries = listOf(PomegBerry, KelpsyBerry, QualotBerry, HondewBerry, GrepaBerry)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BerryItemVH =
        ListCellMartItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
            .let { BerryItemVH(it, onBerryClicked) }

    override fun onBindViewHolder(holder: BerryItemVH, position: Int) = holder
        .onBind(berries.get(position))

    override fun getItemCount(): Int = berries.size
}

class BerryItemVH(
    private val binding: ListCellMartItemBinding,
    private val onBerryClicked: (berry: Berries) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun onBind(
        berry: Berries
    ) = with(binding) {
        Glide.with(root.context)
            .load(berry.imageUrl)
            .into(imgMartItem)
        labelMartItem.text = berry.name
        labelMartPrice.text = "${berry.price} Poke-coins"
        root.setOnClickListener {
            onBerryClicked.invoke(berry)
        }
    }
}


