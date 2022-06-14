package io.github.ch8n.pokehurddle.ui.pokeMart.pages

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.github.ch8n.pokehurddle.databinding.FragmentListBinding
import io.github.ch8n.pokehurddle.ui.MainViewModel
import io.github.ch8n.pokehurddle.ui.pokeMart.adapters.BerriesListAdapter
import io.github.ch8n.pokehurddle.ui.utils.ViewBindingFragment

// pokehurddle > ui > pokeMart > pages > MartBerriesFragment.kt
@AndroidEntryPoint
class MartBerriesFragment : ViewBindingFragment<FragmentListBinding>() {
    //👇 get shared view model
    private val viewModel: MainViewModel by activityViewModels()

    override fun setup() = with(binding) {
        //👇 create berries list adapter
        val berriesAdapter = BerriesListAdapter(
            //👇 this would be triggered when berry is click on adapter
            onBerryClicked = { berry ->
                //👇 purchase berry
                viewModel.buyBerry(
                    berry = berry,
                    //👇 on purchase error show snackbar with error message
                    onError = { msg -> msg.snack() },
                    //👇 on purchase successful show snackbar
                    onSuccess = {
                        "You purchased ${berry.name} x1!".snack()
                    }
                )
            })

        //👇 apply grid layout to the recycler view
        list.layoutManager = GridLayoutManager(requireContext(), 2)
        //👇 apply adapter to recycler view
        list.adapter = berriesAdapter
    }

    //👇 passing inflater function to bind the UI with fragment
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentListBinding
        get() = FragmentListBinding::inflate
}