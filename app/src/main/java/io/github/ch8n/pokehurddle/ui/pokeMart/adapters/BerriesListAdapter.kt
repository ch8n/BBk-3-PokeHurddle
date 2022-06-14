package io.github.ch8n.pokehurddle.ui.pokeMart.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.github.ch8n.pokehurddle.data.models.Berries
import io.github.ch8n.pokehurddle.databinding.ListCellMartItemBinding

// pokehurddle > ui > pokeMart > adapters > BerriesListAdapter.kt
class BerriesListAdapter(
    //👇 on click callback for berry item on recycler view
    private val onBerryClicked: (berry: Berries) -> Unit,
) : RecyclerView.Adapter<BerryItemVH>() {

    //👇 list of all berries in our systen
    private val berries = Berries.values().toList()

    //👇 create Berry View Holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BerryItemVH =
        ListCellMartItemBinding
            //👇 create binding and view for viewholder iten
            .inflate(LayoutInflater.from(parent.context), parent, false)
            //👇 create and pass binding and click callback in view holder
            .let { BerryItemVH(it, onBerryClicked) }

    //👇 bind viewholder with berry data
    override fun onBindViewHolder(holder: BerryItemVH, position: Int) = holder
        .onBind(berries.get(position))

    //👇 applying size of berry
    override fun getItemCount(): Int = berries.size
}

class BerryItemVH(
    private val binding: ListCellMartItemBinding,
    private val onBerryClicked: (berry: Berries) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun onBind(berry: Berries) = with(binding) {
        //👇 load image url into image view
        Glide.with(root.context)
            .load(berry.imageUrl)
            .into(imgMartItem)
        //👇 apply berry name
        labelMartItem.text = berry.name
        //👇 apply berry price
        labelMartPrice.text = "${berry.price} Poke-coins"
        //👇 on click of viewholder root trigger click callback
        imgMartItem.setOnClickListener {
            onBerryClicked.invoke(berry)
        }
    }
}
