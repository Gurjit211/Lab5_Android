package com.swiftcourier.deliveryapp

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var deliveryListLayout: LinearLayout
    private lateinit var editTextOrderID: EditText
    private lateinit var buttonFetchOrder: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("DeliveryData", MODE_PRIVATE)

        // Find views
        deliveryListLayout = findViewById(R.id.deliveryList)
        editTextOrderID = findViewById(R.id.editTextOrderID)
        buttonFetchOrder = findViewById(R.id.buttonFetchOrder)

        // Button to fetch order details
        buttonFetchOrder.setOnClickListener {
            val orderID = editTextOrderID.text.toString()
            if (orderID.isNotEmpty()) {
                displayOrder(orderID)
            } else {
                Toast.makeText(this, "Enter a valid Order ID", Toast.LENGTH_SHORT).show()
            }
        }

        // Load and display deliveries
        loadDeliveries()
    }

    // ✅ Load default deliveries and store them in SharedPreferences
    private fun loadDeliveries() {
        val orders = listOf(
            mapOf("id" to "410", "address" to "2 Kinora street", "status" to "Pending"),
            mapOf("id" to "5911", "address" to "22 Rampart Drive", "status" to "Delivered"),
            mapOf("id" to "295", "address" to "11 Steeles", "status" to "Pending")
        )

        for (order in orders) {
            val orderID = order["id"]!!
            val address = order["address"]!!
            val status = order["status"]!!

            // ✅ Store order data in SharedPreferences if not already stored
            if (sharedPreferences.getString("order_${orderID}_address", null) == null) {
                saveOrderToSharedPreferences(orderID, address, status)
            }

            // ✅ Display order in UI
            addOrderToUI(orderID, address, status)
        }
    }

    // ✅ Save delivery order data in SharedPreferences
    private fun saveOrderToSharedPreferences(orderID: String, address: String, status: String) {
        val editor = sharedPreferences.edit()
        editor.putString("order_${orderID}_address", address)
        editor.putString("order_${orderID}_status", status)
        editor.apply()
    }

    // ✅ Display the list of orders dynamically
    private fun addOrderToUI(orderID: String, address: String, status: String) {
        val orderView = LinearLayout(this)
        orderView.orientation = LinearLayout.HORIZONTAL
        orderView.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        val textView = TextView(this)
        textView.text = "Order: $orderID | $address | Status: $status"
        textView.layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)

        val button = Button(this)
        button.text = "Update"
        button.setOnClickListener {
            val intent = Intent(this, DeliveryDetailsActivity::class.java)
            intent.putExtra("orderID", orderID)
            intent.putExtra("address", address)
            intent.putExtra("status", status)
            startActivity(intent)
        }

        orderView.addView(textView)
        orderView.addView(button)
        deliveryListLayout.addView(orderView)
    }

    // ✅ Display order details when an order ID is entered
    private fun displayOrder(orderID: String) {
        val addressKey = "order_${orderID}_address"
        val statusKey = "order_${orderID}_status"

        val address = sharedPreferences.getString(addressKey, null)
        val status = sharedPreferences.getString(statusKey, null)

        if (address == null || status == null) {
            Toast.makeText(this, "Order not found", Toast.LENGTH_SHORT).show()
        } else {
            val intent = Intent(this, DeliveryDetailsActivity::class.java)
            intent.putExtra("orderID", orderID)
            intent.putExtra("address", address)
            intent.putExtra("status", status)
            startActivity(intent)
        }
    }
}
