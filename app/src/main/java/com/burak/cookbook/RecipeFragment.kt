package com.burak.cookbook

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.burak.cookbook.databinding.FragmentRecipeBinding


class RecipeFragment : Fragment() {

    private var _binding: FragmentRecipeBinding? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.foodImageView.setOnClickListener{ selectImage(it)}
        binding.saveButton.setOnClickListener{ save(it)}
        binding.deleteButton.setOnClickListener{ delete(it) }

        arguments?.let {
            val info = RecipeFragmentArgs.fromBundle(it).info

            if(info == "new"){
                // Add new recipe
                binding.deleteButton.isEnabled = false
                binding.saveButton.isEnabled = true
                binding.foodNameText.setText("")
                binding.recipeText.setText("")
                binding.mealIngredientsText.setText("")
            } else {
                // Show old recipe
                binding.deleteButton.isEnabled = true
                binding.saveButton.isEnabled = false
            }
        }
    }

    fun save(view : View){

    }

    fun delete(view : View){

    }

    fun selectImage(view : View){

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}