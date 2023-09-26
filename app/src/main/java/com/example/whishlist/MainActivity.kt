package com.example.whishlist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.content.Intent
import android.net.Uri

class MainActivity : AppCompatActivity() {
    private val itemList = mutableListOf<Item>()
    private lateinit var adapter: ItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize the RecyclerView and adapter
        val recyclerView: RecyclerView = findViewById(R.id.list_items)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = ItemAdapter(itemList,
            deleteItemClickListener = { item ->
                // Handle item deletion here
                showDeleteConfirmationDialog(item)
            },
            openUrlClickListener = { url ->
                // Handle opening the URL here
                openUrl(url)
            }
        )

        recyclerView.adapter = adapter

        // Handle user input and add new items
        val submitButton: Button = findViewById(R.id.button)
        val itemNameEditText: EditText = findViewById(R.id.editTextText)
        val itemPriceEditText: EditText = findViewById(R.id.editTextText2)
        val itemUrlEditText: EditText = findViewById(R.id.editTextText3)

        submitButton.setOnClickListener {
            // Get user input
            val itemName = itemNameEditText.text.toString()
            val itemPrice = itemPriceEditText.text.toString()
            val itemUrl = itemUrlEditText.text.toString()

            // Create a new item
            val newItem = Item(itemName, itemPrice, itemUrl)

            // Add the new item to the data source
            itemList.add(newItem)

            // Notify the adapter that the data has changed
            adapter.notifyDataSetChanged()

            // Clear the input fields
            itemNameEditText.text.clear()
            itemPriceEditText.text.clear()
            itemUrlEditText.text.clear()
        }
    }

    // Create a confirmation dialog for item deletion
    private fun showDeleteConfirmationDialog(item: Item) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Delete this item?")
        builder.setPositiveButton("Delete") { dialog, which ->
            // Delete the item from the data source
            deleteItem(item)
        }
        builder.setNegativeButton("Cancel") { dialog, which ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    // Handle item deletion
    private fun deleteItem(item: Item) {
        itemList.remove(item)
        adapter.notifyDataSetChanged()
    }

    // Handle opening the URL
    private fun openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }
}
