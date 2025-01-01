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
import com.burak.cookbook.databinding.FragmentRecipeBinding
import com.google.android.material.snackbar.Snackbar
import java.io.IOException


class RecipeFragment : Fragment() {

    private var _binding: FragmentRecipeBinding? = null

    private val binding get() = _binding!!
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private var chooseImage : Uri? = null
    private var chooseBitmap : Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerLauncher()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}