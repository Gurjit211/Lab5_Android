package com.swiftcourier.deliveryapp

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class DeliveryDetailsActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var textViewOrderDetails: TextView
    private lateinit var buttonUpdateStatus: Button
    private lateinit var buttonNavigate: Button
    private var orderID: String? = null
    private var address: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delivery_details)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("DeliveryData", MODE_PRIVATE)

        // Get order details from Intent
        orderID = intent.getStringExtra("orderID")
        address = intent.getStringExtra("address")
        val status = intent.getStringExtra("status")

        // Find views
        textViewOrderDetails = findViewById(R.id.textViewOrderDetails)
        buttonUpdateStatus = findViewById(R.id.buttonUpdateStatus)
        buttonNavigate = findViewById(R.id.buttonNavigate)

        // Display order details
        if (orderID != null && address != null && status != null) {
            textViewOrderDetails.text = "Order ID: $orderID\nAddress: $address\nStatus: $status"
        } else {
            Toast.makeText(this, "Order details not found!", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // ✅ Update Status Button
        buttonUpdateStatus.setOnClickListener {
            updateOrderStatus()
        }

        // ✅ Navigate to Address Button
        buttonNavigate.setOnClickListener {
            navigateToAddress()
        }
    }

    // ✅ Function to Update Order Status
    private fun updateOrderStatus() {
        if (orderID == null) return

        sharedPreferences.edit().putString("order_${orderID}_status", "Delivered").apply()
        Toast.makeText(this, "Order $orderID marked as Delivered!", Toast.LENGTH_SHORT).show()

        // ✅ Update UI immediately
        textViewOrderDetails.text = "Order ID: $orderID\nAddress: $address\nStatus: Delivered"

        // ✅ Close activity and return to MainActivity
        finish()
    }

    // ✅ Function to Open Google Maps for Navigation
    private fun navigateToAddress() {
        if (address.isNullOrEmpty()) {
            Toast.makeText(this, "Invalid address!", Toast.LENGTH_SHORT).show()
            return
        }

        val gmmIntentUri = Uri.parse("geo:0,0?q=$address")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")

        // ✅ Check if Google Maps is installed
        if (mapIntent.resolveActivity(packageManager) != null) {
            startActivity(mapIntent)
        } else {
            Toast.makeText(this, "Google Maps not installed!", Toast.LENGTH_SHORT).show()
        }
    }
}
