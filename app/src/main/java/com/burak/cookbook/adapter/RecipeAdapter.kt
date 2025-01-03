package com.burak.cookbook.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.burak.cookbook.databinding.RecyclerRowBinding
import com.burak.cookbook.model.Recipe
import com.burak.cookbook.view.ListFragmentDirections

class RecipeAdapter(val recipeList : List<Recipe>) :
    RecyclerView.Adapter<RecipeAdapter.RecipeHolder>() {

    class RecipeHolder (val binding: RecyclerRowBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeHolder {
        val recyclerRowBinding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return RecipeHolder(recyclerRowBinding)
    }

    override fun getItemCount(): Int {
        return recipeList.size
    }

    override fun onBindViewHolder(holder: RecipeHolder, position: Int) {
        holder.binding.recyclerViewTextView.text = recipeList[position].name
        holder.itemView.setOnClickListener{
            val action = ListFragmentDirections.actionListFragmentToRecipeFragment(info = "old", id = recipeList[position].id)
            Navigation.findNavController(it).navigate(action)
        }
    }
}