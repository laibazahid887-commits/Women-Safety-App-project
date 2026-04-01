package com.example.safezone

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class EmergencyCall : AppCompatActivity() {

    private val CALL_REQUEST_CODE = 1
    private val CONTACT_PICK_CODE = 101
    private val CONTACT_PERMISSION_CODE = 102
    private var phoneToCall: String = ""
    private var selectedContactNumber: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emergency_call)

        val emergencyButton = findViewById<Button>(R.id.emergencyButton)
        val alertButton = findViewById<Button>(R.id.alertButton)
        val highwayButton = findViewById<Button>(R.id.highwayButton)
        val addContactButton = findViewById<Button>(R.id.addContactButton)
        val selectedContactText = findViewById<TextView>(R.id.selectedContactText)
        val deleteContactButton = findViewById<Button>(R.id.deleteContactButton)
        val callSelectedContactButton = findViewById<Button>(R.id.callSelectedContactButton)

        // Disable buttons initially
        callSelectedContactButton.isEnabled = false
        deleteContactButton.isEnabled = false

        emergencyButton.setOnClickListener {
            phoneToCall = "1122"
            makePhoneCall()
        }

        alertButton.setOnClickListener {
            phoneToCall = "15"
            makePhoneCall()
        }

        highwayButton.setOnClickListener {
            phoneToCall = "1124"
            makePhoneCall()
        }

        addContactButton.setOnClickListener {
            checkContactPermissionAndPick()
        }

        deleteContactButton.setOnClickListener {
            selectedContactText.text = "No contact added"
            selectedContactNumber = null
            callSelectedContactButton.isEnabled = false
            deleteContactButton.isEnabled = false
            Toast.makeText(this, "Contact removed", Toast.LENGTH_SHORT).show()
        }

        callSelectedContactButton.setOnClickListener {
            if (!selectedContactNumber.isNullOrEmpty()) {
                phoneToCall = selectedContactNumber!!
                makePhoneCall()
            } else {
                Toast.makeText(this, "No contact selected", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun makePhoneCall() {
        val phoneNumber = "tel:$phoneToCall"

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CALL_PHONE),
                CALL_REQUEST_CODE
            )
        } else {
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse(phoneNumber)
            startActivity(callIntent)
        }
    }

    private fun checkContactPermissionAndPick() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_CONTACTS),
                CONTACT_PERMISSION_CODE
            )
        } else {
            pickContact()
        }
    }

    private fun pickContact() {
        val intent = Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI)
        startActivityForResult(intent, CONTACT_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CONTACT_PICK_CODE && resultCode == Activity.RESULT_OK) {
            val contactUri: Uri? = data?.data
            val projection = arrayOf(
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER
            )

            contactUri?.let {
                val cursor = contentResolver.query(it, projection, null, null, null)
                if (cursor != null && cursor.moveToFirst()) {
                    val nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                    val numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

                    val name = cursor.getString(nameIndex)
                    val number = cursor.getString(numberIndex)

                    selectedContactNumber = number

                    val selectedContactText = findViewById<TextView>(R.id.selectedContactText)
                    selectedContactText.text = "Contact: $name\nNumber: $number"

                    findViewById<Button>(R.id.callSelectedContactButton).isEnabled = true
                    findViewById<Button>(R.id.deleteContactButton).isEnabled = true

                    cursor.close()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            CALL_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    makePhoneCall()
                }
            }
            CONTACT_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickContact()
                } else {
                    Toast.makeText(this, "Contact permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
