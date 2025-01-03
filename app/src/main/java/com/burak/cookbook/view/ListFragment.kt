package com.burak.cookbook.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.burak.cookbook.adapter.RecipeAdapter
import com.burak.cookbook.databinding.FragmentListBinding
import com.burak.cookbook.model.Recipe
import com.burak.cookbook.roomdb.RecipeDAO
import com.burak.cookbook.roomdb.RecipeDatabase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers


class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private lateinit var db : RecipeDatabase
    private  lateinit var  recipeDao : RecipeDAO
    private val mDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = Room.databaseBuilder(requireContext(),RecipeDatabase::class.java,"Recipe").build()
        recipeDao =db.recipeDao()
    }

    private fun handleResponse(recipes: List<Recipe>) {
        val tarifAdapter = RecipeAdapter(recipes)
        binding.RecipeRecyclerView.adapter = tarifAdapter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.floatingActionButton.setOnClickListener{ newAdd(it) }
        binding.RecipeRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        getTheData()
    }

    private fun getTheData() {
        mDisposable.add(
            recipeDao.getALL()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResponse)
        )
    }

    fun newAdd(view : View) {
        val action = ListFragmentDirections.actionListFragmentToRecipeFragment(info = "new", id = -1)
        Navigation.findNavController(view).navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mDisposable.clear()
    }

}