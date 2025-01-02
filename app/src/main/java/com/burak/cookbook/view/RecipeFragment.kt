package com.burak.cookbook.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.room.Room
import com.burak.cookbook.databinding.FragmentRecipeBinding
import com.burak.cookbook.model.Recipe
import com.burak.cookbook.roomdb.RecipeDAO
import com.burak.cookbook.roomdb.RecipeDatabase
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.ByteArrayOutputStream
import java.io.IOException


class RecipeFragment : Fragment() {

    private var _binding: FragmentRecipeBinding? = null

    private val binding get() = _binding!!
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private var chooseImage : Uri? = null
    private var chooseBitmap : Bitmap? = null
    private val mDisposable = CompositeDisposable()

    private lateinit var db : RecipeDatabase
    private  lateinit var  recipeDao : RecipeDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerLauncher()

        db = Room.databaseBuilder(requireContext(),RecipeDatabase::class.java,"Recipe").build()
        recipeDao =db.userDao()
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
        val name = binding.foodNameText.text.toString()
        val recipe = binding.recipeText.text.toString()
        val mealIngredients = binding.mealIngredientsText.text.toString()

        if(chooseImage != null) {
            val smallBitmap = makeMiniBitmap(chooseBitmap!!, 300)
            val outputStream = ByteArrayOutputStream()
            smallBitmap.compress(Bitmap.CompressFormat.PNG,50,outputStream)
            val byteArray = outputStream.toByteArray()

            val recipe = Recipe(name,recipe,mealIngredients,byteArray)

            // RxJava

            mDisposable.add(
                recipeDao.insert(recipe)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResponseInsert)
            )

        }

    }

    private fun handleResponseInsert() {
        // return to previous fragment
        val action = RecipeFragmentDirections.actionRecipeFragmentToListFragment()
        Navigation.findNavController(requireView()).navigate(action)
    }

    fun delete(view : View){

    }

    fun selectImage(view : View){


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                // not allowed, need to be allowed
                if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),Manifest.permission.READ_MEDIA_IMAGES)) {
                    // we need to show the snackbar, we need to specify why we are asking for permission from the user
                    Snackbar.make(view, "We need to access the gallery and choose an image!", Snackbar.LENGTH_INDEFINITE).setAction(
                        "Allow!",
                        View.OnClickListener {
                            // we will ask for permission
                            permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                        }
                    ).show()
                } else {
                    // we will ask for permission
                    permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                }
            } else {
                // permission is granted, you can go directly to the gallery
                val intenttoGalery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intenttoGalery)
            }
        } else {
            if (ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                // not allowed, need to be allowed
                if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    // we need to show the snackbar, we need to specify why we are asking for permission from the user
                    Snackbar.make(view, "We need to access the gallery and choose an image!", Snackbar.LENGTH_INDEFINITE).setAction(
                        "Allow!",
                        View.OnClickListener {
                            // we will ask for permission
                            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                        }
                    ).show()
                } else {
                    // we will ask for permission
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            } else {
                // permission is granted, you can go directly to the gallery
                val intenttoGalery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intenttoGalery)
            }
        }



    }

    private fun registerLauncher() {

        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if(result.resultCode == AppCompatActivity.RESULT_OK) {
                val intentFromResult = result.data
                if (intentFromResult != null) {
                    chooseImage = intentFromResult.data
                    try {
                        if(Build.VERSION.SDK_INT >= 28){
                            val source = ImageDecoder.createSource(requireActivity().contentResolver,chooseImage!!)
                            chooseBitmap = ImageDecoder.decodeBitmap(source)
                            binding.foodImageView.setImageBitmap(chooseBitmap)
                        } else {
                            chooseBitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver,chooseImage)
                            binding.foodImageView.setImageBitmap(chooseBitmap)
                        }
                    } catch (e: IOException) {
                        println(e.localizedMessage)
                    }

                }
            }
        }

        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
            if (result) {
                // permission granted
                // we can go to the gallery
                val intenttoGalery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intenttoGalery)
            } else {
                // not allowed
                Toast.makeText(requireContext(), "Not allowed!", Toast.LENGTH_LONG).show()

            }

        }

    }

    private fun makeMiniBitmap(chooseUserBitmap : Bitmap, maxSize :Int) : Bitmap {
        var width = chooseUserBitmap.width
        var height = chooseUserBitmap.height

        val bitmapRatio : Double = width.toDouble() / height.toDouble()

        if(bitmapRatio > 1) {
            // image horizontal
            width = maxSize
            val shortenedHeight = width / bitmapRatio
            height = shortenedHeight.toInt()
        } else {
            // image vertical
            height = maxSize
            val shortenedWidth = height * bitmapRatio
            width = shortenedWidth.toInt()
        }

        return Bitmap.createScaledBitmap(chooseUserBitmap,width,height,true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}